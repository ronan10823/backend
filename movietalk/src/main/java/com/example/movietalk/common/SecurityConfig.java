package com.example.movietalk.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                .anyRequest().permitAll());

        http.formLogin(login -> login.loginPage("/member/login"));

        // .successHandler(loginSuccessHandler()).permitAll()

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

        return http.build();
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

    // @Bean
    // LoginSuccessHandler loginSuccessHandler() {
    // return new LoginSuccessHandler();
    // }

    @Bean
    PasswordEncoder passwordEncoder() {
        // 운영, 실무, 여러 암호화 알고리즘 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // 연습, 단일 알고리즘 사용
        // return new BCryptPasswordEncoder();
    }

}
