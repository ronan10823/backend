package com.example.jpa.entity;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "memotbl")
@Entity
public class Memo extends BaseEntity {
    // 테이블(memotbl) 컬럼 : mno, memo_text, create_date, update_date
    // 클래스 필드명 == 테이블 컬럼명
    // 클래스 필드명 != 테이블 컬럼명 (@Column(name="") 이 속성을 적절하게 사용하면 된다.)

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "mno")
    private Long id;

    @Column(nullable = false, name = "memo_text")
    private String text;

    // text revising method
    public void changeText(String text) {
        this.text = text;
    }

}
