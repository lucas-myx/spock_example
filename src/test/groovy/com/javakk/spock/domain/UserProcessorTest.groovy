package com.javakk.spock.domain

import com.javakk.spock.infrastructure.UserDTO
import com.javakk.spock.repository.UserCenterHandler
import spock.lang.Specification

class UserProcessorTest extends Specification {
    def processor = new UserProcessor()
    def handler = Mock(UserCenterHandler)

    void setup() {
        processor.userCenterHandler = handler
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
