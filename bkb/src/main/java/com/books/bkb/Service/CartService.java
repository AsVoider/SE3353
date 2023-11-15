package com.books.bkb.Service;

import com.books.bkb.Entity.CartItem;

import java.util.Set;

public interface CartService {
    Set<CartItem> getCartItems(Integer userId);

    void delOne(Integer userId ,Integer BookId);

    void addOne(Integer userId, Integer BookId);

    void clear(Integer userId);
}
