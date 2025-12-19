package com.example.club.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.club.handler.LoginSuccessHandler;

import lombok.extern.log4j.Log4j2;

// 시큐리티 설정 클래스 
@EnableWebSecurity // 모든 웹 요청에 대해 Security Filter Chain 적용
@Log4j2
@Configuration // 스프링 설정 클래스
public class SecurityConfig {

    @Bean // == 객체 생성
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/assets/**", "/member/auth", "/img/**").permitAll()
                .requestMatchers("/member/**").hasRole("USER")
                .requestMatchers("/manager/**").hasAnyRole("MANAGER")
                .requestMatchers("/admin/**").hasAnyRole("ADMIN"))
                // .httpBasic(Customizer.withDefaults());
                .formLogin(login -> login
                        .loginPage("/member/login").permitAll()
                        // .defaultSuccessUrl("/", true))
                        .successHandler(loginSuccessHandler()))
                .oauth2Login(login -> login.successHandler(loginSuccessHandler())) // 소셜 로그인 설정 가능하게 변경 중
                .logout(logout -> logout
                        .logoutUrl("/member/logout") // 로그아웃 post
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));

        return http.build();
    }

    @Bean
    LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // 운영, 실무에서 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // return new BCryptPasswordEncoder();
        // 연습용, 단일 알고리즘 사용
    }

}
