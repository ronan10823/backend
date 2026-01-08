package com.example.movietalk.movie.entity;

import com.example.movietalk.common.BaseEntity;
import com.example.movietalk.member.entity.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = { "movie", "member" })
@Entity
public class Review extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long rno;

    private int grade; // 리뷰점수

    private String text; // 리뷰

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "mno")
    private Movie movie;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "mid")
    private Member member;

    // 전체 setteㄱ는 적용하지 말고, 어지간하면 변경해야하는 것이 생기면 그것만 만들자.
    public void changeText(String text) {
        this.text = text;
    }

    public void changeGrade(int grade) {
        this.grade = grade;
    }
}
