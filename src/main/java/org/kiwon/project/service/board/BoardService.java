package org.kiwon.project.service.board;


import org.kiwon.project.dto.board.BoardDTO;
import org.kiwon.project.dto.page.PageRequestDTO;
import org.kiwon.project.dto.page.PageResultDTO;
import org.kiwon.project.entity.board.Board;
import org.kiwon.project.entity.member.Member;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    BoardDTO get(Long bno);

    void modify(BoardDTO boardDTO);

    void removeWithReplies(Long bno);

    default Board dtoToEntity(BoardDTO dto){

        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        return board;
    }

    default BoardDTO entityToDTO(Board board, Member member, Long replyCount){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .cnt(board.getCnt())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerName(member.getNickName())
                .writerEmail(member.getEmail())
                .replyCount(replyCount.intValue())
                .build();

        return boardDTO;
    }
}
