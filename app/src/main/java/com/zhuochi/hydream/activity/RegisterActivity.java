package com.zhuochi.hydream.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.entity.RegisterEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.ToastUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册
 *
 * @author Cuixc
 * @date on  2018/5/9
 */

public class RegisterActivity extends BaseAutoActivity {
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_input_password)//输入密码
            EditText loginInputPassword;
    @BindView(R.id.repeat_input_password)//重复输入密码
            EditText loginInputrepeatPassword;
    @BindView(R.id.login_input_code)
    EditText loginInputCode;
    @BindView(R.id.login_code)
    TextView loginCode;
    @BindView(R.id.login_enter)
    Button loginEnter;
    private String mPhoneNum;
    private XiRequestParams params;
    private Handler handler;
    private int time = 59;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        handler = new Handler();
        params = new XiRequestParams(this);
    }

    @OnClick({R.id.login_code, R.id.login_enter, R.id.message_back})
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
                    getVerificationCode(mPhoneNum);
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
                String TrepeatPassword = loginInputrepeatPassword.getText().toString();
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
                    getRegister(mPhoneNum, mVerificationCode, TrepeatPassword);
                }
                loginEnter.setClickable(true);
                break;
            case R.id.message_back:
                finish();
                break;
        }
    }

    /**
     * 点击注册
     *
     * @param mPhoneNum 手机号
     * @param code      验证码
     * @param pwd       密码
     */
    private void getRegister(String mPhoneNum, String code, String pwd) {
        params.addCallBack(this);
        params.getReg(mPhoneNum, code, pwd);
    }

    //获取验证码接口
    private void getVerificationCode(String mPhoneNum) {
        params.addCallBack(this);
        params.getSendsmsCode(mPhoneNum, "reg");
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "sendSMSCode"://验证码
                //返回成功开始倒计时
                countdown();
                break;
            case "reg": //注册
                LoadingAnimDialog.dismissLoadingAnimDialog();
                Map map = (Map) result.getData().getData();
                try {
                    ToastUtils.show(result.getData().getMsg());
                    String gson = GsonUtils.parseMapToJson(map);
                    finish();
                    RegisterEntity entity = new Gson().fromJson(gson, RegisterEntity.class);
                    //保存一下
//                    SPUtils.saveString("token_id", entity.getToken());
//                    SPUtils.saveString("user_statue", String.valueOf(entity.getUser_status()));
//                    SPUtils.saveString("mobile_phone", entity.getMobile());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        LoadingAnimDialog.dismissLoadingAnimDialog();
        super.onRequestFailure(tag, s);
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reset();
    }

    private void reset() {
        time = 59;
        handler.removeCallbacksAndMessages(null);
        loginCode.setClickable(true);
    }
}
