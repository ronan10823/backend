package com.example.movietalk.movie.dto;

import java.time.LocalDateTime;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ReviewDTO {
    // dto는 화면에 무엇을 보여주고 싶은지 그 항목만 작성하면 된다.

    private Long rno;

    private int grade;

    private String text;

    // review 단에는 movie 자체보다는 mno만 필요하다.
    private Long mno;

    // Member 의 mid, email, nickname 을 필요로 한다.
    private Long mid;
    private String email;
    private String nickname;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
