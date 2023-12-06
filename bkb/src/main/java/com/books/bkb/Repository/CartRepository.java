package com.books.bkb.Repository;

import com.books.bkb.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findCartByUserId(Integer id);

    Optional<Cart> findCartById(Integer id);
}
