package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.JetPlayer;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseActivity;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.config.AppManager;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.UserInfoEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.UserUtils;
import com.zhuochi.hydream.view.RoundedImageView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户信息界面
 */
public class UserActivity extends BaseAutoActivity {

    @BindView(R.id.userinfo_photo)
    RoundedImageView userinfoPhoto;
    @BindView(R.id.info_authe)
    ImageView infoAuthe;
    @BindView(R.id.user_img)
    RelativeLayout userImg;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.tv_autograph)
    TextView tvAutograph;
    @BindView(R.id.line_name)
    LinearLayout lineName;
    @BindView(R.id.tv_userInfo)
    TextView tvUserInfo;
    @BindView(R.id.tv_userSchoolData)
    TextView tvUserSchoolData;
    @BindView(R.id.tv_userID)
    TextView tvUserID;
    @BindView(R.id.tv_userConsumptionLog)
    TextView tvUserConsumptionLog;
    @BindView(R.id.tv_userMyWallet)
    TextView tvUserMyWallet;
    @BindView(R.id.tv_userCommonProblem)
    TextView tvUserCommonProblem;
    @BindView(R.id.tv_userOpinion)
    TextView tvUserOpinion;
    @BindView(R.id.tv_userRestPwd)
    TextView tvUserRestPwd;
    @BindView(R.id.tv_userCallAdmin)
    TextView tvUserCallAdmin;
    private XiRequestParams xiparams;
    private UserInfoEntity mEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        xiparams = new XiRequestParams(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getBaseInfo();
    }

    private void getBaseInfo() {
        xiparams.addCallBack(this);
        xiparams.getUserBaseInfo(UserUtils.getInstance(this).getMobilePhone());
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getUserBaseInfo":
                Map map = (Map) result.getData().getData();
                try {
                    String gson = GsonUtils.parseMapToJson(map);
                    mEntity = new Gson().fromJson(gson, UserInfoEntity.class);
                    SPUtils.saveString("nickName", mEntity.getUser_nickname()); //token 唯一标识
                    SPUtils.saveString("Avatar", mEntity.getAvatar()); //token 唯一标识
                    setData(mEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "contactManager"://获取站长信息
                String mobile = ((LinkedTreeMap) result.getData().getData()).get("mobile").toString();
                callPhone(mobile);
                break;
        }
        super.onRequestSuccess(tag, result);
    }

    //给界面set值
    private void setData(UserInfoEntity entity) {

        tvUserName.setText(entity.getUser_nickname());
        if (TextUtils.isEmpty(tvUserName.getText().toString())) {
            String phone = SPUtils.getString(Constants.MOBILE_PHONE, "");
            tvUserName.setText(phone);
        }
        String singnature = entity.getSignature();//获取个性签名
        if (singnature.length() > 15) {
            String line1 = singnature.substring(0, 15);
            String line2 = singnature.substring(15, singnature.length());
            singnature = line1 + "\n" + line2;
        }
        tvAutograph.setText(singnature);
        if (!entity.getAvatar().isEmpty()) {
            ImageLoadUtils.loadImage(this, userinfoPhoto, entity.getAvatar());
        }
    }

    @OnClick({R.id.line_name, R.id.tv_setting, R.id.tv_Ranking_list, R.id.tv_userInfo, R.id.tv_userSchoolData, R.id.user_back, R.id.userinfo_photo,
            R.id.tv_userID, R.id.tv_userConsumptionLog, R.id.tv_userMyWallet, R.id.tv_userCommonProblem, R.id.tv_userOpinion, R.id.tv_userRestPwd, R.id.tv_userCallAdmin, R.id.line_autograph})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userinfo_photo:
            case R.id.line_autograph:
            case R.id.user_img:
            case R.id.line_name://个人信息
                Intent intent = new Intent(this, UserInfoActivity.class);
                //若获取当前基础信息不成功 不能跳转下一步
                if (mEntity != null) {
                    intent.putExtra("TokenEntity", new Gson().toJson(mEntity));
                    startActivityForResult(intent, 101);
                }
                break;
            case R.id.tv_setting://设置界面
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.tv_Ranking_list://排行榜
                startActivity(new Intent(this, RankingActivity.class));
                break;
            case R.id.tv_userInfo://个人信息
           /*     try {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(this, Uri.parse(" http://other.web.ri01.sycdn.kuwo.cn/resource/n2/96/19/2142954236.mp3"));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    mediaPlayer.stop();

                } catch (Exception e) {

                }
*/
                Uri uri = Uri.parse("http://other.web.ri01.sycdn.kuwo.cn/resource/n2/96/19/2142954236.mp3");
                Ringtone mRingtone = RingtoneManager.getRingtone(this, uri);
                mRingtone.play();

                Intent intent1 = new Intent(UserActivity.this, UserInfoActivity.class);
                if (mEntity != null) {
                    intent1.putExtra("TokenEntity", new Gson().toJson(mEntity));
                    startActivityForResult(intent1, 101);
                    mRingtone.stop();
                }


                break;
            case R.id.tv_userSchoolData://校园资料
                startActivity(new Intent(this, CampusInformationActivity.class));
                break;
            case R.id.tv_userID://认证信息
                startActivity(new Intent(this, OneCardInfoActivity.class));
                break;
            case R.id.tv_userConsumptionLog://消费记录
                startActivity(new Intent(this, RecordLog.class));
                break;
            case R.id.tv_userMyWallet://我的钱包
                startActivity(new Intent(this, WalletActivity.class));
                break;
            case R.id.tv_userCommonProblem://常见问题
                startActivity(new Intent(this, CommonProblemActivity.class));
                break;
            case R.id.tv_userOpinion://意见反馈

                startActivity(new Intent(this, FeedBackActivity.class));
                break;
            case R.id.tv_userRestPwd://重置密码
                startActivity(new Intent(this, ResetPwdActivity.class));
                break;
            case R.id.tv_userCallAdmin://联系站长
                xiparams.addCallBack(this);
                xiparams.getContactManager(UserUtils.getInstance(this).getMobilePhone());
                break;
            case R.id.user_back:
                finish();
                //防止多个接口finish掉全部activity 以下判断 重新启动新的activity
                if (!AppManager.INSTANCE.containActivity(HomeActivity.class)) {
                    startActivity(new Intent(UserActivity.this, HomeActivity.class));
                }
                break;
        }
    }


    /**
     * 拨打站长电话
     *
     * @param mobile 手机号
     */
    private void callPhone(String mobile) {
        try {
            if (mobile == null || mobile.isEmpty()) {
                mobile = SPUtils.getString("kefu", "");
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mobile));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && data != null) {
            String entity = data.getStringExtra("entity");
//            UserUpdateEntity userUpdateEntity=new Gson().fromJson(entity,UserUpdateEntity.class);
//            tvUserName.setText(userUpdateEntity.getUser_nickname());
//            tvAutograph.setText(userUpdateEntity.getSignature());
            getBaseInfo();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            //防止多个接口finish掉全部activity 以下判断 重新启动新的activity
            if (!AppManager.INSTANCE.containActivity(HomeActivity.class)) {
                startActivity(new Intent(UserActivity.this, HomeActivity.class));
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
