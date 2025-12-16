package com.example.board.reply.service;

// import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.post.entity.Board;
import com.example.board.reply.dto.ReplyDTO;
import com.example.board.reply.entity.Reply;
import com.example.board.reply.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Transactional
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;

    public Long create(ReplyDTO dto) {
        Reply reply = dtoToEntity(dto);
        return replyRepository.save(reply).getRno();
    }

    public void delete(Long rno) {
        replyRepository.deleteById(rno);
    }

    public Long update(ReplyDTO dto) {
        Reply reply = replyRepository.findById(dto.getRno()).get();
        reply.changeText(dto.getText());
        return reply.getRno();
    }

    @Transactional(readOnly = true)
    public ReplyDTO getRow(Long rno) {
        Reply reply = replyRepository.findById(rno).orElseThrow();
        return entityToDto(reply);
    }

    @Transactional(readOnly = true)
    public List<ReplyDTO> getList(Long bno) {

        Board board = Board.builder().bno(bno).build();
        List<Reply> result = replyRepository.findByBoardOrderByRno(board); // -> bno를 써서 찾을 수 없다. rno만으로만 찾을 수 있다.
        // 파라미터에 들어가는 변수가 왜 bno가 아니라 board인 걸까?

        // Reply => ReplyDTO 변경 후 리턴
        // 1) ModelMapper 이용 (구조 == Reply와 ReplyDTO == 가 완전히 동일할 때만 사용 가능)
        // 2) 변환하는 메소드 이용

        // // // 1) List로 하는 방법 (기본 코드)
        // List<ReplyDTO> list = new ArrayList<>();
        // for (Reply reply2 : result) {
        // ReplyDTO dto = entityToDto(reply2);
        // list.add(dto);
        // }
        // return list;

        // Stream 형식 사용 시
        // result.stream().map(reply ->
        // entityToDto(reply)).collect(Collectors.toList());
        return result.stream().map(this::entityToDto).collect(Collectors.toList());

    }

    public ReplyDTO entityToDto(Reply reply) {
        ReplyDTO dto = ReplyDTO.builder()
                .rno(reply.getRno())
                .text(reply.getText())
                .replyer(reply.getReplyer())
                .bno(reply.getBoard().getBno())
                .createDate(reply.getCreateDate())
                .updateDate(reply.getUpdateDate())
                .build();
        return dto;
    }

    public Reply dtoToEntity(ReplyDTO dto) {
        Reply reply = Reply.builder()
                .rno(dto.getRno())
                .text(dto.getText())
                .replyer(dto.getReplyer())
                .board(Board.builder().bno(dto.getBno()).build())
                .build();
        return reply;
    }
}
