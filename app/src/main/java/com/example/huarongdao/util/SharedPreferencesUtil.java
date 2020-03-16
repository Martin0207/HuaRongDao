package com.example.huarongdao.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    public static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences("difficulty", Context.MODE_PRIVATE);
    }

}
