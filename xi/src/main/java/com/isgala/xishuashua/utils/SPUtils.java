package com.isgala.xishuashua.utils;

import com.isgala.xishuashua.config.BathHouseApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * SharedPreference工具类
 *
 * @author gong
 */
public class SPUtils {

    private static SharedPreferences sp;

    /**
     * 保存boolean信息
     *
     * @param key   保存信息的标示
     * @param value 保存信息的值
     */
    public static void saveBoolean(String key, boolean value) {
        if (sp == null) {
            sp = BathHouseApplication.mApplicationContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
        /*
         * Editor edit = sp.edit(); edit.putBoolean(key, value); edit.commit();
		 */
    }

    /**
     * 获取保存的boolean信息
     *
     * @param key      保存信息的标示
     * @param defValue 缺省的值
     * @return
     */
    public static boolean getBoolean(String key, boolean defValue) {
        if (sp == null) {
            sp = BathHouseApplication.mApplicationContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

    /**
     * @param key   保存信息的标示
     * @param value 保存信息的值
     */
    public static void saveString(String key, String value) {
        if (sp == null) {
            sp = BathHouseApplication.mApplicationContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
		/*
		 * Editor edit = sp.edit(); edit.putBoolean(key, value); edit.commit();
		 */
    }

    /**
     * 此方法内部加载了application的context
     *
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(String key, String defValue) {
        if (sp == null) {
            sp = BathHouseApplication.mApplicationContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    /**
     * 缓存网络信息
     *
     * @param key   保存信息的标示
     * @param value 保存信息的值
     */
    public static void saveFloat(String key, float value) {
        if (sp == null) {
            sp = BathHouseApplication.mApplicationContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putFloat(key, value).commit();
        /*
		 * Editor edit = sp.edit(); edit.putBoolean(key, value); edit.commit();
		 */
    }

    /**
     * 此方法内部加载了application的context
     *
     * @param key
     * @param defValue
     * @return
     */
    public static float getFloat(String key, float defValue) {
        if (sp == null) {
            sp = BathHouseApplication.mApplicationContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getFloat(key, defValue);
    }
    public static String getLocalVersion(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}
