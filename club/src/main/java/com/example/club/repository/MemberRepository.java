package com.example.club.repository;

// import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

import com.example.club.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

    // 구글 로그인 접근 여부
    @EntityGraph(attributePaths = { "roles" }, type = EntityGraphType.LOAD)
    @Query("select m from Member m where m.email=:email and m.fromSocial=:fromSocial")
    Optional<Member> findByEmailAndFromSocial(String email, boolean fromSocial);
}
