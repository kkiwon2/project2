package org.kiwon.project.repository;

import org.junit.jupiter.api.Test;
import org.kiwon.project.entity.member.Member;
import org.kiwon.project.entity.movie.Movie;
import org.kiwon.project.entity.movie.Review;
import org.kiwon.project.repository.movie.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMoviewReviews(){

        //200개의 리뷰를 등록
        IntStream.rangeClosed(1,10).forEach(i -> {
            
            //영화 번호 1~100까지 랜덤
            Long mno = (long)(Math.random()*100) + 1;

            //리뷰어(회원) 이메일
            Member member = Member.builder().email("r99@zerock.org").build();

            Review movieReview = Review.builder()
                    .member(member)
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int)(Math.random() * 5) + 1)
                    .text("이 영화에 대한 느낌..." + i)
                    .build();

            reviewRepository.save(movieReview);
            
        });
    }

    @Test
    public void testGetMovieReviews(){

        Movie movie = Movie.builder().mno(97L).build();

        List<Review> result = reviewRepository.findByMovie(movie);

        result.forEach(review -> {

            System.out.println(review.getReviewnum());
            System.out.println("\t"+review.getGrade());
            System.out.println("\t"+review.getText());
            System.out.println("\t"+review.getMember().getEmail());
            System.out.println("---------------------------------");
        });
    }
}
