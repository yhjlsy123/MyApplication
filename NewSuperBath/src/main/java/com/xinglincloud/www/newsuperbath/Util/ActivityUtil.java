package com.xinglincloud.www.newsuperbath.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Activity 相关操作 工具类
 */

public class ActivityUtil {
    /**
     * 存储打开的Activity的列表
     */
    private static ArrayList<Activity> ACTIVITY_LIST = new ArrayList<>();

    /**
     * 添加Activity到集合
     */
    public static void addActivityToList(Activity acty) {
        ACTIVITY_LIST.add(acty);
    }

    /**
     * 将Activity从集合移除
     */
    public static void removeActivityFromList(Activity acty) {
        ACTIVITY_LIST.remove(acty);
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (Activity acty : ACTIVITY_LIST) {
            if (acty != null && !acty.isFinishing()) {
                acty.finish();
            }
        }

        ACTIVITY_LIST.clear();
    }

    /**
     * 结束某个Activity
     *
     * @param classObj Activity的class对象
     */
    public static void finishActivity(Class<?>... classObj) {
        for (Class<?> obj : classObj) {
            for (Activity acty : ACTIVITY_LIST) {
                if (acty != null && acty.getClass() == obj && !acty.isFinishing()) {
                    acty.finish();
                    break;
                }
            }
        }
    }

    /**
     * 开启新的Activity
     */
    public static void startActivity(Activity context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    /**
     * 开启新的Activity
     */
    public static void startActivity(Activity context, Class<?> cls, String paramName, boolean value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivity(intent);
    }

    /**
     * 开启新的Activity
     */
    public static void startActivity(Activity context, Class<?> cls, String paramName, String value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivity(intent);
    }

    /**
     * 开启新的Activity
     */
    public static void startActivity(Activity context, Class<?> cls, String paramName, int value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivity(intent);
    }

    /**
     * 开启新的Activity
     */
    public static void startActivity(Activity context, Class<?> cls, String paramName, Parcelable value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivity(intent);
    }

    /**
     * 开启新的Activity
     */
    public static void startActivity(Activity context, Class<?> cls, String paramName, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, bundle);
        context.startActivity(intent);
    }

    /**
     * 开启新的Activity
     *
     */
    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 开启新的Activity
     *
     */
    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, Parcelable value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 开启新的Activity
     *
     */
    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, boolean value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 开启新的Activity
     *
     */
    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, int value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 开启新的Activity
     *
     */
    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, String value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 开启新的Activity
     *
     */
    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, bundle);
        context.startActivityForResult(intent, requestCode);
    }
    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
//        boolean flag=false;
        for (ActivityManager.RunningTaskInfo taskInfo : list) {
            if (taskInfo.topActivity.getShortClassName().contains(className)) { // 说明它已经启动了
//                flag = true;
                return true;
            }
        }
        return false;
    }

}
