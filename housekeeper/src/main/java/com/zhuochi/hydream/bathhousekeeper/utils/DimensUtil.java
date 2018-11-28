package com.zhuochi.hydream.bathhousekeeper.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.zhuochi.hydream.bathhousekeeper.config.BathHouseApplication;


/**
 * Created by and on 2016/11/3.
 */

public class DimensUtil {
    private static int screenWidth;
    private static int screenHeight;

    public static int dpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    /**
     * 动态设置字体
     *
     * @return
     */
    public static float getFontSizeRate() {
        return (float) (getWidthRate() > getHeightRate() ? getHeightRate() : getWidthRate());
    }

    /**
     * 宽度的缩放比
     *
     * @return
     */
    public static double getWidthRate() {
        double rate = getScreenWidth() / 750.00d;
        return rate;
    }

    /**
     * 基于6P的宽度比
     *
     * @return
     */
    public static double get6PWidthRate() {
        double rate = getScreenWidth() / 1080.00d;
        return rate;
    }

    /**
     * 基于6P的宽度比
     *
     * @return
     */
    public static double get6PHeightRate() {
        double rate = getScreenHeight() / 1920.00d;
        return rate;
    }

    /**
     * 高度的缩放比
     *
     * @return
     */
    public static double getHeightRate() {

        double rate = getScreenHeight() / 1334.00d;

        return rate;
    }

    /**
     * 高度的缩放比
     *
     * @return
     */
    public static double getHeightRate(int height) {

        double rate = height * 1.0d / getScreenHeight();

        return rate;
    }

    /**
     * 宽度的缩放比
     *
     * @return
     */
    public static double getWidthRate(int width) {
        double rate = width * 1.0d / getScreenWidth();
        return rate;
    }

    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        if (screenWidth == 0) {
            DisplayMetrics displayMetrics = new DisplayMetrics();

            WindowManager windowManager = (WindowManager) BathHouseApplication.mApplicationContext
                    .getSystemService(Context.WINDOW_SERVICE);

            windowManager.getDefaultDisplay().getMetrics(displayMetrics);

            screenWidth = displayMetrics.widthPixels;
        }
        return screenWidth;
    }

    /**
     * 获取屏幕的高度
     *
     * @return
     */
    public static int getScreenHeight() {
        if (screenHeight == 0) {
            WindowManager w = (WindowManager) BathHouseApplication.mApplicationContext.getSystemService(Context.WINDOW_SERVICE);
            Display d = w.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            d.getMetrics(metrics);

            screenHeight = metrics.heightPixels;

            // includes window decorations (statusbar bar/menu bar)
            if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
                try {
                    screenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
                } catch (Exception ignored) {
                }
            // includes window decorations (statusbar bar/menu bar)
            if (Build.VERSION.SDK_INT >= 17)
                try {
                    Point realSize = new Point();
                    Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                    screenHeight = realSize.y;
                } catch (Exception ignored) {
                }
        }
        return screenHeight;
    }

    /**
     * 获取状态栏的高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        int result = 0;
        try {
            int resourceId = BathHouseApplication.mApplicationContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = BathHouseApplication.mApplicationContext.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
