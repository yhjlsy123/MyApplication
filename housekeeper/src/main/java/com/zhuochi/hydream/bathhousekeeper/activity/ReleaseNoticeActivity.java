package com.zhuochi.hydream.bathhousekeeper.activity;

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

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.bean.DeviceTypBean;
import com.zhuochi.hydream.bathhousekeeper.bean.MsgTypeBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.DevicePram;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;
import com.zhuochi.hydream.bathhousekeeper.utils.Constant;
import com.zhuochi.hydream.bathhousekeeper.utils.JumpUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;
import com.zhuochi.hydream.bathhousekeeper.view.HeightSelectView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布公告
 *
 * @author Cuixc
 * @date on  2018/9/7
 */

public class ReleaseNoticeActivity extends BaseActivity implements HeightSelectView.SelcetCallBack {


    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_menu)
    ImageView toolbarMenu;
    @BindView(R.id.toolbar_menu_tv)
    TextView toolbarMenuTv;
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
    @BindView(R.id.txt_deviceType_label)
    TextView txtDeviceTypeLabel;
    @BindView(R.id.msg_type)
    TextView msgType;
    @BindView(R.id.msg_container)
    RelativeLayout msgContainer;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.txt_deviceType)
    TextView txtDeviceType;
    @BindView(R.id.push_content)
    EditText pushContent;
    @BindView(R.id.push_continer)
    RelativeLayout pushContiner;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.conten_detail)
    EditText contenDetail;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.comit)
    Button comit;
    private XiRequestParams params;
    private HeightSelectView mHeightSelectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_notice);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        toolbarTitle.setText("发布公告");
        mHeightSelectView = new HeightSelectView(this, this);

    }

    @OnClick({R.id.toolbar_back, R.id.linear_select_school, R.id.msg_container, R.id.comit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.linear_select_school:
                Intent intent = new Intent(this, SchoolSelectAcitvity.class);
                DevicePram dpr = new DevicePram();
                dpr.setActivity(ReleaseNoticeActivity.class);
                dpr.setRequestCode(1);
                intent.putExtra(Constant.intentPram, dpr);
                startActivityForResult(intent, 1);
                break;
            case R.id.comit:
                comitReq();
                break;

            case R.id.msg_container:
                JumpUtils.jumpAcitvityOnePram(ReleaseNoticeActivity.this, MsgTypeActivity.class, null);
                break;

        }
    }


    @Override
    public void CallBackSelect(int org_id, int org_area_id, int boothroom_id, String device_key, String StartTime, String EndTime) {
        int id = org_id;
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
        if (TextUtils.isEmpty(tvSchool.getTag(R.id.tv_school).toString())) {
            return;
        }
        if (TextUtils.isEmpty(tvCampus.getTag(R.id.tv_campus).toString())) {
            return;
        }
        if (TextUtils.isEmpty( msgType.getTag(R.id.msg_type).toString())) {
            return;
        }
        if (checkbox.isChecked()) {
            parm.put("publish", 1);

        } else {
            parm.put("publish", 2);
        }
        parm.put("org_id", tvSchool.getTag(R.id.tv_school).toString());
        parm.put("org_area_id", tvCampus.getTag(R.id.tv_campus));
        parm.put("msg_sort", msgType.getTag(R.id.msg_type).toString());
        parm.put("title", editName.getText().toString());
        parm.put("describe", contenDetail.getText().toString());
        parm.put("content", pushContent.getText().toString());
        params.comnRequest("NoticeApi/putOutBullentin", parm);
    }


    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        super.onRequestSuccess(tag, result);
        if (result.getData().getCode() == 200) {
            ToastUtils.show(result.getData().getMsg());
            finish();
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
                        MsgTypeBean item = (MsgTypeBean) data.getParcelableExtra("data");
                        msgType.setText(item.getType_name());
                        msgType.setTag(R.id.msg_type, item.getId());
                        break;
                }
                break;
        }

    }

}
