package org.kiwon.project.entity.board;

import lombok.*;
import org.kiwon.project.entity.BaseEntity;
import org.kiwon.project.entity.member.Member;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "writer")
@Getter
public class Board extends BaseEntity {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동시퀀스?
    private Long bno;       //게시판 번호
    private String title;   //게시판 제목
    private String content; //게시판 내용

    @Builder.Default
    private Long cnt = 0L;       //조회수



    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeCnt() {this.cnt += 1;
        System.out.println("조회수 증가 this.cnt" + this.cnt);}


}
