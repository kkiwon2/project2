package org.kiwon.project.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    
    //리뷰 번호
    private Long reviewnum;
    
    //영화 번호 FK
    private Long mno;
    
    //회원 이메일 FK - 영화 리뷰를 작성한
    private String email;
    
    //회원 닉네임 - 영화 리뷰를 작성한
    private String nickname;
    
    //영화 평점
    private int grade;
    
    //영화 리뷰 내용
    private String text;
    
    //등록시간 수정시간
    private LocalDateTime regDate, modDate;
    
    
}
