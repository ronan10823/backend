package com.example.movietalk.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.movietalk.member.dto.AuthUserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        AuthUserDTO dto = (AuthUserDTO) authentication.getPrincipal();

        List<String> roleNames = new ArrayList<>();
        dto.getAuthorities().forEach(auth -> {
            roleNames.add(auth.getAuthority());
        });

        if (roleNames.contains("ROLE_ADMIN")) {
            response.sendRedirect("/movie/create");
            return;
        } else if (roleNames.contains("ROLE_MEMBER")) {
            response.sendRedirect("/member/profile");
            return;
        }
        response.sendRedirect("/");

    }
}
