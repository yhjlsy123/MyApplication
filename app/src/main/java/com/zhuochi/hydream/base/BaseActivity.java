package com.zhuochi.hydream.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.config.AppManager;
import com.zhuochi.hydream.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.ResponseListener;
import com.zhuochi.hydream.utils.StatusBarUtil;
import com.zhuochi.hydream.utils.ToastUtils;

/**
 * @author Cuixc
 * @date on  2018/5/29
 */

public class BaseActivity extends AppCompatActivity implements ResponseListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 添加Activity到堆栈
        AppManager.INSTANCE.addActivity(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {

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
    public void onRequestFailure(String tag, Object s) {
        LoadingTrAnimDialog.dismissLoadingAnimDialog();
        if (JSON.toJSONString(s).contains("com.android.volley.ServerError")) {
            ToastUtils.show("系统错误");
            return;
        }
    }
}
