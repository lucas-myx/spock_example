package com.javakk.spock.service

import com.javakk.spock.dao.MoneyDAO
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import org.powermock.reflect.Whitebox
import org.spockframework.runtime.Sputnik
import spock.lang.Specification
import spock.lang.Unroll

/**
 * 测试抽象类方法或父类方法
 * @Author: www.javakk.com
 * @Description: 公众号:Java老K
 * @Date: Created in 14:53 2020/10/05
 * @Modified By:
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(Sputnik.class)
@PrepareForTest([SubService.class])
class AbstractServiceTest extends Specification {
    @Unroll
    def "测试抽象方法"() {
        given: "mock抽象类方法"
        def sub = PowerMockito.mock(SubService)
        PowerMockito.when(sub.parentMethod()).thenReturn(parentValue) // mock掉抽象类的parentMethod, 返回动态mock值:mockParentReturn
        PowerMockito.when(sub.doSomething()).thenCallRealMethod()

        expect: "调用doSomething方法"
        sub.doSomething() == result

        where: "验证分支场景"
        parentValue | result
        "parent1"   | "sub1"
        "parent2"   | "sub2"
        "parent3"   | "sub3"
        "parent4"   | "other"
    }

    @Unroll
    def "测试抽象方法和实例方法"() {
        given: "mock抽象类方法"
        def sub = PowerMockito.mock(SubService)
        PowerMockito.when(sub.parentMethod()).thenReturn(parentValue) // mock掉抽象类的parentMethod, 返回动态mock值:mockParentReturn
        PowerMockito.when(sub.doSomethingAndDao()).thenCallRealMethod()

        def moneyDAO = Mock(MoneyDAO)
        Whitebox.setInternalState(sub, "moneyDAO", moneyDAO) // 将Spock mock的对象moneyDAO使用powermock赋值给SubService的引用moneyDAO
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
