package com.javakk.spock.model;

import java.util.List;

public class UserVO {
    private int id;
    private String name;
    private int age;
    private String sex;
    private int postCode; // 邮编
    private String abbreviation; // 省份简称
    private String country; // 国家
    private String telephone;
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

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<OrderVO> getUserOrders() {
        return userOrders;
    }

    public void setUserOrders(List<OrderVO> userOrders) {
        this.userOrders = userOrders;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", postCode=" + postCode +
                ", abbreviation='" + abbreviation + '\'' +
                '}';
    }
}
