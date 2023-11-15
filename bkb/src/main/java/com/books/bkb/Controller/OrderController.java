package com.books.bkb.Controller;

import com.books.bkb.DTO.OrderReceiveDTO;
import com.books.bkb.DTO.OrderSendDTO;
import com.books.bkb.Entity.Order;
import com.books.bkb.Service.BookServe;
import com.books.bkb.Service.OrderServe;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class OrderController {
    @Autowired
    BookServe bookServe;
    @Autowired
    OrderServe orderServe;
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;
    @GetMapping("/public/orders/{userId}")
    public List<OrderSendDTO> getOrders(@PathVariable Integer userId)
    {
        System.out.println(userId);
        return orderServe.getUserAllOrder(userId);
    }

    @GetMapping("/admin/orders")
    public List<Order> getAll()
    {
        return orderServe.getAll();
    }

    @PostMapping("/public/checkout")
    public ResponseEntity<String> checkout(@RequestBody OrderReceiveDTO body) throws JsonProcessingException {
//        for(var it : body.getOrderItems())
//        {
//            System.out.println(it.getId() + " " + it.getNum());
//            System.out.println("\n");
//        }
//        orderServe.checkout(id, cart, body);
        kafkaTemplate.send("orderConsumer", "aaa", new ObjectMapper().writeValueAsString(body));
        return ResponseEntity.ok(String.valueOf(body.getId()));
    }
}
