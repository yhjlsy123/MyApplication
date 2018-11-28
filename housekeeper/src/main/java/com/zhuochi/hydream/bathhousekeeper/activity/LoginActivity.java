package com.zhuochi.hydream.bathhousekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.EntitySumUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;
import com.zhuochi.hydream.bathhousekeeper.view.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*登录*/
public class LoginActivity extends BaseActivity {

    @BindView(R.id.my_company_icon)
    RoundedImageView myCompanyIcon;
    @BindView(R.id.user_account)
    EditText userAccount;
    @BindView(R.id.user_password)
    EditText userPassword;
    @BindView(R.id.back_password)
    TextView backPassword;
    @BindView(R.id.line_content)
    LinearLayout lineContent;
    @BindView(R.id.tv_quickLogin)
    TextView tvQuickLogin;
    @BindView(R.id.activity_login)
    RelativeLayout activityLogin;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private XiRequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
//        highApiEffects();
    }

    @OnClick({R.id.back_password, R.id.tv_quickLogin, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_password:
                startActivity(new Intent(this, LoginBackActivity.class));
                break;
            case R.id.tv_quickLogin:
                startActivity(new Intent(this, LoginQuickActivity.class));
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(userAccount.getText().toString())) {
                    ToastUtils.show("用户名不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(userPassword.getText().toString())) {
                    ToastUtils.show("密码不能为空！");
                    return;
                }
                LoginRequest(userAccount.getText().toString(), userPassword.getText().toString());
                break;
        }
    }

    private void LoginRequest(String name, String pwd) {

        params.addCallBack(this);
//        params.LoginRequest("admin@2018","admin");
        params.LoginRequest(pwd,name);

    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "loginWidthPwd":
                //ToastUtils.show(result.getData().getMsg());
                EntitySumUtils.LoginSubmit(result, this);
                //startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }

    @Override
    public void onRequestFailure(String tag, Object s) {

        super.onRequestFailure(tag, s);
    }
}
