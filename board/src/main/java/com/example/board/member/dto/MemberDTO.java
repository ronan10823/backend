package com.example.board.member.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class MemberDTO extends User implements OAuth2User {

    // member entity 정보 + 인증정보
    private String email;

    private String password;

    private String name;

    private boolean fromSocial;

    // OAuth2User 가 넘겨주는 attr 담기 위해
    private Map<String, Object> attr;

    public MemberDTO(String username, String password, boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.fromSocial = fromSocial;
        this.email = username;
        this.password = password;
    }

    // OAuth2User
    public MemberDTO(String username, String password, boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities, Map<String, Object> attr) {
        this(username, password, fromSocial, authorities);
        this.attr = attr;
    }

    // OAuth2User
    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }
}
