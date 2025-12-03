package com.example.book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.book.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn); // where isbn = 'A10000' , 즉, like 개념이 없다. %와 같은 것도 없다.
    // unique라서 하나만 나오기 때무네 List -> Optional 로 변경

    List<Book> findByTitleContaining(String title); // = where title = '자바'
}
