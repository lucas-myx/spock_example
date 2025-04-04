package com.javakk.spock

import com.javakk.spock.util.LogUtils
import spock.lang.Specification

/**
 * Spock基类
 * @Author: www.javakk.com
 * @Description: 公众号:Java老K
 * @Date: Created in 20:53 2020/7/16
 * @Modified By:
 */
class BaseSpock extends Specification{

    void setup() {
        println "Spock setup"
        // mock掉一些项目中常用的类,比如日志记录
        SpyStatic(LogUtils.class)
        // mock LogUtils的所有静态方法
        LogUtils._ >> _
    }
}
