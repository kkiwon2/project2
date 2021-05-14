package org.kiwon.project.repository.search;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.kiwon.project.entity.board.Board;
import org.kiwon.project.entity.board.QBoard;
import org.kiwon.project.entity.board.QReply;
import org.kiwon.project.entity.member.QMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl  extends QuerydslRepositorySupport implements SearchBoardRepository{

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search1() {

       log.info("search1...............");

        QMember qMember = QMember.member;
        QBoard qBoard = QBoard.board;
        QReply qReply = QReply.reply;

        JPQLQuery<Board> jpqlQuery = from(qBoard);
        jpqlQuery.leftJoin(qMember).on(qBoard.writer.eq(qMember));
        jpqlQuery.leftJoin(qReply).on(qReply.board.eq(qBoard));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(qBoard, qMember.email, qReply.count());
        tuple.groupBy(qBoard);

        log.info("--------------------------");
        log.info(tuple);
        log.info("--------------------------");

        List<Tuple> result = tuple.fetch();

        log.info(result);

        return null;
    }


    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.info("searchPage...............");

        QBoard board = QBoard.board;
        QMember member = QMember.member;
        QReply reply = QReply.reply;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);

        booleanBuilder.and(expression);

        if(type != null){
            String[] typeArr = type.split("");

            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for(String t : typeArr){

                switch (t){
                    case "t":
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        Sort sort = pageable.getSort();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            System.out.println("prop(order.getProperty) : " + prop);

            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            tuple.orderBy(new OrderSpecifier<>(direction, orderByExpression.get(prop)));
        });
        tuple.groupBy(board);

        //Page처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info(result);

        long count = tuple.fetchCount();

        log.info("COUNT : " + count);

        return new PageImpl<Object[]>(result.stream().map(tuple1 -> tuple1.toArray()).collect(Collectors.toList()),
                pageable, count);



    }
}
