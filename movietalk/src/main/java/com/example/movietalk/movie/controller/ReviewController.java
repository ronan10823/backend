package com.example.movietalk.movie.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RequestMapping("/reviews")
@Log4j2
@RequiredArgsConstructor
@RestController
public class ReviewController {
    // reviews로 들어오면, 한 게시물의 mno에 대한 댓글을 모두 가져올 것이다.
    // 특정 영화에 달려 있는 모든 영화 리뷰 가져오기 /reviews/300(mno)/all + GET -> 주소를 먼저 정해야 한다.
    @GetMapping("/{mno}/all")
    public String getReviews(@PathVariable Long mno) {
        return new String();
    }

    // 특정 영화의 리뷰 수정 (두 번으로 나눠진다)
    // 1) 영화 가져오기 /reviews/{mno}/rno + GET
    @GetMapping("/{mno}/{rno}")
    public String getReview(@PathVariable Long rno) {
        return new String();
    }

    // 2) 실제 리뷰 수정작업 /reviews/300(mno)/rno + PUT
    @PutMapping("/{mno}/{rno}")
    public String putReview(@PathVariable Long rno, @RequestBody String entity) {

        return entity;
    }

    // 특정 영화의 리뷰 삭제 /reviews/mno/rno + DELETE
    @DeleteMapping("/{mno}/{rno}")
    public String deleteReview(@PathVariable Long rno, @RequestBody String entity) {

        return entity;
    }

    // 특정 영화의 리뷰 추가 /reviews/mno + POST
    @PostMapping("/{mno}")
    public String postReview(@PathVariable Long mno) {

        return "";
    }

}
