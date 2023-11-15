package com.books.bkb.DAO.inter;

import com.books.bkb.Entity.Cart;


public interface CartDAO {

    Cart findByUserId(Integer id);
    Cart findById(Integer id);
    void save(Cart cart);
}
