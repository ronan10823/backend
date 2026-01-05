package com.example.movietalk.member.dto;

import com.example.movietalk.member.entity.constant.Role;

import groovy.transform.ToString;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CustomUserDTO {

    private Long mid;

    @NotBlank(message = "필수 입력 요소입니다.")
    @Email(message = "이메일 형식을 확인해주세요")
    private String email;

    @NotBlank(message = "필수 입력 요소입니다.")
    private String password;

    @NotBlank(message = "필수 입력 요소입니다.")
    private String nickname;

    private Role role;
}
