package com.example.board.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.board.post.dto.BoardDTO;
import com.example.board.post.dto.PageRequestDTO;
import com.example.board.post.dto.PageResultDTO;
// import com.example.board.post.entity.Board;
import com.example.board.post.service.BoardService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RequestMapping("/board")
@Log4j2
@Controller
public class PostController {

    private final BoardService boardService;

    @GetMapping("/create")
    public void getCreate(BoardDTO dto, PageRequestDTO pageRequestDTO) {
        log.info("작성 폼 요청");
    }

    @PostMapping("/create")
    public String postCreate(@Valid BoardDTO dto, BindingResult result, PageRequestDTO pageRequestDTO,
            RedirectAttributes rttr) {
        log.info("작성 {}", dto);

        if (result.hasErrors()) {
            return "/board/create";
        }

        // 서비스 호출
        Long bno = boardService.insert(dto);

        rttr.addFlashAttribute("msg", bno + "번 게시물 등록 완료");
        return "redirect:/board/list";
    }

    @PostMapping("/remove")
    public String postDelete(BoardDTO dto, PageRequestDTO pageRequestDTO, RedirectAttributes rttr) {
        log.info("삭제 {} {}", dto, pageRequestDTO);

        boardService.update(dto);

        rttr.addFlashAttribute("msg", "게시글 삭제가 완료되었습니다.");
        rttr.addAttribute("page", pageRequestDTO.getPage());
        rttr.addAttribute("size", pageRequestDTO.getSize());
        return "redirect:/board/list";
    }

    @GetMapping({ "/read", "/modify" })
    public void getRead(@RequestParam("bno") Long bno, Model model, PageRequestDTO pageRequestDTO) {
        log.info("get or modify {}", bno);

        BoardDTO dto = boardService.getRow(bno);
        model.addAttribute("dto", dto);
    }

    @PostMapping("/modify")
    public String postModify(BoardDTO dto, PageRequestDTO pageRequestDTO, RedirectAttributes rttr) {
        log.info("수정 {} {}", dto, pageRequestDTO);

        boardService.update(dto);

        rttr.addFlashAttribute("msg", "게시글 수정이 완료되었습니다.");
        rttr.addAttribute("bno", dto.getBno());
        rttr.addAttribute("page", pageRequestDTO.getPage());
        rttr.addAttribute("size", pageRequestDTO.getSize());
        return "redirect:/board/read";
    }

    // ?page=1&size=10
    @GetMapping("/list")
    public void getList(PageRequestDTO pageRequestDTO, Model model) {
        log.info("list 요청 {}", pageRequestDTO);

        PageResultDTO<BoardDTO> result = boardService.getList(pageRequestDTO);

        model.addAttribute("result", result);
    }

}
