package com.example.todo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "todotbl")
@Entity
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // TINYITN: 1BYTE(-128-127)
    @Column(columnDefinition = "tinyint DEFAULT 0")
    private boolean completed;

    @Column(columnDefinition = "tinyint DEFAULT 0")
    private boolean important;

    // title, completed, important 변경 가능
    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeCompleted(boolean completed) {
        this.completed = completed;
    }

    public void changeImportant(boolean important) {
        this.important = important;
    }
}
