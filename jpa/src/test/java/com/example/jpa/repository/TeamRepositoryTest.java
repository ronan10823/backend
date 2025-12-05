package com.example.jpa.repository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Team;
import com.example.jpa.entity.TeamMember;

import jakarta.transaction.Transactional;

@SpringBootTest
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Test
    public void insertTest() {
        // TeamMember member = TeamMember.builder().name("홍길동").build();
        // teamMemberRepository.save(member);

        Team team = Team.builder().name("team1").build();
        teamRepository.save(team);

        TeamMember member = TeamMember.builder().name("홍길동").team(team).build();
        teamMemberRepository.save(member);
    }

    @Test
    public void insertTest2() {

        // Team team = Team.builder().id(1L).build();
        Team team = teamRepository.findById(3L).get();

        TeamMember member = TeamMember.builder().name("성춘향").team(team).build();
        teamMemberRepository.save(member);
    }

    @Test
    public void insertTest3() {
        // TeamMember member = TeamMember.builder().name("홍길동").build();
        // teamMemberRepository.save(member);

        Team team = Team.builder().name("team3").build();
        teamRepository.save(team);

    }

    @Test
    public void readTest() {

        // Team team = Team.builder().id(1L).build();
        Team team = teamRepository.findById(1L).get();
        System.out.println(team); // Team(id=1, name=null)

        // 외래키가 적용된 테이블이기 때문에 join 을 바로 해서 코드 실행
        TeamMember member = teamMemberRepository.findById(1L).get();
        System.out.println(member); // TeamMember(id=1, name=홍길동, team=Team(id=1, name=team1)) // TeamMember(id=1,
                                    // name=홍길동)

        // 팀원 => team check
        // System.out.println("팀 명 " + member.getTeam().getName());

        // 팀 -> 팀원 조회 (X)
    }

    @Transactional
    @Test
    public void readTest4() {

        TeamMember member = teamMemberRepository.findById(4L).get();
        System.out.println(member);
        // ......
        System.out.println(member.getTeam());
    }

    @Test
    public void updateTest() {

        // 팀 이름 변경
        // Team team = Team.builder().id(1L).build();
        Team team = teamRepository.findById(1L).get();
        team.changeName("플라워");
        teamRepository.save(team);
        System.out.println(team); // Team(id=1, name=null)

        // 팀 변경
        TeamMember teamMember = teamMemberRepository.findById(2L).get();
        // teamMember.changeTeam(Team team);
        teamMember.changeTeam(Team.builder().id(2L).build());
        System.out.println(teamMemberRepository.save(teamMember));
    }

    @Test
    public void deleteTest() {

        // 1. 팀원 삭제
        // 2. 삭제하려고 하는 팀의 팀원들을 다른 팀으로 변경

        // 팀원(팀 정보 이용해) 찾기
        List<TeamMember> result = teamMemberRepository.findByTeam(Team.builder().id(1L).build());
        result.forEach(m -> {
            m.changeTeam(Team.builder().id(2L).build());
            teamMemberRepository.save(m);
        });

        // 팀 삭제
        teamRepository.deleteById(1L);
    }

    @Test
    public void deleteTest2() {

        // 1. 팀원 삭제
        // 2. 삭제하려고 하는 팀의 팀원들을 다른 팀으로 변경

        // 팀원(팀 정보 이용해) 찾기
        List<TeamMember> result = teamMemberRepository.findByTeam(Team.builder().id(2L).build());
        // 팀원 삭제
        result.forEach(m -> {
            teamMemberRepository.save(m);
        });

        // 팀 삭제
        teamRepository.deleteById(2L);
    }

    // ------------- 팀 -> 멤버 조회 ---------------- //
    @Transactional
    @Test
    public void readTest2() {

        Team team = teamRepository.findById(3L).get();

        // 팀 => 팀원 조회 // 2개의 셀렉트 구문으로 가져온다.
        System.out.println(team); // select * from teamtbl where id = 3;
        System.out.println(team.getMembers()); // select * from tema_member where
        // team_id =3;
    }

    @Transactional
    @Test
    public void readTest3() {

        Team team = teamRepository.findById(3L).get();

        // 팀 => 팀원 조회( left join)
        System.out.println(team);
        // System.out.println(team.getMembers());
    }
}
