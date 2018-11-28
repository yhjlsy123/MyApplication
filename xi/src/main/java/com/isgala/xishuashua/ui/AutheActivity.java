package com.isgala.xishuashua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.config.AppManager;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.VolleySingleton;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by and on 2016/11/26.
 */

public class AutheActivity extends BaseAutoActivity {
    @BindView(R.id.authe_name)
    EditText autheName;
    @BindView(R.id.authe_number)
    EditText autheNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authe);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.authe_back, R.id.authe_enter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.authe_back:
                check();
                finish();
                break;
            case R.id.authe_enter:
                authe();
                break;
        }
    }

    //如果栈里不包含HomeActivity 便启动一个(说明是第一次启动)
    private void check() {
        if (!AppManager.INSTANCE.containActivity(HomeActivity.class)) {
            startActivity(new Intent(AutheActivity.this, HomeActivity.class));
        }
    }

    private void authe() {
        String name = autheName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.show("请输入姓名");
            return;
        }
        String number = autheNumber.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            ToastUtils.show("请输入学号");
            return;
        }
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
        HashMap<String, String> map = new HashMap<>();
        map.put("realname", name);
        map.put("student_number", number);
        VolleySingleton.post(Neturl.CHECK_AUTHE, "auth", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Result result1 = JsonUtils.parseJsonToBean(result, Result.class);
                if (result1 != null && result1.data != null) {
                    ToastUtils.show(result1.data.msg);
                    check();
                    finish();
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        });
    }
}
