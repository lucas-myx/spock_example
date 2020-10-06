package com.javakk.spock

import com.javakk.spock.util.IDNumberUtils
import com.javakk.spock.util.LogUtils
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import org.spockframework.runtime.Sputnik
import spock.lang.Specification

/**
 * Spock基类
 * @Author: www.javakk.com
 * @Description: 公众号:Java老K
 * @Date: Created in 20:53 2020/7/16
 * @Modified By:
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(Sputnik.class)
@PrepareForTest([LogUtils.class])
@PowerMockIgnore(["javax.management.*", "javax.net.ssl.*"])
@SuppressStaticInitializationFor(["com.javakk.spock.util.LogUtils"])
class BaseSpock extends Specification{

    void setup() {
        println "Spock setup"
        // mock掉一些项目中常用的类,比如日志记录
        PowerMockito.mockStatic(LogUtils.class)
    }
}
