package com.isgala.xishuashua.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import java.text.DecimalFormat;

/**
 * 一些小工具
 *
 * @author and
 */
public class Tools {
    /**
     * 将钱数格式化成(*.00)
     *
     * @param money
     * @return
     */
    public static String formatMoney(String money) {
        if (TextUtils.isEmpty(money)) {
            money = "0";
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(Double.valueOf(money.trim()));
    }


    private static long lastClickTime;

    /**
     * 双击关闭
     *
     * @return 可以关闭返回true
     */
    public synchronized static boolean dblClose() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 3000) {
            return true;
        }
        lastClickTime = time;
        ToastUtils.show("再按一次退出程序");
        return false;
    }

    private static boolean notClick;

    /**
     * 将字符串转成int类型,null返回0;
     */
    public static int toInt(String str) {
        int temp = 0;
        if (!TextUtils.isEmpty(str)) {
            try {
                temp = Integer.valueOf(str);
            } catch (Exception e) {
            }
        }
        return temp;
    }

    /**
     * 将字符串转成float类型,null返回0;
     */
    public static float toFloat(String str) {
        float temp = 0.00f;
        if (!TextUtils.isEmpty(str)) {
            try {
                temp = Float.valueOf(str);
            } catch (Exception e) {
            }
        }
        return temp;
    }

    public static int measureHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将时间格式化：xx′xx″
     *
     * @param s
     * @return
     */
    public static String formatTime(long s) {
        Integer mi = 60;
        long minute = s / mi;
        long second = (s - mi * minute);

        StringBuffer stringBuffer = new StringBuffer();

        if (minute > 0) {
            if (minute < 10) {
                stringBuffer.append(0);
            }
            stringBuffer.append(minute + "′");
        }
        if (minute == 0) {
            if (second > 0) {
                if (second < 10) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(second + "″");
            }
        } else {
            if (second > 0) {
                if (second < 10) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(second + "″");
            } else {
                stringBuffer.append("00″");
            }
        }
        return stringBuffer.toString();
    }
}
