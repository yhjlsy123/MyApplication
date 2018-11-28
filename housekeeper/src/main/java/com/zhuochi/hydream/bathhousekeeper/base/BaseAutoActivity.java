package com.zhuochi.hydream.bathhousekeeper.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.config.AppManager;
import com.zhuochi.hydream.bathhousekeeper.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.bathhousekeeper.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.ResponseListener;
import com.zhuochi.hydream.bathhousekeeper.utils.StatusBarUtil;
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
        StatusBarUtil.setColor(this, getResources().getColor(R.color.blue_system),0);
        AppManager.INSTANCE.addActivity(this);
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

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {

    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        LoadingAnimDialog.dismissLoadingAnimDialog();
    }

}
