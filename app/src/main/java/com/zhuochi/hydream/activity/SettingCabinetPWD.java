package com.zhuochi.hydream.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.dialog.ResetEquipmentPWD;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.UserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by and on 2016/12/8.
 */

public class SettingCabinetPWD extends BaseAutoActivity {
//    boolean first = true;/*是否首次设置设备密码*/

    @BindView(R.id.passw_now)
    EditText passwNow;
    @BindView(R.id.passw_creat)
    EditText passwCreat;
    @BindView(R.id.passw_sure)
    EditText passwSure;
    @BindView(R.id.setting_pwd_commit)
    Button settingPwdCommit;
    @BindView(R.id.lin_passw_now)
    LinearLayout linPasswNow;
    @BindView(R.id.line_passw_now)
    View linePasswNow;
    XiRequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pwd);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
//        getBaseInfo();//获取个人信息，判断是否设置过设备密码
        initData();
    }

    //获取个人信息
    private void getBaseInfo() {
        params.addCallBack(this);
        params.getUserBaseInfo(UserUtils.getInstance(this).getMobilePhone());
    }

    // 根据是否是首次设置密码，选择显示与不显示填写当前密码布局
    private void initData() {
//        if (first) {
//            linePasswNow.setVisibility(View.GONE);
//            linPasswNow.setVisibility(View.GONE);
//        } else {
//            linePasswNow.setVisibility(View.VISIBLE);
//            linPasswNow.setVisibility(View.VISIBLE);
//        }
    }

    @OnClick({R.id.line_back, R.id.setting_pwd_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.line_back:
                onBackPressed();
                finish();
                break;
            case R.id.setting_pwd_commit:
                commit();
//                showResetEquipmentPWDDialog();
                break;
        }
    }

    //提交密码，判断密码是否符合规范
    private void commit() {
        String secondPwd = passwCreat.getText().toString().trim();
        if (TextUtils.isEmpty(secondPwd) || secondPwd.length() < 8) {
            ToastUtils.show("请输入8-10位密码创建密码");
            return;
        }
        String thirdPwd = passwSure.getText().toString().trim();
        if (TextUtils.isEmpty(thirdPwd) || thirdPwd.length() < 8) {
            ToastUtils.show("请再次输入8-10位密码");
            return;
        }
        if (!TextUtils.equals(thirdPwd, secondPwd)) {
            ToastUtils.show("两次密码输入不一致");
            return;
        }
        //根据是否是首次设置密码，选择设置密码或者修改密码
//        if (first) {
            setDevicePwd(thirdPwd);
//        } else {
//            String firstPwd = passwNow.getText().toString().trim();
//            if (TextUtils.isEmpty(firstPwd) || firstPwd.length() < 8) {
//                ToastUtils.show("请输入8-10位密码原始密码");
//                return;
//            }
//            changeDevicePwd(firstPwd, thirdPwd);
//        }

    }

    //修改成功后显示提示窗口
    public synchronized void showResetEquipmentPWDDialog() {
        ResetEquipmentPWD.Builder builder = new ResetEquipmentPWD.Builder(this);
//        if (first) {
            builder.create(1).show();
//        } else {
//            builder.create(2).show();
//        }

    }


    /**
     * setDevicePwd
     *
     * @todo 设置设备密码
     * @params string $devicePwd 设备密码
     */
    private void setDevicePwd(String pwd) {
        params.addCallBack(this);
        params.setDevicePwd(pwd);
    }

    /**
     * changeDevicePwd
     *
     * @todo 修改设备密码
     * @params string $originalDevicePwd 原始设备密码
     * @params string $inputDevicePwd 要修改的设备密码
     */
    private void changeDevicePwd(String originalDevicePwd, String inputDevicePwd) {
        params.addCallBack(this);
        params.changeDevicePwd(originalDevicePwd, inputDevicePwd);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "setDevicePwd":
                showResetEquipmentPWDDialog();
                break;
//            case "changeDevicePwd":
//                showResetEquipmentPWDDialog();
//                break;
//            case "getUserBaseInfo":
//                Map map = (Map) result.getData().getData();
//                try {
//                    String gson = GsonUtils.parseMapToJson(map);
//                    UserInfoEntity mEntity = new Gson().fromJson(gson, UserInfoEntity.class);
//                    if (!mEntity.getDevice_pwd().isEmpty()) {//判断是否设置过设备密码
//                        first = false;
//                        initData();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
        }
        super.onRequestSuccess(tag, result);
    }
}
