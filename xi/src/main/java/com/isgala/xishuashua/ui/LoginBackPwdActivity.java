package com.isgala.xishuashua.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.BackPasswordEntity;
import com.isgala.xishuashua.bean_.Location;
import com.isgala.xishuashua.bean_.LoginResult;
import com.isgala.xishuashua.bean_.Loginsms;
import com.isgala.xishuashua.config.AppManager;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingAnimDialog;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.dialog.PicVrcodeDialog;
import com.isgala.xishuashua.dialog.TipDialog;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.LogUtils;
import com.isgala.xishuashua.utils.NetworkUtil;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.umeng.analytics.MobclickAgent;

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

import static com.isgala.xishuashua.api.Neturl.FORGET_PASSWORD;
import static com.isgala.xishuashua.api.Neturl.REGISTER_TEXT;
import static com.isgala.xishuashua.api.Neturl.USER_LOGIN;


/**
 * 找回密码,修改密码
 */

public class LoginBackPwdActivity extends BaseAutoActivity {
    @BindView(R.id.login_photo)
    ImageView circleimageview;
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
    private Handler handler;
    private String TAG = "LoginBackPwdActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_back_password);
        ButterKnife.bind(this);
        handler = new Handler();
        loginPhone.setText(SPUtils.getString(Constants.PHONE_NUMBER,""));
    }

    @Override
    public void onResume() {       super.onResume();
        MobclickAgent.onPageStart("LoginBackPwdActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("LoginBackPwdActivity");
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.login_code, R.id.login_enter,R.id.message_back})
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
                if (!judgePhoneNums(mPhoneNum)) {
                    ToastUtils.show("手机号码有误，请核对后重新输入");
                    loginCode.setClickable(true);
                    return;
                }
                if (NetworkUtil.isNetworkAvailable()) {// 当网络可用时开始倒计时,获取短信
                    if (TextUtils.isEmpty(SPUtils.getString("uuid", ""))) {// uuid为空时，再次获取uuid
                        obtainLocation();
                        return;
                    }
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
                if (!judgePhoneNums(mPhoneNum)) {
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
                String InputPassword=loginInputPassword.getText().toString();
                if (TextUtils.isEmpty(InputPassword)) {
                    ToastUtils.show("请输入密码");
                    loginEnter.setClickable(true);
                    return;
                }
                if (InputPassword.length()<6){
                    ToastUtils.show("密码不能小于6位");
                    loginEnter.setClickable(true);
                    return;
                }
                String TrepeatPassword=loginInputrepeatPassword.getText().toString();
                if (TextUtils.isEmpty(TrepeatPassword)) {
                    ToastUtils.show("请输入重复密码");
                    loginEnter.setClickable(true);
                    return;
                }
                if (!InputPassword.equals(TrepeatPassword)){
                    ToastUtils.show("两次密码输入不一致");
                    loginEnter.setClickable(true);
                    return;
                }
                if (NetworkUtil.isNetworkAvailable()) {
                    LoadingAnimDialog.showLoadingAnimDialog(this);
                    login(mPhoneNum, mVerificationCode);
                }
                loginEnter.setClickable(true);
                break;
            case R.id.message_back:
                finish();
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
        SPUtils.saveString("balance", "");//初始化保存在本地的余额记录
        Map<String, String> params = new HashMap<String, String>();
        String password=loginInputPassword.getText().toString();//密码
        String repeatpassword=loginInputrepeatPassword.getText().toString();//重复密码

        params.put("mobile", phoneNum);//用户账号(必须,一般是手机号码)
        params.put("vrcode", verificationCode);//验证码
        params.put("uuid", SPUtils.getString("uuid", ""));
        params.put("password",password);//密码
        params.put("re_password",repeatpassword);//重置密码
        LogUtils.e(TAG, params.toString());
        VolleySingleton.postNoDEVICETOKEN(FORGET_PASSWORD, "forget_password", params, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                BackPasswordEntity result1 = JsonUtils.parseJsonToBean(result, BackPasswordEntity.class);
                if (result1 != null && result1.data != null) {
                    if (result1.data.status.equals("1")) {//1.密码修改表示成功
                        ToastUtils.show(result1.data.msg);
                        LoadingAnimDialog.dismissLoadingAnimDialog();
                        finish();
                    }else {
                        ToastUtils.show(result1.data.msg);//不成功直接抛msg
                        LoadingAnimDialog.dismissLoadingAnimDialog();
                    }
                }else {
                    ToastUtils.show(result1.msg);
                    LoadingAnimDialog.dismissLoadingAnimDialog();
                }
            }
        }, null);
    }

    private void jump(LoginResult.DataBean data) {
        ToastUtils.show(data.message);
        String s_id = data.s_id;
        String campus_id = data.campus;
        SPUtils.saveString(Constants.OAUTH_TOKEN, data.oauth_token);
        SPUtils.saveString(Constants.OAUTH_TOKEN_SECRET, data.oauth_token_secret);
        SPUtils.saveString(Constants.S_ID, s_id);
        SPUtils.saveString(Constants.CAMPUS, campus_id);
        String device_type=data.device_type;
        String b_id = data.b_id;
        SPUtils.saveString(Constants.YB_ID, b_id);
        SPUtils.saveString(Constants.DEVICE_TYPE,device_type);
        Class nClass;
        if (TextUtils.isEmpty(s_id) || TextUtils.equals(s_id, "0")) {//1.判断是否有设置过学校信息
            nClass = SchoolList.class;
        } else if (TextUtils.isEmpty(campus_id) || TextUtils.equals(campus_id, "0")) {//2.判断是否设置过校区信息
            nClass = SchoolList.class;
        } else {
            nClass = HomeActivity.class;
        }
        SPUtils.saveBoolean(Constants.IS_LOGIN, true);
        Intent intent = new Intent(this, nClass);
        intent.putExtra("s_id", s_id);
        startActivity(intent);
        AppManager.INSTANCE.finishAllActivity();
        LoadingAnimDialog.dismissLoadingAnimDialog();
    }

    /**
     * 获取短信验证码
     *
     * @param phoneNumber
     */
    public void getVerificationCode(final String phoneNumber, String picCode) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phoneNumber);
        params.put("uuid", SPUtils.getString("uuid", ""));
        if (TextUtils.isEmpty(picCode)) {
            picCode = "";
        }
        params.put("captcha", picCode);
        params.put("type", "login");
        VolleySingleton.postNoDEVICETOKEN(Neturl.GET_LOGIN_CODE, "getcode", params, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Loginsms loginsms = JsonUtils.parseJsonToBean(result, Loginsms.class);
                if (loginsms != null && loginsms.data != null) {
                    loginInputCode.requestFocus();
                    countdown();
                    ToastUtils.show(loginsms.data.message);
                } else {
                    showPicVrcodeDialog(phoneNumber);
                    loginCode.setClickable(true);
                    LogUtils.e(TAG, "日志:短信发送已达限制");
                }
            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                loginCode.setClickable(true);
                ToastUtils.show("获取验证码失败，请检查网络状况");
            }
        });
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

        VolleySingleton.postNoDEVICETOKEN(Neturl.GET_PIC_CODE, "get_pic_code", params, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Loginsms loginsms = JsonUtils.parseJsonToBean(result, Loginsms.class);
                if (loginsms != null && loginsms.data != null) {
                    getNetWorkBitMap(loginsms.data.images, iv);
                }
            }
        }, null);
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


    /**
     * 获取用户的设备信息
     */
    private void obtainLocation() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("lat", SPUtils.getString(Constants.LOCATON_LAT, ""));
        map.put("lng", SPUtils.getString(Constants.LOCATON_LNG, ""));
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String deviceId = TelephonyMgr.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = "";
        }
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String mac = wm.getConnectionInfo().getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            map.put("mac", mac);
        }
        map.put(Constants.OAUTH_TOKEN, SPUtils.getString(Constants.OAUTH_TOKEN, ""));
        map.put(Constants.OAUTH_TOKEN_SECRET, SPUtils.getString(Constants.OAUTH_TOKEN_SECRET, ""));

        map.put("version", Build.VERSION.RELEASE);
        map.put("os", "0");
        map.put("imei", deviceId);
        map.put("uuid", SPUtils.getString("uuid", ""));
        map.put("type", Build.MODEL);// 设备型号
