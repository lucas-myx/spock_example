package com.javakk.spock.model;

import java.util.List;

public class UserDTO {
    private int id;
    private String name;
    private int age;
    private String sex;
    private String province;
    private String telephone;
    private String idNo;
    private String currency; // 币种
    private List<OrderVO> userOrders; // 用户订单

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<OrderVO> getUserOrders() {
        return userOrders;
    }

    public void setUserOrders(List<OrderVO> userOrders) {
        this.userOrders = userOrders;
    }
}
