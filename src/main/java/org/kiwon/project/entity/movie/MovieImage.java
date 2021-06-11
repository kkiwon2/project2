package org.kiwon.project.entity.movie;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "movie") //연관 관계가 설정되어있을 때 movie 엔티티도 불러오면 무한루프에 걸릴 위험이 있음
public class MovieImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;      //영화 이미지 번호
    
    private String uuid;    //영화 이미지이름의 중복값 방지 고유번호
    
    private String imgName; //영화 이미지 이름
    
    private String path;    //이미지의 저장 경로 -> 년/월/일 폴더 구조를 사용할 것임
    
    @ManyToOne(fetch = FetchType.LAZY)      //다
    private Movie movie;    //Movie 테이블이 PK를 가지고, movie_image 테이블은 FK를 가지게 하기 위해 @ManyToOne를 사용함

    
}
