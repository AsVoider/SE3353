package com.books.bkb.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;
@Getter
@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id")
    private Book book;
    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnore
    private Cart cart;
    private Integer num;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return id.equals(cartItem.id) && Objects.equals(book, cartItem.book) && Objects.equals(cart, cartItem.cart) && Objects.equals(num, cartItem.num);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, cart, num);
    }
}
