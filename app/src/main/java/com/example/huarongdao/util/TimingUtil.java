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
            int second = timing % 60;
            String sec = second < 10 ? "0" + second : String.valueOf(second);
            if (min >= 10) {
                return min + ":" + sec;
            } else {
                return "0" + min + ":" + sec;
            }
        }
    }

}
