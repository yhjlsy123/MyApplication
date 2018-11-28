package com.zhuochi.hydream.bathhousekeeper.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by Administrator on 2016/6/22 0022.
 */

public class JumpUtils {
    /**
     * 跳Atvity 穿多个字符串参数 取按照常规去
     */
    public static void jumpAcitvity(Context context, Class<?> tClass, Map<String, String> dataIntent) {
        Intent intent;
        intent = new Intent();
        Bundle bundle = new Bundle();
        if (null != dataIntent) {
            for (Map.Entry<String, String> entry : dataIntent.entrySet()) {
                System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                bundle.putString(entry.getKey(), entry.getValue());
            }
        }
        intent.setClass(context, tClass);
        context.startActivity(intent);
    }

    /**
     * 跳ativity 待序列化参数
     */

    public static void jumpAcitvityParce(Context context, Class<?> tClass, Parcelable obj) {
        Intent intent;
        intent = new Intent();
        intent.putExtra(Constant.intentPram, obj);
        intent.setClass(context, tClass);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, 100);
            return;
        }
        context.startActivity(intent);
    }

    /**
     * 跳ativity 待序列化参数
     */

    public static void jumpAcitvityParce(Context context, Class<?> tClass, Parcelable obj, String atach) {
        Intent intent;
        intent = new Intent();
        intent.putExtra(Constant.intentPram, obj);
        intent.putExtra(Constant.addition, atach);
        intent.setClass(context, tClass);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, 100);
            return;
        }
        context.startActivity(intent);
    }

    /**
     * 跳ativity 待序列化参数
     */

    public static void jumpAcitvityAndCloseActParce(Context context, Class<?> tClass, Parcelable obj, String atach) {
        Intent intent;
        intent = new Intent();
        intent.putExtra(Constant.intentPram, obj);
        intent.putExtra(Constant.addition, atach);
        intent.setClass(context, tClass);
        if (context instanceof Activity) {
            startActivityForResult((Activity) context, intent, true);
        }
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, Intent intent, Boolean isColse) {
        activity.startActivityForResult(intent, 100);
        if (isColse)
            activity.finish();
        return;
    }


    /**
     * 跳ativity 待序列化参数
     */

    public static void jumpAcitvityOnePram(Context context, Class<?> tClass, String pram) {
        Intent intent;
        intent = new Intent();
        intent.putExtra(Constant.intentPram, pram);
        intent.setClass(context, tClass);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, 100);
            return;
        }
        context.startActivity(intent);
    }

}