package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.LoginDTO;

import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;




@Controller
@RequestMapping("/member")
@Log4j2
public class LoginController {
    

    @GetMapping("/login")
    public void getLogin() {        
        log.info("로그인 페이지 요청");
    }

    // @PostMapping("/login")
    // public void postLogin(String id, String password) {
    // }

    // @PostMapping("/login")
    // public void postLogin(LoginDTO login) {
    //     log.info("login post");
    //     log.info("{}", login);
    // }

    @PostMapping("/login")
    public void postLogin(@ModelAttribute("login") LoginDTO login) {
        log.info("login post");
        log.info("{}", login);
    }


    // ?no=1&name=홍길동
    // @RequestParam : http 요청의 파라미터를 컨트롤러 메서드의 매개변수로 바인딩(연결)
    @GetMapping("path")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    

    // @RequestMapping(path="/test", method=RequestMethod.GET)
    // public String requestMethodName(@RequestParam String param) {
    //     return new String();
    // }
    // @RequestMapping(path="/test", method=RequestMethod.POST)
    // public String requestMethodName1(@RequestParam String param) {
    //     return new String();
    // }
    
    
}