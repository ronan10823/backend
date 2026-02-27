package com.example.novels.service;

import java.time.LocalDate;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.novels.novel.dto.NovelDTO;
import com.example.novels.novel.service.NovelService;

// @Disabled
@SpringBootTest
public class NovelServiceTest {

    @Autowired
    private NovelService novelService;

    @Test
    public void aiGenerateTest() {

        NovelDTO dto = NovelDTO.builder()
                .title("그만 배우기의 기술")
                .author("팻 플린")
                .available(false)
                .publishedDate(LocalDate.of(2026, 2, 6))
                .summary(
                        """
                                원제 “린 러닝Lean Learning”의 의미처럼 “군살을 뺀 학습”이라는 역설적 발상의 책에서 팻 플린은 “덜 배워서 더 이루는” 궁극의 학습법을 제시한다. 이 책은 넘치는 영감을 현실의 성과로 바꾸는 4단계 프레임워크와 함께 당신을 ‘지식 완벽주의’의 덫에서 건져낼 강력한 무기들을 공개한다.
                                """)
                .gid(4L)
                .build();

        Long id = novelService.create(dto);
        // novelService.generateDescription(id);
    }
}
