package com.books.bkb.Service;

import com.books.bkb.DTO.OrderReceiveDTO;
import com.books.bkb.DTO.OrderSendDTO;
import com.books.bkb.Entity.Book;
import com.books.bkb.Entity.Order;

import java.util.List;

public interface OrderServe {
    List<OrderSendDTO> getUserAllOrder(Integer userId);
    void addOrder(Order order);
    boolean checkout(OrderReceiveDTO orderReceiveDTO);
    List<Order> getAll();
    Book checkThenSave(Integer id, Integer num) throws Exception;
    void clearCart(Integer id);
}
