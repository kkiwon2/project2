package org.kiwon.project.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    private Long mno;

    private String title;

    private String email;

    private String nickName;

    private double avg;     //영화의 평균 평점

    private int reviewCnt;  //영화의 리뷰 수 -> JPA의 count()

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    @Builder.Default
    private Long cnt = 0L;  //영화 조회수

    //MovieDTO는 화면에 영화 이미지들도 같이 수집해서 전달해야 하므로 내부적으로 리스트를 이용해서 수집합니다.
    @Builder.Default
    private List<MovieImageDTO> imageDTOList = new ArrayList<>();
}
