package com.books.bkb.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    @JsonManagedReference
    private Book book;

    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Integer orderId;

    @Column(name = "num")
    private Integer num;

    @Column(name = "price")
    private BigDecimal price;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }
}
