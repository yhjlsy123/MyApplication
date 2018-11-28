package com.zhuochi.hydream.bathhousekeeper.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    /**
     * 计算屏幕宽度
     *
     * @param context 上下文环境
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context){
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    /**
     * 计算屏幕高度
     *
     * @param context 上下文环境
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context context){
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    /**
     * 返回状态栏高度
     *
     * @param context 上下文环境
     * @return 状态栏高度
     */
    public static int getRectTop(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 平面 计算两点距离
     */
    public static int getDistance(int x1, int x2, int y1, int y2){
        int xx = Math.abs(x1 - x2);
        int yy = Math.abs(y1 - y2);
        return (int) Math.sqrt(xx * xx + yy * yy);
    }

    /**
     * 设置屏幕亮度
     */
    public static void setScreenLight(Activity activity, float light){
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        layoutParams.alpha = light;
        activity.getWindow().setAttributes(layoutParams);
    }
}
