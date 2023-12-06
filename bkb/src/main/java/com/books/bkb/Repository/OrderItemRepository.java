package com.books.bkb.Repository;

import com.books.bkb.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    void save(List<OrderItem> orderItems);

    List<OrderItem> findOrderItemByOrderId(Integer orderId);
}
