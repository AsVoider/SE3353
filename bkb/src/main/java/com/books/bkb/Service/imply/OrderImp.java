package com.books.bkb.Service.imply;

import com.books.bkb.DAO.inter.*;
import com.books.bkb.DTO.OrderReceiveDTO;
import com.books.bkb.DTO.OrderSendDTO;
import com.books.bkb.Entity.*;
import com.books.bkb.Service.OrderServe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderImp implements OrderServe {
    OrderDAO orderDAO;
    UserDAO userDAO;
    BookDAO bookDAO;
    CartDAO cartDAO;
    OrderItemDAO orderItemDAO;
    @Autowired
    public OrderImp(OrderDAO orderDAO, UserDAO userDAO, BookDAO bookDAO, CartDAO cartDAO, OrderItemDAO orderItemDAO) {
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
        this.bookDAO = bookDAO;
        this.cartDAO = cartDAO;
        this.orderItemDAO = orderItemDAO;
    }

    @Override
    public List<OrderSendDTO> getUserAllOrder(Integer userId)
    {
        List<Order> list = orderDAO.getUserAllOrder(userId);
        List<OrderSendDTO> orderSends = new ArrayList<>();
        for(var i : list) {
            var orderSend = new OrderSendDTO();
            orderSend.setId(i.getId());
            orderSend.setPrice(i.getPrice());
            orderSend.setSendTo(i.getSendTo());
            orderSend.setAddress(i.getAddress());
            orderSend.setPhoneNum(i.getPhoneNum());
            orderSend.setBuytime(i.getBuyTime());
            orderSend.setOrderItems(orderItemDAO.findByOrderId(i.getId()));
            orderSends.add(orderSend);
        }
        return orderSends;
    }
    @Override
    public List<Order> getAll()
    {
        return orderDAO.getAll();
    }
    @Override
    public void addOrder(Order order)
    {
        orderDAO.saveOrder(order);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean checkout(OrderReceiveDTO orders)
    {
        try {
            Order order = new Order();
            Integer userId = orders.getId();
            Integer cart = orders.getCart();
            order.setUserId(userId);
            order.setBuyTime(new Date());
            order.setSendTo(orders.getSendTO());
            order.setPhoneNum(orders.getPhoneNum());
            order.setAddress(orders.getAddress());
            order.setNote(orders.getNote());
            BigDecimal price = BigDecimal.valueOf(0);

            System.out.println("hello");

            List<OrderItem> list = new ArrayList<>();

            for(var i : orders.getOrderItems())
            {
                Book book = checkThenSave(i.getId(), i.getNum());
                OrderItem orderItem = new OrderItem();
                orderItem.setNum(i.getNum());
                orderItem.setBook(book);
                orderItem.setPrice(book.getPrice());
                list.add(orderItem);
                price = price.add(new BigDecimal(i.getNum()).multiply(book.getPrice()));
                System.out.println("aaa");
            }
            order.setPrice(price);
            var OrderId = orderDAO.saveOrder(order).getId();

            for(var listItem : list) {
                listItem.setOrderId(OrderId);
            }

            System.out.println("hello2");

            orderItemDAO.save(list);

            System.out.println("hello3");

            if(cart == 0)
            {
                clearCart(userId);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public Book checkThenSave(Integer id, Integer num) throws Exception {
        Book bk = bookDAO.findOneBook(id);
        if (bk.getIsExist() < num) {
            throw new RuntimeException("book storage low");
        }
        bk.setIsExist(bk.getIsExist() - num);
        return bookDAO.saveBook(bk);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void clearCart(Integer id) {
        try {
            Cart cart = cartDAO.findByUserId(id);
            cart.getItems().clear();
            cartDAO.save(cart);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
