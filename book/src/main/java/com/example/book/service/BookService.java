package com.example.book.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.book.dto.BookDTO;
import com.example.book.dto.PageRequestDTO;
import com.example.book.dto.PageResultDTO;
import com.example.book.entity.Book;
import com.example.book.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper mapper;

    // crud 메소드를 호출하는 서비스 메소드 작성
    public String create(BookDTO bookDTO) {
        // bookDTO => entity 변경
        // 1. 코드작성
        // 2. ModelMapper 라이브러리 사용
        // Book book = mapper.map(bookDTO, Book.class);
        return bookRepository.save(mapper.map(bookDTO, Book.class)).getTitle();
    }

    // R(하나만 조회, 여러 개 조회)
    // 검색 : title => %자바%
    // isbn or id => 하나만 조회
    @Transactional(readOnly = true)
    public List<BookDTO> readTitle(String title) {

        List<Book> result = bookRepository.findByTitleContaining(title);

        // List<Book> => List<BookDTO> 변경 후 리턴
        // List<BookDTO> list = new ArrayList<>();
        // result.forEach(book -> {
        // list.add(mapper.map(book, BookDTO.class));
        // });

        return result.stream().map(book -> mapper.map(book, BookDTO.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookDTO readIsbn(String isbn) {

        Book book = bookRepository.findByIsbn(isbn).orElseThrow();

        // Optional<Book> => BookDTO 변경 후 리턴
        return mapper.map(book, BookDTO.class);
    }

    @Transactional(readOnly = true)
    public BookDTO readId(Long id) {

        Book book = bookRepository.findById(id).orElseThrow();

        // Optional<Book> => BookDTO 변경 후 리턴
        return mapper.map(book, BookDTO.class);
    }

    public Long update(BookDTO upDto) {
        Book book = bookRepository.findById(upDto.getId()).orElseThrow();
        book.changePrice(upDto.getPrice());
        book.changeDescription(upDto.getDescription());
        // return bookRepository.save(book).getId(); dirty checking
        return book.getId();
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PageResultDTO<BookDTO> getList(PageRequestDTO pageRequestDTO) {
        // pageNumber : 0 으로 시작(1page 개념)
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                Sort.by("id").descending());

        // findAll(Predicate predicate, Pageable pageable)
        Page<Book> result = bookRepository.findAll(bookRepository.makePredicate(null, null), pageable);

        // get() : Stream<Book>
        List<BookDTO> dtoList = result.get()
                .map(book -> mapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
        // 전체 행의 개수
        long totalCount = result.getTotalElements();

        // 넘어갈 때, DTO의 PageResultDTO<E>에 담아서 넘어간다.
        return PageResultDTO.<BookDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(totalCount)
                .build();
    }
}