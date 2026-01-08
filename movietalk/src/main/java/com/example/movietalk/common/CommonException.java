package com.example.movietalk.common;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ControllerAdvice
public class CommonException {

    @ExceptionHandler(NoResourceFoundException.class)
    public String notFound() {
        log.info("404에러 처리");
        return "/except/url404";
    }

    @ExceptionHandler(Exception.class)
    public String error(Exception e, Model model) {
        log.info("500 에러 처리", e);
        model.addAttribute("e", e);
        return "/except/500";
    }
}
