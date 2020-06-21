package com.javakk.spock.util;

/**
 * 日志和埋点工具类，日志可能是记录在ElasticSearch,Cat等分布式中间件上，
 * 方便全文检索和日志查询，这些日志在项目启动前都需要进行资源的初始化操作，
 * 有些会读取本地或公司配置中心的一些配置，因此比较耗费时间，
 * 这种情况一般在写单测时都会mock掉
 */
public class LogUtils {

    private static String envirConfig;

    static {
        // 初始化环境配置,如果被mock了该方法体的代码不会执行
        System.out.println("environment init...");
    }

    public static void info(String title, String content){
        // 异步调用ES服务记录日志或埋点，如果被mock了该方法体的代码不会执行
        System.out.println("LogUtils.info");
    }

    public static void warn(String title, Throwable ex){
        // 异步调用ES服务记录日志或埋点，如果被mock了该方法体的代码不会执行
        System.out.println("LogUtils.warn");
    }

    public static void error(String title, Throwable ex){
        // 异步调用ES服务记录日志或埋点，如果被mock了该方法体的代码不会执行
        System.out.println("LogUtils.error");
    }
}
