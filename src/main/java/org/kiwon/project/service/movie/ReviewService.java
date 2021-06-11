package org.kiwon.project.service.movie;

import org.kiwon.project.dto.movie.ReviewDTO;
import org.kiwon.project.entity.member.Member;
import org.kiwon.project.entity.movie.Movie;
import org.kiwon.project.entity.movie.Review;

import java.util.List;

public interface ReviewService {

    //영화 모든 영화리뷰를 가져온다
    List<ReviewDTO> getListOfMovie(Long mno);

    //영화 리뷰를 추가한다.
    Long register(ReviewDTO movieReviewDTO);

    //특정한 영화리뷰를 수정
    void modify(ReviewDTO movieReviewDTO);

    //영화 리뷰 삭제
    void remove(Long reviewnum);

    default Review dtoToEntity(ReviewDTO movieReviewDTO){

        Review review = Review.builder()
                .reviewnum(movieReviewDTO.getReviewnum())
                .movie(Movie.builder().mno(movieReviewDTO.getMno()).build())
                .member(Member.builder().email(movieReviewDTO.getEmail()).build())
                .grade(movieReviewDTO.getGrade())
                .text(movieReviewDTO.getText())
                .build();

        return review;
    }

    default ReviewDTO entityToDto(Review movieReview){

        ReviewDTO movieReviewDTO = ReviewDTO.builder()
                .reviewnum(movieReview.getReviewnum())
                .mno(movieReview.getMovie().getMno())
                .email(movieReview.getMember().getEmail())
                .nickname(movieReview.getMember().getNickName())
                .grade(movieReview.getGrade())
                .text(movieReview.getText())
                .regDate(movieReview.getRegDate())
                .modDate(movieReview.getModDate())
                .build();

        return movieReviewDTO;
    }
}
