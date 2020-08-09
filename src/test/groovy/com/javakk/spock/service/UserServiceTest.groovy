package com.javakk.spock.service

import com.javakk.spock.dao.MoneyDAO
import com.javakk.spock.model.OrderVO
import com.javakk.spock.model.UserDTO
import com.javakk.spock.dao.UserDao
import com.javakk.spock.model.UserVO
import spock.lang.Specification
import spock.lang.Unroll

/**
 * 用户服务测试类
 * @author 公众号:Java老K
 * 个人博客:www.javakk.com
 */
class UserServiceTest extends Specification {
    def userService = new UserService()
    def userDao = Mock(UserDao)
    def moneyDAO = Mock(MoneyDAO)

    void setup() {
        userService.userDao = userDao
        userService.moneyDAO = moneyDAO
    }

    def "GetUserById"() {
        given: "设置请求参数"
        def user1 = new UserDTO(id:1, name:"张三", province: "上海")
        def user2 = new UserDTO(id:2, name:"李四", province: "江苏")

        and: "mock掉接口返回的用户信息"
        userDao.getUserInfo() >> [user1, user2]

        when: "调用获取用户信息方法"
        def response = userService.getUserById(1)

        then: "验证返回结果是否符合预期值"
        with(response) {
            name == "张三"
            abbreviation == "沪"
            postCode == 200000
        }
    }

    @Unroll
    def "当输入的用户id为:#uid 时返回的邮编是:#postCodeResult，处理后的电话号码是:#telephoneResult"() {
        given: "mock掉接口返回的用户信息"
        userDao.getUserInfo() >> users

        when: "调用获取用户信息方法"
        def response = userService.getUserById(uid)

        then: "验证返回结果是否符合预期值"
        with(response) {
            postCode == postCodeResult
            telephone == telephoneResult
        }

        where: "表格方式验证用户信息的分支场景"
        uid | users                         || postCodeResult | telephoneResult
        1   | getUser("上海", "13866667777") || 200000         | "138****7777"
        1   | getUser("北京", "13811112222") || 100000         | "138****2222"
        2   | getUser("南京", "13833334444") || 0              | null
    }

    def getUser(String province, String telephone){
        return [new UserDTO(id: 1, name: "张三", province: province, telephone: telephone)]
    }

    def "测试void方法"() {
        given: "设置请求参数"
        def userVO = new UserVO(name:"James", country: "美国")
        userVO.userOrders = [new OrderVO(orderNum: "1", amount: 10000), new OrderVO(orderNum: "2", amount: 1000)]

        when: "调用设置订单金额的方法"
        userService.setOrderAmountByExchange(userVO)

        then: "验证调用获取最新汇率接口的行为是否符合预期: 一共调用2次, 第一次输出的汇率是0.1413, 第二次是0.1421"
        2 * moneyDAO.getExchangeByCountry(_) >> 0.1413 >> 0.1421

        and: "验证根据汇率计算后的金额结果是否正确"
        with(userVO){
            userOrders[0].amount == 1413
            userOrders[1].amount == 142.1
        }
    }
}
