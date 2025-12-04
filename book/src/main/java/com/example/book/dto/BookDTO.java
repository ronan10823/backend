package com.example.book.dto;

import groovy.transform.ToString;
import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @ToString
// @Setter
// @Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookDTO {
    private Long id;
    private String isbn;
    private String title;

    private String description;
    private int price;
    private String author;
}
