package com.javakk.spock.service

import com.javakk.spock.BaseSpock
import com.javakk.spock.dao.UserDao
import com.javakk.spock.model.UserDTO
import com.javakk.spock.util.IDNumberUtils
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest

/**
 * 测试继承Spock基类
 * @Author: www.javakk.com
 * @Description: 公众号:Java老K
 * @Date: Created in 20:53 2020/7/16
 * @Modified By:
 */
@PrepareForTest([IDNumberUtils.class])
class BaseBaseSpockTest extends BaseSpock {
    def processor = new UserService()
    def dao = Mock(UserDao)

    void setupSpec() { // 类似于JUnit的@BeforeClass
        println "setupSpec"
    }

    void setup() { // 类似于JUnit的@Before
        println "setup"
        processor.userDao = dao
        PowerMockito.mockStatic(IDNumberUtils.class)
    }

    void cleanup() { // 类似于JUnit的@After
        println "cleanup"
    }

    void cleanupSpec() { // 类似于JUnit的@AfterClass
        println "cleanupSpec"
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
