package com.isgala.xishuashua.utils;

import android.text.TextUtils;
import android.util.Log;

import static com.isgala.xishuashua.config.BathHouseApplication.DEGUG;
import static com.lidroid.xutils.util.LogUtils.customTagPrefix;

/**
 * Created by and on 2016/11/3.
 */

public class LogUtils {
    private static final int level = 3;

    private static final int LEVEL_V = 0;
    private static final int LEVEL_D = 1;
    private static final int LEVEL_I = 2;
    private static final int LEVEL_W = 3;
    private static final int LEVEL_E = 4;
    private static final String APPLICATION = "BATHROOM : ";

    public static void v(String TAG, Object content) {
        if (DEGUG && level <= LEVEL_V) {
            Log.v(TAG, String.valueOf(content));
        }
    }

    public static void d(String TAG, Object content) {
        if (DEGUG && level <= LEVEL_D) {
            Log.d(TAG, String.valueOf(content));
        }
    }

    public static void i(String TAG, Object content) {
        if (level <= LEVEL_I) {
            Log.i(TAG, String.valueOf(content));
        }
    }

    public static void w(String TAG, Object content) {
        if (DEGUG && level <= LEVEL_W) {
            Log.w(TAG, String.valueOf(content));
        }
    }

    public static void e(String TAG, Object content) {
        if (DEGUG && level <= LEVEL_E) {
            Log.e(APPLICATION + TAG, String.valueOf(content));
        }
    }

    private static String generateTag() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = "%s.%s(Line:%d)"; // 占位符
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(),
                caller.getLineNumber()); // 替换
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":"
                + tag;
        return tag;
    }
}
