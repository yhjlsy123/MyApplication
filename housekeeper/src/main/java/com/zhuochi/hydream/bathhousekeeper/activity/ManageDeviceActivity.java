package com.zhuochi.hydream.bathhousekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.zxing.client.android.CaptureActivity;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.bean.DeviceEditBean;
import com.zhuochi.hydream.bathhousekeeper.entity.ManageDeviceEntity;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.GsonUtils;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;
import com.zhuochi.hydream.bathhousekeeper.utils.JumpUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.UserUtils;
import com.zhuochi.hydream.bathhousekeeper.view.FlowRadioGroup;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 洗浴管理
 *
 * @author Cuixc
 * @date on  2018/8/16
 */

public class ManageDeviceActivity extends BaseActivity {


    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_menu)
    ImageView toolbarMenu;
    @BindView(R.id.toolbar_menu_tv)
    TextView toolbarMenuTv;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_bathroomName)
    TextView tvBathroomName;
    @BindView(R.id.tv_uuid)
    TextView tvUuid;
    @BindView(R.id.tv_school)
    TextView tvSchool;
    @BindView(R.id.tv_campus)
    TextView tvCampus;
    @BindView(R.id.checkbox_man)
    RadioButton checkboxMan;
    @BindView(R.id.checkbox_girl)
    RadioButton checkboxGirl;
    @BindView(R.id.sex_group)
    FlowRadioGroup sexGroup;
    @BindView(R.id.edit_floor_number)
    TextView editFloorNumber;
    @BindView(R.id.edit_floor)
    TextView editFloor;
    @BindView(R.id.tv_guard)
    TextView tvGuard;
    @BindView(R.id.t_lablel)
    TextView tLablel;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_id_time)
    TextView tvIdTime;
    @BindView(R.id.editReason)
    EditText editReason;
    @BindView(R.id.s_wish)
    RadioButton sWish;
    @BindView(R.id.s_good)
    RadioButton sGood;
    @BindView(R.id.s_colse)
    RadioButton sColse;
    @BindView(R.id.s_maintain)
    RadioButton sMaintain;
    @BindView(R.id.s_del)
    RadioButton sDel;
    @BindView(R.id.s_group)
    FlowRadioGroup sGroup;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.uuid_container)
    LinearLayout uuidContainer;

    private XiRequestParams params;
    private String DeviceKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manage);
        ButterKnife.bind(this);
        toolbarTitle.setText("设备管理");
        params = new XiRequestParams(this);
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sexGroup.setClickable(false);

        sGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //        status状态(0期待中；1正常；2禁用；4维护；8删除)
                switch (checkedId) {
                    case R.id.s_colse:
                        save.setTag(R.id.save, 2);
                        break;
                    case R.id.s_del:
                        save.setTag(R.id.save, 8);
                        break;
                    case R.id.s_good:
                        save.setTag(R.id.save, 1);
                        break;
                    case R.id.s_wish:
                        save.setTag(R.id.save, 0);
                        break;
                    case R.id.s_maintain:
                        save.setTag(R.id.save, 4);
                        break;
                }
            }
        });
        getListData();
    }

    private void getListData() {

        DeviceKey = getIntent().getStringExtra("DeviceKey");
        params.addCallBack(this);
        Map<String, Object> pram = Common.intancePram();
        pram.put("user_id", UserUtils.getInstance(this).getUserID());
        pram.put("device_key", DeviceKey);

        params.orgAreaDeviceEdit(UserUtils.getInstance(this).getUserID(), DeviceKey);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "orgAreaDeviceEdit":
                Map map = (Map) result.getData().getData();
                String gson = GsonUtils.parseMapToJson(map);
                ManageDeviceEntity entity = JSON.parseObject(gson, ManageDeviceEntity.class);
                setDate(entity);
                break;


            case "Org/orgAreaDeviceEditSave":
                if (result.getData().getCode() == 200) {
                    ToastUtils.show(result.getData().getMsg());
                    setResult(1);
                    finish();
                }
                break;
        }
        super.onRequestSuccess(tag, result);
    }

    private void setDate(ManageDeviceEntity entity) {
        tvBathroomName.setText(entity.getBathroom());
        tvUuid.setText(entity.getUuid());
        tvSchool.setText(entity.getOrg_name());
        tvCampus.setText(entity.getOrg_area_name());
        editFloorNumber.setText(entity.getBuilding_number());
        editFloor.setText(entity.getFloor() + "");
        editReason.setText(entity.getMaintenance_tip());
        tvGuard.setText(entity.getNum() + "");
        if (entity.getGender().equals("女")) {
            checkboxGirl.setChecked(true);
        } else {
            checkboxMan.setChecked(true);
        }
        if (null != entity.getService_time_1_id()) {
            tvTime.setText(entity.getService_time_1_id().getDaily_start_time() + "- - -" + entity.getService_time_1_id().getDaily_end_time());
        }
        if (null != entity.getService_time_2_id()) {
            tvIdTime.setText(entity.getService_time_2_id().getDaily_start_time() + "- - -" + entity.getService_time_2_id().getDaily_end_time());

        }

//        status状态(0离线；1正常；2禁用；4维护；8删除)
        switch (new BigDecimal(entity.getStatus()).intValue()) {
            case 0:
                sWish.setChecked(true);
                break;
            case 1:
                sGood.setChecked(true);
                break;
            case 2:
                sColse.setChecked(true);
                break;
            case 4:
                sMaintain.setChecked(true);
                break;
            case 8:
                sDel.setChecked(true);
                break;

        }


    }

    private void comit() {
        Map<String, Object> pram = Common.intancePram();
        pram.put("user_id", UserUtils.getInstance(this).getUserID());
        pram.put("device_key", DeviceKey);

        Map<String, Object> deviceInfo = new HashMap<String, Object>();
        if (null == save.getTag(R.id.save)) {
            return;

        }
        if (null != tvTime.getTag(R.id.tv_time)) {
            deviceInfo.put("service_time_1_id", tvTime.getTag(R.id.tv_time));
        }
        if (null != tvIdTime.getTag(R.id.tv_id_time)) {
            deviceInfo.put("service_time_2_id", tvIdTime.getTag(R.id.tv_id_time));
        }
        if (!(TextUtils.isEmpty(tvUuid.getText().toString()))) {
            deviceInfo.put("uuid", tvUuid.getText().toString());
        }

        deviceInfo.put("status", save.getTag(R.id.save) + "");
        pram.put("deviceInfo", deviceInfo);
        params.comnRequest("Org/orgAreaDeviceEditSave", pram);

    }

    @OnClick({R.id.tv_time, R.id.tv_id_time, R.id.save, R.id.uuid_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_time:
                JumpUtils.jumpAcitvityOnePram(this, DeviceEditTimeAcitivity.class, null);
                tvUuid.setTag(R.id.tv_uuid, 0);
                break;
            case R.id.tv_id_time:
                JumpUtils.jumpAcitvityOnePram(this, DeviceEditTimeAcitivity.class, null);
                tvUuid.setTag(R.id.tv_uuid, 1);
                break;
            case R.id.save:
                comit();
                break;
            case R.id.uuid_container:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, 2);
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                DeviceEditBean itemTime = (DeviceEditBean) data.getParcelableExtra("data");
                switch (resultCode) {
                    case 100:
                        switch ((int) tvUuid.getTag(R.id.tv_uuid)) {
                            case 0:
                                tvTime.setText(itemTime.getDaily_start_time() + "- - -" + itemTime.getDaily_end_time());
                                tvTime.setTag(R.id.tv_time, itemTime.getId());
                                break;
                            case 1:
                                tvIdTime.setText(itemTime.getDaily_start_time() + "- - -" + itemTime.getDaily_end_time());
                                tvIdTime.setTag(R.id.tv_id_time, itemTime.getId());
                                break;
                        }
                        break;
                }
                break;
            case 2:
                switch (resultCode) {
                    case 222:
                        String uuid = data.getStringExtra("rQcode");
                        tvUuid.setText(uuid);
                        break;
                }
                break;
        }
    }
}
