package com.books.bkb.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "buy_time")
    private Date buyTime;

    @Column(name = "send_to")
    private String sendTo;

    @Column(name = "phone_num")
    private String phoneNum;

    @Column(name = "address")
    private String address;

    @Column(name = "note")
    private String note;

    @Column(name = "price")
    private BigDecimal Price;

}
