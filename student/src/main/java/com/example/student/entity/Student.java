package com.example.student.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
// import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.student.entity.constant.Grade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EntityListeners(value = AuditingEntityListener.class)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @Column
    private String addr;
    
    @Column(columnDefinition = "varchar(1) CONSTRAINT chk_gender CHECK (gender IN ('M', 'F'))")
    private String gender;
    
    // grade => FRESHMAN, SOPHOMORE, JUNIOR, SENIOR
    @Enumerated(EnumType.STRING) // 기본값은 숫자인 0부터 시작
    @Column
    private Grade grade;
    
    @CreatedDate // spring boot 설정 후 삽입
    private LocalDateTime createDateTime;

    @LastModifiedDate
    private LocalDateTime upDateTime;

    public void changeName(String name) {
        this.name = name;
    }

    public void changeGrade(Grade grade) {
        this.grade = grade;
    }
}
