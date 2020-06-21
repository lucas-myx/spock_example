package com.javakk.spock.controller

import com.javakk.spock.model.APIException
import com.javakk.spock.model.UserVO
import com.javakk.spock.service.UserService
import com.javakk.spock.util.LogUtils
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import org.spockframework.runtime.Sputnik
import spock.lang.Specification
import spock.lang.Unroll


@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(Sputnik.class)
@PrepareForTest([LogUtils.class])
@SuppressStaticInitializationFor(["com.javakk.spock.util.LogUtils"])
class UserControllerTest extends Specification {

    def userController = new UserController()
    def service = Mock(UserService)

    void setup() {
        PowerMockito.mockStatic(LogUtils.class) // mock静态方法
        userController.userService = service
    }

    def "AddUser"() {
        given: "设置请求参数"
        def user = new UserVO(id:1, name:"James", age: 33)

        and: "mock用户服务的addUser方法，返回指定值"
        service.addUser(_) >> true

        when: "调用添加用户信息接口"
        def response = userController.addUser(user)

        then: "验证添加用户是否成功，then标签下的代码默认是布尔表达式"
        response // 等同于 Assert.assertTrue(response)
    }

    @Unroll
    def "验证用户信息的合法性: #expectedMessage"() {
        when: "调用校验用户方法"
        userController.validateUser(user)

        then: "捕获异常并设置需要验证的异常值"
        def exception = thrown(expectedException)
        exception.errorCode == expectedErrCode
        exception.errorMessage == expectedMessage

        where: "表格方式验证用户信息的合法性"
        user           || expectedException | expectedErrCode | expectedMessage
        getUser(10001) || APIException      | "10001"         | "user is null"
        getUser(10002) || APIException      | "10002"         | "user name is null"
        getUser(10003) || APIException      | "10003"         | "user age is null"
        getUser(10004) || APIException      | "10004"         | "user telephone is null"
        getUser(10005) || APIException      | "10005"         | "user sex is null"
    }

    def getUser(errCode) {
        def user = new UserVO()
        def condition1 = {
            user.name = "杜兰特"
        }
        def condition2 = {
            user.age = 20
        }
        def condition3 = {
            user.telephone = "15801833812"
        }

        switch (errCode) {
            case 10001:
                user = null
                break
            case 10002:
                user = new UserVO()
                break
            case 10003:
                condition1()
                break
            case 10004:
                condition1()
                condition2()
                break
            case 10005:
                condition1()
                condition2()
                condition3()
                break
        }
        return user
    }
}
