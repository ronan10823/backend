package com.example.movietalk.member.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Setter
@ToString
@Getter
public class AuthUserDTO extends User {

    private CustomUserDTO customUserDTO;
    // Collection : List, Set
    // List<SimpleGrantedAuthority> list = new ArrayList<>();
    // list.add("ROLE_");
    // List.of(new CustomUserDTO(), new CustomUserDTO())

    // User(부모)의 디폴트 생성자가 없다. 그래서 자식 클래스에서 만들어야 한다.
    // 반드시 있어야 한다.
    public AuthUserDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthUserDTO(CustomUserDTO customUserDTO) {
        super(customUserDTO.getEmail(), customUserDTO.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + customUserDTO.getRole())));
        // Collection 형태로 붙여야 한다. GranteddAuthority이거나, 이걸 상속하는 모양으로 Collection의 형태여야
        // 한다.
        // GrantedAuthority 는 interface -> 구현하거나, SimpleGrantedAuthority를 선언하면 된다.
        // 하려는 이야기 = super의 세 번째 인자에는 List를 선언하거나, List.of + GrantedAuthority를 상속/선언해야
        // 한다.
        this.customUserDTO = customUserDTO;
    }

}
