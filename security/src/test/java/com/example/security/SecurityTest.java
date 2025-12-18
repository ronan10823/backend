package com.example.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class SecurityTest {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Test
    public void testEncoder() {
        String password = "1111";

        String encodePass = passwordEncoder.encode(password); // 암호화하는 메소드
        System.out.println("raw password " + password + " encode password " + encodePass);

        System.out.println(passwordEncoder.matches(password, encodePass)); // 매치하는 메소드
        System.out.println(passwordEncoder.matches("2222", encodePass));

    }
}
