package org.kiwon.project.entity.movie;

import lombok.*;
import org.kiwon.project.entity.BaseEntity;
import org.kiwon.project.entity.member.Member;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Movie extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;       //영화 번호
    
    private String title;   //영화 제목

    private String content; //영화 내용

    //영화를 작성한 사용자 이메일이 필요해서 연관관계 추가했음 6/11
    @ManyToOne
    private Member member;

    @Builder.Default
    private Long cnt=0L;       //조회수

    public void changeCnt() { this.cnt += 1; }
    
    //영화의 제목 수정 -> Entity 자체에서 처리
    public void changeTitle(String title) {
        this.title = title;
    }
    //영화의 내용 수정 -> Entity 자체에서 처리
}
