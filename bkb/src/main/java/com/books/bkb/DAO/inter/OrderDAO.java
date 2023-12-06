package com.books.bkb.DAO.inter;


import com.books.bkb.Entity.Order;

import java.util.List;

public interface OrderDAO {
    List<Order> getUserAllOrder(Integer userId);
    Order saveOrder(Order order);
    List<Order> getAll();
}
