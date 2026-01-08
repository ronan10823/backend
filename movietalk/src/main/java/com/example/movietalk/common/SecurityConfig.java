package com.example.movietalk.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import lombok.extern.log4j.Log4j2;

@EnableMethodSecurity // @PreAuthorize, @PostAuthorize 가능
@EnableWebSecurity // 모든 웹 요청에 대해 Security Filter Chain 적용
@Log4j2
@Configuration // 스프링 설정 클래스
public class SecurityConfig {

    // 시큐리티 설정 클래스

    @Bean // == 객체 생성
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/assets/**", "/img/**", "/js/**").permitAll()
                // exception handler가 먼저 걸려서 다르게 해야한다.
                // 그래서 무엇을 열어줘야 하는지 말해줘야 한다
                .requestMatchers("/movie/list").permitAll()
                .requestMatchers("/movie/create").hasRole("ADMIN")
                .requestMatchers("/movie/register").permitAll()
                .requestMatchers("/upload/display/**").permitAll()
                .anyRequest().authenticated()); // 지금은 다 인증받아야 하는 것으로 변경되었다.

        http.formLogin(login -> login
                .loginPage("/member/login").permitAll()
                // .defaultSuccessUrl("/movie/list", true) 로그인 성공 후 정해진 경로가 단순할 때
                .successHandler(loginSuccessHandler())); // 권한에 따라 서로 다르게 가야하는 경우

        // http.oauth2Login(login -> login.successHandler(loginSuccessHandler()));
        // 로그아웃 (controller에서는 post)
        http.logout(logout -> logout
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/"));

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        // csrf 기능 중지
        // http.csrf(csrf -> csrf.disable());
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/upload/**"));

        // http.rememberMe(remember -> remember.rememberMeServices(rememberMeServices));

        // 접근 제한 처리 (Security 설정에 핸들러 연결)
        http.exceptionHandling(e -> e.accessDeniedHandler(customAccessDeniedHandler()));

        return http.build();
    }

    // 핸들러를 Bean으로 연결
    @Bean
    CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    // @Bean
    // RememberMeServices rememberMeServices(UserDetailsService userDetailsService)
    // {
    // // 토큰 생성용 알고리즘
    // RememberMeTokenAlgorithm eTokenAlgorithm = RememberMeTokenAlgorithm.SHA256;

    // TokenBasedRememberMeServices services = new
    // TokenBasedRememberMeServices("myKey", userDetailsService,
    // eTokenAlgorithm);
    // // 브라우저에서 넘어온 remember-me 쿠키 검증용 알고리즘
    // services.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5);
    // // 7일 유효기간
    // services.setTokenValiditySeconds(60 * 60 * 24 * 7);
    // return services;
    // }

    @Bean
    LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // 운영, 실무, 여러 암호화 알고리즘 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // 연습, 단일 알고리즘 사용
        // return new BCryptPasswordEncoder();
    }

}
