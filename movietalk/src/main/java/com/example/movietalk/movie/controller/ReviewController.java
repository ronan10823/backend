package com.example.movietalk.movie.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movietalk.movie.dto.ReviewDTO;
import com.example.movietalk.movie.service.ReviewServive;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final ReviewServive reviewServive;

    // reviews로 들어오면, 한 게시물의 mno에 대한 댓글을 모두 가져올 것이다.
    // 특정 영화에 달려 있는 모든 영화 리뷰 가져오기 /reviews/300(mno)/all + GET -> 주소를 먼저 정해야 한다.
    @GetMapping("/{mno}/all")
    public List<ReviewDTO> getReviews(@PathVariable Long mno) {
        log.info("특정 영화의 리뷰 요청 {}", mno);
        List<ReviewDTO> list = reviewServive.getList(mno);
        return list;
    }

    // 특정 영화의 리뷰 수정 (두 번으로 나눠진다)
    // 1) 영화 가져오기 /reviews/{mno}/rno + GET
    @GetMapping("/{mno}/{rno}")
    public ReviewDTO getReview(@PathVariable Long rno) {
        log.info("특정 영화의 특정 리뷰 요청 {}", rno);

        return reviewServive.getRow(rno);
    }

    // 2) 실제 리뷰 수정작업 /reviews/300(mno)/rno + PUT
    @PutMapping("/{mno}/{rno}")
    public ResponseEntity<Long> putReview(@PathVariable Long rno, @RequestBody ReviewDTO dto) {
        log.info("특정 영화의 특정 리뷰 수정 {}", dto);
        rno = reviewServive.updateRow(dto);

        // 상태에 대한 코드를 같이 보내고 싶다면? response Entity를 써라.
        // HttpStatus.ACCEPTED;
        return new ResponseEntity<Long>(rno, HttpStatus.valueOf(200));

    }

    // 특정 영화의 리뷰 삭제 /reviews/mno/rno + DELETE
    @DeleteMapping("/{mno}/{rno}")
    public ResponseEntity<String> deleteReview(@PathVariable Long rno) {
        log.info("특정 영화의 삭제 {}", rno);

        reviewServive.deleteRow(rno);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    // 특정 영화의 리뷰 추가 /reviews/mno + POST
    @PostMapping("/{mno}")
    public Long postReview(@PathVariable Long mno, @RequestBody ReviewDTO dto) {
        // 추가하는 거라서 내용을 받아서 보내야 하기에, @RequestBody가 필요하다.
        log.info("특정 영화 특정 리뷰 추가 {}", dto);
        // ReviewDTO dto 받은 걸 넘기면 된다.
        // 왼쪽 = 새롭게 들어간 댓글 번호
        Long rno = reviewServive.insertRow(dto);
        return rno;
    }

}
