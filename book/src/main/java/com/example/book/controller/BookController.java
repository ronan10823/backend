package com.example.book.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.book.dto.BookDTO;
// import com.example.book.dto.RegisterDTO;
import com.example.book.service.BookService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/book")
@Log4j2
@Controller
public class BookController {

    private final BookService bookService;

    @GetMapping("/register")
    public void getRegister(BookDTO dto) {
        log.info("등록 화면 요청 ");
    }

    @PostMapping("/register")
    public String postRegister(@Valid BookDTO dto, BindingResult result, RedirectAttributes rttr) {
        log.info("도서 등록 요청 {}", dto);

        if (result.hasErrors()) {
            return "/book/register";
        }

        String title = bookService.create(dto);
        rttr.addAttribute("msg", title + " 도서가 등록되었습니다.");

        return "redirect:/book/list";
    }

    @GetMapping("/list")
    public void getList(Model model) {

        log.info("목록 화면 요청");
        List<BookDTO> list = bookService.getList();
        model.addAttribute("list", list);
    }

    @GetMapping({ "/read", "/modify" })
    public void getRead(@RequestParam("id") Long id, Model model) {
        log.info("도서 상세 조회 {}", id);

        BookDTO dto = bookService.readId(id);
        model.addAttribute("dto", dto);

    }

    @PostMapping("/modify")
    public String postMethodName(BookDTO dto, RedirectAttributes rttr) {
        log.info("수정 요청 {}", dto);

        Long id = bookService.update(dto);

        rttr.addFlashAttribute("msg", "도서 정보가 수정되었습니다.");
        rttr.addAttribute("id", id);
        return "redirect:/book/read";
    }

    @PostMapping("/remove")
    public String postRemove(@RequestParam("id") Long id, RedirectAttributes rttr) {
        log.info("book 삭제 {}", id);
        bookService.delete(id);
        rttr.addFlashAttribute("msg", "도서가 삭제되었습니다.");
        return "redirect:/book/list";
    }

}
