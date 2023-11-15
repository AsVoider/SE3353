package com.books.bkb.DAO.imp;

import com.books.bkb.DAO.inter.CartDAO;
import com.books.bkb.Entity.Cart;
import com.books.bkb.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class CartIMP implements CartDAO {
    @Autowired
    CartRepository cartRepository;

    @Override
    public Cart findByUserId(Integer id)
    {
        return cartRepository.findCartByUserId(id).orElse(null);
    }

    @Override
    public Cart findById(Integer id)
    {
        return cartRepository.findCartById(id).orElse((null));
    }

    @Override
    public void save(Cart cart)
    {
        cartRepository.save(cart);
    }
}
