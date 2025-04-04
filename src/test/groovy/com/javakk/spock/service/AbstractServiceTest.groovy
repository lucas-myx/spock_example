package com.javakk.spock.service

import com.javakk.spock.dao.MoneyDAO
import org.mockito.Mockito
import spock.lang.Specification

/**
 * 测试抽象类方法或父类方法
 * @Author: www.javakk.com
 * @Description: 公众号:Java老K
 * @Date: Created in 14:53 2020/10/05
 * @Modified By:
 */
class AbstractServiceTest extends Specification {

    def "测试抽象方法"() {
        given: "mock抽象类方法"
        def sub = Mockito.mock(SubService)
        Mockito.when(sub.parentMethod()).thenReturn(parentValue)
        Mockito.when(sub.doSomething()).thenCallRealMethod()

        expect: "调用doSomething方法"
        sub.doSomething() == result

        where: "验证分支场景"
        parentValue | result
        "parent1"   | "sub1"
        "parent2"   | "sub2"
        "parent3"   | "sub3"
        "parent4"   | "other"
    }

    def "测试抽象方法和实例方法"() {
        given: "mock抽象类方法"
        def sub = Mockito.mock(SubService)
        Mockito.when(sub.parentMethod()).thenReturn(parentValue)
        Mockito.when(sub.doSomethingAndDao()).thenCallRealMethod()

        def moneyDAO = Mock(MoneyDAO)
        sub.moneyDAO = moneyDAO
        moneyDAO.getExchangeByCountry(_) >> money // 这样就可以使用spock的动态mock

        expect: "调用doSomething方法"
        sub.doSomethingAndDao() == result

        where: "验证分支场景"
        parentValue | money || result
        "parent1"   | 100   || "100 CNY"
        "parent2"   | 200   || "200 USD"
        "parent3"   | 300   || "300 EUR"
        "parent4"   | 400   || "400"
    }
}
