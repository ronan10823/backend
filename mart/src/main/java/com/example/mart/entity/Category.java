package com.example.mart.entity;

import java.util.ArrayList;
import java.util.List;

// import java.util.ArrayList;
// import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.JoinTable;
// import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "category_id")
    private Long id;

    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "category")
    private List<CategoryItem> categoryItems = new ArrayList<>();
    // 다대다 관계 JPA 에게 직접 실행
    // 단점: 컬럼 추가 어려움 (실무에서 사용하기 어렵다)
    // @Builder.Default
    // @ManyToMany
    // @JoinTable(name = "category_item", joinColumns = @JoinColumn(name =
    // "category_id"), inverseJoinColumns = @JoinColumn(name = "item_id"))
    // private List<Item> items = new ArrayList<>();
    // 이제 내가 직접 CAtegory item을 직접 만드는 것이다.
}
