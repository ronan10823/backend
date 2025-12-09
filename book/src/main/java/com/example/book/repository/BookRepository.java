package com.example.book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.book.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn); // where isbn = 'A10000' , 즉, like 개념이 없다. %와 같은 것도 없다.
    // unique라서 하나만 나오기 때무네 List -> Optional 로 변경

    List<Book> findByTitleContaining(String title); // = where title = '자바'

    // where author = ''
    List<Book> findByAuthor(String author);

    // where author like '%영'
    List<Book> findByAuthorEndingWith(String author);

    // where author like '박%'
    List<Book> findByAuthorStartingWith(String author);

    // where author like '%진수%'
    List<Book> findByAuthorContaining(String author);

    // 도서 가격이 12000 이상 35000 이하
    List<Book> findByPriceBetween(int startPrice, int EndPrice);

}
