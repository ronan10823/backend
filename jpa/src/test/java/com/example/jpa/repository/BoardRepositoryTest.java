package com.example.jpa.repository;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Board;
import com.example.jpa.entity.Item;
import com.example.jpa.entity.constant.ItemSellStatus;

@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRespository;

    // Board 10 개 삽입
    @Test
    public void insertTest() {
        for (int i = 1; i < 11; i++) {
            Board board = Board.builder()
                    .title("board title" + i)
                    .content("board content" + i)
                    .writer("writer" + i)
                    .build();

            boardRespository.save(board);
        }
    }

    // 수정 : title, content
    @Test
    public void updateTest() {
        // item 상태
        Board board = boardRespository.findById(1L).get();
        board.changeTitle("change title");
        board.changeContent("change content");
        boardRespository.save(board);
    }

    // 조회
    @Test
    public void readTest() {
        System.out.println(boardRespository.findById(2L).get());
    }

    @Test
    public void readTest2() {
        boardRespository.findAll().forEach(item -> System.out.println(item));
    }

    // 삭제
    @Test
    public void deleteTest() {
        boardRespository.deleteById(6L);
    }

    // 쿼리 메소드
    @Test
    public void queryMethodTest() {
        System.out.println(boardRespository.findByTitle("board title2"));
        System.out.println(boardRespository.findByContent("board content2"));
        System.out.println(boardRespository.findByTitleEndingWith("title3"));
        System.out.println(boardRespository.findByTitleContainingAndIdGreaterThanOrderByIdDesc("title", 3L));
        System.out.println(boardRespository.findByWriterContaining("writer"));
        System.out.println(boardRespository.findByTitleContainingOrContentContaining("title", "content"));
    }

    @Test
    public void queryMethodTest2() {
        System.out.println(boardRespository.findByTitle2("board title2"));
        System.out.println(boardRespository.findByContent2("board content2"));
        System.out.println(boardRespository.findByTitleEndingWith2("title3"));
        System.out.println(boardRespository.findByTitleAndId("title", 3L));
        System.out.println(boardRespository.findByWriterContaining2("writer"));
        System.out.println(boardRespository.findByTitleOrContent("title", "content"));
    }

    @Test
    public void queryMethodTest3() {
        System.out.println(boardRespository.findByTitleAndId2("title", 3L));
    }

    @Test
    public void queryMethodTest4() {

        List<Object[]> result = boardRespository.findByTitle3("title");
        for (Object[] objects : result) {
            // System.out.println(Arrays.toString(objects));
            String title = (String) objects[0];
            String writer = (String) objects[1];
            System.out.println();
        }
    }

}