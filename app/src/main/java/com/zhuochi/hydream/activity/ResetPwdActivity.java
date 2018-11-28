package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改密码
 * Created by Administrator on 2018/3/19.
 */

public class ResetPwdActivity extends BaseAutoActivity {

    @BindView(R.id.user_bathe)
    RelativeLayout userBathe;//洗澡密码吹风机密码
    @BindView(R.id.user_account_number)
    RelativeLayout userAccountNumber;//  账号密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.line_back, R.id.user_bathe, R.id.user_account_number})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.line_back:
                finish();
                break;
            case R.id.user_bathe:
                startActivity(new Intent(ResetPwdActivity.this, SettingCabinetPWD.class));
                break;
            case R.id.user_account_number:
                startActivity(new Intent(ResetPwdActivity.this, SettingAccountPWDActivity.class));
                break;
        }
    }
}
