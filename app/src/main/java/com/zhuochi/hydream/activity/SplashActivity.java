package com.zhuochi.hydream.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.klcxkj.zqxy.MyApp;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.PermissionsChecker;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.UserUtils;
import com.zhuochi.hydream.utils.VersionDialogFragment;

import butterknife.ButterKnife;

import com.zhuochi.hydream.R;

/**
 * 启动页面
 * Created by and on 2016/11/4.
 */

public class SplashActivity extends BaseAutoActivity implements VersionDialogFragment.setOnclickCancel {
    private String TAG = "SplashActivity";
    private long intoTime;
    private Handler handler = new Handler();
    private VersionDialogFragment dialogFragment;
    private RelativeLayout mlineShow;
    private TextView mCancel;
    private TextView mConfirm;
    private TextView mContent;


    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        CrashReport.initCrashReport(getApplicationContext(), "0e94f61550", true);
        mPermissionsChecker = new PermissionsChecker(this);
        intoTime = System.currentTimeMillis();
        ButterKnife.bind(this);
        mlineShow = (RelativeLayout) findViewById(R.id.line_show);
        mCancel = (TextView) findViewById(R.id.txt_cancel);
        mConfirm = (TextView) findViewById(R.id.txt_confirm);
        mContent = (TextView) findViewById(R.id.content);
        MyApp.clearData(this);
    }

    @Override
    public void OnclickCancel() {
        dialogFragment.dismiss();
        obtainLocation();
    }

    /**
     * 下一步操作
     */
    public void next() {
        Class nextClass = null;
        boolean isFirstEnter = SPUtils.getBoolean("is_first_enter", true);

        int ORG_ID = UserUtils.getInstance(this).getOrgID();//学校ID
        int userID = UserUtils.getInstance(this).getUserID();
        String phone = UserUtils.getInstance(this).getMobilePhone();


        if (isFirstEnter) {
            // 1. 动画结束，跳转到引导界面
            nextClass = GuideActivity.class;
        } else if (userID != 0 && !phone.isEmpty()) {

            if (ORG_ID == 0) {//3.判断是否有设置过学校信息
                nextClass = SchoolList.class;
            } else {
                nextClass = HomeActivity.class;
            }
        } else {
            nextClass = LoginActivity.class;
        }
        finish();
        Intent intent = new Intent(this, nextClass);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        //不实现,禁止用户在此界面退出App
    }


    /**
     * 获取用户的设备信息 uuid
     */
    private void obtainLocation() {
        XiRequestParams params = new XiRequestParams(SplashActivity.this);
        params.addCallBack(this);
        params.getToken();
    }

    /**
     * 跳转
     */
    private void jump() {
        final long pass = 3000 - (System.currentTimeMillis() - intoTime);
        if (handler != null)
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    next();
                }
            }, pass > 0 ? pass : 0);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {

            case "getToken":
                try {
                    if (result.getData().getData() != null) {
                        String uuid = ((LinkedTreeMap) result.getData().getData()).get("token").toString();
                        SPUtils.saveString("token_id", uuid);
                    }
                    jump();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        super.onRequestSuccess(tag, result);
    }

    public void onResume() {
        super.onResume();

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            jump();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }
}
