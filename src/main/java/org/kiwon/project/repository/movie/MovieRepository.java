package org.kiwon.project.repository.movie;

import org.kiwon.project.entity.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    //Movie 객체와, MovieImage 객체 하나, double값으로 나오는 영화의 평균 평점, Long 타입의 리뷰개수를 Object[]로 반환
    @Query("select m, mi, avg(coalesce(r.grade,0)), count(distinct r) from Movie m " +
            "left outer join MovieImage mi on mi.movie = m " +
            "left outer join Review r on r.movie = m " +
            "group by m ")
    Page<Object[]> getListPage(Pageable pageable);

    //Movie객체, MovieImage객체, 영화의 평균 평점, 리뷰 개수를 Object[] 반환
    //MovieImage가 2개인 경우는 리스트내에 2개 요소가 있음
    @Query("select m, mi, avg(coalesce(r.grade,0)), count(distinct r) from Movie m " +
            "left outer join MovieImage mi on mi.movie = m " +
            "left outer join Review r on r.movie = m " +
            "where m.mno = :mno group by mi")
    List<Object[]> getMovieWithAll(Long mno);   //특정 영화 조회



}
