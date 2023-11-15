package com.books.bkb.Entity;

import java.math.BigDecimal;

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

    public String getName() {
        return name;
    }

    public BigDecimal getSpent() {
        return spent;
    }

    public Integer getNum() {
        return num;
    }
}
