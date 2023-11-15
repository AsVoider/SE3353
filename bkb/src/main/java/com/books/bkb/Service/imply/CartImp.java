package com.books.bkb.Service.imply;

import com.books.bkb.DAO.inter.BookDAO;
import com.books.bkb.DAO.inter.CartDAO;
import com.books.bkb.DAO.inter.UserDAO;
import com.books.bkb.Entity.Book;
import com.books.bkb.Entity.Cart;
import com.books.bkb.Entity.CartItem;
import com.books.bkb.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CartImp implements CartService {
    @Autowired
    CartDAO cartDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    BookDAO bookDAO;


    @Override
    public Set<CartItem> getCartItems(Integer UserId) {
        Cart cart = cartDAO.findByUserId(UserId);
        //System.out.println("here\n");
        return cart.getItems();
    }

    @Override
    public void delOne(Integer UserId, Integer BookId){
        Cart cart = cartDAO.findByUserId(UserId);
        Book book = bookDAO.findOneBook(BookId);
        assert cart != null;
        var cartItems = cart.getItems();
        for(var aItem : cartItems)
        {
            if(aItem.getBook().getId() == book.getId())
            {
                Integer i = aItem.getNum();
                if(i == 1)
                    cartItems.remove(aItem);
                else aItem.setNum(--i);
            }
        }
        cart.setItems(cartItems);
        cartDAO.save(cart);
    }

    @Override
    public void addOne(Integer userId, Integer BookId){
        Cart cart = cartDAO.findByUserId(userId);
        Book book = bookDAO.findOneBook(BookId);
        assert cart != null;
        var cartItems = cart.getItems();
        CartItem cartItem = new CartItem();
        Integer num = 0;
        for(var Acartitem : cartItems)
        {
            if(Acartitem.getBook().getId() == book.getId())
            {
                num = Acartitem.getNum();
                Acartitem.setNum(++num);
            }
        }
        if(num == 0)
        {
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setNum(1);
            cartItems.add(cartItem);
        }
        cart.setItems(cartItems);
        cartDAO.save(cart);
    }

    @Override
    public void clear(Integer UserId){
        Cart cart = cartDAO.findByUserId(UserId);
        cart.getItems().clear();
        cartDAO.save(cart);
    }
}
