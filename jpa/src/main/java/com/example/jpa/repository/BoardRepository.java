package com.example.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

import com.example.jpa.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 엔티티마다 하나씩 리파지토리를 만들어야 한다.

    // title = ?
    List<Board> findByTitle(String title);

    // content = ?
    List<Board> findByContent(String content);

    // title = %?
    List<Board> findByTitleEndingWith(String title);

    // title = %?% and id > 0 order by id desc
    List<Board> findByTitleContainingAndIdGreaterThanOrderByIdDesc(String title, Long id);

    // writer = %?%
    List<Board> findByWriterContaining(String writer);

    // title like %?% or content like %?%
    List<Board> findByTitleContainingOrContentContaining(String title, String content);

    // Query Annotation : Entity 기준

    // @Query("select b from Board b where b.title = ?1") // 반드시 대문자를 사용해야 한다.
    // Boardl. 왜? entity에서 클래스 이름이 Board였으니까.
    @Query("select b from Board b where b.title = :title") // 반드시 대문자를 사용해야 한다. Boardl. 왜? entity에서 클래스 이름이 Board였으니까.
    List<Board> findByTitle2(String title); // findby와 Query Annotaino이 있을때, @Query가 우선시된다.

    @Query("select b.title, b.writer from Board b where b.title like %:title%")
    List<Object[]> findByTitle3(String title);

    @Query("select b from Board b where b.content = :content")
    List<Board> findByContent2(String content);

    // @Query("select b from Board b where b.title like %?1")
    @Query("select b from Board b where b.title like %:title")
    List<Board> findByTitleEndingWith2(String title);

    @Query("select b from Board b where b.title like %?1% and b.id > ?2 order by b.id desc")
    List<Board> findByTitleAndId(String title, Long id);

    @Query("select b from Board b where b.writer like %?1%")
    List<Board> findByWriterContaining2(String writer);

    @Query("select b from Board b where b.title like %?1% or b.content like %?2%")
    List<Board> findByTitleOrContent(String title, String content);

    // @Query + nativeQuery(sql 구문 형식) -> 굳이 대문자로 쓸 필요는 없다.
    @Query(value = "select b.* from boardtbl b where b.title like concat('%',:title,'%') and b.id > :id order by b.id desc", nativeQuery = true)
    List<Board> findByTitleAndId2(String title, Long id);
}
