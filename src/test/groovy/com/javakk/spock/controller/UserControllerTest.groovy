package com.javakk.spock.controller

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
}
