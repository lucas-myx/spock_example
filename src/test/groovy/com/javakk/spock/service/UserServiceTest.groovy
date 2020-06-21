package com.javakk.spock.service

import com.javakk.spock.model.UserDTO
import com.javakk.spock.dao.UserDao
import spock.lang.Specification
import spock.lang.Unroll

class UserServiceTest extends Specification {
    def processor = new UserService()
    def dao = Mock(UserDao)

    void setup() {
        processor.userDao = dao
    }

    def "GetUserById"() {
        given: "设置请求参数"
        def user1 = new UserDTO(id:1, name:"张三", province: "上海")
        def user2 = new UserDTO(id:2, name:"李四", province: "江苏")

        and: "mock掉接口返回的用户信息"
        dao.getUserInfo() >> [user1, user2]

        when: "调用获取用户信息方法"
        def response = processor.getUserById(1)

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
        dao.getUserInfo() >> users

        when: "调用获取用户信息方法"
        def response = processor.getUserById(uid)

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
}
