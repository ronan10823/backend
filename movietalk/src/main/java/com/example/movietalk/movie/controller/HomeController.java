package com.example.movietalk.movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHome() {
        return "redirect:/movie/list";
    }

    // // 존재하지 않는 url -> /error 내부 포워딩
    // @GetMapping("/error")
    // public String getError() {
    // return "/except/url404";
    // }

    @GetMapping("/access/denied")
    public void getDenied() {
    }

}
