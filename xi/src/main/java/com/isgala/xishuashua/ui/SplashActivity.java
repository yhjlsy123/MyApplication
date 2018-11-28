package com.isgala.xishuashua.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.Location;
import com.isgala.xishuashua.bean_.UpgradeEntity;
import com.isgala.xishuashua.bean_.UserInfoEntity;
import com.isgala.xishuashua.config.AppManager;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.VersionDialogFragment;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.klcxkj.zqxy.MyApp;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.ButterKnife;

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

    //定位6.0动态权限申请
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppManager.INSTANCE.getsize() > 1) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        intoTime = System.currentTimeMillis();
        ButterKnife.bind(this);
        location();
        loadUserInfo();
        mlineShow = (RelativeLayout) findViewById(R.id.line_show);
        mCancel = (TextView) findViewById(R.id.txt_cancel);
        mConfirm = (TextView) findViewById(R.id.txt_confirm);
        mContent = (TextView) findViewById(R.id.content);
        MyApp.clearData(this);
    }

    private void loadUserInfo() {
        if (!TextUtils.isEmpty(SPUtils.getString(Constants.OAUTH_TOKEN, ""))) {
            VolleySingleton.post(Neturl.USER_INFO, "userinfo", null, new VolleySingleton.CBack() {
                @Override
                public void runUI(String result) {
                    UserInfoEntity userInfoEntity = JsonUtils.parseJsonToBean(result, UserInfoEntity.class);
                    if (userInfoEntity != null && userInfoEntity.data != null) {
                        SPUtils.saveString(Constants.CAMPUS, userInfoEntity.data.campus);
                        SPUtils.saveString(Constants.S_ID, userInfoEntity.data.s_id);
                    }
                }
            });
        }

        obtainLocation();
    }




    /**
     * 提示升级
     */
    public void showDialog(final UpgradeEntity entity) {
        mContent.setText(entity.data.getUpdate());
        mlineShow.setVisibility(View.VISIBLE);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlineShow.setVisibility(View.GONE);
                obtainLocation();
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SplashActivity.this, "进行更新操作吧", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(entity.data.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
//                mlineShow.setVisibility(View.GONE);
            }
        });
//       dialogFragment=VersionDialogFragment.getInstance("提示升级",entity.data.getUpdate(),entity.data.getUrl());
//        dialogFragment.show(getSupportFragmentManager(), new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(SplashActivity.this, "进行更新操作吧", Toast.LENGTH_SHORT).show();
//                Uri uri = Uri.parse(entity.data.getUrl());
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//                dialogFragment.dismiss();
//            }
//        },this);

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
        String s_id = SPUtils.getString(Constants.S_ID, "");
        if (isFirstEnter) {
            // 1. 动画结束，跳转到引导界面
            nextClass = GuideActivity.class;
        } else if (SPUtils.getBoolean(Constants.IS_LOGIN, false)) {//2.判断是否登录
            String campus_id = SPUtils.getString(Constants.CAMPUS, "");
            if (TextUtils.isEmpty(s_id) || TextUtils.equals(s_id, "0")) {//3.判断是否有设置过学校信息
                nextClass = SchoolList.class;
            } else if (TextUtils.isEmpty(campus_id) || TextUtils.equals(campus_id, "0")) {//4.判断是否设置过校区信息
                nextClass = SchoolList.class;
            } else {
                nextClass = HomeActivity.class;
            }
        } else {
            nextClass = LoginRevisionActivity.class;
        }
        Intent intent = new Intent(this, nextClass);
        intent.putExtra("s_id", s_id);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //不实现,禁止用户在此界面退出App
    }

    private LocationManager lm;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            SPUtils.saveString(Constants.LOCATON_LAT, "" + location.getLatitude());
            SPUtils.saveString(Constants.LOCATON_LNG, "" + location.getLongitude());
            if (lm != null) {
                lm.removeUpdates(this);
                lm = null;
            }
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


    /**
     * 获取经纬度操作
     */

    private void location() {
        //定位
        SPUtils.saveString(Constants.LOCATON_LAT, "");
        SPUtils.saveString(Constants.LOCATON_LNG, "");
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        } else {
            requestLocation();
        }
        SPUtils.saveString(Constants.LOCATION, "");//初始化
        obtainLocation();
    }


    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 100);
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


    /**
     * 获取用户的设备信息
     */
    private void obtainLocation() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("lat", SPUtils.getString(Constants.LOCATON_LAT, ""));
        map.put("lng", SPUtils.getString(Constants.LOCATON_LNG, ""));
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String deviceId="";
        try {
            deviceId = TelephonyMgr.getDeviceId();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = "";
        }
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String mac = wm.getConnectionInfo().getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            map.put("mac", mac);
        }
        map.put("version", android.os.Build.VERSION.RELEASE);
        map.put("os", "0");
        map.put("imei", deviceId);

        map.put("uuid", SPUtils.getString("uuid", ""));
        map.put("type", android.os.Build.MODEL);// 设备型号
        VolleySingleton.postNoDEVICETOKEN(Neturl.INIT_APP, "initapp", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                SPUtils.saveString(Constants.LOCATION, result);
                Location location = JsonUtils.parseJsonToBean(result, Location.class);
                if (location != null && location.data != null) {
                    SPUtils.saveString("uuid", location.data.uuid);
                    SPUtils.saveString("kefu", location.data.kefu);
                }
                jump();
            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                jump();
            }
        });
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

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashActivity");
        MobclickAgent.onPause(this);
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
        if (lm != null) {
            lm.removeUpdates(locationListener);
            lm = null;
            locationListener = null;
        }
        super.onDestroy();
    }


}
