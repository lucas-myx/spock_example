package com.javakk.spock.mapper;

import com.javakk.spock.model.UserDTO;
import com.javakk.spock.model.UserVO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toUserDTO(UserVO userVO){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userVO.getId());
        userDTO.setName(userVO.getName());
        userDTO.setSex(userVO.getSex());
        userDTO.setCurrency("RMB");
        return userDTO;
    }
}
