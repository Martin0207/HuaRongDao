package com.example.huarongdao.util;

public class TimingUtil {

    /**
     * 计时化作字符串输出
     */
    public static String timing2String(int timing) {
        if (timing < 60) {
            if (timing < 10) {
                return "00:0" + timing;
            }
            return "00:" + timing;
        } else {
            int min = timing / 60;
            if (min >= 10) {
                return min + ":" + timing % 60;
            } else {
                return "0" + min + ":" + timing % 6;
            }
        }
    }

}
