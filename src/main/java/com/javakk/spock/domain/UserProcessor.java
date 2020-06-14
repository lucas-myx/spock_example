package com.javakk.spock.domain;

import com.javakk.spock.infrastructure.UserDTO;
import com.javakk.spock.infrastructure.UserVO;
import com.javakk.spock.repository.UserCenterHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserProcessor {

    @Autowired
    UserCenterHandler userCenterHandler;

    public UserVO getUserById(int uid){
        List<UserDTO> users = userCenterHandler.getUserInfo();
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
}
