package org.kiwon.project.repository.movie;

import org.kiwon.project.entity.movie.MovieImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {

    @Modifying
    @Query("delete from MovieImage m where m.movie.mno = :mno")
    void deleteByMno(Long mno);

}
