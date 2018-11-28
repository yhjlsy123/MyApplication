package com.isgala.xishuashua.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.ActivityBean;
import com.isgala.xishuashua.bean_.AppActivity;
import com.isgala.xishuashua.bean_.Location;
import com.isgala.xishuashua.bean_.UpgradeEntity;
import com.isgala.xishuashua.config.AppManager;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.AppActivityDialog;
import com.isgala.xishuashua.dialog.DownloadDialog;
import com.isgala.xishuashua.dialog.TipDialog;
import com.isgala.xishuashua.dialog.UpdateDialog;
import com.isgala.xishuashua.dialog.UpdateDialogApp;
import com.isgala.xishuashua.fragment.DrawerContent;
import com.isgala.xishuashua.fragment.HomeContent;
import com.isgala.xishuashua.utils.DimensUtil;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.Tools;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.klcxkj.zqxy.databean.MessageEvent;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseAutoActivity {
    private final String TAG = "HomeActivity";
    public static final String CONTENT_TAG = "home_content";
    public static final String DRAWER_TAG = "home_drawer";
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.drawer_home)
    FrameLayout mDrawerView;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setDrawerWidth();
        replaceView();
        getUpdateEdition();
        String locationJson = SPUtils.getString(Constants.LOCATION, "");
//        if (!TextUtils.isEmpty(locationJson)) {
//            location = JsonUtils.parseJsonToBean(locationJson, Location.class);
//            if (location != null && location.data != null) {
//                checkVersionCode();
//            }
//        }
         //增加水控机功能
