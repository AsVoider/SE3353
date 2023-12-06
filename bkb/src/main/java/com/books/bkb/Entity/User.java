package com.books.bkb.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
@JsonIgnoreProperties(value = { "handler", "hibernateLazyInitializer", "fieldHandler", "orders" })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String Email;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Order> Orders = new ArrayList<>();

}
