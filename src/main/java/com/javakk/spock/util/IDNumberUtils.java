package com.javakk.spock.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证号码工具类<p>
 * 15位：6位地址码+6位出生年月日（900101代表1990年1月1日出生）+3位顺序码
 * 18位：6位地址码+8位出生年月日（19900101代表1990年1月1日出生）+3位顺序码+1位校验码
 * 顺序码奇数分给男性，偶数分给女性。
 * @author 公众号:Java老K
 * 个人博客:www.javakk.com
 */
@SuppressWarnings("all")
public class IDNumberUtils {
    /**
     * 通过身份证号码获取出生日期、性别、年龄
     * @param certificateNo
     * @return 返回的出生日期格式：1990-01-01   性别格式：F-女，M-男
     */
    public static Map<String, String> getBirAgeSex(String certificateNo) {
        String birthday = "";
        String age = "";
        String sex = "";

        int year = Calendar.getInstance().get(Calendar.YEAR);
        char[] number = certificateNo.toCharArray();
        boolean flag = true;
        if (number.length == 15) {
            for (int x = 0; x < number.length; x++) {
                if (!flag) return new HashMap<>();
                flag = Character.isDigit(number[x]);
            }
        } else if (number.length == 18) {
            for (int x = 0; x < number.length - 1; x++) {
                if (!flag) return new HashMap<>();
                flag = Character.isDigit(number[x]);
            }
        }
        if (flag && certificateNo.length() == 15) {
            birthday = "19" + certificateNo.substring(6, 8) + "-" + certificateNo.substring(8, 10) + "-" + certificateNo.substring(10, 12);
            sex = Integer.parseInt(certificateNo.substring(certificateNo.length() - 3, certificateNo.length())) % 2 == 0 ? "女" : "男";
            age = (year - Integer.parseInt("19" + certificateNo.substring(6, 8))) + "";
        } else if (flag && certificateNo.length() == 18) {
            birthday = certificateNo.substring(6, 10) + "-" + certificateNo.substring(10, 12) + "-" + certificateNo.substring(12, 14);
            sex = Integer.parseInt(certificateNo.substring(certificateNo.length() - 4, certificateNo.length() - 1)) % 2 == 0 ? "女" : "男";
            age = (year - Integer.parseInt(certificateNo.substring(6, 10))) + "";
        } else {
            birthday = certificateNo.substring(6, 10);
            sex = Integer.parseInt(certificateNo.substring(certificateNo.length() - 4, certificateNo.length() - 1)) % 2 == 0 ? "女" : "男";
            age = (year - Integer.parseInt(certificateNo.substring(6, 10))) + "";
        }
        Map<String, String> map = new HashMap<>();
        map.put("birthday", birthday);
        map.put("age", age);
        map.put("sex", sex);
        return map;
    }
}
