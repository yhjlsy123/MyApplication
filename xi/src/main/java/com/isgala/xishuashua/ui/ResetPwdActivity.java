package com.isgala.xishuashua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.base.BaseAutoActivity;

/**
 * 修改密码
 * Created by Administrator on 2018/3/19.
 */

public class ResetPwdActivity extends BaseAutoActivity {
    private RelativeLayout mUpdateBathe;//洗澡密码吹风机密码
    private RelativeLayout mUpdateAccountNumber;//  账号密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        initView();
    }
    void initView(){
        mUpdateBathe=(RelativeLayout) findViewById(R.id.user_bathe);
        mUpdateAccountNumber=(RelativeLayout) findViewById(R.id.user_account_number);
        ImageView back=(ImageView) findViewById(R.id.h5_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mUpdateBathe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPwdActivity.this, SettingCabinetPWD.class));
            }
        });
        mUpdateAccountNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改账号密码
                startActivity(new Intent(ResetPwdActivity.this,LoginBackPwdActivity.class));
            }
        });
    }


}
