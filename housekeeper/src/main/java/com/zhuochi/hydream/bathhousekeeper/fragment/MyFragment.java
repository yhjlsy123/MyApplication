package com.zhuochi.hydream.bathhousekeeper.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.activity.AboutUsActivity;
import com.zhuochi.hydream.bathhousekeeper.activity.ChangePasswActivity;
import com.zhuochi.hydream.bathhousekeeper.activity.CommonProblemActivity;
import com.zhuochi.hydream.bathhousekeeper.activity.FeedBackActivity;
import com.zhuochi.hydream.bathhousekeeper.activity.LoginActivity;
import com.zhuochi.hydream.bathhousekeeper.activity.UserInfoActivity;
import com.zhuochi.hydream.bathhousekeeper.base.BaseFragment;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.RegisterEntity;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.GsonUtils;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.APKVersionCodeUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ImageLoadUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.UserUtils;
import com.zhuochi.hydream.bathhousekeeper.view.RoundedImageView;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*我的页面*/
public class MyFragment extends BaseFragment {

    @BindView(R.id.my_company_name)
    TextView myCompanyName;
    @BindView(R.id.my_company_icon)
    RoundedImageView myCompanyIcon;
    @BindView(R.id.apk_version)
    TextView apkVersion;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private XiRequestParams params;
    private RegisterEntity mRegisterEntity;
    private int USERINFO = 101;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == view)
            view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, view);
        params = new XiRequestParams(getActivity());
        initData();
        return view;
    }

    private void initData() {
        params.addCallBack(this);
        params.getUserBaseInfoRequest(SPUtils.getInt(Constants.USER_ID, 0));
        toolbarBack.setVisibility(View.GONE);
        toolbarTitle.setText("我的");

        apkVersion.setText(APKVersionCodeUtils.getVerName(getActivity()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getUserBaseInfo":
                Map map = (Map) result.getData().getData();
                String gson = GsonUtils.parseMapToJson(map);
                mRegisterEntity = JSON.parseObject(gson, RegisterEntity.class);
                SPUtils.saveString("Avatar", mRegisterEntity.getAvatar());
                myCompanyName.setText(mRegisterEntity.getUser_login());
                ImageLoadUtils.loadImage(getActivity(), mRegisterEntity.getAvatar(), R.mipmap.ic_launcher, myCompanyIcon);
                break;
        }
        super.onRequestSuccess(tag, result);
    }

    @OnClick({R.id.my_company_edit, R.id.user_info, R.id.change_paswd, R.id.user_common_problem, R.id.user_Opinion, R.id.aboutus, R.id.version_now, R.id.exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_info:
            case R.id.my_company_edit://跳转到个人信息页面
                if (mRegisterEntity != null) {
                    Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                    intent.putExtra("entity", new Gson().toJson(mRegisterEntity));
                    startActivityForResult(intent, USERINFO);
                } else {
                    startActivity(new Intent(getActivity(), UserInfoActivity.class));
                }
                break;
            case R.id.change_paswd:
                startActivity(new Intent(getActivity(), ChangePasswActivity.class));
                break;
            case R.id.user_common_problem:
                startActivity(new Intent(getActivity(), CommonProblemActivity.class));
                break;
            case R.id.user_Opinion:
                startActivity(new Intent(getActivity(), FeedBackActivity.class));
                break;
            case R.id.aboutus:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            case R.id.version_now:
                break;
            case R.id.exit:
                showDialog();
                break;


        }
    }


    private void showDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
//            normalDialog.setTitle("我是一个普通Dialog");
        normalDialog.setMessage("确认退出吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO JG 解除极光推送绑定
                        Set<String> set = new HashSet<String>();
//                        int str=UserUtils.getInstance(this).getOrgAreaID();
//
//                        set.add("orgArea_" + str);//绑定 校区（Tag）
//                        JPushInterface.deleteTags(SettingActivity.this, 0, set);
//                        JPushInterface.deleteAlias(SettingActivity.this, 0);
                        UserUtils.setDataNull();

                        SPUtils.saveString("nickName", "");
//                        SPUtils.saveInt(Constants.ORG_ID, 0);//学校ID
//                        SPUtils.saveInt(Constants.ORG_AREA_ID, 0);//校区ID
//                        SPUtils.saveInt(Constants.BUILDING_ID, 0);//楼层ID
//                        SPUtils.saveInt(Constants.DEVICE_AREA_ID, 0);//当前绑定区域（浴室）
                        if (SPUtils.celerData()) {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        }

                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
