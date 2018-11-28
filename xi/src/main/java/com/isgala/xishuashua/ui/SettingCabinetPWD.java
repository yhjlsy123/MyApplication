package com.isgala.xishuashua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.umeng.analytics.MobclickAgent;


import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by and on 2016/12/8.
 */

public class SettingCabinetPWD extends BaseAutoActivity {
    @BindView(R.id.setting_pwd_input)
    EditText settingPwdInput;
    @BindView(R.id.setting_pwd_inputagain)
    EditText settingPwdInputagain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pwd);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rank_back, R.id.setting_pwd_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rank_back:
                onBackPressed();
                finish();
                break;
            case R.id.setting_pwd_commit:
                commit();
                break;
        }
    }

    private void commit() {
        String firstPwd = settingPwdInput.getText().toString().trim();
        if (TextUtils.isEmpty(firstPwd) || firstPwd.length() < 8) {
            ToastUtils.show("请输入8-10位密码");
            return;
        }
        String secondPwd = settingPwdInputagain.getText().toString().trim();
        if (TextUtils.isEmpty(secondPwd) || secondPwd.length() < 8) {
            ToastUtils.show("请再次输入8-10位密码");
            return;
        }
        if (!TextUtils.equals(firstPwd, secondPwd)) {
            ToastUtils.show("两次密码输入不一致");
            return;
        }
        setUpdateBathe(firstPwd);
    }

    /**
     * 重置洗澡密码接口
     *
     * @param firstPwd
     */
    private void setUpdateBathe(String firstPwd) {
        HashMap<String, String> map = new HashMap<>();
        map.put("ppwd", firstPwd);
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
        VolleySingleton.post(Neturl.SET_LOCK_PWD, "set_pwd", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Result resultBean = JsonUtils.parseJsonToBean(result, Result.class);
                if (resultBean != null && resultBean.data != null) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("envent", " set pwd ");
                    MobclickAgent.onEventValue(SettingCabinetPWD.this, "setpwd", hashMap, 991);
                    ToastUtils.show(resultBean.data.msg);
                    finish();
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        });
    }

}
