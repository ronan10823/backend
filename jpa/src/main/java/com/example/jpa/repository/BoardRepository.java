package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 엔티티마다 하나씩 리파지토리를 만들어야 한다. 
    
}
