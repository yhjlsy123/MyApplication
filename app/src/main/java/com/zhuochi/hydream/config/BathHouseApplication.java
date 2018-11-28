package com.zhuochi.hydream.config;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.klcxkj.zqxy.MyApp;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.activity.LoginActivity;
import com.zhuochi.hydream.utils.CrashHandler;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.UserUtils;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

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
    public static final boolean DEGUG = false;
    public static int VERSION_CODE = 9;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
        CrashReport.initCrashReport(getApplicationContext(), "0e94f61550", false);//初始化bugly
        CrashHandler.getInstance().init(this);//开启了之后捕捉不到bugly
        AutoLayoutConifg.getInstance().useDeviceSize();
//        Bugtags.start("be8b9ce8f6f236fcc59d228c088dac9d", this, Bugtags.BTGInvocationEventBubble);
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        setStyleCustom(getApplicationContext());

        try {
            VERSION_CODE = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setStyleCustom(Context context) {
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(context, R.layout.customer_not_layout, R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.mipmap.ic_launcher;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
    }


    /**
     * 登录过期，跳转到登录界面
     */
    public static void jumpLogin() {
        UserUtils.setDataNull();
        ToastUtils.show("登录过期,请重新登录！！！");
        //TODO JG 解除极光推送绑定
        Set<String> set = new HashSet<String>();
        int str = UserUtils.getInstance(mApplicationContext).getOrgAreaID();
        set.add("orgArea_" + str);//绑定 校区（Tag）
        JPushInterface.deleteTags(mApplicationContext, 0, set);//删除标签
        JPushInterface.deleteAlias(mApplicationContext, 0);//删除别名

        SPUtils.saveInt(Constants.ORG_ID, 0);//学校ID
        SPUtils.saveString(Constants.MOBILE_PHONE, "");
        SPUtils.saveBoolean(Constants.IS_LOGIN, false);
        Intent intent = new Intent(mApplicationContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApp.clearData(mApplicationContext);
        mApplicationContext.startActivity(intent);
        AppManager.INSTANCE.finishAllActivity();
        //防止多个接口finish掉全部activity 以下判断 重新启动新的activity
        if (!AppManager.INSTANCE.containActivity(LoginActivity.class)) {
            mApplicationContext.startActivity(new Intent(mApplicationContext, LoginActivity.class));
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

