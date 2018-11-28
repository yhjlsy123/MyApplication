package com.zhuochi.hydream.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 一卡通认证
 *
 * @author Cuixc
 * @date on  2018/5/31
 */

public class OneCardActivity extends BaseAutoActivity {
    @BindView(R.id.dorm_number)
    EditText dormNumber;
    @BindView(R.id.input_pwd)
    EditText inputPwd;
    XiRequestParams params;
    @BindView(R.id.line_success)
    LinearLayout lineSuccess;
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_onecard);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);

    }

    @OnClick({R.id.userinfo_cancle, R.id.btn_onclick})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.userinfo_cancle:
                finish();
                break;
            case R.id.btn_onclick:

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                String onkarNumber = dormNumber.getText().toString();
                if (TextUtils.isEmpty(onkarNumber)) {
                    ToastUtils.show("一卡通账号不能为空");
                    return;
                }
                String onkarPwd = inputPwd.getText().toString();
                if (TextUtils.isEmpty(onkarPwd)) {
                    ToastUtils.show("密码不能为空");
                    return;
                }
                setAuthentication(onkarNumber, onkarPwd);
                break;
        }
    }

    /**
     * 一卡通认证
     *
     * @param cardNumber 账号
     * @param cardPwd    密码
     */
    private void setAuthentication(String cardNumber, String cardPwd) {
        String phone = SPUtils.getString(Constants.MOBILE_PHONE, "");
        params.addCallBack(this);
        params.validateIdNo(phone, cardNumber, cardPwd);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "validateIdNo":
                if (result.getData().getCode() == 200) {
                    lineSuccess.setVisibility(View.VISIBLE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 3000);

                }else {
                    ToastUtils.show(result.getData().getMsg());
                }
                break;
        }

        super.onRequestSuccess(tag, result);
    }
}
