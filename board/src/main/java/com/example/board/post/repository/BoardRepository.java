package com.example.board.post.repository;

import java.util.List;
// import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.board.post.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {

    // on 구문 생략 기준 : 일치하는 칼럼
    @Query("select b, m from Board b join b.writer m")
    List<Object[]> getBoardWithWriterList();

    // bno를 사용해서 댓글 가져오기(삭제용)
    @Query("select b, r from Board b left join Reply r on r.board = b where b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    // 하나 조회 (목적: 하나의 게시글에 들어가면, 하단에 댓글을 나오게 하고 싶다. )
    @Query("select b, m, count(r) from Board b left join b.writer m left join Reply r on r.board = b where b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);

    // 목록화면 => 페이지 나누기 필요
    @Query(value = "select b, m, count(r) from Board b left join b.writer m left join Reply r on r.board = b group by b", countQuery = "select count(b) from Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

}
