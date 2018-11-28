package com.zhuochi.hydream.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.bean_.ActivityBean;
import com.zhuochi.hydream.bean_.Location;
import com.zhuochi.hydream.bean_.UpgradeEntity;
import com.zhuochi.hydream.config.AppManager;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.H5Dialog;
import com.zhuochi.hydream.dialog.TipDialog;
import com.zhuochi.hydream.dialog.UpdateDialogApp;
import com.zhuochi.hydream.entity.AdContent;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.pushbean.InitSettingEntity;
import com.zhuochi.hydream.fragment.HomeContent;
import com.zhuochi.hydream.http.DESCryptogRaphy;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.DialogUtils;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.Tools;
import com.zhuochi.hydream.utils.UserUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 2018.11.9
 */
public class HomeActivity extends BaseAutoActivity {
    private final String TAG = "HomeActivity";
    public static final String CONTENT_TAG = "home_content";
    public static final String DRAWER_TAG = "home_drawer";
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    private Location location;
    private XiRequestParams params;
    private MqttAndroidClient mqttAndroidClient;
    private String subscriptionTopic = "stateChange_614fff6b-67de-4673-a2c5-cd138c7f9694";
    private H5Dialog adDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        replaceView();
        getInitSetting();
        upgradeApp();
        loadAd();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        params = new XiRequestParams(this);
        replaceView();
        getInitSetting();
        upgradeApp();

    }


    private void loadAd() {
        params.addCallBack(this);
        Map<String, Object> pram = new HashMap<String, Object>();
        pram.put("deviceAreaId", SPUtils.getInt("Device_area_id", 0) + "");
        pram.put("deviceTypeKey", 1);
        pram.put("token", UserUtils.getInstance(this).getTokenID());
        params.comonRequest(pram, "AdApi/getAd");
    }


    /**
     * 检测版本更新
     */
    private void upgradeApp() {
        params.addCallBack(this);
        params.upgradeApp(SPUtils.getLocalVersion(this));
    }

    private void showDialogUpdate(final UpgradeEntity entity) {
        if (entity.getStatus() == 1) {
            UpdateDialogApp.Builder builder = new UpdateDialogApp.Builder(this);
            builder.create(entity.getIntro(), entity.getIs_force(), entity.getNew_version()).show();
            builder.setConfirm(new UpdateDialogApp.OnConfirmListener() {
                @Override
                public void confirm() {
                    Toast.makeText(HomeActivity.this, "进行更新操作吧", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse(entity.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
    }

    /*初始化设置*/
    private void getInitSetting() {
        params.addCallBack(this);
        params.getInit();
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getInit":
                Map map = (Map) result.getData().getData();
                try {
                    String gson = GsonUtils.parseMapToJson(map);
                    //初始化设置 保存到本地
                    Log.i("cxcha", "hahhhh" + gson);
                    SPUtils.saveString("initSetting", gson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "upgradeApp":
//               ToastUtils.show(result.getData().getMsg());
                Map map1 = (Map) result.getData().getData();
                String gs = GsonUtils.parseMapToJson(map1);
                UpgradeEntity entity = JSON.parseObject(gs, UpgradeEntity.class);
                showDialogUpdate(entity);
                break;

            case "AdApi/getAd":
                if (result.getData().getCode() == 200) {
                    Log.i("lsy", JSON.toJSONString(result.getData().getData()));
                    AdContent ad = JSON.parseObject(JSON.toJSONString(result.getData().getData()), AdContent.class);
                    if (!(TextUtils.isEmpty(ad.getAdEnabled())) && TextUtils.equals("1", ad.getAdEnabled())) {
                        adDialog = new H5Dialog(this, ad.getAdContent());
                        adDialog.setBlank(true);
                        adDialog.show();
                        DialogUtils.centerMatchWidthScreen(adDialog, new BigDecimal(ad.getAdWidth()).intValue(), new BigDecimal(ad.getAdHeight()).intValue());


                    }
                }
                break;
        }
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


//    /**
//     * 显示活动弹框
//     *
//     * @param data
//     */
//    private void showAppActivityDialog(final ActivityBean data) {
//        AppActivityDialog.Builder builder = new AppActivityDialog.Builder(this);
//        builder.setPositiveButton(new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(HomeActivity.this, H5Activity.class);
//                intent.putExtra("title", data.name);
//                intent.putExtra("url", data.link_url);
//                startActivity(intent);
//                dialog.dismiss();
//            }
//        });
//        builder.create(Tools.toInt(data.width), Tools.toInt(data.height), data.img_url).show();
//        // 解开
////        SPUtils.saveString(data.id, data.id);
//    }


    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 跳转到应用设置中心
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


    @Override
    public void onPause() {
        super.onPause();
    }

    HomeContent homeContent;

    /**
     * 替换布局
     */
    private void replaceView() {
        homeContent = new HomeContent();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_home, homeContent, CONTENT_TAG).commitAllowingStateLoss();
        //侧拉菜单
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (Tools.dblClose()) {
                AppManager.INSTANCE.appExit(this);
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 是否触发按键为back键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else {// 如果不是back键正常响应
            return super.onKeyDown(keyCode, event);
        }
    }


    private String mTarget;
    private boolean HAS_NEW_VERSION = false;
    private static final int REQUEST_CODE_INSTALL = 101;
    public static final int PAYRESULT = 999;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INSTALL) {
            if (resultCode == 0) {
                SPUtils.saveBoolean(mTarget, true);
            }
        }
    }


    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 100);
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
