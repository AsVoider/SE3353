package com.books.bkb.Consumer;

import com.books.bkb.DTO.OrderReceiveDTO;
import com.books.bkb.Service.OrderServe;
import com.books.bkb.utils.WebSocket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderConsumer {
    OrderServe orderServe;
    KafkaTemplate<String, Object> kafkaTemplate;
    WebSocket webSocket;

    @Autowired
    public OrderConsumer(OrderServe orderServe, KafkaTemplate<String, Object> kafkaTemplate, WebSocket webSocket) {
        this.orderServe = orderServe;
        this.kafkaTemplate = kafkaTemplate;
        this.webSocket = webSocket;
    }

    @KafkaListener(topics = "orderConsumer", groupId = "orderGroup")
    public void OrderHandler(ConsumerRecord<String, String> record) throws JsonProcessingException {
        OrderReceiveDTO orderReceiveDTO = new ObjectMapper().readValue(record.value(), OrderReceiveDTO.class);
        Map<String, Object> map = new HashMap<>();
        if(orderServe.checkout(orderReceiveDTO))
        {
            map.put("Code", 200);
            map.put("Info", "Order is Done");
        } else {
            map.put("Code", 404);
            map.put("Info", "Something is Wrong");
        }
        kafkaTemplate.send("orderFinished", String.valueOf(orderReceiveDTO.getId()), new ObjectMapper().writeValueAsString(map));
    }

    @KafkaListener(topics = "orderFinished", groupId = "orderGroup")
    public void OnOrderFinished(ConsumerRecord<String, String> record) throws InterruptedException {
        System.out.println(record.key() + "   " + record.value());
        Thread.sleep(1000);
        webSocket.sendOneMessage(record.key(), record.value());
    }
}
