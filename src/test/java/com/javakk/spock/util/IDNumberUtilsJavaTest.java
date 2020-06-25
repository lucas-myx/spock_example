package com.javakk.spock.util;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.function.Predicate;


/**
 * 使用junit的Parameters参数化注解测试多分支的方法，
 * 可以对比IDNumberUtilsTest.groovy，两个单测的测试结果一样,
 * 但语法上没有Spock的写法简洁和直观，报错信息也不够详细
 */
@RunWith(JUnitParamsRunner.class)
public class IDNumberUtilsJavaTest {

    @Test
    @Parameters(method = "getBirAgeSexParams")
    public void getBirAgeSex(String certificateNo, Predicate<Map<String, String>> predicate) {
        Map<String, String> minuteMap = IDNumberUtils.getBirAgeSex(certificateNo);
        Assert.assertTrue(predicate.test(minuteMap));
    }

    public Object[] getBirAgeSexParams() {
        return new Object[]{
                new Object[]{
                        "310168199809187333", (Predicate<Map<String, String>>) map ->
                        "{birthday=1998-09-18, sex=男, age=22}".equals(map.toString())
                },
                new Object[]{
                        "320168200212084268", (Predicate<Map<String, String>>) map ->
                        "{birthday=2002-12-08, sex=女, age=18}".equals(map.toString())
                },
                new Object[]{
                        "330168199301214267", (Predicate<Map<String, String>>) map ->
                        "{birthday=1993-01-21, sex=女, age=27}".equals(map.toString())
                },
                new Object[]{
                        "411281870628201", (Predicate<Map<String, String>>) map ->
                        "{birthday=1987-06-28, sex=男, age=33}".equals(map.toString())
                },
                new Object[]{
                        "427281730307862", (Predicate<Map<String, String>>) map ->
                        "{birthday=1973-03-07, sex=女, age=47}".equals(map.toString())
                },
                new Object[]{
                        "479281691111377", (Predicate<Map<String, String>>) map ->
                        "{birthday=1969-11-11, sex=男, age=51}".equals(map.toString())
                }
        };
    }
}