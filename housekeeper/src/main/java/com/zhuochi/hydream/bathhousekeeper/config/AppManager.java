package com.zhuochi.hydream.bathhousekeeper.config;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.zhuochi.hydream.bathhousekeeper.activity.FeedbackManageActivity;
import com.zhuochi.hydream.bathhousekeeper.activity.MainActivity;

import java.util.Stack;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * 应用程序Activity管理类，用于Activity管理和应用程序退出
 *
 * @author gong
 */
public enum AppManager {

    // 单一实例，用枚举来实现
    INSTANCE;

    private static Stack<Activity> activityStack;

    public int getsize() {
        return activityStack.size();
    }

    /**
     * 添加Activity到堆栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 是否包含某个activity
     *
     * @param targetClass
     * @return
     */
    public boolean containActivity(Class<?> targetClass) {
        boolean flag = false;
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(targetClass)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取当前Activity
     *
     * @return
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        activity.finish();
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activityStack == null)
            return;
        if (activity != null) {
            activity.finish();
            // activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 从栈中移除
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param clazz
     */
    public void finishActivity(Class<?> clazz) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(clazz)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public synchronized void finishAllActivity() {
        if (activityStack == null)
            return;
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (activityStack.get(i) != null) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 保留指定的Acitivity
     */
    public synchronized void toSaveAcitivity(Class<?> calss, Intent intent) {
        if (activityStack == null)
            return;
        for (int i = 0; i < activityStack.size(); i++) {
            if (calss.equals(activityStack.get(i).getClass()) || activityStack.get(i) instanceof MainActivity) {

            } else if (null != activityStack.get(i)) {
                activityStack.get(i).setResult(100, intent);
                activityStack.get(i).finish();
                activityStack.remove(activityStack.get(i));

            }


        }
    }

    /**
     * 退出应用程序
     *
     * @param context
     */
    public void appExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
