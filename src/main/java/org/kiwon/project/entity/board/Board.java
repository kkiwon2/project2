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
    private Long bno;   //게시판 번호
    private String title;
    private String content;



    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }


}
