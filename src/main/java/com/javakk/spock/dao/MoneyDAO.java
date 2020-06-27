package com.javakk.spock.dao;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class MoneyDAO {
    public BigDecimal getExchangeByCountry(String country){
        // 模拟调用汇率接口获取最新的汇率
        return BigDecimal.ONE;
    }

    public BigDecimal getExchangeByCurrency(String currency){
        // 模拟调用汇率接口获取最新的汇率
        return BigDecimal.ONE;
    }
}
