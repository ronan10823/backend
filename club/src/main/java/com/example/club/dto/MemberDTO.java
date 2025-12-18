package com.example.club.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class MemberDTO extends User {
    // member entity 정보 + 인증정보
    // member Entity 정보를 그대로 담을 수 있어야 한다.
    // 인증 정보도 담을 수 있어야 한다. > 단, 인증 정보는 제멋대로 선언하면 안된다. 어떤 식으로 선언해야 하는가?
    // Security User를 extense하는 방식으로 선언해야 한다.

    private String email;

    private String password;

    private String name;

    private Boolean fromSocial;

    public MemberDTO(String username, String password, boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities); // 매개 변수에 넣고 싶은 변수들을 넣어보면 된다.
        this.fromSocial = fromSocial;
        this.email = username;
    }
}
