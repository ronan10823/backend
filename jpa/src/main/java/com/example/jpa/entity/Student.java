package com.example.jpa.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;

@EntityListeners(value = AuditingEntityListener.class)
@Builder
@Table(name = "stutbl")
@Entity
public class Student {

    // @GeneratedValue(strategy = GenerationType.AUTO) == default (Hibernate 가 자동으로 생성)
    // @SequenceGenerator(name = "stu_seq_gen", sequenceName = "stu_deq", allocationSize = 1) //  직접 제어하고 싶을 때
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stu_seq_gen") 
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL(auto_increment), Oracle(sequence)
    @Id
    private Long id;

    // @Column(name = "sname", length = 50, nullable = false, unique = true) // same varchar(50) not null
    @Column(columnDefinition = "varchar(50) not null unique")
    private String name;

    @Column
    private String addr;
    
    
    @Column(columnDefinition = "varchar(1) CONSTRAINT chk_gender CHECK (gender IN ('M', 'F'))")
    private String gender;
    
    @CreationTimestamp // insert 시 자동으로 삽입
    private LocalDateTime createDateTime1;
    
    @CreatedDate // spring boot 설정 후 삽입
    private LocalDateTime createDateTime2;

}
