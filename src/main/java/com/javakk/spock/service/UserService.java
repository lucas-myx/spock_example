package com.javakk.spock.service;

import com.javakk.spock.model.UserDTO;
import com.javakk.spock.model.UserVO;
import com.javakk.spock.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public UserVO getUserById(int uid){
        List<UserDTO> users = userDao.getUserInfo();
        UserDTO userDTO = users.stream().filter(u -> u.getId() == uid).findFirst().orElse(new UserDTO());
        UserVO userVO = new UserVO();
        userVO.setId(userDTO.getId());
        userVO.setName(userDTO.getName());
        userVO.setSex(userDTO.getSex());
        userVO.setAge(userDTO.getAge());
        if (userDTO.getProvince().equals("上海")) {
            userVO.setAbbreviation("沪");
            userVO.setPostCode(200000);
        }

        return userVO;
    }

    public boolean addUser(UserVO userVO){
        return true;
    }
}
