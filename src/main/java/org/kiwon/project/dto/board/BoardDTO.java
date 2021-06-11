package org.kiwon.project.dto.board;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardDTO {

    private Long bno;           //게시판 번호

    private String title;       //게시판 제목

    private String content;     //게시판 내용

    private String writerEmail; //회원ID

    private String writerName;  //회원닉네임


    private Long cnt;      //게시글 조회수

    private int replyCount;     //댓글 개수

    private LocalDateTime regDate;

    private LocalDateTime modDate;


}
