package com.javakk.spock.dao;

import com.javakk.spock.model.UserDTO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {

    public List<UserDTO> getUserInfo(){
        // 模拟用户中心服务接口调用
        List<UserDTO> users = new ArrayList<>();
        UserDTO user = new UserDTO();
        user.setId(1);
        user.setName("张三");
        user.setAge(28);
        user.setProvince("上海");
        users.add(user);
        return users;
    }

    public boolean insertUser(UserDTO userDTO){
        // 模拟数据库调用添加用户操作
        return true;
    }

    public BigDecimal getExchange(String country){
        // 模拟调用汇率接口获取最新的汇率
        return BigDecimal.ONE;
    }
}
