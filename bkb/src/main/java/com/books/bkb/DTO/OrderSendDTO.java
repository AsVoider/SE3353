package com.books.bkb.DTO;

import com.books.bkb.Entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderSendDTO {
    Integer id;

    String phoneNum;

    String address;

    BigDecimal price;

    Date buytime;

    String sendTo;

    List<OrderItem> orderItems;
}
