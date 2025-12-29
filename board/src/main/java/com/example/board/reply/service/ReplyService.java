package com.example.board.reply.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.member.entity.Member;
import com.example.board.post.entity.Board;
import com.example.board.reply.dto.ReplyDTO;
import com.example.board.reply.entity.Reply;
import com.example.board.reply.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

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
        List<Reply> result = replyRepository.findByBoardOrderByRno(board);

        // Reply => ReplyDTO 변경 후 리턴
        // 1) ModelMapper 이용(구조가 완전히 동일)
        // 2) 변환하는 메소드 이용

        // List<ReplyDTO> list = new ArrayList<>();
        // for (Reply reply2 : result) {
        // ReplyDTO dto = entityToDto(reply2);
        // list.add(dto);
        // }
        // return list;

        // stream 형식 사용 시
        // return result.stream().map(reply ->
        // entityToDto(reply)).collect(Collectors.toList());
        // System.out::print => static 구조
        return result.stream().map(this::entityToDto).collect(Collectors.toList());

    }

    private ReplyDTO entityToDto(Reply reply) {
        ReplyDTO dto = ReplyDTO.builder()
                .rno(reply.getRno())
                .text(reply.getText())
                // .replyer(reply.getReplyer())
                .replyerEmail(reply.getReplyer().getEmail())
                .replyerName(reply.getReplyer().getName())
                .bno(reply.getBoard().getBno())
                .updateDate(reply.getUpdateDate())
                .createDate(reply.getCreateDate())
                .build();
        return dto;
    }

    private Reply dtoToEntity(ReplyDTO dto) {

        Member member = Member.builder()
                .email(dto.getReplyerEmail())
                .build();

        Reply reply = Reply.builder()
                .rno(dto.getRno())
                .text(dto.getText())
                // .replyer(dto.getReplyer())
                .replyer(member)
                .board(Board.builder().bno(dto.getBno()).build())
                .build();
        return reply;
    }
}
