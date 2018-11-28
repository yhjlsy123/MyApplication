package com.xinglincloud.www.newsuperbath.Util;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class VersionUtil {
    public static String getVersion(Activity a ) {
        try {
            PackageManager manager = a.getPackageManager();
            PackageInfo info = manager.getPackageInfo(a.getPackageName(), 0);
            String version = info.versionName;
            return "版本：" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "找不到版本号";
        }
    }
}
