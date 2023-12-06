package com.books.bkb.Repository;

import com.books.bkb.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAllByUserId(Integer userId);
    List<Order> findByBuyTimeBetween(Date start, Date end);
}
