package com.example.memo.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@EntityListeners(value = AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "memotbl")
@Entity
public class Memo {
    // 테이블(memotbl) 컬럼 : mno, memo_text, create_date, update_date
    // 클래스 필드명 == 테이블 컬럼명
    // 클래스 필드명 != 테이블 컬럼명(@Column(name=""))

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="mno")
    private Long id;

    @Column(nullable = false, name = "memo_text")
    private String text; 

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime updateDate;


    // text 수정 메소드
    public void changeText(String text) {
        this.text = text;
    }
}
