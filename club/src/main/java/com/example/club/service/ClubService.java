package com.example.club.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.club.dto.MemberDTO;
import com.example.club.entity.Member;
import com.example.club.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Setter
@ToString
@Log4j2
public class ClubService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("clubservice username {}", username);

        // .orElseThrow(); 기본으로 하면 => NoSuchElementException 얘가 나온다.
        // 어차피 이 메소드는 UsernameNotFoundException를 무조건 날리게 되어있다.
        Member member = memberRepository.findByEmailAndFromSocial(username, false)
                .orElseThrow(() -> new UsernameNotFoundException("이메일 확인"));
        // Supplier<? extends X> exceptionSupplier 이 모양을 하면 nosuchelementexception을 제거할
        // 수 있다.

        // member => MemberDTO

        return new MemberDTO(username, username, false, null);
    }

}