//        EventBus.getDefault().register(this);
        if (!HAS_NEW_VERSION)//没有新版本,可以显示广告(活
            // 动)
            requestActivity();
    }
    /**
     * 检测版本升级
     */
    private void getUpdateEdition() {
        HashMap<String, String> map = new HashMap<>();
        map.put("client_type", "1");//1:用户端 2：投资商端
        map.put("device", "1");// 1：android 2:IOS
        //获取当前app版本
        map.put("app_version_code", SPUtils.getLocalVersion(this));
        VolleySingleton.post(Neturl.GET_UPGRADEAPP, "upgradeApp", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                UpgradeEntity entity = JsonUtils.parseJsonToBean(result, UpgradeEntity.class);
                if (entity != null && entity.data != null) {
                    if (entity.data.getStatus() == 1) {
                        showDialogUpdate(entity);
                    }
                }else {

                }
//                if (entity.data != null && entity.data.getStatus() == 1) {//Status 是1提示版本升级  0 暂无更新
//                    showDialog(entity);
//                } else {
//                    obtainLocation();
//                }
            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {

            }
        });
    }
    private void showDialogUpdate(final UpgradeEntity entity){
        UpdateDialogApp.Builder builder=new UpdateDialogApp.Builder(this);
        builder.create(entity.data.getUpdate(),entity.data.getIs_force()).show();
        builder.setConfirm(new UpdateDialogApp.OnConfirmListener(){
            @Override
            public void confirm() {
                Toast.makeText(HomeActivity.this, "进行更新操作吧", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(entity.data.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
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
     * 请求首页活动弹框的数据
     */
    private void requestActivity() {
        VolleySingleton.post(Neturl.ACTIVITY_APP, "activity_app", null, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                AppActivity appActivity = JsonUtils.parseJsonToBean(result, AppActivity.class);
                if (appActivity != null) {
                    if (TextUtils.equals(appActivity.status, "1")) {
//                        if (TextUtils.isEmpty(SPUtils.getString(appActivity.data.id, "")))//显示过就不显示了
                        if (!TextUtils.isEmpty(appActivity.data.id))
                            showAppActivityDialog(appActivity.data);
                    } else {
                        ToastUtils.show(appActivity.msg);
                    }
                }
            }
        });
    }

    /**
     * 显示活动弹框
     *
     * @param data
     */
    private void showAppActivityDialog(final ActivityBean data) {
        AppActivityDialog.Builder builder = new AppActivityDialog.Builder(this);
        builder.setPositiveButton(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(HomeActivity.this, H5Activity.class);
                intent.putExtra("title", data.name);
                intent.putExtra("url", data.link_url);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.create(Tools.toInt(data.width), Tools.toInt(data.height), data.img_url).show();
        // 解开
//        SPUtils.saveString(data.id, data.id);
    }


    /**
     * 设置侧拉菜单的宽度
     */
    private void setDrawerWidth() {
        try {
            ViewGroup.LayoutParams layoutParams = mDrawerView.getLayoutParams();
            layoutParams.width = (int) (550 * DimensUtil.getWidthRate());
            mDrawerView.setLayoutParams(layoutParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //检测是否有定位权限
//        checkLocation();
    }

    /**
     * 检查是否有定位信息(没有,请求定位)
     */
    private void checkLocation() {
//        if (TextUtils.isEmpty(SPUtils.getString(Constants.LOCATON_LAT, ""))) {
//            TipDialog.show_(this, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getAppDetailSettingIntent(HomeActivity.this);
//                }
//            }, "定位失败", "为了方便您的使用,请手动开启应用的定位权限");
//        }
        requestLocation();
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
        MobclickAgent.onPause(this);
    }

    HomeContent homeContent;

    /**
     * 替换布局
     */
    private void replaceView() {
        homeContent = new HomeContent();
        homeContent.setUp(mDrawer);
        DrawerContent drawerContent = new DrawerContent();
        drawerContent.setUp(mDrawer);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_home, homeContent, CONTENT_TAG).commitAllowingStateLoss();
         //侧拉菜单
        getSupportFragmentManager().beginTransaction().replace(R.id.drawer_home, drawerContent, DRAWER_TAG).commitAllowingStateLoss();
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


    private int from;
    private String mTarget;
    private boolean HAS_NEW_VERSION = false;
    private static final int REQUEST_CODE_INSTALL = 101;

    /**
     * 检测版本更新
     */
    public void checkVersionCode() {
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            mTarget = Environment.getExternalStorageDirectory() + "/xi/apk/com.isgala.xi." + location.data.version.now + ".apk";
            if (versionCode < Tools.toInt(location.data.version.min_code)) {
                HAS_NEW_VERSION = true;
                Intent intent = new Intent();
                intent.setAction("NEW_VERSION");
                sendOrderedBroadcast(intent, null);
                from = 1;
                if (from == 1) {
                    showUpdateDialog(location.data.version);
                }
                return;
            } else if (versionCode >= Tools.toInt(location.data.version.min_code)
                    && versionCode < Tools.toInt(location.data.version.code)) {
                if (!SPUtils.getBoolean(mTarget, false)) {
                    HAS_NEW_VERSION = true;
                    Intent intent = new Intent();
                    intent.setAction("NEW_VERSION");
                    sendOrderedBroadcast(intent, null);
                    from = 0;
                    if (from == 0) {
                        showUpdateDialog(location.data.version);
                    }
                }
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查是否有最新的安装包
     */
    private boolean checkPackageExits() {
        File file = new File(mTarget);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 弹出更新提示对话框
     */
    private void showUpdateDialog(final Location.Version version) {
        final boolean exits = checkPackageExits();
        UpdateDialog.Builder builder = new UpdateDialog.Builder(this, from, version, exits);
        builder.setPositiveButton(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (exits) {
                    installApk(new File(mTarget), version);
                } else {
                    downloadApk(from, version);
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SPUtils.saveBoolean(mTarget, true);
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    /**
     * 下载apk
     */
    protected void downloadApk(final int from, final Location.Version version) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            HttpUtils http = new HttpUtils();
            http.download(version.url, mTarget, new RequestCallBack<File>() {

                // 开始时的回调
                @Override
                public void onStart() {
                    super.onStart();
                    showDownloadDialog(from);
                }

                // 下载中的回调
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    Intent intent = new Intent();
                    intent.setAction("DOWNLOAD");
                    intent.putExtra("max", total);
                    intent.putExtra("progress", current);
                    sendOrderedBroadcast(intent, null);
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    if (mDownloadDialog != null)
                        mDownloadDialog.dismiss();
                    File mApk = responseInfo.result;
                    installApk(mApk, version);
                }

                @Override
                public void onFailure(com.lidroid.xutils.exception.HttpException error, String msg) {
                    error.printStackTrace();
                    if (mDownloadDialog != null)
                        mDownloadDialog.dismiss();
                }
            });
        } else {
            ToastUtils.show("SD卡不可用");
        }
    }

    /**
     *
     * 安装apk
     *
     * @param file
     */
    protected void installApk(File file, Location.Version version) {

        if (checkPackageExits()) {
            // 使用隐式意图, 调用系统应用 Package Installer, 让它帮我们安装. (静默安装, ROOT)
            // <intent-filter>
            // <action android:name="android.intent.action.VIEW" />
            // <category android:name="android.intent.category.DEFAULT" />
            // <data android:scheme="content" />
            // <data android:scheme="file" />
            // <data android:mimeType="application/vnd.android.package-archive"
            // />
            // </intent-filter>
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            // 避免用户在安装过程中取消
            startActivityForResult(intent, REQUEST_CODE_INSTALL);
        } else {
            showUpdateDialog(version);
            ToastUtils.show("文件丢失，请重新下载");
        }
    }

    private DownloadDialog mDownloadDialog;

    /**
     * 弹出下载对话框
     */
    private void showDownloadDialog(int from) {
        DownloadDialog.Builder builder = new DownloadDialog.Builder(this, from);
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
    }

    public static final int PAYRESULT = 999;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INSTALL) {
            if (resultCode == 0) {
                SPUtils.saveBoolean(mTarget, true);
            }
//            if (from == 1) {
//                checkVersionCode();
//            }
        }
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
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 100);
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
