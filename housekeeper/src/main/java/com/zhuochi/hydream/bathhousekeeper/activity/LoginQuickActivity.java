package com.zhuochi.hydream.bathhousekeeper.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.bathhousekeeper.dialog.TipDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;
import com.zhuochi.hydream.bathhousekeeper.utils.LogUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.NetworkUtil;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 快捷登录
 * Created by and on 2016/11/4.
 */

public class LoginQuickActivity extends BaseActivity {
    //    @BindView(R.id.login_phone)
//    EditText loginPhone;
//    @BindView(R.id.login_input_code)
//    EditText loginInputCode;
//    @BindView(R.id.login_code)
//    TextView loginCode;
    @BindView(R.id.login_enter)
    Button loginEnter;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.user_account)
    EditText userAccount;
    @BindView(R.id.code_password)
    EditText codePassword;
    @BindView(R.id.back_password)
    TextView backPassword;
    @BindView(R.id.line_content)
    LinearLayout lineContent;
    @BindView(R.id.login_code)
    Button loginCode;
//    @BindView(R.id.login_enter)
//    Button loginEnter;
    private String mPhoneNum;
    private Handler handler;
    private String TAG = "LoginQuickActivity";
    private XiRequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_quick);
        ButterKnife.bind(this);
//        getSupportActionBar().hide();
        handler = new Handler();
        params = new XiRequestParams(this);
        toolbarTitle.setText("快捷登录");
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
                mPhoneNum = userAccount.getText().toString();
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
                mPhoneNum = userAccount.getText().toString();
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
                String mVerificationCode = codePassword.getText().toString();
                if (TextUtils.isEmpty(mVerificationCode)) {
                    ToastUtils.show("请输入验证码");
                    loginEnter.setClickable(true);
                    return;
                }
                if (NetworkUtil.isNetworkAvailable()) {
                    LoadingAnimDialog.showLoadingAnimDialog(this);
                    login(mPhoneNum, mVerificationCode);
                }
                loginEnter.setClickable(true);
                break;

            case R.id.toolbar_back:
                finish();
        }
    }

    /**
     * 快捷登录
     *
     * @param phoneNum
     * @param verificationCode
     */
    private void login(String phoneNum, String verificationCode) {
        params.addCallBack(this);
        params.getFastLogin(phoneNum, verificationCode);
    }


    /**
     * 获取短信验证码
     *
     * @param phoneNumber
     */
    public void getVerificationCode(String phoneNumber, String typeCode) {
        params.addCallBack(this);
        params.sendSMSCode(phoneNumber, "login");
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        LoadingAnimDialog.dismissLoadingAnimDialog();
        super.onRequestFailure(tag, s);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "sendSMSCode"://验证码
                //返回成功开始倒计时
                countdown();
                break;
            case "fastLogin"://登录
//                EntitySumUtils.LoginSubmit(result, this);
                LoadingAnimDialog.dismissLoadingAnimDialog();
                ToastUtils.show(result.getData().getMsg());
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }


    /**
     * 显示图片验证码的弹窗
     */
//    private void showPicVrcodeDialog(final String phoneNumber) {
//        PicVrcodeDialog.Builder builder = new PicVrcodeDialog.Builder(this);
//        builder.create().show();
//        getPicCode(builder.getIvCode());
//        builder.setVrCodeListener(new PicVrcodeDialog.VrCodeListener() {
//            @Override
//            public void refreshCode(ImageView ivCode) {
//                getPicCode(ivCode);
//            }
//
//            @Override
//            public void getUserInputCode(String userInputCode) {
//                if (TextUtils.isEmpty(userInputCode))
//                    return;
//                getVerificationCode(phoneNumber, userInputCode);
//            }
//        });
//    }

//    /**
//     * 获取图形验证码
//     */
//    private void getPicCode(final ImageView iv) {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("uuid", SPUtils.getString("uuid", ""));
//    }


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
        if (lm != null) {
            lm.removeUpdates(locationListener);
            lm = null;
            locationListener = null;
        }
        super.onDestroy();
        reset();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private LocationManager lm;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LogUtils.e("Login", "onLocationChanged");
            SPUtils.saveString(Constants.LOCATON_LAT, "" + location.getLatitude());
            SPUtils.saveString(Constants.LOCATON_LNG, "" + location.getLongitude());
            if (lm != null) {
                lm.removeUpdates(this);
                lm = null;
            }
            TipDialog.dismiss_();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

}
