package com.zhuochi.hydream.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;

import com.alibaba.fastjson.JSON;
import com.bugtags.library.Bugtags;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.activity.BalanceRefundActivity;
import com.zhuochi.hydream.activity.LoginActivity;
import com.zhuochi.hydream.activity.LoginBackPwdActivity;
import com.zhuochi.hydream.activity.LoginQuickActivity;
import com.zhuochi.hydream.activity.RegisterActivity;
import com.zhuochi.hydream.activity.SplashActivity;
import com.zhuochi.hydream.activity.UserActivity;
import com.zhuochi.hydream.activity.WalletActivity;
import com.zhuochi.hydream.config.AppManager;
import com.zhuochi.hydream.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.ResponseListener;
import com.zhuochi.hydream.utils.StatusBarUtil;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhy.autolayout.AutoLayoutActivity;


/**
 *
 */
public class BaseAutoActivity extends AutoLayoutActivity implements ResponseListener {
    // 记录处于前台的Activity
    private static BaseAutoActivity foregroundActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (this instanceof LoginActivity || this instanceof LoginQuickActivity || this instanceof LoginBackPwdActivity || this instanceof UserActivity || this instanceof SplashActivity || this instanceof WalletActivity || this instanceof RegisterActivity) {

        } else {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        }

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        foregroundActivity = this;
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bugtags.onPause(this);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Bugtags.onDispatchTouchEvent(this, ev);
        return super.dispatchTouchEvent(ev);
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

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {

    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        LoadingAnimDialog.dismissLoadingAnimDialog();
        if (JSON.toJSONString(s).contains("com.android.volley.ServerError")) {
            ToastUtils.show("系统错误");
            return;
        }
    }


}
