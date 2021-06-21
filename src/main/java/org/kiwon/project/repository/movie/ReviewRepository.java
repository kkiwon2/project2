package org.kiwon.project.repository.movie;

import org.kiwon.project.entity.member.Member;
import org.kiwon.project.entity.movie.Movie;
import org.kiwon.project.entity.movie.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //영화의 번호를 이용하여 리뷰내용을 찾는 쿼리
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    //비효율적인 삭제방법
//    Member테이블에서 특정 회원을 삭제시 review테이블의 포렌키를 먼저 삭제해야됨 -> 트랜잭션처리
//    void deleteByMember(Member member);

    //효율적인 삭제방법 @Query 사용하기
    @Modifying  //update나 delete를 이용하기 위해서는 @Modifying어노테이션이 반드시 필요
    @Query("delete from Review r where r.member = :member")
    void deleteByMember(Member member);

    //영화의 번호를 이용하여 Reviw테이블의 row 삭제 -> 6.14추가
    @Modifying
    @Query("delete from Review r where r.movie.mno = :mno")
    void deleteByMovie(Long mno);

}
