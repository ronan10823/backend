package com.example.movietalk.movie.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movietalk.member.entity.Member;
import com.example.movietalk.movie.dto.ReviewDTO;
import com.example.movietalk.movie.entity.Movie;
import com.example.movietalk.movie.entity.Review;
import com.example.movietalk.movie.repository.MovieRepository;
import com.example.movietalk.movie.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Transactional
@RequiredArgsConstructor
@Log4j2
@Service
public class ReviewServive {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    // @Autowired @Required를 쓰지 않을 거면 이렇게 쓰면 된다.
    // private ReviewRepository repository;

    public Long insertRow(ReviewDTO dto) {
        // DTO -> entity
        Review review = dtoToEntity(dto);

        // 1. save를 하려면 Review 모양으로 만들어야 한다.
        return reviewRepository.save(review).getRno();
        // 3. review에 저장을 하고, Rno를 가져와서 반환

        // return reviewRepository.save(dtoToEntity(dto).getRno());로 해도 된다.
    }

    public void deleteRow(Long rno) {
        reviewRepository.deleteById(rno);
    }

    public Long updateRow(ReviewDTO dto) {
        // 업데이트는 무조건 업데이트할 대상을 찾아와야 한다.
        // findbyid 로 DB에 있는 dto의 rno에 해당하는 내용을 가져온다. 그래서 review에 담아준다.
        Review review = reviewRepository.findById(dto.getRno()).get();
        // 그 이후에 변경 사항을 적용해준다.
        review.changeText(dto.getText());
        review.changeGrade(dto.getGrade());
        // save() 를 호출하지 않는 이유는 dirty checking 때문
        return review.getRno();
    }

    @Transactional(readOnly = true)
    public ReviewDTO getRow(Long rno) {

        // Review review = reviewRepository.findById(rno).get();
        // ReviewDTO dto = entityToDTO(review);
        // return dto;

        return entityToDTO(reviewRepository.findById(rno).get());
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getList(Long mno) {

        Movie movie = movieRepository.findById(mno).get();
        List<Review> reviews = reviewRepository.findByMovie(movie);

        // // entity를 dto로 변환해서 어떻게 담는지 방법 1
        // List<ReviewDTO> list = new ArrayList<>();
        // reviews.forEach(review -> {
        // ReviewDTO dto = entityToDTO(review);
        // list.add(dto);
        // });

        // entity 를 dto로 변환해서 어떻게 담는지 방법 2
        List<ReviewDTO> list = reviews.stream()
                .map(review -> entityToDTO(review))
                .collect(Collectors.toList());
        return list;
    };

    private Review dtoToEntity(ReviewDTO dto) {
        // ReviewDTO => Review
        Review review = Review.builder()
                .rno(dto.getRno())
                .grade(dto.getGrade())
                .text(dto.getText())
                // 중요!!!!!! 어떤 영화에 대한 review인지를 반드시 알려줘야 한다! 왜? entity에 movie가 관계가 있기 때문!
                .movie(Movie.builder().mno(dto.getMno()).build())
                .member(Member.builder().mid(dto.getMid()).build())
                .build();
        return review;
    }

    // Review 모양이고, 내가 하고 싶은건 ReviewDTO에 넣고 싶다.
    private ReviewDTO entityToDTO(Review review) {
        // 무엇을 가져올지는 movieRepositoryTest에서 이미 결정해놓았다.
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .rno(review.getRno())
                .grade(review.getGrade())
                .text(review.getText())
                .email(review.getMember().getEmail())
                .mid(review.getMember().getMid())
                .nickname(review.getMember().getNickname())
                .mno(review.getMovie().getMno())
                .createDate(review.getCreateDate())
                .updateDate(review.getUpdateDate())
                .build();
        return reviewDTO;
    }
}
