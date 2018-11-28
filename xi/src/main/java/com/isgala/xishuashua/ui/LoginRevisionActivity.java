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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.Location;
import com.isgala.xishuashua.bean_.LoginResult;
import com.isgala.xishuashua.bean_.Loginsms;
import com.isgala.xishuashua.config.AppManager;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingAnimDialog;
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

import static com.isgala.xishuashua.api.Neturl.GET_LOGIN_PASSWORD;
import static com.isgala.xishuashua.api.Neturl.REGISTER_TEXT;
import static com.isgala.xishuashua.api.Neturl.USER_LOGIN;


/**
 * 最新登录
 */

public class LoginRevisionActivity extends BaseAutoActivity {
    @BindView(R.id.login_photo)
    ImageView circleimageview;
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_input_password)
    EditText loginInputCode;
    @BindView(R.id.login_enter)
    Button loginEnter;
    private String mPhoneNum;
    private Handler handler;
    private String TAG = "LoginRevisionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        ButterKnife.bind(this);
        handler = new Handler();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("LoginRevisionActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("LoginRevisionActivity");
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.login_enter, R.id.shiyongshuoming, R.id.txt_loginQuick, R.id.txt_backPassword})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.login_code:
//                loginCode.setClickable(false);
//                mPhoneNum = loginPhone.getText().toString();
//                if (TextUtils.isEmpty(mPhoneNum)) {
//                    ToastUtils.show("请输入您的手机号码");
//                    loginCode.setClickable(true);
//                    return;
//                }
//                // 通过规则判断手机号
//                if (!judgePhoneNums(mPhoneNum)) {
//                    ToastUtils.show("手机号码有误，请核对后重新输入");
//                    loginCode.setClickable(true);
//                    return;
//                }
//                if (NetworkUtil.isNetworkAvailable()) {// 当网络可用时开始倒计时,获取短信
//                    if (TextUtils.isEmpty(SPUtils.getString("uuid", ""))) {// uuid为空时，再次获取uuid
//                        obtainLocation();
//                        return;
//                    }
//                    getVerificationCode(mPhoneNum, "");
//                } else {
//                    loginCode.setClickable(true);
//                }
//                break;
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
            case R.id.shiyongshuoming:
                Intent intent = new Intent(LoginRevisionActivity.this, H5Activity.class);
                intent.putExtra("title", "用户注册使用协议");
                intent.putExtra("url", REGISTER_TEXT);
                startActivity(intent);
                break;
            case R.id.txt_loginQuick://快捷登录
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.txt_backPassword://找回密码
                startActivity(new Intent(this,LoginBackPwdActivity.class));
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
        params.put("user_account", phoneNum);//账号
        params.put("password", verificationCode);//密码
        params.put("uuid", SPUtils.getString("uuid", ""));
        params.put("device_tokens", SPUtils.getString(Constants.DEVICE_TOKEN, ""));
        LogUtils.e(TAG, params.toString());
        VolleySingleton.postNoDEVICETOKEN(GET_LOGIN_PASSWORD, "login_with_password", params, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                LoginResult result1 = JsonUtils.parseJsonToBean(result, LoginResult.class);
                if (result1 != null && result1.data != null) {
                    if (result1.data.status.equals("1")){//判断用户登录状态1.正常 2.禁用 3.删除
                        jump(result1.data);
                    }else {
                        ToastUtils.show(result1.msg);//状态不对  直接抛msg提示语
                        LoadingAnimDialog.dismissLoadingAnimDialog();
                    }
                } else {
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
        String device_type = data.device_type;
        String b_id = data.b_id;
        String phone=data.phone;
        SPUtils.saveString(Constants.PHONE_NUMBER, phone);
        SPUtils.saveString(Constants.YB_ID, b_id);
        SPUtils.saveString(Constants.DEVICE_TYPE, device_type);
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


    @Override
    protected void onDestroy() {
        if (lm != null) {
            lm.removeUpdates(locationListener);
            lm = null;
            locationListener = null;
        }
        super.onDestroy();
    }


    /**
     * 检查是否有定位信息
     */
    private void checkLocation() {
//        if (TextUtils.isEmpty(SPUtils.getString(Constants.LOCATON_LAT, ""))) {
//            TipDialog.show_(this, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getAppDetailSettingIntent(LoginRevisionActivity.this);
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
            ActivityCompat.requestPermissions(LoginRevisionActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 100);
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
