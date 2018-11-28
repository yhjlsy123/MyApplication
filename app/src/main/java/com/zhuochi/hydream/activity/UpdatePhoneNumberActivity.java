package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.config.AppManager;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.dialog.PicVrcodeDialog;
import com.zhuochi.hydream.dialog.ResetNumberPWD;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.UserUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;


/**
 * 更换手机号码
 * Created by and on 2016/11/4.
 */

public class UpdatePhoneNumberActivity extends BaseAutoActivity {


    @BindView(R.id.login_phone)
    TextView loginPhone;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.update_phone)
    EditText updatePhone;
    @BindView(R.id.login_input_code)
    EditText loginInputCode;
    @BindView(R.id.login_code)
    Button loginCode;
    @BindView(R.id.login_enter)
    Button loginEnter;
    private String mPhoneNum;


    private Handler handler;
    private String TAG = "LoginQuickActivity";
    private XiRequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phonenumber);
        ButterKnife.bind(this);
        handler = new Handler();
        params = new XiRequestParams(this);
        loginPhone.setText(SPUtils.getString(Constants.MOBILE_PHONE, ""));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick({R.id.login_code, R.id.login_enter, R.id.wallet_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_code:
                loginCode.setClickable(false);
                mPhoneNum = updatePhone.getText().toString();
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
                String login_Phone = loginPhone.getText().toString();
                String mVerificationCode = loginInputCode.getText().toString();
                if (TextUtils.isEmpty(mVerificationCode)) {
                    ToastUtils.show("请输入验证码");
                    loginEnter.setClickable(true);
                    return;
                }
                String pwds = pwd.getText().toString();
                if (TextUtils.isEmpty(pwds)) {
                    ToastUtils.show("请输入您的密码");
                    loginEnter.setClickable(true);
                    return;
                }
                String updatephone = updatePhone.getText().toString();
                if (TextUtils.isEmpty(updatephone)) {
                    ToastUtils.show("请输入您要更换的手机号码");
                    loginEnter.setClickable(true);
                    return;
                }
                // 通过规则判断手机号
                if (!Common.judgePhoneNums(updatephone)) {
                    ToastUtils.show("手机号码有误，请核对后重新输入");
                    loginEnter.setClickable(true);
                    return;
                }
                if (NetworkUtil.isNetworkAvailable()) {
                    LoadingAnimDialog.showLoadingAnimDialog(this);
                    updatePhone(login_Phone, mVerificationCode, updatephone, pwds);
                }
                loginEnter.setClickable(true);
                break;
            case R.id.wallet_back:
                finish();
                break;
        }
    }

    /**
     * 更换手机号
     *
     * @param phoneNum
     * @param verificationCode
     */
    private void updatePhone(String phoneNum, String verificationCode, String newPhone, String pwd) {
        params.addCallBack(this);
        params.changeMobile(phoneNum, pwd, newPhone, verificationCode);
    }


    /**
     * 获取短信验证码
     *
     * @param phoneNumber
     */
    public void getVerificationCode(final String phoneNumber, String picCode) {
        params.addCallBack(this);
        params.getSendsmsCode(phoneNumber, "change_mobile");
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "sendSMSCode"://验证码
                loginInputCode.requestFocus();
                countdown();
                break;
            case "changeMobile"://更换手机号
//                LoadingAnimDialog.dismissLoadingAnimDialog();
//                Intent intent = new Intent();
////                        intent.putExtra("newPhone", newPhone);
//                setResult(101, intent);
//                finish();
                showResetnNumberPWDDialog();
                break;
        }
        super.onRequestSuccess(tag, result);
    }


    /**
     * 显示图片验证码的弹窗
     */
    private void showPicVrcodeDialog(final String phoneNumber) {
        PicVrcodeDialog.Builder builder = new PicVrcodeDialog.Builder(this);
        builder.create().show();
        getPicCode(builder.getIvCode());
        builder.setVrCodeListener(new PicVrcodeDialog.VrCodeListener() {
            @Override
            public void refreshCode(ImageView ivCode) {
                getPicCode(ivCode);
            }

            @Override
            public void getUserInputCode(String userInputCode) {
                if (TextUtils.isEmpty(userInputCode))
                    return;
                getVerificationCode(phoneNumber, userInputCode);
            }
        });
    }

    /**
     * 获取图形验证码
     */
    private void getPicCode(final ImageView iv) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uuid", SPUtils.getString("uuid", ""));

//        VolleySingleton.postNoDEVICETOKEN(Neturl.GET_PIC_CODE, "get_pic_code", params, new VolleySingleton.CBack() {
//            @Override
//            public void onRequestSuccess(String result) {
//                Loginsms loginsms = JsonUtils.parseJsonToBean(result, Loginsms.class);
//                if (loginsms != null && loginsms.data != null) {
//                    getNetWorkBitMap(loginsms.data.images, iv);
//                }
//            }
//        }, null);
    }


    /**
     * 加载网络验证码
     *
     * @param picurl
     * @param iv
     */
    public void getNetWorkBitMap(final String picurl, final ImageView iv) {
        synchronized (String.class) {
            new Thread() {
                public void run() {
                    try {
                        URL url = new URL(picurl);
                        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                        uc.connect();
                        if (uc.getResponseCode() == 200) {
                            InputStream is = uc.getInputStream();
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            byte[] bytes = new byte[1024];
                            int length = -1;
                            while ((length = is.read(bytes)) != -1) {
                                bos.write(bytes, 0, length);
                            }
                            byte[] by = bos.toByteArray();
                            bos.close();
                            is.close();
                            setPicCode(by, iv);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }

    /**
     * 将流文件设置给图片控件
     *
     * @param iv
     * @throws IOException
     */
    public void setPicCode(final byte[] by, final ImageView iv) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeByteArray(by, 0, by.length);
                iv.setImageBitmap(bitmap);
            }
        });

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

    /*更改手机号后，弹窗，点击登录，跳转登录页面*/
    public synchronized void showResetnNumberPWDDialog() {
        ResetNumberPWD.Builder builder = new ResetNumberPWD.Builder(this);
        builder.setOnClickListener(new ResetNumberPWD.OnSuccessListener() {
            @Override
            public void login() {
                UserUtils.setDataNull();
                SPUtils.saveString(Constants.S_ID, "");
                SPUtils.saveString(Constants.TOKEN_ID, "");
                SPUtils.saveString(Constants.OAUTH_TOKEN_SECRET, "");
                SPUtils.saveString(Constants.CAMPUS, "");
                SPUtils.saveBoolean(Constants.IS_LOGIN, false);
                JPushInterface.cleanTags(UpdatePhoneNumberActivity.this, 0);
                startActivity(new Intent(UpdatePhoneNumberActivity.this, LoginActivity.class));
                AppManager.INSTANCE.finishAllActivity();
                LoadingAnimDialog.dismissLoadingAnimDialog();
            }
        });
        builder.create(3).show();
    }

}
