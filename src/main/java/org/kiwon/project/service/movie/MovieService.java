package org.kiwon.project.service.movie;

import org.kiwon.project.dto.movie.MovieDTO;
import org.kiwon.project.dto.movie.MovieImageDTO;
import org.kiwon.project.dto.page.PageRequestDTO;
import org.kiwon.project.dto.page.PageResultDTO;
import org.kiwon.project.entity.member.Member;
import org.kiwon.project.entity.movie.Movie;
import org.kiwon.project.entity.movie.MovieImage;
import org.kiwon.project.entity.movie.Review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {

    //영화를 등록하는 메서드
    Long register(MovieDTO movieDTO);

    //영화 목록을 처리하는 메서드
    PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO);
    
    //특정한 영화의 번호를 이용해서 MovieDTO를 반환하는 메서드 -> 영화 조회 페이지
    MovieDTO getMovie(Long mno);
    
    //dto를 Entity로 변환 -> MovieDTO는 List로 ImageDTOList를 가지고있으므로 Map으로 Entity 2개를 처리해야 함
    default Map<String, Object> dtoToEntity(MovieDTO movieDTO){

        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .cnt(movieDTO.getCnt())
                .member(Member.builder().email(movieDTO.getEmail()).build())    //6.11일 추가 - 영화리스트 작성자 처리
                .build();

        entityMap.put("movie", movie);

        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

        //MovieImageDTO 처리 -> 이미지 리스트가 널이 아니고 0보다 크다면
        if(imageDTOList != null && imageDTOList.size() > 0){

            List<MovieImage> movieImageList = imageDTOList.stream().map(movieImageDTO -> {

                MovieImage movieImage = MovieImage.builder()
                        .path(movieImageDTO.getPath())
                        .imgName(movieImageDTO.getImgName())
                        .uuid(movieImageDTO.getUuid())
                        .movie(movie)
                        .build();
                return movieImage;
            }).collect(Collectors.toList());

            entityMap.put("imgList", movieImageList);
        }//end if
        return entityMap;
    }//end dtoToEntity

    // JPA를 통해서 나오는 엔티티 객체들과, Double(평점), Long(리뷰 개수)값을 MovieDTO로 변환하는 메서드
    // 굳이 List<MovieImage> 엔티로 받는 이유는 조회 화면에서 여러 개의 MovieImage를 처리하기 위함임
    default MovieDTO entitiesToDTO(Movie movie, List<MovieImage> movieImages, Double avg, Long reviewCnt){


        MovieDTO movieDTO = MovieDTO.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .cnt(movie.getCnt())
                .email(movie.getMember().getEmail())    //6.11일 추가 - 영화리스트 작성자 처리
                .nickName(movie.getMember().getNickName()) //6.11일 추가 - 영화리스트 작성자 처리
                .regDate(movie.getRegDate())
                .modDate(movie.getModDate())
                .build();

        //MovieDTO 내에 이미지들을 저장하는 리스트가 있음
        if(movieImages!= null && movieImages.size() > 0) {    // 6월 11일 오류테스트
            List<MovieImageDTO> movieImageDTOList = movieImages.stream().map(movieImage -> {
                return MovieImageDTO.builder()
                        .imgName(movieImage.getImgName())
                        .path(movieImage.getPath())
                        .uuid(movieImage.getUuid())
                        .build();
            }).collect(Collectors.toList());

            movieDTO.setImageDTOList(movieImageDTOList);
            movieDTO.setAvg(avg);
            movieDTO.setReviewCnt(reviewCnt.intValue());
        }
        return movieDTO;
    }//end entitiesToDTO
}
