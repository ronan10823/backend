package com.example.board.repository;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.member.entity.Member;
import com.example.board.member.entity.constant.MemberRole;
import com.example.board.member.repository.MemberRepository;
import com.example.board.post.dto.PageRequestDTO;
import com.example.board.post.entity.Board;
import com.example.board.post.repository.BoardRepository;
import com.example.board.reply.entity.Reply;
import com.example.board.reply.repository.ReplyRepository;

@Disabled
@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertMemberTest() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Member member = Member.builder()
                    .email("user" + i + "@gmail.com")
                    .password(passwordEncoder.encode("1111"))
                    .fromSocial(false)
                    .name("user" + i)
                    .build();
            member.addMemberRole(MemberRole.USER);

            if (i > 9) {
                member.addMemberRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        });
    }

    @Test
    public void insertBoardTest() {
        IntStream.rangeClosed(1, 100).forEach(i -> {

            int idx = (int) (Math.random() * 10) + 1;
            Member member = Member.builder().email("user" + idx + "@gmail.com").build();

            Board board = Board.builder()
                    .title("title...." + i)
                    .content("content...." + i)
                    .writer(member)
                    .build();
            boardRepository.save(board);
        });
    }

    @Test
    public void insertReplyTest() {
        IntStream.rangeClosed(1, 100).forEach(i -> {

            long idx = (long) (Math.random() * 100) + 1;
            Board board = Board.builder().bno(idx).build();

            int midx = (int) (Math.random() * 10) + 1;
            Member member = Member.builder().email("user" + midx + "@gmail.com").build();

            Reply reply = Reply.builder()
                    .text("reply...." + i)
                    .replyer(member).board(board).build();

            replyRepository.save(reply);
        });
    }

    // @Test
    // public void insertReplyTest2() {
    // Board board = Board.builder().bno(601L).build();

    // IntStream.rangeClosed(1, 15).forEach(i -> {

    // Reply reply = Reply.builder().text("reply...." + i).replyer("guest" +
    // i).board(board).build();

    // replyRepository.save(reply);
    // });
    // }

    // board 읽기
    @Transactional(readOnly = true)
    @Test
    public void readBoardTest() {
        // JPA 제공
        List<Board> list = boardRepository.findAll();
        list.forEach(board -> {
            System.out.println(board);
            System.out.println(board.getWriter());
        });
    }

    @Test
    public void getBoardWithWriterListTest() {

        // [Board(bno=98, title=title....98, content=content....98),
        // Member(email=user9@gmail.com, password=1111, name=user9)]
        List<Object[]> result = boardRepository.getBoardWithWriterList();
        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
        }
    }

    @Transactional(readOnly = true)
    @Test
    public void getBoardWithWriterTest() {
        // JPA
        Board board = boardRepository.findById(33L).get();
        System.out.println(board);
        // 댓글 가져오기
        System.out.println(board.getReplies());
    }

    @Test
    public void getBoardWithWriterTest2() {
        // JPQL(@Query)
        List<Object[]> result = boardRepository.getBoardWithReply(33L);
        // for (Object[] objects : result) {
        // System.out.println(Arrays.toString(objects));
        // }

        result.forEach(obj -> System.out.println(Arrays.toString(obj)));
    }

    // @Test
    // public void getBoardWithReplyCountTest() {

    // Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

    // Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);
    // // for (Object[] objects : result) {
    // // // System.out.println(Arrays.toString(objects));
    // // Board board = (Board) objects[0];
    // // Member member = (Member) objects[1];
    // // Long replyCnt = (Long) objects[2];
    // // System.out.println(board);
    // // System.out.println(member);
    // // System.out.println(replyCnt);
    // // }

    // // Stream<Object[]> data = result.get();
    // // Stream<Object[]> data2 = result.getContent().stream();

    // result.get().forEach(obj -> {
    // // System.out.println(Arrays.toString(obj);
    // Board board = (Board) obj[0];
    // Member member = (Member) obj[1];
    // Long replyCnt = (Long) obj[2];
    // });

    // // Object[] => String
    // Function<Object[], String> f = Arrays::toString;

    // // Object[] objects
    // result.get().forEach(obj -> System.out.println(f.apply(obj))); //
    // [Ljava.lang.Object;@2a19eaf0
    // }

    @Test
    public void getBoardByBnoTest() {
        Object result = boardRepository.getBoardByBno(601L);
        Object[] arr = (Object[]) result;
        System.out.println(Arrays.toString(arr));
    }

    // delete 테스트
    @Commit
    @Transactional
    @Test
    public void deleteByBnoTest() {
        replyRepository.deleteByBno(91L);
        boardRepository.deleteById(91L);
    }

    // querydsl 테스트

    @Test
    public void listTest() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(0)
                .size(20)
                .type("tcw")
                .keyword("title")
                .build();

        // Pageable pageable = PageRequest.of(pageRequestDTO.getPage(),
        // pageRequestDTO.getSize(),
        // Sort.by("bno").descending().and(Sort.by("title").ascending()));

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize());
        Page<Object[]> result = boardRepository.list(pageRequestDTO.getType(), pageRequestDTO.getKeyword(), pageable);

        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
        }
    }

}
