package com.zhuochi.hydream.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.bean_.Location;
import com.zhuochi.hydream.config.AppManager;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.TipDialog2;
import com.zhuochi.hydream.entity.pushbean.InitSettingEntity;
import com.zhuochi.hydream.http.RequestURL;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.UserUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;


/**
 * 设置界面
 * Created by and on 2016/11/8.
 */

public class SettingActivity extends BaseAutoActivity {
    @BindView(R.id.line_versionNumber)
    RelativeLayout lineVersionNumber;
    @BindView(R.id.product)
    RadioButton product;
    @BindView(R.id.test)
    RadioButton test;
    @BindView(R.id.dev)
    RadioButton dev;
    @BindView(R.id.line_test)
    RadioGroup lineTest;
    private Location.Version mVersion;
    private File mApk;
    private int from;
    private static final int REQUEST_CODE_INSTALL = 101;
    private String mTarget;
    private String mGson;
    private InitSettingEntity mInitEntity;
    private RadioGroup line_test;
    private String[] perms = {Manifest.permission.CALL_PHONE};
    private final int PERMS_REQUEST_CODE = 200;
    private int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);


        ((TextView) findViewById(R.id.setting_versionNumber)).setText("V" + SPUtils.getLocalVersion(this));
        mGson = SPUtils.getString("initSetting", "");
        if (!TextUtils.isEmpty(mGson)) {
            mInitEntity = new Gson().fromJson(mGson, InitSettingEntity.class);
            String servicePhone = mInitEntity.getServicePhone();
            ((TextView) findViewById(R.id.setting_phone)).setText(servicePhone);
        }
        SwitchTest();

    }

    //长按  显示测试版本
    private void SwitchTest() {
        line_test = (RadioGroup) findViewById(R.id.line_test);
        RelativeLayout line_versionNumber = (RelativeLayout) findViewById(R.id.line_versionNumber);
        line_versionNumber.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int role = SPUtils.getInt(Constants.USER_OAUTH, 0);
                if (role == 2) {
                    line_test.setVisibility(View.VISIBLE);
                }

                return true;
            }
        });

        line_versionNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line_test.setVisibility(View.GONE);
            }
        });
        switch (RequestURL.URL) {
            case "http://newdev.gaopintech.cn/api/v1/":
                product.setChecked(true);
                break;
            case "http://new_dev.gaopintech.cn/api/v1/":
                test.setChecked(true);
                break;
            case "http://hx.94lihai.com/api/v1/":
                dev.setChecked(true);
                break;


        }
        line_test.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, final int checkedId) {

                TipDialog2.show_two(SettingActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (checkedId) {
                            case R.id.product:
//                        生产
                                SPUtils.saveString("baseUrl", "http://newdev.gaopintech.cn/api/v1/");
                                break;
                            case R.id.test:
//                        测试
                                SPUtils.saveString("baseUrl", "http://new_dev.gaopintech.cn/api/v1/");
                                break;
                            case R.id.dev:
//                        开发
                                SPUtils.saveString("baseUrl", "http://hx.94lihai.com/api/v1/");
                                break;

                        }
                        AppManager.INSTANCE.appExit(SettingActivity.this);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (RequestURL.URL) {
                            case "http://newdev.gaopintech.cn/api/v1/":
                                product.setChecked(true);
                                break;
                            case "http://new_dev.gaopintech.cn/api/v1/":
                                test.setChecked(true);
                                break;
                            case "http://hx.94lihai.com/api/v1/":
                                dev.setChecked(true);
                                break;


                        }
                    }
                }, "退出提示", "确定要退出切换服务器吗?");

            }
        });


    }

    @OnClick({R.id.setting_back, R.id.userguide, R.id.setting_checkversion, R.id.setting_exit, R.id.setting_aboutus, R.id.user_use, R.id.setting_callus})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_back:
                finish();
                break;

            case R.id.setting_aboutus:
                Intent intent_ = new Intent(SettingActivity.this, AboutusActivity.class);
                intent_.putExtra("title", "关于我们");
                intent_.putExtra("url", mInitEntity.getAboutUsUrl());
                startActivity(intent_);
                break;
            case R.id.setting_checkversion:
                break;
            case R.id.setting_callus:
                /**
                 * 添加权限动态管理申请适应6.0及以上系统的处理
                 */
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {//Android 6.0以上版本需要获取权限
                    requestPermissions(perms, PERMS_REQUEST_CODE);//请求权限
                } else {
                    callPhone();
                }
                break;
            case R.id.user_use:
                Intent intent2 = new Intent(SettingActivity.this, H5Activity.class);
                intent2.putExtra("title", "用户协议");
                intent2.putExtra("url", mInitEntity.getRegistrationAgreementUrl());
                startActivity(intent2);
                break;
            case R.id.setting_exit:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(SettingActivity.this);
//            normalDialog.setTitle("我是一个普通Dialog");
        normalDialog.setMessage("确认退出吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO JG 解除极光推送绑定
                        Set<String> set = new HashSet<String>();
                        int str = UserUtils.getInstance(SettingActivity.this).getOrgAreaID();

                        set.add("orgArea_" + str);//绑定 校区（Tag）
                        JPushInterface.deleteTags(SettingActivity.this, 0, set);
                        JPushInterface.deleteAlias(SettingActivity.this, 0);
                        UserUtils.setDataNull();
                        SPUtils.saveString(Constants.MOBILE_PHONE, "");
                        SPUtils.saveInt(Constants.ORG_ID, 0);//学校ID
                        SPUtils.saveInt(Constants.ORG_AREA_ID, 0);//校区ID
                        SPUtils.saveInt(Constants.BUILDING_ID, 0);//楼层ID
                        SPUtils.saveInt(Constants.DEVICE_AREA_ID, 0);//当前绑定区域（浴室）
                        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                        finish();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();

    }

    private void callPhone() {
        try {
//            String number = SPUtils.getString("kefu", "");
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mInitEntity.getServicePhone()));
            startActivity(intent);
        } catch (Exception e) {

        }

    }


    /**
     * 获取权限回调方法
     *
     * @param permsRequestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        switch (permsRequestCode) {
            case PERMS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();

                } else {
                    PermissionsActivity.startActivityForResult(this, REQUEST_CODE, perms);

//                    Log.i("MainActivity", "没有权限操作这个请求");
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            ToastUtils.show(" 请开启拨号权限");
        }
        if (requestCode == REQUEST_CODE_INSTALL) {
            if (resultCode == 0) {
                SPUtils.saveBoolean(mTarget, true);
            }
        }

    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
