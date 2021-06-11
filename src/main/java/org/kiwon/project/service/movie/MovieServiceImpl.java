package org.kiwon.project.service.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kiwon.project.dto.movie.MovieDTO;
import org.kiwon.project.dto.page.PageRequestDTO;
import org.kiwon.project.dto.page.PageResultDTO;
import org.kiwon.project.entity.member.Member;
import org.kiwon.project.entity.movie.Movie;
import org.kiwon.project.entity.movie.MovieImage;
import org.kiwon.project.entity.movie.Review;
import org.kiwon.project.repository.movie.MovieImageRepository;
import org.kiwon.project.repository.movie.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    private final MovieImageRepository imageRepository;

    //영화 등록메서드
    @Transactional
    @Override
    public Long register(MovieDTO movieDTO) {

        log.info("MovieServiceImpl register...................");
        log.info("MovieDTO : " + movieDTO);

        //dto를 Entity로 변환하는 메서드를 통해 Map객체를 가져옴
        Map<String, Object> entityMap = dtoToEntity(movieDTO);

        //Map컬렉션에 저장된 Movie엔티티를 가져옴
        Movie movie = (Movie) entityMap.get("movie");

        //Map컬렉션에 저장된 MovieImage엔티티를 가져옴
        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

        movieRepository.save(movie);

            movieImageList.forEach(movieImage -> {
                imageRepository.save(movieImage);
            });

            return movie.getMno();
    }
    
    @Override
    public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO) {
        
        //내림차순 정렬
        Pageable pageable = requestDTO.getPageable(Sort.by("mno").descending());

        Page<Object[]> result = movieRepository.getListPage(pageable);

        //JPA결과로 나오는 Object[]를 MovieDTO로 변환 -> PageResult객체로 전달
        Function<Object[], MovieDTO> fn = (arr -> entitiesToDTO(
                (Movie)arr[0],
                (List<MovieImage>)(Arrays.asList((MovieImage)arr[1])),  //Arrays.asList()는 고정된 ArraysList를 반환함
                (Double)arr[2],
                (Long) arr[3]
        ));

        return new PageResultDTO<>(result, fn);
    }


    @Override
    public MovieDTO getMovie(Long mno) {

        List<Object[]> result = movieRepository.getMovieWithAll(mno);

        //Movie 엔티티는 가장 앞에 존재 - MovieImage가 여러개라면 Movie또한 여러개이지만 Movie의 모든 row값은 동일한 값이다.
        Movie movie = (Movie)result.get(0)[0];

        //영화 조회수 증가
        movie.changeCnt();
        //영화 조회수 DB에 갱신
        movieRepository.save(movie);

        //MovieImage객체를 담을 리스트 -> 리스트로 담는 이유는 이미지 개수가 여러개일 경우가 있음
        List<MovieImage> movieImageList = new ArrayList<>();
        //영화의 이미지 개수만큼 MovieImage객체가 필요
        result.forEach(arr -> {
            MovieImage movieImage = (MovieImage)arr[1];
            movieImageList.add(movieImage);
        });

        //영화의 평균 평점 - 모든 Row가 동일한 값임
        Double avg = (Double) result.get(0)[2];
        //영화의 리뷰 개수 - 모든 Row가 동일한 값임
        Long reviewCnt = (Long) result.get(0)[3];

        return entitiesToDTO(movie, movieImageList, avg, reviewCnt);
    }
}
