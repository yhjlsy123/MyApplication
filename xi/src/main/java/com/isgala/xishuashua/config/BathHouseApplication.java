package com.isgala.xishuashua.config;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.ui.H5Activity;
import com.isgala.xishuashua.ui.HomeActivity;
import com.isgala.xishuashua.ui.LoginRevisionActivity;
import com.isgala.xishuashua.utils.LogUtils;
import com.isgala.xishuashua.utils.SPUtils;
import com.klcxkj.zqxy.MyApp;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.Map;

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

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
        CrashHandler.init(this);
        AutoLayoutConifg.getInstance().useDeviceSize();
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e("BathHouseApplication", "device Token : " + deviceToken);
                SPUtils.saveString(Constants.DEVICE_TOKEN, deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {


            }
        });
        mPushAgent.setMessageHandler(new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                Intent intent = new Intent();
                intent.setAction(Constants.NEW_MESSAGE);
                sendBroadcast(intent);
                LogUtils.e("UMessage Notification", uMessage.getRaw().toString());
                return super.getNotification(context, uMessage);
            }

            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {//处理自定义消息
//                LogUtils.e("BathHouseApplication", "customMessage : " + msg.custom);
                LogUtils.e("BathHouseApplication", "customMessage extra : " + msg.extra);
                //自定义消息 提示： 更新排队位置 或 已经可以变成服务中，已经结束服务
                receive(msg);
            }
        });
        mPushAgent.setNotificationClickHandler(new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                LogUtils.e("dealWithCustomAction", uMessage.getRaw().toString());
                Map<String, String> extra = uMessage.extra;
                String href = extra.get("url");
                if (!TextUtils.isEmpty(href)) {
                    Intent intent = new Intent(context, H5Activity.class);
                    intent.putExtra("title", "消息详情");
                    intent.putExtra("url", href);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    super.dealWithCustomAction(context, uMessage);
                }
            }
        });
        mPushAgent.setMuteDurationSeconds(1);
        mPushAgent.setDisplayNotificationNumber(3);
        mPushAgent.setDebugMode(false);
        //统计
        MobclickAgent.openActivityDurationTrack(false);//禁止默认的统计模式
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);//普通场景
        MobclickAgent.enableEncrypt(true);//统计的日志加密,默认不加密
        MobclickAgent.setDebugMode(true);
        try {
            VERSION_CODE = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收友盟的推送
     *
     * @param msg
     */
    private void receive(UMessage msg) {
        String status = msg.extra.get("status");
        String action = msg.extra.get("page");
        String id = msg.extra.get("id");
        Intent intent = new Intent();
        intent.putExtra("action", action);
        if (!TextUtils.isEmpty(id)) {
            intent.putExtra("id", id);
        }
        if (TextUtils.equals(Constants.LINEUP, status)) {//排队界面  更新排队位置
            long[] vibrates = {0, 1000, 1000, 1000};
            // 设置通知提醒
            if (TextUtils.equals(action, "jump")) {
                String uri = "android.resource://" + mApplicationContext.getPackageName() + "/" + R.raw.xizaole;
                Uri sound = Uri.parse(uri);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("通知").setWhen(System.currentTimeMillis()).setSound(sound).setVibrate(vibrates)
                        .setContentText("轮到您洗澡了").setAutoCancel(true);
                mBuilder.setTicker("您现在可以进行洗澡了");//第一次提示消息的时候显示在通知栏上
                Intent resultIntent = new Intent(getApplicationContext(),
                        HomeActivity.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(
                        getApplicationContext(), 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                // 设置通知主题的意图
                mBuilder.setContentIntent(resultPendingIntent);
                //获取通知管理器对象
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }
            intent.setAction(Constants.LINEUP);
            sendOrderedBroadcast(intent, null);
        } else if (TextUtils.equals(Constants.SERVICESTART, status)) { //可以变成服务中
            intent.setAction(Constants.SERVICESTART);
            sendOrderedBroadcast(intent, null);
        } else if (TextUtils.equals(Constants.SERVICEFINISH, status)) { //已经结束服务
            intent.setAction(Constants.SERVICEFINISH);
            String order_id = msg.extra.get("order_id");
            intent.putExtra("order_id", order_id);
            sendOrderedBroadcast(intent, null);
        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 登录过期，跳转到登录界面
     */
    public static void jumpLogin() {
        SPUtils.saveString("balance", "");
        SPUtils.saveString(Constants.S_ID, "");
        SPUtils.saveString(Constants.CAMPUS, "");
        SPUtils.saveString(Constants.OAUTH_TOKEN, "");
        SPUtils.saveString(Constants.OAUTH_TOKEN_SECRET, "");
        SPUtils.saveString("uuid", "");
        SPUtils.saveBoolean(Constants.IS_LOGIN, false);
        Intent intent = new Intent(mApplicationContext, LoginRevisionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mApplicationContext.startActivity(intent);
        MyApp.clearData(mApplicationContext);
        AppManager.INSTANCE.finishAllActivity();
    }
}

