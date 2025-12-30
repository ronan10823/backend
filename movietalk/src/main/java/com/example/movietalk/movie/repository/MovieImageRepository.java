package com.example.movietalk.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.movietalk.movie.entity.Movie;
import com.example.movietalk.movie.entity.MovieImage;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {

    // 영화 movie를 기준으로 MovieImage 삭제
    @Modifying // commit 과 유사한 개념
    @Query("delete from MovieImage mi where mi.movie = :movie")
    void deleteByMovie(Movie movie);

}