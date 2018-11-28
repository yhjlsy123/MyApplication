package com.zhuochi.hydream.bathhousekeeper.activity;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;
import com.zhuochi.hydream.bathhousekeeper.utils.NetworkUtil;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 找回密码
 */

public class LoginBackActivity extends BaseActivity {
    //    @BindView(R.id.message_back)
//    ImageView messageBack;
//    @BindView(R.id.userinfo_photo)
//RoundedImageView userinfoPhoto;
    @BindView(R.id.login_input_code)
    EditText loginInputCode;
    @BindView(R.id.login_code)
    Button loginCode;
    @BindView(R.id.login_input_password)
    EditText loginInputPassword;
    @BindView(R.id.repeat_input_password)
    EditText repeatInputPassword;
    @BindView(R.id.login_enter)
    Button loginEnter;
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    //    @BindView(R.id.tv_title)
//    TextView tvTitle;
    private String mPhoneNum;
    private Handler handler;
    private String TAG = "LoginBackPwdActivity";
    private XiRequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_backpwd);
        ButterKnife.bind(this);
//        getSupportActionBar().hide();

        handler = new Handler();
        params = new XiRequestParams(this);
        loginPhone.setText(SPUtils.getString(Constants.MOBILE_PHONE, ""));
        toolbarTitle.setText("忘记密码");
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick({R.id.login_code, R.id.login_enter, R.id.toolbar_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_code:
                loginCode.setClickable(false);
                mPhoneNum = loginPhone.getText().toString();
                if (TextUtils.isEmpty(mPhoneNum)) {
                    ToastUtils.show("请输入您的手机号码");
                    loginCode.setClickable(true);
                    return;
                }
                // 通过规则判断手机号
                if (!Common.judgePhoneNums(mPhoneNum)) {
                    ToastUtils.show("手机号码有误，请核对后重新输入");
                    loginCode.setClickable(true);
                    return;
                }
                if (NetworkUtil.isNetworkAvailable()) {// 当网络可用时开始倒计时,获取短信
                    getVerificationCode(mPhoneNum, "");
                } else {
                    loginCode.setClickable(true);
                }
                break;
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
                    ToastUtils.show("请输入验证码");
                    loginEnter.setClickable(true);
                    return;
                }
                String InputPassword = loginInputPassword.getText().toString();
                if (TextUtils.isEmpty(InputPassword)) {
                    ToastUtils.show("请输入密码");
                    loginEnter.setClickable(true);
                    return;
                }
                if (InputPassword.length() < 6) {
                    ToastUtils.show("密码不能小于6位");
                    loginEnter.setClickable(true);
                    return;
                }
                String TrepeatPassword = repeatInputPassword.getText().toString();
                if (TextUtils.isEmpty(TrepeatPassword)) {
                    ToastUtils.show("请输入重复密码");
                    loginEnter.setClickable(true);
                    return;
                }
                if (!InputPassword.equals(TrepeatPassword)) {
                    ToastUtils.show("两次密码输入不一致");
                    loginEnter.setClickable(true);
                    return;
                }
                if (NetworkUtil.isNetworkAvailable()) {
                    LoadingAnimDialog.showLoadingAnimDialog(this);
                    forgetPassword(mPhoneNum, mVerificationCode);
                }
                loginEnter.setClickable(true);
                break;
            case R.id.toolbar_back:
                finish();
                break;
        }
    }

    /**
     * 重置账号密码
     *
     * @param phoneNum
     * @param verificationCode
     */
    private void forgetPassword(String phoneNum, String verificationCode) {
        String password = loginInputPassword.getText().toString();//密码
        params.addCallBack(this);
        params.forgetPassword(phoneNum, verificationCode, password);
    }


    /**
     * 获取短信验证码
     *
     * @param phoneNumber
     */
    public void getVerificationCode(final String phoneNumber, String picCode) {
        params.addCallBack(this);
        params.sendSMSCode(phoneNumber, "find_pwd");
    }


    private int time = 59;

    /**
     * 倒计时
     */
    private void countdown() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (time > 0) {
                    time--;
                    countdown();
                    loginCode.setClickable(false);
                } else {
                    loginCode.setText("再次获取验证码");
                    time = 59;
                    loginCode.setClickable(true);
                }
            }
        }, 999);
        loginCode.setText(String.format("%s S", time));
    }


    private void reset() {
        time = 59;
        handler.removeCallbacksAndMessages(null);
        loginCode.setClickable(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reset();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "sendSMSCode"://验证码
                //返回成功开始倒计时
                countdown();
                break;
            case "forgetPassword"://找回密码
                ToastUtils.show(result.getData().getMsg());
                finish();
                break;
        }
        super.onRequestSuccess(tag, result);
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        LoadingAnimDialog.dismissLoadingAnimDialog();
        super.onRequestFailure(tag, s);
    }


    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                } else {
                    // 没有获取到权限，做特殊处理
                    ToastUtils.show("获取位置权限失败，请手动开启");
                }
                break;
            default:
                break;
        }
    }


}
