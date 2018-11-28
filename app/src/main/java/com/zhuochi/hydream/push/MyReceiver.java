package com.zhuochi.hydream.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.activity.BalanceLog;
import com.zhuochi.hydream.activity.HomeActivity;
import com.zhuochi.hydream.activity.HtmlActivity;
import com.zhuochi.hydream.activity.MyFeedBackActivity;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.entity.pushbean.NoticeEntity;
import com.zhuochi.hydream.entity.pushbean.PushMsgEntity;
import com.zhuochi.hydream.entity.pushbean.SettingsEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * @author Cuixc
 * @date on  2018/5/22
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";
    private static PushMsgEntity mEntity;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                if (mEntity!=null) {
                    if (mEntity.getType().equals("busRefresh")) {//刷新总线
                        intent.setAction(Constants.REFRESHBUS);
                        context.sendOrderedBroadcast(intent, null);
                    }
                }
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");

                if (mEntity!=null){
                    if (mEntity.getType().equals("notice")){//公告
                        Map map = (Map) mEntity.getContent();
                        String gson = GsonUtils.parseMapToJson(map);
                        NoticeEntity noticeEntity=new Gson().fromJson(gson,NoticeEntity.class);
                        if (!TextUtils.isEmpty(noticeEntity.getUrl())){//跳转H5
                            Intent intentH5=new Intent(context, HtmlActivity.class);
                            intentH5.putExtra("url",noticeEntity.getUrl());
                            intentH5.putExtra("title",noticeEntity.getTitle());
                            context.startActivity(intentH5);
                        }
                    }else if (mEntity.getType().equals("feedBack")){//意见反馈
                        context.startActivity(new Intent(context, MyFeedBackActivity.class));
                    }else if (mEntity.getType().equals("refundSuccess")){//余额明细
                        context.startActivity(new Intent(context, BalanceLog.class));
                    }else {
                        context.startActivity(new Intent(context, HomeActivity.class));
                    }
                }

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);

                Logger.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    mEntity=new Gson().fromJson(json.toString(),PushMsgEntity.class);
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {

        long[] vibrates = {0, 1000, 1000, 1000};
//        String gson=bundle.getString("data");
        Intent intent=new Intent();
        try {
            JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
            mEntity = new Gson().fromJson(json.toString(), PushMsgEntity.class);

        }catch (Exception e){
            e.printStackTrace();
        }
        if (mEntity.getType().equals("busRefresh")){//刷新总线
            intent.setAction(Constants.REFRESHBUS);
            context.sendOrderedBroadcast(intent, null);
        }else  if (mEntity.getType().equals("reserveSuccess")) {//reserveSuccess排队成功
            String uri = "android.resource://" + context.getPackageName() + "/" + R.raw.xizaole;
            Uri sound = Uri.parse(uri);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("通知").setWhen(System.currentTimeMillis()).setSound(sound).setVibrate(vibrates)
                    .setContentText("轮到您洗澡了").setAutoCancel(true);
            mBuilder.setTicker("您现在可以进行洗澡了");//第一次提示消息的时候显示在通知栏上
            Intent resultIntent = new Intent(context,
                    HomeActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(
                    context, 0, resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            // 设置通知主题的意图
            mBuilder.setContentIntent(resultPendingIntent);
            //获取通知管理器对象
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
            intent.setAction(Constants.LINEUP);
            context.sendOrderedBroadcast(intent, null);
        }else if (mEntity.getType().equals("notice")){
//            Map map = (Map) mEntity.getContent();
//            String gson = GsonUtils.parseMapToJson(map);
//            NoticeEntity noticeEntity=new Gson().fromJson(gson,NoticeEntity.class);
//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                    context).setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentText(noticeEntity.getTitle()).setAutoCancel(true);
//            mBuilder.setTicker("您现在可以进行洗澡了");//第一次提示消息的时候显示在通知栏上
//            Intent resultIntent = new Intent(context,
//                    HomeActivity.class);
//            PendingIntent resultPendingIntent = PendingIntent.getActivity(
//                    context, 0, resultIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            // 设置通知主题的意图
//            mBuilder.setContentIntent(resultPendingIntent);
//            //获取通知管理器对象
//            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            mNotificationManager.notify(0, mBuilder.build());
        }else if (mEntity.getType().equals("settings")){
            Map map = (Map) mEntity.getContent();
            String gson = GsonUtils.parseMapToJson(map);
            intent.putExtra("entity",gson);
            intent.setAction(Constants.REFRESHBUS);
            context.sendOrderedBroadcast(intent, null);
        }

    }
}
