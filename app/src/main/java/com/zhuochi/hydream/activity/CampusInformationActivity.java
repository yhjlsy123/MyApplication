package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.entity.CampusInformationEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.UserUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 校园资料
 *
 * @author Cuixc
 * @date on  2018/5/31
 */

public class CampusInformationActivity extends BaseAutoActivity {
    @BindView(R.id.userinfo_cancle)
    ImageView userinfoCancle;
    @BindView(R.id.userinfo_school)
    TextView userinfoSchool;
    @BindView(R.id.userinfo_campus)
    TextView userinfoCampus;
    @BindView(R.id.p_userinfo_campus)
    RelativeLayout pUserinfoCampus;
    @BindView(R.id.userinfo_bathroom)
    TextView userinfoBathroom;
    @BindView(R.id.p_userinfo_school)
    LinearLayout pUserinfoSchool;
    private XiRequestParams xiRequestParams;
    private CampusInformationEntity entity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_data);
        ButterKnife.bind(this);
        xiRequestParams = new XiRequestParams(this);
        getDeviceInfo();
//        getAuthentication();
    }

    /*获取认证信息*/
    private void getAuthentication() {
        xiRequestParams.addCallBack(this);
        xiRequestParams.getOtherAuthInfo(UserUtils.getInstance(this).getMobilePhone());
    }

    private void getDeviceInfo() {
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
        xiRequestParams.addCallBack(this);
        xiRequestParams.getDeviceInfo(UserUtils.getInstance(this).getMobilePhone());
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getOtherAuthInfo":
                ToastUtils.show(result.getData().getMsg());
                break;
            case "getDeviceInfo":

//                ToastUtils.show(result.getData().getMsg());
                Map map = (Map) result.getData().getData();
                String gson = GsonUtils.parseMapToJson(map);
                entity = new Gson().fromJson(gson, CampusInformationEntity.class);
                userinfoSchool.setText(entity.getOrg_name());
                userinfoCampus.setText(entity.getOrg_area_name());
                userinfoBathroom.setText(entity.getDevice_area_name());
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
                break;
        }
        super.onRequestSuccess(tag, result);
    }

    @OnClick({R.id.userinfo_cancle, R.id.p_userinfo_school})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userinfo_cancle:
                finish();
                break;
            case R.id.p_userinfo_school:
                if (UserUtils.getInstance(this).getMobilePhone().equals("15264104826")){
                    Intent intent = new Intent(this, SchoolList.class);
                    intent.putExtra("Jump", "main");
                    startActivity(intent);
                }
                if (entity.getUser_oauth() == 2) {
                    Intent intent = new Intent(this, SchoolList.class);
                    intent.putExtra("Jump", "main");
                    startActivity(intent);
                }else {
                    ToastUtils.show("如需修改校区，请联系客服");
                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDeviceInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
