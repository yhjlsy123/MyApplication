package com.isgala.xishuashua.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.bean_.ContactEntity;
import com.isgala.xishuashua.bean_.UserInfoEntity;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.ui.FeedBackActivity;
import com.isgala.xishuashua.ui.H5Activity;
import com.isgala.xishuashua.ui.RankingActivity;
import com.isgala.xishuashua.ui.RecordLog;
import com.isgala.xishuashua.ui.ResetPwdActivity;
import com.isgala.xishuashua.ui.SettingActivity;
import com.isgala.xishuashua.ui.UserInfo;
import com.isgala.xishuashua.ui.WalletActivity;
import com.isgala.xishuashua.utils.ImageLoadUtils;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.isgala.xishuashua.view.RoundedImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.isgala.xishuashua.api.Neturl.STATION_CONTACT;
import static com.isgala.xishuashua.config.BathHouseApplication.mApplicationContext;

/**
 * 侧拉菜单
 * Created by and on 2016/11/3.
 */

public class DrawerContent extends Fragment {
    private static final String TAG = "DrawerContent";
    @BindView(R.id.drawer_photo)
    RoundedImageView drawerPhoto;
    @BindView(R.id.drawer_nikename)
    TextView drawerNikename;
    @BindView(R.id.drawer_info_authe)
    ImageView drawerInfoAuthe;
    private View content;
    private DrawerLayout mDrawer;
    private Intent intent;
    private static String CALL_CONTACT = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        content = inflater.inflate(R.layout.drawer_home, null);
        ButterKnife.bind(this, content);
        initView();
        getContactMaster();
        return content;
    }

    private void initView() {
        String userinfo = SPUtils.getString(Constants.USER_INFO, "");
        if (!TextUtils.isEmpty(userinfo)) {
            UserInfoEntity userInfoEntity = JsonUtils.parseJsonToBean(userinfo, UserInfoEntity.class);
            if (userInfoEntity != null && userInfoEntity.data != null) {
                ImageLoadUtils.loadImage(mApplicationContext, drawerPhoto, userInfoEntity.data.photo);
                drawerNikename.setText(userInfoEntity.data.nickname);
                if (TextUtils.equals("1", userInfoEntity.data.auth)) {
                    drawerInfoAuthe.setVisibility(View.VISIBLE);
                }
            }
        }
        HashMap<String, String> map = new HashMap<>();
        VolleySingleton.post(Neturl.USER_INFO, "userinfo", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                UserInfoEntity userInfoEntity = JsonUtils.parseJsonToBean(result, UserInfoEntity.class);
                if (userInfoEntity != null && userInfoEntity.data != null) {
                    SPUtils.saveString(Constants.USER_INFO, result);
                    ImageLoadUtils.loadImage(mApplicationContext, drawerPhoto, userInfoEntity.data.photo);
                    drawerNikename.setText(userInfoEntity.data.nickname);
                    if (TextUtils.equals("1", userInfoEntity.data.auth)) {
                        drawerInfoAuthe.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    /**
     * 获取  站长联系方式
     */
    private void getContactMaster() {
        Map<String, String> map = new HashMap<>();
        map.put("s_id", SPUtils.getString(Constants.S_ID, ""));
        VolleySingleton.postNoDEVICETOKEN(STATION_CONTACT, "station_contact", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                ContactEntity result1 = JsonUtils.parseJsonToBean(result, ContactEntity.class);
                if (result1 != null && result1.data != null) {
                    CALL_CONTACT = result1.data.getM_tel();
//                    if (result1.data.status.equals("1")){
//                        ToastUtils.show(result1.data.msg);
//                        LoadingAnimDialog.dismissLoadingAnimDialog();
//                        Intent intent=new Intent();
//                        intent.putExtra("newPhone",newPhone);
//                        setResult(101,intent);
//                        finish();
//                    }else {
//                        LoadingAnimDialog.dismissLoadingAnimDialog();
//                        ToastUtils.show(result1.data.msg);
//                    }
                } else {
                    CALL_CONTACT = SPUtils.getString("kefu", "");
                }
            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
//                loginCode.setClickable(true);
            }
        });
    }

    /**
     * 绑定侧拉菜单
     *
     * @param drawer
     */
    public void setUp(DrawerLayout drawer) {
        mDrawer = drawer;
    }

    @OnClick({R.id.drawer_money, R.id.drawer_record, R.id.drawer_setting, R.id.drawer_rank, R.id.drawer_enter_info, R.id.drawer_resetpwd, R.id.drawer, R.id.drawer_yijianfankui, R.id.drawer_changjianwenti, R.id.setting_contact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawer_money:
                startActivity(new Intent(getContext(), WalletActivity.class));
                break;
            case R.id.drawer_record:
                startActivity(new Intent(getContext(), RecordLog.class));
                break;
            case R.id.drawer_yijianfankui:
                startActivity(new Intent(getContext(), FeedBackActivity.class));
                break;
            case R.id.drawer_setting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.drawer_rank:
                startActivity(new Intent(getContext(), RankingActivity.class));
                break;
            case R.id.drawer_enter_info:
                startActivityForResult(new Intent(getContext(), UserInfo.class), 100);
                break;
            case R.id.drawer_changjianwenti:
                Intent intent = new Intent(getContext(), H5Activity.class);
                intent.putExtra("title", "常见问题");
                intent.putExtra("url", Neturl.USER_GUIDE);
                startActivity(intent);
                break;
            case R.id.drawer_resetpwd:
                startActivity(new Intent(getContext(), ResetPwdActivity.class));
                break;
            case R.id.setting_contact://联系站长
                callPhone();
                break;
        }
    }

    private void callPhone() {
        try {
            String number = CALL_CONTACT;
            if (number == null || number.isEmpty()) {
                number = SPUtils.getString("kefu", "");
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) {
            if (data.getBooleanExtra("change", false)) {//判断是否需要更新头像和昵称
                String photo = data.getStringExtra("photo");
                if (!TextUtils.isEmpty(photo)) {
                    ImageLoadUtils.loadImage(getActivity(), drawerPhoto, photo);
                }
                String nikename = data.getStringExtra("nikename");
                if (!TextUtils.isEmpty(nikename)) {
                    drawerNikename.setText(nikename);
                }
            }
        }
    }
}
