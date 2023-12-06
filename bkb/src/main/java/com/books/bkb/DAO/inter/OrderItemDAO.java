package com.books.bkb.DAO.inter;

import com.books.bkb.Entity.OrderItem;

import java.util.List;

public interface OrderItemDAO {
    OrderItem save(OrderItem orderItem);

    void save(List<OrderItem> orderItems);

    List<OrderItem> findByOrderId(Integer orderId);
}
