package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.config.AppManager;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.dialog.ResetNumberPWD;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.UserInfoEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.UserUtils;
import com.zhuochi.hydream.view.RoundedImageView;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/*
* 修改密码*/
public class SettingAccountPWDActivity extends BaseAutoActivity {
    @BindView(R.id.userinfo_photo)
    RoundedImageView userinfoPhoto;
    @BindView(R.id.tv_pwd_now)
    EditText tvPwdNow;
    @BindView(R.id.tv_pwd_new)
    EditText tvPwdNew;
    @BindView(R.id.tv_pwd_repeat)
    EditText tvPwdRepeat;
    @BindView(R.id.login_enter)
    Button loginEnter;
    private XiRequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account_pwd);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        getBaseInfo();//获取用户信息显示头像
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

    @OnClick({R.id.login_enter, R.id.message_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_enter:
                loginEnter.setClickable(false);

                String PwdNow = tvPwdNow.getText().toString();
                if (TextUtils.isEmpty(PwdNow)) {
                    ToastUtils.show("请输入原始密码");
                    loginEnter.setClickable(true);
                    return;
                }
                String PwdNew = tvPwdNew.getText().toString();
                if (TextUtils.isEmpty(PwdNew)) {
                    ToastUtils.show("请输入新密码");
                    loginEnter.setClickable(true);
                    return;
                }
                if (PwdNew.length() < 6) {
                    ToastUtils.show("密码不能小于6位");
                    loginEnter.setClickable(true);
                    return;
                }
                String PwdRepeat = tvPwdRepeat.getText().toString();
                if (TextUtils.isEmpty(PwdRepeat)) {
                    ToastUtils.show("请再次输入输入密码");
                    loginEnter.setClickable(true);
                    return;
                }
                if (!PwdNew.equals(PwdRepeat)) {
                    ToastUtils.show("两次密码输入不一致");
                    loginEnter.setClickable(true);
                    return;
                }
                if (NetworkUtil.isNetworkAvailable()) {
                    LoadingAnimDialog.showLoadingAnimDialog(this);
                    changePassword(PwdNow, PwdRepeat);
                }
                loginEnter.setClickable(true);
                break;
            case R.id.message_back:
                finish();
                break;
        }
    }

    /**
     * 修改密碼
     *
     * @param old_pwd     原密码
     * @param changed_pwd 修改之后密码
     */
    private void changePassword(String old_pwd, String changed_pwd) {
        params.addCallBack(this);
        params.changePassword(SPUtils.getString(Constants.MOBILE_PHONE, ""), old_pwd, changed_pwd);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

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
            case "changePassword"://重置密码
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
                int str=UserUtils.getInstance(SettingAccountPWDActivity.this).getOrgAreaID();
                set.add("orgArea_" +str);//绑定 校区（Tag）
                JPushInterface.deleteTags(SettingAccountPWDActivity.this, 0, set);
                JPushInterface.deleteAlias(SettingAccountPWDActivity.this, 0);
                startActivity(new Intent(SettingAccountPWDActivity.this, LoginActivity.class));
                AppManager.INSTANCE.finishAllActivity();

                LoadingAnimDialog.dismissLoadingAnimDialog();
            }
        });
        builder.create(1).show();
    }

}
