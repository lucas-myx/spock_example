package com.javakk.spock.repository;

import com.javakk.spock.infrastructure.UserDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserCenterHandler {

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
}