//        LogUtils.e("splash :",
//                "mac :" + mac + "\nversion :" + android.os.Build.VERSION.RELEASE + "\nimei : " + deviceId + "\ntype : "
//                        + android.os.Build.MODEL + "\nuuid : "
//                        + SPUtils.getString("uuid", ""));
        VolleySingleton.postNoDEVICETOKEN(Neturl.INIT_APP, "initapp", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                SPUtils.saveString(Constants.LOCATION, result);
                Location location = JsonUtils.parseJsonToBean(result, Location.class);
                if (location != null && location.data != null) {
                    SPUtils.saveString("uuid", location.data.uuid);
                    SPUtils.saveString("kefu", location.data.kefu);
                    getVerificationCode(mPhoneNum, "");
                }
            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                loginCode.setClickable(true);
            }
        });

    }

    /**
     * 判断手机号码是否合理
     *
     * @return
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11) && isMobileNo(phoneNums)) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    private boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     *
     * @param phoneNums
     * @return
     */
    private boolean isMobileNo(String phoneNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][345789]\\d{9}";// "[1]"代表第1位为数字1，"[34758]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phoneNums))
            return false;
        else
            return phoneNums.matches(telRegex);
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


    /**
     * 检查是否有定位信息
     */
    private void checkLocation() {
//        if (TextUtils.isEmpty(SPUtils.getString(Constants.LOCATON_LAT, ""))) {
//            TipDialog.show_(this, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getAppDetailSettingIntent(LoginBackPwdActivity.this);
//                }
//            }, "定位失败", "为了方便您的使用,请手动开启应用的定位权限");
//        }
        location();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        checkLocation();
    }

    /**
     * 跳转到设置定位权限页（23以上跳转应用详情页）
     *
     * @param context
     */
    private void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于23
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else {
            localIntent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        }
        startActivityForResult(localIntent, 200);
    }

    ///////////////////////////////////////////////////////////////////定位///////////////////////////////////////////////////////////////////

    /**
     * 获取经纬度操作
     */
    private void location() {
        //定位
//        SPUtils.saveString(Constants.LOCATON_LAT, "");
//        SPUtils.saveString(Constants.LOCATON_LNG, "");
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        } else {
            requestLocation();
        }
    }


    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(LoginBackPwdActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 100);
        } else {
            requestLocation();
        }
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
                    requestLocation();
                } else {
                    // 没有获取到权限，做特殊处理
                    ToastUtils.show("获取位置权限失败，请手动开启");
                }
                break;
            default:
                break;
        }
    }

    private LocationManager lm;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
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

    public void requestLocation() {
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
        } catch (Exception e) {
        }
    }
}
