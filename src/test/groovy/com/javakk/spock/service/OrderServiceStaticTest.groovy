package com.javakk.spock.service

import com.javakk.spock.dao.OrderDao
import com.javakk.spock.mapper.OrderMapper
import com.javakk.spock.mapper.UserMapper
import com.javakk.spock.model.OrderDTO
import com.javakk.spock.model.OrderVO
import com.javakk.spock.model.UserVO
import com.javakk.spock.util.HttpContextUtils


import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import org.powermock.reflect.Whitebox
import org.spockframework.runtime.Sputnik
import spock.lang.Specification
import spock.lang.Unroll

/**
 * 测试静态方法mock
 * @Author: www.javakk.com
 * @Description: 公众号:Java老K
 * @Date: Created in 20:53 2020/7/16
 * @Modified By:
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(Sputnik.class)
@PrepareForTest([HttpContextUtils.class, OrderMapper.class])
@SuppressStaticInitializationFor(["com.javakk.spock.mapper.OrderMapper"])
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
        PowerMockito.mockStatic(HttpContextUtils.class)
    }

    /**
     * 测试spock的mock和power mock静态方法组合用法的场景
     */
    @Unroll
    def "当来源是#source时，订单类型为:#type"() {
        given: "mock当前上下文的请求来源"
        PowerMockito.when(HttpContextUtils.getCurrentSource()).thenReturn(source)

        and: "mock当前上下文的币种"
        PowerMockito.when(HttpContextUtils.getCurrentCurrency()).thenReturn(currency)

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

    /**
     * 测试spock的mock和powermock静态final变量结合的用法
     */
    @Unroll
    def "ConvertUserOrders"() {
        given: "mock掉OrderMapper的静态final变量INSTANCE,并结合spock设置动态返回值"
        def orderMapper = Mock(OrderMapper.class)
        Whitebox.setInternalState(OrderMapper.class, "INSTANCE", orderMapper)
        orderMapper.convert(_) >> order

        when: "调用用户订单转换方法"
        def userOrders = orderService.convertUserOrders([new OrderDTO()])

        then: "验证返回结果是否符合预期值"
        with(userOrders) {
            it[0].orderDesc == desc
        }

        where: "表格方式验证订单属性转换结果"
        order                || desc
        new OrderVO(type: 1) || "App端订单"
        new OrderVO(type: 2) || "H5端订单"
        new OrderVO(type: 3) || "PC端订单"
    }

}
