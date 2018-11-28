package com.zhuochi.hydream.bathhousekeeper.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.Base;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.bean.DeviceTypBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.dialog.SelectTimeDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.DevicePram;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;
import com.zhuochi.hydream.bathhousekeeper.utils.Constant;
import com.zhuochi.hydream.bathhousekeeper.utils.JumpUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;
import com.zhuochi.hydream.bathhousekeeper.view.datapicker.DialogAlertListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布活动
 *
 * @author Cuixc
 * @date on  2018/9/7
 */

public class ReleaseActivity extends BaseActivity {
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.txt_school)
    TextView txtSchool;
    @BindView(R.id.tv_school)
    TextView tvSchool;
    @BindView(R.id.tv_campus)
    TextView tvCampus;
    @BindView(R.id.linear_select_school)
    RelativeLayout linearSelectSchool;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.txt_deviceType)
    TextView txtDeviceType;
    @BindView(R.id.linear_select_type)
    RelativeLayout linearSelectType;
    @BindView(R.id.edit_discount)
    EditText editDiscount;
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.relese)
    Button relese;
    private XiRequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        ButterKnife.bind(this);
        toolbarTitle.setText("发布活动");
        params = new XiRequestParams(this);

    }

    @OnClick({R.id.toolbar_back, R.id.linear_select_type, R.id.relese, R.id.linear_select_school, R.id.tv_startTime, R.id.tv_endTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.linear_select_type:
                JumpUtils.jumpAcitvityOnePram(ReleaseActivity.this, DeviceTypeActivity.class, null);
                break;
            case R.id.relese:
                params.addCallBack(this);
                comitReq();
                break;

            case R.id.tv_startTime:
                new SelectTimeDialog(ReleaseActivity.this, new DialogAlertListener() {
                    @Override
                    public void onDialogOk(Dialog dlg, String date1, String date2) {
                        tvStartTime.setText(date2);
                        dlg.dismiss();
                    }

                    @Override
                    public void onDialogCancel(Dialog dlg) {

                    }
                }).show();
                break;
            case R.id.tv_endTime:
                new SelectTimeDialog(ReleaseActivity.this, new DialogAlertListener() {
                    @Override
                    public void onDialogOk(Dialog dlg, String date1, String date2) {
                        tvEndTime.setText(date2);
                        dlg.dismiss();
                    }

                    @Override
                    public void onDialogCancel(Dialog dlg) {

                    }
                }).show();
                break;


            case R.id.linear_select_school:
                Intent intent = new Intent(this, SchoolSelectAcitvity.class);
                DevicePram dpr = new DevicePram();
                dpr.setActivity(ReleaseActivity.class);
                dpr.setRequestCode(1);
                intent.putExtra(Constant.intentPram, dpr);
                startActivityForResult(intent, 1);
                break;
        }
    }

    private void comitReq() {
        Map<String, Object> parm = Common.intancePram();
        parm.put("user_id", SPUtils.getInt(Constants.USER_ID, 0));
        if (TextUtils.isEmpty(editName.getText().toString())) {
            return;
        }
        if (TextUtils.isEmpty(txtDeviceType.getText().toString())) {
            return;
        }
        if (TextUtils.isEmpty(editDiscount.getText().toString())) {
            return;
        }
        if (TextUtils.isEmpty(tvStartTime.getText().toString())) {
            return;
        }
        if (TextUtils.isEmpty(tvEndTime.getText().toString())) {
            return;
        }
        if (TextUtils.isEmpty(tvSchool.getTag(R.id.tv_school).toString())) {
            return;
        }
        if (TextUtils.isEmpty(tvCampus.getTag(R.id.tv_campus).toString())) {
            return;
        }
        if (TextUtils.isEmpty(editDiscount.getText().toString())) {
            return;
        }
        Map<String, Object> acitivityInfor = new HashMap<String, Object>();
        if (checkbox.isChecked()) {
            acitivityInfor.put("status", 1);

        } else {
            acitivityInfor.put("status", 2);
        }
        acitivityInfor.put("activity_name", editName.getText().toString());
        acitivityInfor.put("client_org_id", tvSchool.getTag(R.id.tv_school));
        acitivityInfor.put("device_type_key", txtDeviceType.getText().toString());
        acitivityInfor.put("discount_factor", editDiscount.getText().toString());
        acitivityInfor.put("start_time", tvStartTime.getText().toString());
        acitivityInfor.put("end_time", tvEndTime.getText().toString());
        parm.put("activityInfo", acitivityInfor);
        params.comnRequest("Activity/add", parm);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        super.onRequestSuccess(tag, result);
        switch ("Activity/add") {
            case "Activity/add":
                if (result.getData().getCode() == 200) {
                    ToastUtils.show(result.getData().getMsg());
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                switch (resultCode) {
                    case 100:
                        DevicePram dpr = (DevicePram) data.getParcelableExtra("data");
                        tvCampus.setTag(R.id.tv_campus, dpr.getOrg_area_id());
                        tvSchool.setTag(R.id.tv_school, dpr.getOrg_id());
                        tvCampus.setText(dpr.getOrg_area_name());
                        tvSchool.setText(dpr.getOrg_name());
                        break;
                }
                break;
            case 100:
                switch (resultCode) {
                    case 100:
                        DeviceTypBean item = (DeviceTypBean) data.getParcelableExtra("data");
                        txtDeviceType.setText(item.getName_text());
                        txtDeviceType.setTag(R.id.txt_deviceType, item.getName());
                        break;
                }
                break;
        }

    }


}
