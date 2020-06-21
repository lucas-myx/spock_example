package com.javakk.spock.service

import com.javakk.spock.dao.UserDao
import com.javakk.spock.model.UserDTO
import com.javakk.spock.util.IDNumberUtils
import com.javakk.spock.util.LogUtils
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import org.spockframework.runtime.Sputnik
import spock.lang.Specification

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(Sputnik.class)
@PrepareForTest([LogUtils.class, IDNumberUtils.class])
@SuppressStaticInitializationFor(["com.javakk.spock.util.LogUtils"])
class UserServiceStaticTest extends Specification {
    def processor = new UserService()
    def dao = Mock(UserDao)

    void setup() {
        processor.userDao = dao
        // mock静态方法
        PowerMockito.mockStatic(LogUtils.class)
        PowerMockito.mockStatic(IDNumberUtils.class)
    }

    def "GetUserByIdStatic"() {
        given: "设置请求参数"
        def user1 = new UserDTO(id:1, name:"张三", province: "上海")
        def user2 = new UserDTO(id:2, name:"李四", province: "江苏")
        def idMap = ["birthday": "1992-09-18", "sex": "男", "age": "28"]

        and: "mock掉接口返回的用户信息"
        dao.getUserInfo() >> [user1, user2]

        and: "mock静态方法返回值"
        PowerMockito.when(IDNumberUtils.getBirAgeSex(Mockito.any())).thenReturn(idMap)

        when: "调用获取用户信息方法"
        def response = processor.getUserByIdStatic(1)

        then: "验证返回结果是否符合预期值"
        with(response) {
            name == "张三"
            abbreviation == "沪"
            postCode == 200000
            age == 28
        }
    }
}
