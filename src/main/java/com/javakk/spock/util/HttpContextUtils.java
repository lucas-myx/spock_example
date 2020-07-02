package com.javakk.spock.util;

import com.javakk.spock.model.RequestHeadVO;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class HttpContextUtils {
    private static final int CONTEXT_DEFAULT_SIZE = 64;
    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<Map<String, Object>>() {
        protected Map<String, Object> initialValue() {
            return new ConcurrentHashMap(CONTEXT_DEFAULT_SIZE);
        }
    };

    public static final String REQUEST_HEAD_KEY = "Request_Head";

    public static void initialize(RequestHeadVO requestHead) {
        if (CONTEXT.get().containsKey(REQUEST_HEAD_KEY)) {
            (CONTEXT.get()).remove(REQUEST_HEAD_KEY);
        }
        CONTEXT.get().put(REQUEST_HEAD_KEY, requestHead);
    }

    private static RequestHeadVO getRequestHead() {
        return (RequestHeadVO)CONTEXT.get().get(REQUEST_HEAD_KEY);
    }

    /**
     * 获取当前币种
     *
     * @return
     */
    public static String getCurrentCurrency() {
        RequestHeadVO requestHead = getRequestHead();
        return Optional.of(requestHead).map(RequestHeadVO::getCurrency).orElse(null);
    }

    /**
     * 获取当前来源: APP,WAP(H5),ONLINE(PC)
     *
     * @return
     */
    public static String getCurrentSource() {
        RequestHeadVO requestHead = getRequestHead();
        return Optional.of(requestHead).map(RequestHeadVO::getSource).orElse(null);
    }
}
