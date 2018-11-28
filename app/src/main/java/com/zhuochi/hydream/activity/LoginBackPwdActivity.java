package com.zhuochi.hydream.activity;

import android.content.pm.PackageManager;
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
import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.dialog.PicVrcodeDialog;
import com.zhuochi.hydream.dialog.ResetNumberPWD;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.UserInfoEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.UserUtils;
import com.zhuochi.hydream.view.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;


/**
 * 找回密码
 */

public class LoginBackPwdActivity extends BaseAutoActivity {
    @BindView(R.id.message_back)
    ImageView messageBack;
    @BindView(R.id.userinfo_photo)
    RoundedImageView userinfoPhoto;
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
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String mPhoneNum;
    private Handler handler;
    private String TAG = "LoginBackPwdActivity";
    private XiRequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_back_password);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        handler = new Handler();
        params = new XiRequestParams(this);
        loginPhone.setText(SPUtils.getString(Constants.MOBILE_PHONE, ""));
        tvTitle.setText("忘记密码");
    }

    //    获取用户信息

    private void getBaseInfo() {
        params.addCallBack(this);
        params.getUserBaseInfo(UserUtils.getInstance(this).getMobilePhone());
    }

    //显示头像
    private void setData(UserInfoEntity entity) {
        if (!entity.getAvatar().isEmpty()) {
            ImageLoadUtils.loadImage(this, userinfoPhoto, entity.getAvatar());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
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
            case R.id.message_back:
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
        params.getSendsmsCode(phoneNumber, "find_pwd");
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

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getUserBaseInfo":
                Map map = (Map) result.getData().getData();
                try {
                    String gson = GsonUtils.parseMapToJson(map);
                    UserInfoEntity mEntity = new Gson().fromJson(gson, UserInfoEntity.class);
                    setData(mEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "sendSMSCode"://验证码
                //返回成功开始倒计时
                countdown();
                break;
            case "forgetPassword"://找回密码
                showResetnNumberPWDDialog();
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

                //TODO JG 解除极光推送绑定
                Set<String> set = new HashSet<String>();
                int str=UserUtils.getInstance(LoginBackPwdActivity.this).getOrgAreaID();
                set.add("orgArea_" + str);//绑定 校区（Tag）
                JPushInterface.deleteTags(LoginBackPwdActivity.this, 0, set);
                JPushInterface.deleteAlias(LoginBackPwdActivity.this, 0);
//                startActivity(new Intent(LoginBackPwdActivity.this, LoginActivity.class));
//                AppManager.INSTANCE.finishAllActivity();
                LoadingAnimDialog.dismissLoadingAnimDialog();
                finish();
            }
        });
        builder.create(1).show();
    }

}
