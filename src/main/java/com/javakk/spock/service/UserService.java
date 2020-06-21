package com.javakk.spock.service;

import com.javakk.spock.model.UserDTO;
import com.javakk.spock.model.UserVO;
import com.javakk.spock.dao.UserDao;
import com.javakk.spock.util.IDNumberUtils;
import com.javakk.spock.util.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public UserVO getUserById(int uid){
        List<UserDTO> users = userDao.getUserInfo();
        UserDTO userDTO = users.stream().filter(u -> u.getId() == uid).findFirst().orElse(null);
        UserVO userVO = new UserVO();
        if(null == userDTO){
            return userVO;
        }
        userVO.setId(userDTO.getId());
        userVO.setName(userDTO.getName());
        userVO.setSex(userDTO.getSex());
        userVO.setAge(userDTO.getAge());
        // 显示邮编
        if("上海".equals(userDTO.getProvince())){
            userVO.setAbbreviation("沪");
            userVO.setPostCode(200000);
        }
        if("北京".equals(userDTO.getProvince())){
            userVO.setAbbreviation("京");
            userVO.setPostCode(100000);
        }
        // 手机号处理
        if(null != userDTO.getTelephone() && !"".equals(userDTO.getTelephone())){
            userVO.setTelephone(userDTO.getTelephone().substring(0,3)+"****"+userDTO.getTelephone().substring(7));
        }

        return userVO;
    }

    public boolean addUser(UserVO userVO){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userVO.getId());
        userDTO.setName(userVO.getName());
        userDTO.setSex(userVO.getSex());
        return userDao.insertUser(userDTO);
    }

    public UserVO getUserByIdStatic(int uid){
        List<UserDTO> users = userDao.getUserInfo();
        UserDTO userDTO = users.stream().filter(u -> u.getId() == uid).findFirst().orElse(null);
        UserVO userVO = new UserVO();
        if(null == userDTO){
            return userVO;
        }
        userVO.setId(userDTO.getId());
        userVO.setName(userDTO.getName());
        userVO.setSex(userDTO.getSex());
        // 显示邮编
        if("上海".equals(userDTO.getProvince())){
            userVO.setAbbreviation("沪");
            userVO.setPostCode(200000);
        }
        if("北京".equals(userDTO.getProvince())){
            userVO.setAbbreviation("京");
            userVO.setPostCode(100000);
        }
        // 手机号处理
        if(null != userDTO.getTelephone() && !"".equals(userDTO.getTelephone())){
            userVO.setTelephone(userDTO.getTelephone().substring(0,3)+"****"+userDTO.getTelephone().substring(7));
        }
        Map<String, String> idMap = IDNumberUtils.getBirAgeSex(userDTO.getIdNo());
        userVO.setAge(idMap.get("age")!=null ? Integer.parseInt(idMap.get("age")) : 0);
        LogUtils.info("response user:", userVO.toString());
        return userVO;
    }
}
