package com.example.book.dto;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDTO {

    // ISBN
    @NotEmpty(message = "공백일 수 없습니다")
    private String isbn;

    // TITLE
    @NotBlank(message = "도서명은 필수입니다")
    private String title;

    // AUTHOR
    @NotBlank(message = "저자명은 필수입니다")
    private String author;

    // PRICE
    @Range(min = 0, max = 1000000, message = "가격은 0 ~ 10,000,000 사이입니다.")
    @NotNull(message = "도서 가격은 필수입니다")
    private Integer price; // 왜 Integer 사용?
}
