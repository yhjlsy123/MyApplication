package com.zhuochi.hydream.bathhousekeeper.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.ImageLoadUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;
import com.zhuochi.hydream.bathhousekeeper.view.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*修改密码*/
public class ChangePasswActivity extends BaseActivity {


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


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private XiRequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passw);
//        highApiEffects();
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        initData();
    }

    private void initData() {
        toolbarTitle.setText("修改密码");
        String Avatar = SPUtils.getString("Avatar", "");
        ImageLoadUtils.loadImage(this, Avatar, R.mipmap.about_logo,userinfoPhoto);

    }

    @OnClick({R.id.toolbar_back, R.id.login_enter})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.login_enter:
                chengePwd();
                break;
        }
    }

    //修改密码
    private void chengePwd() {
        String pwdNow = tvPwdNow.getText().toString().trim();
        String pwdNew = tvPwdNew.getText().toString().trim();
        String pwdRepeat = tvPwdRepeat.getText().toString().trim();
        if (TextUtils.isEmpty(pwdNow) || TextUtils.isEmpty(pwdNew) || TextUtils.isEmpty(pwdRepeat)) {
            ToastUtils.show("新旧密码不能为空");
            return;
        }

        if (!pwdNew.equals(pwdRepeat)) {
            ToastUtils.show("新密码不一致");
            return;
        }
        params.addCallBack(this);
        params.changePassword(SPUtils.getInt(Constants.USER_ID, 0), pwdNow, pwdNew);
    }


    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        ToastUtils.show("密码修改成功");
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        super.onRequestFailure(tag, s);
    }
}
