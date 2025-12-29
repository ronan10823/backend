package com.example.movietalk.movie.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieDTO {

    private Long mno;
    private String title;

    // 영화이미지
    @Builder.Default
    private List<MovieImageDTO> movieImages = new ArrayList<>();

    private double avg;

    private Long reviewCnt;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

}
