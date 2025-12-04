package com.example.book.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.book.dto.BookDTO;
import com.example.book.entity.Book;
import com.example.book.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper mapper;

    // crud 메소드 호출하는 서비스 메소드 작성
    public String create(BookDTO bookDTO) {
        // bookDTO -> entity 변경
        // 1. 코드 작성
        // 2. ModelMapper 라이브러리 사용
        // Book book = mapper.map(bookDTO, Book.class)

        return bookRepository.save(mapper.map(bookDTO, Book.class)).getTitle();
        // save로 하면, 원래 Book 새로 돌려준다.
        // 원래는 getId()를 가져와서 return했다. 리턴 여부는 임의로 결정하면 된다.
    }

    // R (하나만 조회, 여러 개 조회)
    // 검색: title => %title% (무엇을 검색하느냐에 따라서 여러 개가 나올수도 있고, 하나만 나올 수도 있다. )
    // isbn => 결과가 한 개밖에 없다.
    // id => 하나만 조회

    public void readTitle(String title) {

        // bookRepository.findBy 우리는 이것만 알고 있다. 그러면 어떻게 하냐?
        // 만들어야 한다.
        List<Book> result = bookRepository.findByTitleContaining(title);

        // result를 바로 반환할 수는 없고, List<Book> -> List<BookDTO>로 변경 후 리턴
        // List<BookDTO> list = new ArrayList<>();
        // result.forEach(book -> {
        // mapper.map(book, BookDTO.class);
        // });

        result.stream().map(book -> mapper.map(book, BookDTO.class)).collect(Collectors.toList());
    }

    public BookDTO readIsbn(String isbn) {

        Book book = bookRepository.findByIsbn(isbn).orElseThrow();

        // Optional<Book> -> Optional<BookDTO>로 변경 후 result 리턴
        return mapper.map(book, BookDTO.class);

    }

    public BookDTO readId(Long id) {

        Book book = bookRepository.findById(id).orElseThrow();

        // Optional<Book> -> Optional<BookDTO>로 변경 후 result 리턴
        return mapper.map(book, BookDTO.class);
    }

    public Long update(BookDTO upDto) {
        // 몇 번을 바꿀거고, 어떤 정보로 바꿀거야? -> 2개의 정보 필요 -> 2개의 정보 정도는 DTO에서 찾는다.
        Book book = bookRepository.findById(upDto.getId()).orElseThrow();
        book.changePrice(upDto.getPrice());
        book.changeDescription(upDto.getDescription());
        return bookRepository.save(book).getId();
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public List<BookDTO> getList() {
        List<Book> result = bookRepository.findAll();
        return result.stream()
                .map(book -> mapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
    }
}
