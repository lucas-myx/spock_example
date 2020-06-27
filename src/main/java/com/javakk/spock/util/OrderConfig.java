package com.javakk.spock.util;

import org.springframework.stereotype.Component;

@Component
public class OrderConfig {

    /**
     * 是否展示订单创建时间
     * @return
     */
    public boolean isShowOrderTime(){
        // 调用统一配置中心CMS获取配置
        return true;
    }
}
