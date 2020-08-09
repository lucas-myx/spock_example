package com.javakk.spock.util

import spock.lang.Specification
import spock.lang.Unroll

/**
 * 身份证号码工具测试类<p>
 * 15位：6位地址码+6位出生年月日（900101代表1990年1月1日出生）+3位顺序码
 * 18位：6位地址码+8位出生年月日（19900101代表1990年1月1日出生）+3位顺序码+1位校验码
 * 顺序码奇数分给男性，偶数分给女性。
 * @author 公众号:Java老K
 * 个人博客:www.javakk.com
 */
class IDNumberUtilsTest extends Specification {

    @Unroll
    def "身份证号:#idNo 的生日,性别,年龄是:#result"() {
        expect: "when + then 组合"
        IDNumberUtils.getBirAgeSex(idNo) == result

        where: "表格方式测试不同的分支逻辑"
        idNo                 || result
        "310168199809187333" || ["birthday": "1998-09-18", "sex": "男", "age": "22"]
        "320168200212084268" || ["birthday": "2002-12-08", "sex": "女", "age": "18"]
        "330168199301214267" || ["birthday": "1993-01-21", "sex": "女", "age": "27"]
        "411281870628201"    || ["birthday": "1987-06-28", "sex": "男", "age": "33"]
        "427281730307862"    || ["birthday": "1973-03-07", "sex": "女", "age": "47"]
        "479281691111377"    || ["birthday": "1969-11-11", "sex": "男", "age": "51"]
    }
}
