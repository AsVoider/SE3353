package com.books.bkb.DAO.imp;

import com.books.bkb.DAO.inter.OrderDAO;
import com.books.bkb.Entity.Order;
import com.books.bkb.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class OrderIMP implements OrderDAO {
    @Autowired
    OrderRepository orderRepository;
    @Override
    public List<Order> getUserAllOrder(Integer userId)
    {
        return orderRepository.findAllByUserId(userId);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
    @Override
    public List<Order> getAll()
    {
        return orderRepository.findAll();
    }
}
