package com.javakk.spock.service

import com.javakk.spock.dao.MoneyDAO
import com.javakk.spock.dao.OrderDao
import com.javakk.spock.mapper.UserMapper
import com.javakk.spock.model.OrderDTO
import com.javakk.spock.model.UserDTO
import com.javakk.spock.model.UserVO
import com.javakk.spock.util.OrderConfig
import spock.lang.Specification
import spock.lang.Unroll

class OrderServiceTest extends Specification {
    def orderService = new OrderService()
    def userMapper = Mock(UserMapper)
    def orderDao = Mock(OrderDao)
    def moneyDAO = Mock(MoneyDAO)
    def orderConfig = Mock(OrderConfig)

    void setup() {
        orderService.userMapper = userMapper
        orderService.orderDao = orderDao
        orderService.moneyDAO = moneyDAO
        orderService.orderConfig = orderConfig
    }

    @Unroll
    def "GetUserOrders"() {
        given: "mock用户转换的方法,订单接口，外汇汇率接口以及配置中心的返回"
        userMapper.toUserDTO(_) >> user
        orderDao.getOrderByUser(_) >> orders
        moneyDAO.getExchangeByCurrency(_) >> exchange
        orderConfig.isShowOrderTime() >> showTime

        when: "调用获取用户订单列表"
        def orderList = orderService.getUserOrders(new UserVO())

        then: "验证返回结果是否符合预期值"
        with(orderList) {
            size() == count
            if(null != it[0]){
                it[0].amount == orderAmount
                it[0].createTime == orderTime
            }
        }

        where: "表格方式验证订单信息的分支场景"
        user                         | orders                     | exchange | showTime || count | orderAmount | orderTime
        new UserDTO(currency: "USD") | getOrders(1, "")           | 0.1413   | false    || 1     | 141.3       | null
        new UserDTO(currency: "CNY") | getOrders(1, "2020-06-27") | 0.1413   | true     || 1     | 1000        | "2020-06-27"
        new UserDTO(currency: "CNY") | getOrders(1, "2020-06-27") | 0.1413   | false    || 1     | 1000        | "****"
        new UserDTO(currency: "USD") | null                       | 0.1413   | false    || 0     | 0           | null
    }

    def getOrders(count, createTime){
        if(count == 0){
            return new ArrayList<OrderDTO>()
        } else {
            return [new OrderDTO(orderNum: "D32142", amount: 1000, createTime: createTime)]
        }
    }
}
