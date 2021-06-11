package org.kiwon.project.controller.moviecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kiwon.project.dto.movie.ReviewDTO;
import org.kiwon.project.service.movie.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//ReviewController는 Ajax로 동작하기 때문에 @RestConroller로 설계했음
//ReviewDTO는 JSON 형태로 변환되어서 처리됨
@RestController
@RequestMapping("/reviews")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //해당 영화의 모든 리뷰를 반환하는 메서드
    @GetMapping("/{mno}/all")
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("mno")Long mno){
        //URL 주소로 동적인 영화번호를 받기위해 {mno}를 사용 -> @PathVariable어노테이션을 이용해 mno변수에 바인딩

        log.info("ReviewController getList...........");
        log.info("MNO : " + mno);

        List<ReviewDTO> reviewDTOList = reviewService.getListOfMovie(mno);

        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

    //새로운 리뷰를 등록
    @PostMapping("/{mno}")
    public ResponseEntity<Long> addReview(@RequestBody ReviewDTO movieReviewDTO){
        //JSON데이터를 @RequestBody어노테이션을 이용하여 ReviewDTO에 바인딩

        log.info("ReviewController addReview..............");
        log.info("reviewDTO : " + movieReviewDTO);

        Long reviewnum = reviewService.register(movieReviewDTO);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }

    //리뷰 수정
    @PutMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> modifyReview(@PathVariable Long reviewnum, @RequestBody ReviewDTO movieReviewDTO){
        //

        log.info("ReviewController modifyReview.............");
        log.info("reviewDTO : " + movieReviewDTO );

        reviewService.modify(movieReviewDTO);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }

    //리뷰 삭제
    @DeleteMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> removeReview(@PathVariable Long reviewnum){
        //

        log.info("ReviewController removeReview.................");
        log.info("reviewnum" + reviewnum);

        reviewService.remove(reviewnum);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }
}
