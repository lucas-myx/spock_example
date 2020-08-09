package com.javakk.spock.service;

import com.javakk.spock.dao.UserDao;
import com.javakk.spock.model.UserDTO;
import com.javakk.spock.model.UserVO;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static mockit.Deencapsulation.setField;

@RunWith(JMockit.class)
public class UserServiceJavaTest {
    
    private UserService processor = new UserService();

    @Injectable
    private UserDao userDao;

    @Test
    public void getUserById(){
        new MockUp<UserDao>() {
            @Mock
            List<UserDTO> getUserInfo() {
                List<UserDTO> users = new ArrayList<>();
                UserDTO user1 = new UserDTO();
                user1.setId(1);
                user1.setName("张三");
                user1.setProvince("上海");
                users.add(user1);
                UserDTO user2 = new UserDTO();
                user2.setId(2);
                user2.setName("李四");
                user2.setProvince("江苏");
                users.add(user2);
                return users;
            }
        };
        setField(processor, "userDao", userDao);
        UserVO response = processor.getUserById(1);
        Assert.assertEquals("张三", response.getName());
        Assert.assertEquals("沪", response.getAbbreviation());
        Assert.assertEquals(200000, response.getPostCode());
    }
}