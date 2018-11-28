package com.zhuochi.hydream.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.pushbean.InitSettingEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.push.LocalBroadcastManager;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.EntitySumUtils;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.ToastUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 最新登录
 */

public class LoginActivity extends BaseAutoActivity {
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_input_password)
    EditText loginInputCode;
    @BindView(R.id.login_enter)
    Button loginEnter;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    private String mPhoneNum;
    private XiRequestParams params;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private MessageReceiver mMessageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        registerMessageReceiver();  // used for receive msg
        tvAgreement.setText("《" + getResources().getString(R.string.app_name) + "用户协议》");
        tvAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInitSetting();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /*初始化设置*/
    private void getInitSetting() {
        params.addCallBack(this);
        params.getInit();
    }

    @OnClick({R.id.login_enter, R.id.txt_loginQuick, R.id.txt_backPassword, R.id.register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_enter:
                loginEnter.setClickable(false);
                mPhoneNum = loginPhone.getText().toString();
                if (TextUtils.isEmpty(mPhoneNum)) {
                    ToastUtils.show("请输入您的手机号码");
                    loginEnter.setClickable(true);
                    return;
                }
                // 通过规则判断手机号
                if (!Common.judgePhoneNums(mPhoneNum)) {
                    ToastUtils.show("手机号码有误，请核对后重新输入");
                    loginEnter.setClickable(true);
                    return;
                }
                String mVerificationCode = loginInputCode.getText().toString();
                if (TextUtils.isEmpty(mVerificationCode)) {
                    ToastUtils.show("请输入密码");
                    loginEnter.setClickable(true);
                    return;
                }
                if (NetworkUtil.isNetworkAvailable()) {
                    LoadingAnimDialog.showLoadingAnimDialog(this);
                    login(mPhoneNum, mVerificationCode);
                }
                loginEnter.setClickable(true);
                break;
            case R.id.txt_loginQuick://快捷登录
                startActivity(new Intent(this, LoginQuickActivity.class));
                break;
            case R.id.txt_backPassword://找回密码
                startActivity(new Intent(this, LoginBackPwdActivity.class));
                break;
            case R.id.register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    /**
     * 登录
     *
     * @param phoneNum
     * @param verificationCode
     */
    private void login(String phoneNum, String verificationCode) {
        params.addCallBack(this);
        params.LoginRequest(verificationCode, phoneNum);
        Log.e("cxt", "login+end");
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "loginWidthPwd":
                loginPhone.setText(null);
                loginInputCode.setText(null);
                EntitySumUtils.LoginSubmit(result, this);
                break;
            case "getInit":
                Map map = (Map) result.getData().getData();
                String mGson = GsonUtils.parseMapToJson(map);
                InitSettingEntity mInitEntity = new Gson().fromJson(mGson, InitSettingEntity.class);
                Intent intent2 = new Intent(LoginActivity.this, H5Activity.class);
                intent2.putExtra("title", "用户协议");
                intent2.putExtra("url", mInitEntity.getRegistrationAgreementUrl());
                startActivity(intent2);
                break;
        }
        super.onRequestSuccess(tag, result);
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        LoadingAnimDialog.dismissLoadingAnimDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
//                    if (!ExampleUtil.isEmpty(extras)) {
//                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
//                    }
//                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e) {
            }
        }
    }


//    @Override
//    public void onBackPressed() {
//            if (Tools.dblClose()) {
//                AppManager.INSTANCE.appExit(this);
//                super.onBackPressed();
//        }
//    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // 是否触发按键为back键
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            onBackPressed();
//            return true;
//        } else {// 如果不是back键正常响应
//            return super.onKeyDown(keyCode, event);
//        }
//    }

}
