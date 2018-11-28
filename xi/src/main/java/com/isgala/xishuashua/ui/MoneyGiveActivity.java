package com.isgala.xishuashua.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.LoginResult;
import com.isgala.xishuashua.bean_.Loginsms;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.config.AppManager;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingAnimDialog;
import com.isgala.xishuashua.dialog.PicVrcodeDialog;
import com.isgala.xishuashua.utils.CashierInputFilter;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.LogUtils;
import com.isgala.xishuashua.utils.NetworkUtil;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.umeng.analytics.MobclickAgent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.isgala.xishuashua.api.Neturl.TRANSFER_ACCOUNTS;


/**
 * 转账
 * Created by and on 2016/11/4.
 */

public class MoneyGiveActivity extends BaseAutoActivity {
    @BindView(R.id.txt_input_phone)
    EditText loginPhone;
    @BindView(R.id.login_input_code)
    EditText loginInputCode;
    @BindView(R.id.login_code)
    TextView loginCode;
    @BindView(R.id.login_enter)
    Button loginEnter;

    @BindView(R.id.txt_select_type)
    TextView txt_selectType;
    @BindView(R.id.txt_input_money)
    EditText txt_inputmoney;
    @BindView(R.id.txt_balance)
    TextView txt_balance;
    private String mPhoneNum;
    private Handler handler;
    private String TAG = "LoginActivity";
    private String mType, MoneyNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give);
        ButterKnife.bind(this);
        handler = new Handler();
        MoneyNum = getIntent().getStringExtra("MoneyNum");
        txt_balance.setText(MoneyNum + "元");
        InputFilter[] inputFilter={new CashierInputFilter()};
        txt_inputmoney.setFilters(inputFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && data != null) {
            String name = data.getStringExtra("name");
            mType = data.getStringExtra("id");
            txt_selectType.setText(name);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("LoginActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("LoginActivity");
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.login_code, R.id.login_enter, R.id.txt_select_type,R.id.pay_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_code:
                loginCode.setClickable(false);
                if (NetworkUtil.isNetworkAvailable()) {// 当网络可用时开始倒计时,获取短信
                    if (TextUtils.isEmpty(SPUtils.getString("uuid", ""))) {// uuid为空时，再次获取uuid
//                        obtainLocation();
                        return;
                    }
                    String phone=SPUtils.getString(Constants.PHONE_NUMBER,"");
                    getVerificationCode(phone, "");
                } else {
                    loginCode.setClickable(true);
                }
                break;
            case R.id.login_enter:
                if (txt_selectType.getText().toString().isEmpty()) {
                    ToastUtils.show("请选择赠送类型");
                    return;
                }
                if (txt_inputmoney.getText().toString().isEmpty()) {
                    ToastUtils.show("请输入赠款金额");
                    return;
                }

                mPhoneNum = loginPhone.getText().toString();
                if (TextUtils.isEmpty(mPhoneNum)) {
                    ToastUtils.show("请输入对方的手机号码");
                    return;
                }
                // 通过规则判断手机号
                if (!judgePhoneNums(mPhoneNum)) {
                    ToastUtils.show("手机号码有误，请核对后重新输入");
                    return;
                }
                loginEnter.setClickable(false);
                String mVerificationCode = loginInputCode.getText().toString();
                if (TextUtils.isEmpty(mVerificationCode)) {
                    ToastUtils.show("请输入验证码");
                    loginEnter.setClickable(true);
                    return;
                }
                if (NetworkUtil.isNetworkAvailable()) {
                    LoadingAnimDialog.showLoadingAnimDialog(this);
//                    login(mPhoneNum, mVerificationCode);
                    setTransfer(mPhoneNum,mVerificationCode);
                }
                loginEnter.setClickable(true);
                break;
            case R.id.txt_select_type://请选择赠送类型
                Intent intent = new Intent(new Intent(this, MoneyGiveTypeActivity.class));
                startActivityForResult(intent, 101);
                break;
            case R.id.pay_back:
                finish();
                break;
        }
    }

    /**
     * 用户转赠自己的资金到其他账号
     */
    private void setTransfer(String mobile,String code){
        Map<String,String> map=new HashMap<>();
        map.put("mobile",mobile);//被转手机号
        map.put("type",mType);//转账类型
        map.put("money",txt_inputmoney.getText().toString());//转账金额
        map.put("vrcode",code);//手机验证码
        map.put("uuid",SPUtils.getString("uuid", ""));//唯一设备id
        VolleySingleton.post(TRANSFER_ACCOUNTS, "transfer_accounts", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Result result1 = JsonUtils.parseJsonToBean(result, Result.class);
                if (result1 != null && result1.data != null) {
                    ToastUtils.show(result1.data.msg);
                }
                LoadingAnimDialog.dismissLoadingAnimDialog();
            }
        });
    }

    private void jump(LoginResult.DataBean data) {
        ToastUtils.show(data.message);
        String s_id = data.s_id;
        String campus_id = data.campus;
        SPUtils.saveString(Constants.OAUTH_TOKEN, data.oauth_token);
        SPUtils.saveString(Constants.OAUTH_TOKEN_SECRET, data.oauth_token_secret);
        SPUtils.saveString(Constants.S_ID, s_id);
        SPUtils.saveString(Constants.CAMPUS, campus_id);
        String device_type = data.device_type;
        String b_id = data.b_id;
        SPUtils.saveString(Constants.YB_ID, b_id);
        SPUtils.saveString(Constants.DEVICE_TYPE, device_type);
        Class nClass;
        if (TextUtils.isEmpty(s_id) || TextUtils.equals(s_id, "0")) {//1.判断是否有设置过学校信息
            nClass = SchoolList.class;
        } else if (TextUtils.isEmpty(campus_id) || TextUtils.equals(campus_id, "0")) {//2.判断是否设置过校区信息
            nClass = SchoolList.class;
        } else {
            nClass = HomeActivity.class;
        }
        SPUtils.saveBoolean(Constants.IS_LOGIN, true);
        Intent intent = new Intent(this, nClass);
        intent.putExtra("s_id", s_id);
        startActivity(intent);
        AppManager.INSTANCE.finishAllActivity();
        LoadingAnimDialog.dismissLoadingAnimDialog();
    }

    /**
     * 获取短信验证码
     *
     * @param phoneNumber
     */
    public void getVerificationCode(final String phoneNumber, String picCode) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phoneNumber);
        params.put("uuid", SPUtils.getString("uuid", ""));
        if (TextUtils.isEmpty(picCode)) {
            picCode = "";
        }
        params.put("captcha", picCode);
        params.put("type", "login");
        VolleySingleton.postNoDEVICETOKEN(Neturl.GET_LOGIN_CODE, "getcode", params, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Loginsms loginsms = JsonUtils.parseJsonToBean(result, Loginsms.class);
                if (loginsms != null && loginsms.data != null) {
                    loginInputCode.requestFocus();
                    countdown();
                    ToastUtils.show(loginsms.data.message);
                } else {
                    showPicVrcodeDialog(phoneNumber);
                    loginCode.setClickable(true);
                    LogUtils.e(TAG, "日志:短信发送已达限制");
                }
            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                loginCode.setClickable(true);
                ToastUtils.show("获取验证码失败，请检查网络状况");
            }
        });
    }

    /**
     * 显示图片验证码的弹窗
     */
    private void showPicVrcodeDialog(final String phoneNumber) {
        PicVrcodeDialog.Builder builder = new PicVrcodeDialog.Builder(this);
        builder.create().show();
        getPicCode(builder.getIvCode());
        builder.setVrCodeListener(new PicVrcodeDialog.VrCodeListener() {
            @Override
            public void refreshCode(ImageView ivCode) {
                getPicCode(ivCode);
            }

            @Override
            public void getUserInputCode(String userInputCode) {
                if (TextUtils.isEmpty(userInputCode))
                    return;
                getVerificationCode(phoneNumber, userInputCode);
            }
        });
    }

    /**
     * 获取图形验证码
     */
    private void getPicCode(final ImageView iv) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uuid", SPUtils.getString("uuid", ""));

        VolleySingleton.postNoDEVICETOKEN(Neturl.GET_PIC_CODE, "get_pic_code", params, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Loginsms loginsms = JsonUtils.parseJsonToBean(result, Loginsms.class);
                if (loginsms != null && loginsms.data != null) {
                    getNetWorkBitMap(loginsms.data.images, iv);
                }
            }
        }, null);
    }


    /**
     * 加载网络验证码
     *
     * @param picurl
     * @param iv
     */
    public void getNetWorkBitMap(final String picurl, final ImageView iv) {
        synchronized (String.class) {
            new Thread() {
                public void run() {
                    try {
                        URL url = new URL(picurl);
                        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                        uc.connect();
                        if (uc.getResponseCode() == 200) {
                            InputStream is = uc.getInputStream();
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            byte[] bytes = new byte[1024];
                            int length = -1;
                            while ((length = is.read(bytes)) != -1) {
                                bos.write(bytes, 0, length);
                            }
                            byte[] by = bos.toByteArray();
                            bos.close();
                            is.close();
                            setPicCode(by, iv);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }

    /**
     * 将流文件设置给图片控件
     *
     * @param iv
     * @throws IOException
     */
    public void setPicCode(final byte[] by, final ImageView iv) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeByteArray(by, 0, by.length);
                iv.setImageBitmap(bitmap);
            }
        });

    }


    private int time = 59;

    /**
     * 倒计时
     */
    private void countdown() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (time > 0) {
                    time--;
                    countdown();
                    loginCode.setClickable(false);
                } else {
                    loginCode.setText("再次获取验证码");
                    time = 59;
                    loginCode.setClickable(true);
                }
            }
        }, 999);
        loginCode.setText(String.format("%s S", time));
    }



    /**
     * 判断手机号码是否合理
     *
     * @return
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11) && isMobileNo(phoneNums)) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    private boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     *
     * @param phoneNums
     * @return
     */
    private boolean isMobileNo(String phoneNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][345789]\\d{9}";// "[1]"代表第1位为数字1，"[34758]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phoneNums))
            return false;
        else
            return phoneNums.matches(telRegex);
    }

    private void reset() {
        time = 59;
        handler.removeCallbacksAndMessages(null);
        loginCode.setClickable(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reset();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
