package com.example.mart.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonSerializable.Base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
// import lombok.ToString;
import lombok.ToString;

@ToString(exclude = { "orderItems" })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "mart_item")
@Entity
public class Item extends BaseEntity {

    // id, name, price, quantity

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private int quantity;

    @Builder.Default
    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categoryItems = new ArrayList<>();

    // 양방향
    // @Builder.Default
    // @ManyToMany(mappedBy = "items")
    // private List<Category> categories = new ArrayList<>();

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
