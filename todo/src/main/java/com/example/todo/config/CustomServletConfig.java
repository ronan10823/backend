package com.example.todo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// CORS : 전역 설정
@Configuration
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // CorsRegistry = Cors 등록
        registry.addMapping("/**") // + /todos/**
                .allowedOrigins("*") // http://localhost:5173 직접 써도 상관없음, 배포하면 주소 변경됨
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .maxAge(300) // 서버 응답 대기 기본 시간 (5분, 300초)
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type"); // 오타 X
    }
}
