package com.example.memo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memo.dto.MemoDTO;
import com.example.memo.service.MemoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
// @RequestMapping("/memo2")
@Log4j2
@RestController // 데이터(JSON)만 주고 받기
public class BasicController {
    // 자바 객체 <-> json

    private final MemoService memoService;

    @GetMapping("/hello")
    public String getHello() {
        return "Hello World"; // 문자열은 브라우저 해석 가능
    }

    // http://localhost:8080/sample1/5
    @GetMapping("/sample1/{id}")
    public MemoDTO getRead(@PathVariable Long id) {

        MemoDTO dto = memoService.read(id);
        return dto;
    }

    @GetMapping("/list")
    public List<MemoDTO> getList() {
        log.info("전체 메모 요청");
        List<MemoDTO> list = memoService.readAll();
        return list;
    }
}
