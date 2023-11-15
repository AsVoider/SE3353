package com.books.bkb.Controller;

import com.books.bkb.Entity.CartItem;
import com.books.bkb.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class CartController {
    @Autowired
    CartService cartService;

    @RequestMapping("/public/Cart/{UserId}")
    public Set<CartItem> getCartItems(@PathVariable Integer UserId){
        return cartService.getCartItems(UserId);
    }

    @RequestMapping("/public/Cart/add/{userId}/{id}")
    public void addOne(@PathVariable Integer userId, @PathVariable Integer id)
    {
        cartService.addOne(userId, id);
    }

    @RequestMapping("/public/Cart/del/{userId}/{id}")
    public void delOne(@PathVariable Integer userId, @PathVariable Integer id)
    {
        cartService.delOne(userId, id);
    }

    @RequestMapping("/public/Cart/{userId}/clear")
    public void clear(@PathVariable Integer userId)
    {
        cartService.clear(userId);
    }
}
