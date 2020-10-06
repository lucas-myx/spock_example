package com.javakk.spock.service;

public abstract class AbstractService {
    String parentMethod(){
        // 发起接口调用或数据库操作
        return "parentMethod value";
    }
}
