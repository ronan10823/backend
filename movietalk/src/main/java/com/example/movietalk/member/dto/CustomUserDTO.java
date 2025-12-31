package com.example.movietalk.member.dto;

import org.springframework.context.annotation.Role;

import groovy.transform.ToString;
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

    private String email;
    private String password;
    private String nickname;

    private Role role;
}
