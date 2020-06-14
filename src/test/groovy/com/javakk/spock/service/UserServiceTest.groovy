package com.javakk.spock.service

import com.javakk.spock.model.UserDTO
import com.javakk.spock.dao.UserDao
import spock.lang.Specification

class UserServiceTest extends Specification {
    def processor = new UserService()
    def handler = Mock(UserDao)

    void setup() {
        processor.userDao = handler
    }

    def "GetUserById"() {
        given: "设置请求参数"
        def user1 = new UserDTO(id:1, name:"张三", province: "上海")
        def user2 = new UserDTO(id:2, name:"李四", province: "江苏")

        and: "mock掉接口返回的用户信息"
        handler.getUserInfo() >> [user1, user2]

        when: "调用获取用户信息方法"
        def response = processor.getUserById(1)

        then: "验证返回结果是否符合预期值"
        with(response) {
            name == "张三"
            abbreviation == "沪"
            postCode == 200000
        }
    }
}
