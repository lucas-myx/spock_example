package com.javakk.spock.service;

import com.javakk.spock.dao.MoneyDAO;
import com.javakk.spock.dao.OrderDao;
import com.javakk.spock.mapper.UserMapper;
import com.javakk.spock.model.OrderDTO;
import com.javakk.spock.model.OrderVO;
import com.javakk.spock.model.UserDTO;
import com.javakk.spock.model.UserVO;
import com.javakk.spock.util.OrderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    OrderDao orderDao;

    @Autowired
    MoneyDAO moneyDAO;

    @Autowired
    OrderConfig orderConfig;

    public List<OrderVO> getUserOrders(UserVO userVO){
        List<OrderVO> orderList = new ArrayList<>();
        UserDTO userDTO = userMapper.toUserDTO(userVO);
        List<OrderDTO> orders = orderDao.getOrderByUser(userDTO);
        if (null == orders || orders.size() == 0){
            return orderList;
        }
        if (!"CNY".equals(userDTO.getCurrency())) { // 非人民币(外币)
            for (OrderDTO orderDTO : orders) {
                OrderVO orderVO = new OrderVO();
                orderVO.setOrderNum(orderDTO.getOrderNum());
                BigDecimal amount = orderDTO.getAmount();
                // 根据币种调用汇率接口获取最新的汇率
                BigDecimal exchange = moneyDAO.getExchangeByCurrency(userDTO.getCurrency());
                amount = amount.multiply(exchange); // 转换成对应的外币金额
                orderVO.setAmount(amount);
                orderList.add(orderVO);
            }
        } else { // 人民币
            for (OrderDTO orderDTO : orders) {
                OrderVO orderVO = new OrderVO();
                orderVO.setOrderNum(orderDTO.getOrderNum());
                orderVO.setAmount(orderDTO.getAmount());
                if (orderConfig.isShowOrderTime()){ // 是否展示真实的订单创建时间
                    orderVO.setCreateTime(orderDTO.getCreateTime());
                } else {
                    orderVO.setCreateTime("****");
                }
                orderList.add(orderVO);
            }
        }

        return orderList;
    }


}
