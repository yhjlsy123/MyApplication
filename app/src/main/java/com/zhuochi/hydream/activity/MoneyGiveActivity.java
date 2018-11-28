package com.zhuochi.hydream.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.adapter.GiveAdapter;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.dialog.PicVrcodeDialog;
import com.zhuochi.hydream.entity.GiveEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.CashierInputFilter;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.UserUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
    @BindView(R.id.recycler_give)
    ListView recyclerGive;
    @BindView(R.id.line_view)
    View lineView;
    @BindView(R.id.img_give)
    ImageView imgGive;
    @BindView(R.id.tv_phone_last)
    TextView tvPhoneLast;
    private String mPhoneNum;
    private Handler handler;
    private String TAG = "LoginQuickActivity";
    private String mType, MoneyNum;
    private XiRequestParams params;
    List<GiveEntity> gives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give);
        ButterKnife.bind(this);
        handler = new Handler();
        MoneyNum = getIntent().getStringExtra("MoneyNum");
//        txt_balance.setText(MoneyNum + "元");
        InputFilter[] inputFilter = {new CashierInputFilter()};
        txt_inputmoney.setFilters(inputFilter);
        params = new XiRequestParams(this);
        String phone = UserUtils.getInstance(this).getMobilePhone();
        String lastNumber = phone.substring(7, phone.length());
        tvPhoneLast.setText("请输入尾号为" + lastNumber + "的手机接收到的验证码");
    }

    /*获取转赠类型*/
    private void getGiveType() {
        params.addCallBack(this);
        params.allowTransferType(UserUtils.getInstance(this).getMobilePhone());
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "allowTransferType"://获取转赠类型
                gives = new ArrayList<>();
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                if (jsonArray.size() > 0) {
                    gives = JSON.parseArray(JSON.toJSONString(jsonArray), GiveEntity.class);
                } else {
                    ToastUtils.show(result.getData().getMsg());
                }
                window_giveType(recyclerGive, gives);
                break;
            case "transfer"://
                // 转赠结果
                ToastUtils.show(result.getData().getMsg());
                LoadingAnimDialog.dismissLoadingAnimDialog();
                finish();
                break;
            case "sendSMSCode"://获取验证码
                loginInputCode.requestFocus();
                countdown();
                ToastUtils.show(result.getData().getMsg());
                break;
        }
        super.onRequestSuccess(tag, result);
    }

    /*渲染 recyclerView
    * RecyclerView recyclerView 控件
    * List<GiveEntity> objects 传入集合
    * */
    private void window_giveType(ListView recyclerView, List<GiveEntity> objects) {
        GiveAdapter giveAdapter = new GiveAdapter(this, objects);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(giveAdapter);
        giveAdapter.setItemClickListener(new GiveAdapter.ItemClickListener() {
            @Override
            public void change(String item, String account, String name) {
                txt_selectType.setText(name);
                txt_balance.setText(account + "元");
                mType = item;
                recyclerGive.setVisibility(View.GONE);
                lineView.setVisibility(View.GONE);
                imgGive.setImageResource(R.mipmap.wallet_arrow);
            }
        });
//        giveAdapter.setonItemClicklistener(new GiveAdapter.onItemClicklistener() {
//            @Override
//            public void ItemClick(int positon) {
//                GiveEntity entity = gives.get(positon);
//                txt_selectType.setText(entity.getValue());
//                txt_balance.setText(entity.getAccount() + "元");
//                mType=entity.getKey();
//                recyclerGive.setVisibility(View.GONE);
//                lineView.setVisibility(View.GONE);
//                imgGive.setImageResource(R.mipmap.wallet_arrow);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick({R.id.login_code, R.id.login_enter, R.id.txt_select_type, R.id.pay_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_code:
                loginCode.setClickable(false);
                if (NetworkUtil.isNetworkAvailable()) {// 当网络可用时开始倒计时,获取短信
                    String phone = SPUtils.getString(Constants.MOBILE_PHONE, "");
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
                    ToastUtils.show("请输入转账金额");
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
                    setTransfer(mPhoneNum, mVerificationCode);
                }
                loginEnter.setClickable(true);
                break;
            case R.id.txt_select_type://请选择赠送类型
                if (recyclerGive.getVisibility() == View.GONE) {
                    getGiveType();
                    recyclerGive.setVisibility(View.VISIBLE);
                    lineView.setVisibility(View.VISIBLE);
                    imgGive.setImageResource(R.mipmap.xuanzexiaoqu_xiala);
                } else {
                    recyclerGive.setVisibility(View.GONE);
                    lineView.setVisibility(View.GONE);
                    imgGive.setImageResource(R.mipmap.wallet_arrow);
                }
                break;
            case R.id.pay_back:
                finish();
                break;
        }
    }

    /**
     * 用户转赠自己的资金到其他账号
     */
    private void setTransfer(String mobile, String code) {
        String money = txt_inputmoney.getText().toString();
        if (mType.isEmpty()) {
            return;
        }
        params.addCallBack(this);
        params.transfer(UserUtils.getInstance(this).getMobilePhone(), mType, money, mobile, code);
//        Map<String, String> map = new HashMap<>();
//        map.put("mobile", mobile);//被转手机号
//        map.put("type", mType);//转账类型
//        map.put("money", txt_inputmoney.getText().toString());//转账金额
//        map.put("vrcode", code);//手机验证码
//        map.put("uuid", SPUtils.getString("uuid", ""));//唯一设备id
//        VolleySingleton.post(TRANSFER_ACCOUNTS, "transfer_accounts", map, new VolleySingleton.CBack() {
//            @Override
//            public void onRequestSuccess(String result) {
//                Result result1 = JsonUtils.parseJsonToBean(result, Result.class);
//                if (result1 != null && result1.data != null) {
//                    ToastUtils.show(result1.data.msg);
//                }
//                LoadingAnimDialog.dismissLoadingAnimDialog();
//            }
//        });
    }


    /**
     * 获取短信验证码
     *
     * @param phoneNumber
     */
    public void getVerificationCode(final String phoneNumber, String picCode) {
        params.addCallBack(this);
        params.getSendsmsCode(phoneNumber, "login");
//        Map<String, String> params = new HashMap<>();
//        params.put("phone", phoneNumber);
//        params.put("uuid", SPUtils.getString("uuid", ""));
//        if (TextUtils.isEmpty(picCode)) {
//            picCode = "";
//        }
//        params.put("captcha", picCode);
//        params.put("type", "login");
//        VolleySingleton.postNoDEVICETOKEN(Neturl.GET_LOGIN_CODE, "getcode", params, new VolleySingleton.CBack() {
//            @Override
//            public void onRequestSuccess(String result) {
//                Loginsms loginsms = JsonUtils.parseJsonToBean(result, Loginsms.class);
//                if (loginsms != null && loginsms.data != null) {
//                    loginInputCode.requestFocus();
//                    countdown();
//                    ToastUtils.show(loginsms.data.message);
//                } else {
//                    showPicVrcodeDialog(phoneNumber);
//                    loginCode.setClickable(true);
//                    LogUtils.e(TAG, "日志:短信发送已达限制");
//                }
//            }
//        }, new VolleySingleton.ErrorBack() {
//            @Override
//            public void onRequestError(String result) {
//                loginCode.setClickable(true);
//                ToastUtils.show("获取验证码失败，请检查网络状况");
//            }
//
//        });
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

//        VolleySingleton.postNoDEVICETOKEN(Neturl.GET_PIC_CODE, "get_pic_code", params, new VolleySingleton.CBack() {
//            @Override
//            public void onRequestSuccess(String result) {
//                Loginsms loginsms = JsonUtils.parseJsonToBean(result, Loginsms.class);
//                if (loginsms != null && loginsms.data != null) {
//                    getNetWorkBitMap(loginsms.data.images, iv);
//                }
//            }
//        }, null);
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
                    loginCode.setText("获取验证码");
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
        String telRegex = "[1][3456789]\\d{9}";// "[1]"代表第1位为数字1，"[34758]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
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