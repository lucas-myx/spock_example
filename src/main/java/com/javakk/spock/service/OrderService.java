package com.javakk.spock.service;

import com.javakk.spock.dao.MoneyDAO;
import com.javakk.spock.dao.OrderDao;
import com.javakk.spock.mapper.OrderMapper;
import com.javakk.spock.mapper.UserMapper;
import com.javakk.spock.model.OrderDTO;
import com.javakk.spock.model.OrderVO;
import com.javakk.spock.model.UserDTO;
import com.javakk.spock.model.UserVO;
import com.javakk.spock.util.HttpContextUtils;
import com.javakk.spock.util.OrderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Autowired
    UserService userService;

    /**
     * 多分支业务场景
     * @param userVO
     * @return
     */
    public List<OrderVO> getUserOrders(UserVO userVO){
        List<OrderVO> orderList = new ArrayList<>();
        UserDTO userDTO = userMapper.toUserDTO(userVO); // VO转DTO
        List<OrderDTO> orders = orderDao.getOrderByUser(userDTO); // 根据用户信息获取订单列表
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

    /**
     * 静态方法多分支场景
     * @param userVO
     * @return
     */
    public List<OrderVO> getUserOrdersBySource(UserVO userVO){
        List<OrderVO> orderList = new ArrayList<>();
        OrderVO order = new OrderVO();
        if ("APP".equals(HttpContextUtils.getCurrentSource())) { // 手机来源
            if("CNY".equals(HttpContextUtils.getCurrentCurrency())){ // 人民币
                // TODO 针对App端的订单，并且请求币种为人民币的业务逻辑...
                System.out.println("source -> APP, currency -> CNY");
            } else {
                System.out.println("source -> APP, currency -> !CNY");
            }
            order.setType(1);
        } else if ("WAP".equals(HttpContextUtils.getCurrentSource())) { // H5来源
            // TODO 针对H5端的业务逻辑...
            System.out.println("source -> WAP");
            order.setType(2);
        } else if ("ONLINE".equals(HttpContextUtils.getCurrentSource())) { // PC来源
            // TODO 针对PC端的业务逻辑...
            System.out.println("source -> ONLINE");
            order.setType(3);
        }
        orderList.add(order);
        return orderList;
    }

    /**
     * 静态final变量场景
     * @param orders
     * @return
     */
    public List<OrderVO> convertUserOrders(List<OrderDTO> orders){
        List<OrderVO> orderList = new ArrayList<>();
        for (OrderDTO orderDTO : orders) {
            OrderVO orderVO = OrderMapper.INSTANCE.convert(orderDTO); // VO DTO 属性转换
            if (1 == orderVO.getType()) {
                orderVO.setOrderDesc("App端订单");
            } else if(2 == orderVO.getType()) {
                orderVO.setOrderDesc("H5端订单");
            } else if(3 == orderVO.getType()) {
                orderVO.setOrderDesc("PC端订单");
            }
            orderList.add(orderVO);
        }
        return orderList;
    }
}
