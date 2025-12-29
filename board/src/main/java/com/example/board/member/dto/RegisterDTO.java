package com.example.board.member.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class RegisterDTO {

    // 회원가입용
    @Email(message = "이메일 확인")
    @NotBlank(message = "필수입력요소")
    private String email;

    @NotBlank(message = "필수입력요소")
    private String password;

    @NotBlank(message = "필수입력요소")
    private String name;

    private boolean fromSocial;
}
