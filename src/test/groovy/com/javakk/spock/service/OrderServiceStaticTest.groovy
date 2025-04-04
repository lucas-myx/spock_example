package com.javakk.spock.service

import com.javakk.spock.dao.OrderDao
import com.javakk.spock.mapper.OrderMapper
import com.javakk.spock.mapper.UserMapper
import com.javakk.spock.model.OrderDTO
import com.javakk.spock.model.OrderVO
import com.javakk.spock.model.UserVO
import com.javakk.spock.util.HttpContextUtils
import org.mapstruct.factory.Mappers
import spock.lang.Shared
import spock.lang.Specification

/**
 * 测试静态方法mock
 * @Author: www.javakk.com
 * @Description: 公众号:Java老K
 * @Date: Created in 20:53 2020/7/16
 * @Modified By:
 */
class OrderServiceStaticTest extends Specification {
    def orderService = new OrderService()
    def userMapper = Mock(UserMapper)
    def orderDao = Mock(OrderDao)
    def userService = Mock(UserService)

    void setup() {
        orderService.userMapper = userMapper
        orderService.orderDao = orderDao
        orderService.userService = userService
        // mock静态类
        SpyStatic(HttpContextUtils.class)
    }

    def "测试spock的mock和power mock静态方法组合用法的场景, 当来源是#source时，订单类型为:#type"() {
        given: "mock当前上下文的请求来源"
        HttpContextUtils.getCurrentSource() >> source

        and: "mock当前上下文的币种"
        HttpContextUtils.getCurrentCurrency() >> currency

        when: "调用获取用户订单列表"
        def orderList = orderService.getUserOrdersBySource(new UserVO())

        then: "验证返回结果是否符合预期值"
        with(orderList) {
            it[0].type == type
        }

        where: "表格方式验证订单信息的分支场景"
        source   | currency || type
        "APP"    | "CNY"    || 1
        "APP"    | "USD"    || 1
        "WAP"    | ""       || 2
        "ONLINE" | ""       || 3
    }

    @Shared
    def orderMapper = new OrderMapperImpl()

    def "mock掉OrderMapper的静态final变量INSTANCE"() {
        given: "INSTANCE是从Mappers赋值的，所以mock掉Mappers的getMapper方法"
        SpyStatic(Mappers.class)
        Mappers.getMapper(OrderMapper.class) >> orderMapper

        and: "INSTANCE是final只能赋值一次，这里使用共享对象orderMapper，通过每次mock不同的type实现"
        orderMapper.setType(type)

        when: "调用用户订单转换方法"
        def userOrders = orderService.convertUserOrders([new OrderDTO()])

        then: "验证返回结果是否符合预期值"
        with(userOrders) {
            it[0].orderDesc == desc
        }

        where: "数据驱动方式验证订单属性转换结果"
        type || desc
        1    || "App端订单"
        2    || "H5端订单"
        3    || "PC端订单"
    }

    class OrderMapperImpl implements OrderMapper {
        int type
        void setType(int type) {
            this.type = type
        }
        @Override
        OrderVO convert(OrderDTO orderDTO) {
            return new OrderVO(type: this.type)
        }
    }
}
