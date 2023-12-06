package com.books.bkb.Entity;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UserStat {
    String name;
    BigDecimal spent;
    Integer num;

    public UserStat(String name, BigDecimal spent, Integer num)
    {
        this.name = name;
        this.spent = spent;
        this.num = num;
    }

}
