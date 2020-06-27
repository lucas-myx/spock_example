package com.javakk.spock.dao;

import com.javakk.spock.model.OrderDTO;
import com.javakk.spock.model.UserDTO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDao {

    public List<OrderDTO> getOrderByUser(UserDTO user){
        // 模拟订单服务接口调用
        List<OrderDTO> orders = new ArrayList<>();
        OrderDTO order = new OrderDTO();
        order.setOrderNum("D3412345");
        order.setAmount(BigDecimal.valueOf(1000));
        orders.add(order);
        return orders;
    }

}
