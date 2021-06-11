package org.kiwon.project.repository;

import org.junit.jupiter.api.Test;
import org.kiwon.project.entity.movie.Movie;
import org.kiwon.project.entity.movie.MovieImage;
import org.kiwon.project.repository.movie.MovieImageRepository;
import org.kiwon.project.repository.movie.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository imageRepository;

    @Commit
    @Transactional
    @Test
    public void insertMovies(){

        IntStream.rangeClosed(1,100).forEach(i -> {

            Movie movie = Movie.builder().title("Movie....." + i).build();
            System.out.println("===================================");
            movieRepository.save(movie);

            int count = (int)(Math.random() * 5) +1;    //1,2,3,4,5 까지의 랜덤 정수
            for(int j = 0; j < count; j++){ //최대 5개의 이미지가 저장됨
                MovieImage movieImage = MovieImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .movie(movie)
                        .imgName("test" + j + ".jpg").build();
                imageRepository.save(movieImage);
            }
            System.out.println("===================================");
        });
    }

    @Test
    public void testListPage() {
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.Direction.DESC, "mno"));

        Page<Object[]> result = movieRepository.getListPage(pageRequest);

        for(Object[] objects : result.getContent()){
            System.out.println(Arrays.toString(objects));
        }
    }

    //특정영화의 이미지와 리뷰 조회
    @Test
    public void testGetMovieWithAll(){
        List<Object[]> result = movieRepository.getMovieWithAll(97L);

        System.out.println(result);

        for(Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }
    }
}
