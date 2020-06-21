package com.javakk.spock.util

import spock.lang.Specification
import spock.lang.Unroll

class IDNumberUtilsTest extends Specification {
    void setup() {
    }

    @Unroll
    def "身份证号:#idNo 的生日,性别,年龄是:#result"() {
        expect: "[birthday:1987-06-28, sex:男, age:33]"
        IDNumberUtils.getBirAgeSex(idNo) == result

        where:
        idNo                 || result
        "310168199809187333" || ["birthday": "1998-09-18", "sex": "男", "age": "22"]
        "320168200212084268" || ["birthday": "2002-12-08", "sex": "女", "age": "18"]
        "330168199301214267" || ["birthday": "1993-01-21", "sex": "女", "age": "27"]
        "411281870628201"    || ["birthday": "1987-06-28", "sex": "男", "age": "33"]
        "427281730307862"    || ["birthday": "1973-03-07", "sex": "女", "age": "47"]
        "479281691111377"    || ["birthday": "1969-11-11", "sex": "男", "age": "51"]
    }
}
