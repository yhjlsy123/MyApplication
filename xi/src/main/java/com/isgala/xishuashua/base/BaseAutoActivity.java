package com.isgala.xishuashua.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.isgala.xishuashua.config.AppManager;
import com.isgala.xishuashua.dialog.LoadingAnimDialog;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.umeng.message.PushAgent;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 *
 */
public class BaseAutoActivity extends AutoLayoutActivity {
    // 记录处于前台的Activity
    private static BaseAutoActivity foregroundActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Window win = getWindow();
//            win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
//            // 状态栏字体设置为深色，SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 为SDK23增加
//            win.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//
//            // 部分机型的statusbar会有半透明的黑色背景
//            win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            win.setStatusBarColor(Color.TRANSPARENT);// SDK21
//        }
        //透明状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            // Translucent status bar
//            window.setStatusBarColor(Color.TRANSPARENT);// SDK21
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        // 添加Activity到堆栈
        AppManager.INSTANCE.addActivity(this);
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        foregroundActivity = this;
    }

    /**
     * 获取当前处于前台的Activity
     *
     * @return
     */
    public static BaseAutoActivity getForegroundActivity() {
        return foregroundActivity;
    }

    @Override
    protected void onDestroy() {
        try {
            AppManager.INSTANCE.removeActivity(this);
            LoadingAnimDialog.dismissLoadingAnimDialog();
            LoadingTrAnimDialog.dismissLoadingAnimDialog();
            super.onDestroy();
        } catch (Exception e) {
        }
    }
}
