package com.javakk.spock.model;

import java.math.BigDecimal;

public class OrderVO {
    private String orderNum;
    private BigDecimal amount;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
