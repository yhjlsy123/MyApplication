package com.isgala.xishuashua.config;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.isgala.xishuashua.ui.SplashActivity;
import com.isgala.xishuashua.utils.SPUtils;
import com.umeng.analytics.MobclickAgent;

import java.lang.Thread.UncaughtExceptionHandler;

import static com.isgala.xishuashua.config.BathHouseApplication.DEGUG;


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 * 需要在Application中注册，为了要在程序启动器就监控整个程序。
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler mDefaultHandler;
    public static final String TAG = "CrashHandler";
    BathHouseApplication application;

    private CrashHandler(BathHouseApplication application) {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.application = application;
    }

    /**
     * 初始化异常处理类
     *
     * @param application
     */
    public static void init(BathHouseApplication application) {
        CrashHandler crashHandler = new CrashHandler(application);
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(thread, ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {

            long l = SystemClock.currentThreadTimeMillis();
            if (l - SPUtils.getFloat("crash_time", 0) > 10 * 1000) {//非频繁崩溃,可以重启
                Intent intent = new Intent(application.getApplicationContext(), SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent restartIntent = PendingIntent.getActivity(application.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
                );
                AlarmManager mgr = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10, restartIntent); // 30毫秒钟后重启应用
            }
            SPUtils.saveFloat("crash_time", l);
            AppManager.INSTANCE.finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Thread thread, final Throwable ex) {
        if (ex == null || DEGUG) {
            return false;
        }
        MobclickAgent.reportError(application,ex);
        MobclickAgent.onKillProcess(application);
        return true;
    }
}
