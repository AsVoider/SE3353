package com.books.bkb.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderReceiveDTO {
    @JsonProperty("id")
    Integer id;
    @JsonProperty("cart")
    Integer cart;
    @JsonProperty("sendTo")
    String sendTO;
    @JsonProperty("phoneNum")
    String phoneNum;
    @JsonProperty("address")
    String address;
    @JsonProperty("note")
    String note;
    @JsonProperty("orderItems")
    List<OrderItemDTO> orderItems;

}
