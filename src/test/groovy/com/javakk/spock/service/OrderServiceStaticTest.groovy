package com.javakk.spock.service

import com.javakk.spock.model.UserVO
import com.javakk.spock.util.HttpContextUtils
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import org.spockframework.runtime.Sputnik
import spock.lang.Specification
import spock.lang.Unroll

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(Sputnik.class)
@PrepareForTest([HttpContextUtils.class])
class OrderServiceStaticTest extends Specification {
    def orderService = new OrderService()

    void setup() {
        // mock静态类
        PowerMockito.mockStatic(HttpContextUtils.class)
    }

    /**
     * 测试spock的mock和power mock静态方法组合用法的场景
     * @return
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
        "APP"    | "CNY"     | 1
        "APP"    | "USD"     | 1
        "WAP"    | ""        | 2
        "ONLINE" | ""        | 3
    }

}
