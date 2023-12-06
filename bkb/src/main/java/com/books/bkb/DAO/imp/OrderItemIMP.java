package com.books.bkb.DAO.imp;

import com.books.bkb.DAO.inter.OrderItemDAO;
import com.books.bkb.Entity.OrderItem;
import com.books.bkb.Repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class OrderItemIMP implements OrderItemDAO{
    OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemIMP(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void save(List<OrderItem> orderItems) {
        //int i = 1 / 0;
        orderItemRepository.saveAll(orderItems);
    }

    @Override
    public List<OrderItem> findByOrderId(Integer orderId) {
        return orderItemRepository.findOrderItemByOrderId(orderId);
    }
}
