package com.isgala.xishuashua.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.Location;
import com.isgala.xishuashua.config.AppManager;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.DownloadDialog;
import com.isgala.xishuashua.dialog.UpdateDialog;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.Tools;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.isgala.xishuashua.api.Neturl.REGISTER_TEXT;

/**
 * Created by and on 2016/11/8.
 */

public class SettingActivity extends BaseAutoActivity {
    private Location.Version mVersion;
    private DownloadDialog mDownloadDialog;
    private File mApk;
    private int from;
    private static final int REQUEST_CODE_INSTALL = 101;
    private String mTarget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        ((TextView)findViewById(R.id.setting_phone)).setText(SPUtils.getString("kefu", ""));
        ((TextView)findViewById(R.id.setting_versionNumber)).setText("V"+SPUtils.getLocalVersion(this));
    }

    @OnClick({R.id.setting_back, R.id.userguide, R.id.setting_checkversion, R.id.setting_exit, R.id.setting_aboutus, R.id.user_use, R.id.setting_callus})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_back:
                finish();
                break;
            case R.id.userguide:
                Intent intent = new Intent(SettingActivity.this, H5Activity.class);
                intent.putExtra("title", "用户指南");
                intent.putExtra("url", Neturl.USER_GUIDE);
                startActivity(intent);
                break;
            case R.id.setting_aboutus:
                Intent intent_ = new Intent(SettingActivity.this, H5Activity.class);
                intent_.putExtra("title", "关于我们");
                intent_.putExtra("url", Neturl.ABOUT_US);
                startActivity(intent_);
                break;
            case R.id.setting_checkversion:
                checkVersionCode();
                break;
            case R.id.setting_callus:
                callPhone();
                break;
            case R.id.user_use:
                Intent intent2 = new Intent(SettingActivity.this, H5Activity.class);
                intent2.putExtra("title", "用户注册使用协议");
                intent2.putExtra("url", REGISTER_TEXT);
                startActivity(intent2);
                break;
            case R.id.setting_exit:
                SPUtils.saveString(Constants.S_ID, "");
                SPUtils.saveString(Constants.OAUTH_TOKEN, "");
                SPUtils.saveString(Constants.OAUTH_TOKEN_SECRET, "");
                SPUtils.saveString(Constants.CAMPUS, "");
                SPUtils.saveBoolean(Constants.IS_LOGIN, false);
                startActivity(new Intent(SettingActivity.this, LoginRevisionActivity.class));
                AppManager.INSTANCE.finishAllActivity();
                break;
        }
    }

    private void callPhone() {
        try {
            String number = SPUtils.getString("kefu", "");
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        } catch (Exception e) {

        }

    }

    /**
     * 检测版本更新
     */
    private void checkVersionCode() {
        String result = SPUtils.getString(Constants.LOCATION, "");
        Location mBean = JsonUtils.parseJsonToBean(result, Location.class);
        if (mBean == null) {
            return;
        }
        mVersion = mBean.data.version;
        mTarget = Environment.getExternalStorageDirectory() + "/xi/apk/com.isgala.xi." + mBean.data.version.now + ".apk";

        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            if (versionCode < Tools.toInt(mBean.data.version.min_code)) {
                from = 1;
                if (from == 1) {
                    showUpdateDialog();
                }
                return;
            } else if (versionCode >= Tools.toInt(mBean.data.version.min_code)
                    && versionCode < Tools.toInt(mBean.data.version.code)) {
                from = 0;
                if (from == 0) {
                    showUpdateDialog();
                }
                return;
            } else {
                ToastUtils.show("已经更新到最新版本");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 弹出更新提示对话框
     */
    private void showUpdateDialog() {
        final boolean exits = checkPackageExits();
        UpdateDialog.Builder builder = new UpdateDialog.Builder(this, from, mVersion, exits);
        builder.setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (exits) {
                    installApk(new File(mTarget));
                } else {
                    downloadApk(from);
                }
                dialog.dismiss();
            }

        });

        builder.setNegativeButton(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    /**
     * 下载apk
     */
    protected void downloadApk(final int from) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // final ProgressDialog dialog = new ProgressDialog(getActivity());
            HttpUtils http = new HttpUtils();
            http.download(mVersion.url, mTarget, new RequestCallBack<File>() {

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
                    mDownloadDialog.dismiss();
                    mApk = responseInfo.result;
                    installApk(mApk);
                }

                @Override
                public void onFailure(HttpException e, String error) {
                    e.printStackTrace();
                    mDownloadDialog.dismiss();
                }
            });
        } else {
            ToastUtils.show("SD卡不可用");
        }
    }

    /**
     * 弹出下载对话框
     */
    private void showDownloadDialog(int from) {
        DownloadDialog.Builder builder = new DownloadDialog.Builder(this, from);
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
    }

    /**
     * 安装apk
     *
     * @param file
     */
    protected void installApk(File file) {

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
            showUpdateDialog();
            ToastUtils.show("文件丢失，请重新下载");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_INSTALL) {
            if (resultCode == 0) {
                SPUtils.saveBoolean(mTarget, true);
            }
            if (from == 1) {
                checkVersionCode();
            }
        }

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SettingActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SettingActivity");
        MobclickAgent.onPause(this);
    }
}
