package com.example.board.member.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.board.member.dto.MemberDTO;
import com.example.board.member.dto.RegisterDTO;
import com.example.board.member.entity.Member;
import com.example.board.member.entity.constant.MemberRole;
import com.example.board.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Setter
@ToString
@Log4j2
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void register(RegisterDTO dto) throws IllegalStateException {

        // 중복 이메일 확인
        Optional<Member> result = memberRepository.findById(dto.getEmail());
        if (result.isPresent()) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

        Member member = Member.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .fromSocial(false)
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
        member.addMemberRole(MemberRole.USER);
        memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("clubservice username {}", username);

        // orElseThrow() => NoSuchElementException
        Member member = memberRepository.findByEmailAndFromSocial(username, false)
                .orElseThrow(() -> new UsernameNotFoundException("이메일 확인")); // Supplier<? extends X> exceptionSupplier
        // member => MemberDTO
        // [user,manager,admin]
        MemberDTO dto = new MemberDTO(member.getEmail(),
                member.getPassword(), member.isFromSocial(),
                member.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toSet()));

        dto.setName(member.getName());

        return dto;
    }

}
