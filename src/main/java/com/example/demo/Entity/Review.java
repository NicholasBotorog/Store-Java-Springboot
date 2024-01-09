package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "REVIEWS")
@RequiredArgsConstructor
@Data
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String content;

    @Column
    private int rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;
}
