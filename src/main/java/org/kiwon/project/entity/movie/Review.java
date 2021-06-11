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
@ToString(exclude = {"movie", "member"})
public class Review extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewnum;     //리뷰번호
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;        //영화 pk : mno
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;      //회원 pk : email
    
    private int grade;          //영화 평점
    
    private String text;        //영화 리뷰내용

    //리뷰의 평점과 리뷰 내용을 수정하는 메서드는 엔티티에 작성함
    public void changeGrade(int grade){
        this.grade = grade;
    }

    public void changeText(String text){
        this.text = text;
    }
}
