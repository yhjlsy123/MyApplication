package com.zhuochi.hydream.bathhousekeeper.config;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.tencent.bugly.crashreport.CrashReport;
import com.zhuochi.hydream.bathhousekeeper.activity.LoginActivity;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.UserUtils;
import com.zhuochi.hydream.bathhousekeeper.view.HeightSelectView;
import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by and on 2016/11/3.
 */

public class BathHouseApplication extends Application {
    public static Context mApplicationContext;
    /**
     * true :测试(打日志，系统处理异常);
     * false :正式(不打日志,自定义异常处理机制)
     */
    // TODO 上线需修改
    public static final boolean DEGUG = true;
    public static int VERSION_CODE = 9;
    public static HeightSelectView mHeightSelectView;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
        CrashReport.initCrashReport(getApplicationContext(), "ad407a971d", false);//初始化bugly
//        CrashHandler.getInstance().init(this);//开启了之后捕捉不到bugly
        AutoLayoutConifg.getInstance().useDeviceSize();

//        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);     		// 初始化 JPush
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//        builder.detectFileUriExposure();

        try {
            VERSION_CODE = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }



    /**
     * 登录过期，跳转到登录界面
     */
    public static void jumpLogin() {
        UserUtils.setDataNull();
//        SPUtils.saveString("balance", "");
//        SPUtils.saveString(Constants.S_ID, "");
//        SPUtils.saveString(Constants.CAMPUS, "");
//        SPUtils.saveString(Constants.OAUTH_TOKEN, "");
//        SPUtils.saveString(Constants.OAUTH_TOKEN_SECRET, "");
//        SPUtils.saveString("uuid", "");
        SPUtils.saveBoolean(Constants.IS_LOGIN, false);
        Intent intent = new Intent(mApplicationContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mApplicationContext.startActivity(intent);
//        MyApp.clearData(mApplicationContext);
//        JPushInterface.cleanTags(mApplicationContext,0);
        AppManager.INSTANCE.finishAllActivity();
    }
}

