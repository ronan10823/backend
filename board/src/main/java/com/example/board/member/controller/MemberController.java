package com.example.board.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.board.member.dto.RegisterDTO;
import com.example.board.member.service.MemberService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/member")
@RequiredArgsConstructor
@Log4j2
@Controller
public class MemberController {

    private final MemberService clubService;

    @GetMapping("/login")
    public void getLogin() {
        log.info("로그인 폼 요청");
    }

    @GetMapping("/register")
    public void getRegister(RegisterDTO dto) {
        log.info("회원가입 폼 요청");
    }

    @PostMapping("/register")
    public String postRegister(@Valid RegisterDTO dto, BindingResult result, RedirectAttributes rttr) {
        log.info("회원가입 요청 {}", dto);

        if (result.hasErrors()) {
            return "/member/register";
        }

        // service 작업
        try {
            clubService.register(dto);
        } catch (Exception e) {
            // rttr.addAttribute(null, rttr)
            rttr.addFlashAttribute("dupEmail", e.getMessage());
            return "redirect:/member/register";
        }

        return "redirect:/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public void getProfile() {
        log.info("프로필 폼 요청");
    }

    // org.springframework.security.core.Authentication
    @ResponseBody
    @GetMapping("/auth")
    public Authentication getAuthInfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication;
    }
}
