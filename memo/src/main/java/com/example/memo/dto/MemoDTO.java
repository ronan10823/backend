package com.example.memo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MemoDTO {
    // 지금은 엔티티 모양이랑 동일하게 만들 것이다. 

    private Long id;

    @NotBlank(message = "메모 내용은 필수입력 요소입니다")
    private String text;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
