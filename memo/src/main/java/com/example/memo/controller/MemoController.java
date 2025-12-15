package com.example.memo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.memo.dto.MemoDTO;
import com.example.memo.service.MemoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RequestMapping("/memo")

@Log4j2
@Controller
public class MemoController {

    private final MemoService memoService; // call the readAll in MemoService

    @ResponseBody // 리턴값이 데이터
    @GetMapping("/hello")
    public String getHello() {
        return "Hello World"; // 문자열은 브라우저 해석 가능
    }

    // /memo/sample1/3

    @ResponseBody
    @GetMapping("/sample1/{id}")
    public MemoDTO getRead(@PathVariable Long id) {

        MemoDTO dto = memoService.read(id);
        return dto;
    }

    // ResponseEntity: 데이터 + 상태코드(200, 400, 500)
    @ResponseBody
    @GetMapping("/sample1/list")
    public ResponseEntity<List<MemoDTO>> getRead2() {

        List<MemoDTO> list = memoService.readAll();
        return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/list")
    public void getList(Model model) {
        log.info("전체 메모 요청");
        List<MemoDTO> list = memoService.readAll();
        model.addAttribute("list", list);
    }

    @GetMapping({ "/read", "/modify" })
    public void getRead(@RequestParam Long id, Model model) {
        log.info("memo id {}", id);

        // controller requests to the DB which users want.
        // DB will send you the result of every 'queries'.
        MemoDTO dto = memoService.read(id);
        model.addAttribute("dto", dto);

        // // 'memo/read?id=1' request
        // find template -> find /read in /memo with .html
        // // 'memo/modify?id=1' request
        // find template -> find /modify in /memo with .html in templates files.

    }

    @PostMapping("/modify")
    public String postModify(MemoDTO dto, RedirectAttributes rttr) {
        log.info("memo 수정{}", dto);

        Long id = memoService.modify(dto);

        // after success, I want to altert the success.
        // so, we will move to /memo/read and altert success. not the html.
        rttr.addAttribute("id", id);
        return "redirect:/memo/read";
    }

    @PostMapping("/remove")
    public String postRemove(@RequestParam Long id, RedirectAttributes rttr) {
        // form submit -> if you select 'form', many infos comes too.
        // but we can get those infoes in separate.
        log.info("memo remove id {}", id);

        memoService.remove(id);

        // after remove, move and show list
        // In controller, noramllty, the last line of controller method must find
        // 'Templates'.
        // 기본 html의 경우에는 실행해도 아무 내용도 없어서 실행 시 오류가 난다.
        // 그래서, 이 html을 불러들이면서도 데이터를 넣으려면
        // /list를 불러들여서 전체 메모를 넣어야 제대ㅑ로가능해진다.
        rttr.addFlashAttribute("msg", "삭제가 성공되었습니다.");

        return "redirect:/memo/list";
    }

    @GetMapping("/create")
    public void getCreate(@ModelAttribute("dto") MemoDTO dto) {
        log.info("추가 페이지 요청");
    }

    @PostMapping("/create")
    public String postCreate(@ModelAttribute("dto") @Valid MemoDTO dto, BindingResult result, RedirectAttributes rttr) {
        log.info("추가 요청 {} ", dto);

        // 유효성 검증 조건에 일치하지 않는 경우
        if (result.hasErrors()) {
            return "/memo/create";
        }

        // 일치하는 경우
        Long id = memoService.insert(dto);

        rttr.addFlashAttribute("msg", id + " 번 메모리가 삽입되었습니다.");
        return "redirect:/memo/list";
    }

}
