package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.TipDialog2;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 认证信息
 *
 * @author Cuixc
 * @date on  2018/5/31
 */

public class OneCardInfoActivity extends BaseAutoActivity {

    XiRequestParams params;
    boolean authentication = false;//用户一卡通是否认证
    @BindView(R.id.tv_phoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_oneCard)
    TextView tvOneCard;

    @BindView(R.id.bt_authentication)
    TextView btAuthentication;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_onecard_data);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        btAuthentication.setTag(R.id.bt_authentication, 0);
        if (!SPUtils.getString(Constants.MOBILE_PHONE, "").isEmpty()) {
            tvPhoneNumber.setText(SPUtils.getString(Constants.MOBILE_PHONE, ""));
            getOtherAuthInfo();
        }
    }

    /*获取认证信息*/
    private void getOtherAuthInfo() {
        params.addCallBack(this);
        params.getOtherAuthInfo(SPUtils.getString(Constants.MOBILE_PHONE, ""));
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getOtherAuthInfo":
                if (result.getData().getCode() == 100) {
                    btAuthentication.setTag(R.id.bt_authentication, 0);
                    btAuthentication.setText("未认证");
                }
                if (result.getData().getCode() == 200) {
                    ToastUtils.show(result.getData().getMsg());
                    Map map1 = (Map) result.getData().getData();
                    String carName = map1.get("idcard").toString();
                    tvOneCard.setText(carName);
                    btAuthentication.setTag(R.id.bt_authentication, 1);
                    btAuthentication.setClickable(true);
                    btAuthentication.setBackgroundResource(R.drawable.selector_bg_corner_blue);
                    btAuthentication.setText("解绑");
                }
                break;
            case "MemberApi/unbindAuth":
                if (result.getData().getCode() == 200) {
                    btAuthentication.setTag(R.id.bt_authentication, 0);
                    ToastUtils.show(result.getData().getMsg());
                    tvOneCard.setText(null);
                    btAuthentication.setText("未认证");
                }
                break;


        }
        super.onRequestSuccess(tag, result);
    }

    @OnClick({R.id.bt_authentication, R.id.bt_updatephone_number, R.id.userinfo_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userinfo_cancle:
                finish();
                break;
            case R.id.bt_updatephone_number:
                startActivity(new Intent(this, UpdatePhoneNumberActivity.class));
                break;
            case R.id.bt_authentication:
                int au = (int) btAuthentication.getTag(R.id.bt_authentication);
                switch (au) {
                    case 0:
                        startActivity(new Intent(this, OneCardActivity.class));
                        break;
                    case 1:
                        TipDialog2.show_(this, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                releaseCard();
                            }
                        }, "友情提示", "确定要解绑此一卡通吗?");
                        break;
                }

                break;
        }
    }

    private void releaseCard() {
        params.addCallBack(this);
        Map<String, Object> pram = new HashMap<String, Object>();
        pram.put("mobile", SPUtils.getString(Constants.MOBILE_PHONE, ""));
        pram.put("type", "4");
        params.comonRequest(pram, "MemberApi/unbindAuth");
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!SPUtils.getString(Constants.MOBILE_PHONE, "").isEmpty()) {
            tvPhoneNumber.setText(SPUtils.getString(Constants.MOBILE_PHONE, ""));
            getOtherAuthInfo();
        }
    }
}
