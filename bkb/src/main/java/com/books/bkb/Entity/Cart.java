package com.books.bkb.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "user_id")
    private Integer userId;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "cart", fetch = FetchType.EAGER)
    private Set<CartItem> items = new HashSet<>();

}
