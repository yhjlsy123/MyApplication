package com.xinglincloud.www.newsuperbath.Broadcast;

// UpdateReceiver
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.xinglincloud.www.newsuperbath.Activity.MainActivity;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;

/**
 * 监测是否升级、安装、卸载成功
 */
public class UpdateReceiver extends BroadcastReceiver {
    private static final String TAG = "UpdateReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")){
            Toast.makeText(context,"升级了一个安装包，重新启动此程序", Toast.LENGTH_SHORT).show();
            LogUtil.i(TAG, "onReceive: 升级了一个安装包，重新启动此程序");
            startApp(context);
        }

        // 接收安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString();
            System.out.println("安装了:" +packageName + "包名的程序");
        //startApp(context);
        }

        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString();
            System.out.println("卸载了:"  + packageName + "包名的程序");
        }
    }

    /**
     * 监测到升级后执行app的启动 如果不是silenceInstall()可以注释掉
     */
    public void startApp(Context context){
        // 打开自身 一般用于软件升级
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }
}