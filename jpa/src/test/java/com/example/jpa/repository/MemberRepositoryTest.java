package com.example.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Member;
import com.example.jpa.entity.constant.RoleType;

@SpringBootTest
public class MemberRepositoryTest {
    // 이름은 마음대로 써도 되는데, 위에 있는 entity의 폴더 이름과 동일한 파일의 이름을 만들면 안된다. 
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertTest(){

        for (int i = 1; i < 11; i++) {
            Member member = Member.builder()
            .userId("userId" + i)
            .name("guest"+i)
            .role(RoleType.MEMBER)
            .build();

            memberRepository.save(member);
            
        }
    }

    @Test
    public void updateTest(){
        // userId9 인 사람의 role을 변경
        //  무조건 수정할 애들을 찾아야 한다. 
        Optional<Member> result = memberRepository.findById(9L); // userId9의 id는 9이다. 

        // result.get();
        result.ifPresent(member -> { // not null and existed
            member.changeRole(RoleType.ADMIN);
            memberRepository.save(member);
        });
    }

    @Test
    public void deleteTest(){
        memberRepository.deleteById(10L);
    }
    @Test
    public void readTest(){
        Optional<Member> result = memberRepository.findById(5L);
        result.ifPresent(member -> System.out.println(member));
    }
    @Test
    public void readTest2(){
        List<Member> members = memberRepository.findAll();
        members.forEach(member -> System.out.println(member));
    }

}

