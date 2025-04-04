package com.javakk.spock.util;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;


/**
 * 使用junit的Parameters参数化注解测试多分支的方法，
 * 可以对比IDNumberUtilsTest.groovy，两个单测的测试结果一样,
 * 但语法上没有Spock的写法简洁和直观，报错信息也不够详细
 */
public class IDNumberUtilsJavaTest {

    @ParameterizedTest
    @MethodSource("getBirAgeSexParams")
    public void getBirAgeSex(String certificateNo, Predicate<Map<String, String>> predicate) {
        Map<String, String> minuteMap = IDNumberUtils.getBirAgeSex(certificateNo);
        Assertions.assertTrue(predicate.test(minuteMap));
    }

    static Stream<Arguments> getBirAgeSexParams() {
        return Stream.of(
                arguments(
                        "310168199809187333", (Predicate<Map<String, String>>) map ->
                        "{birthday=1998-09-18, sex=男, age=27}".equals(map.toString())
                ),
                arguments(
                        "320168200212084268", (Predicate<Map<String, String>>) map ->
                        "{birthday=2002-12-08, sex=女, age=23}".equals(map.toString())
                ),
                arguments(
                        "330168199301214267", (Predicate<Map<String, String>>) map ->
                        "{birthday=1993-01-21, sex=女, age=32}".equals(map.toString())
                ),
                arguments(
                        "411281870628201", (Predicate<Map<String, String>>) map ->
                        "{birthday=1987-06-28, sex=男, age=38}".equals(map.toString())
                ),
                arguments(
                        "427281730307862", (Predicate<Map<String, String>>) map ->
                        "{birthday=1973-03-07, sex=女, age=52}".equals(map.toString())
                ),
                arguments(
                        "479281691111377", (Predicate<Map<String, String>>) map ->
                        "{birthday=1969-11-11, sex=男, age=56}".equals(map.toString())
                )
        );
    }
}