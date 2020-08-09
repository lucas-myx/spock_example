package com.javakk.spock.controller;

import com.javakk.spock.model.APIException;
import com.javakk.spock.model.OrderVO;
import com.javakk.spock.service.UserService;
import com.javakk.spock.model.UserVO;
import com.javakk.spock.util.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/get")
    public UserVO getUser(int uid){
        return userService.getUserById(uid);
    }

    @RequestMapping("/add")
    public String addUser(UserVO user) throws APIException {
        validateUser(user);
        return userService.addUser(user) ? "success" : "fail";
    }

    /**
     * 校验请求参数user是否合法
     * @param user
     * @throws APIException
     */
    public void validateUser(UserVO user) throws APIException {
        if(user == null){
            throw new APIException("10001", "user is null");
        }
        if(null == user.getName() || "".equals(user.getName())){
            throw new APIException("10002", "user name is null");
        }
        if(user.getAge() == 0){
            throw new APIException("10003", "user age is null");
        }
        if(null == user.getTelephone() || "".equals(user.getTelephone())){
            throw new APIException("10004", "user telephone is null");
        }
        if(null == user.getSex() || "".equals(user.getSex())){
            throw new APIException("10005", "user sex is null");
        }
        if(null == user.getUserOrders() || user.getUserOrders().size() <= 0){
            throw new APIException("10006", "user order is null");
        }
        for(OrderVO order : user.getUserOrders()) {
            if (null == order.getOrderNum() || "".equals(order.getOrderNum())) {
                throw new APIException("10007", "order number is null");
            }
            if (null == order.getAmount()) {
                throw new APIException("10008", "order amount is null");
            }
        }
    }
}
