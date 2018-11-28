package com.isgala.xishuashua.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.bean_.Location;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.bean_.WaitORService;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.dialog.TipDialog;
import com.isgala.xishuashua.dialog.TipDialog2;
import com.isgala.xishuashua.receiver.MessageReceiver;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.LogUtils;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.Tools;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * 等待服务
 * Created by and on 2016/11/10.
 */

public class WaitServiceFragment extends BaseHomeFragment {
    @BindView(R.id.wait_time)
    TextView waitTime;
    @BindView(R.id.wait_bathroomid)
    TextView waitBathroomid;
    @BindView(R.id.wait_bathroomlocation)
    TextView waitBathroomlocation;
    @BindView(R.id.wait_password)
    TextView waitPassword;
    @BindView(R.id.wait_tip1)
    TextView waitTip1;
    @BindView(R.id.wait_tip2)
    TextView waitTip2;
    @BindView(R.id.wait_hide_pwd)
    CheckBox waitHidePwd;
    private final String TAG = "WaitServiceFragment";
    @BindView(R.id.wait_service_root)
    LinearLayout waitServiceRoot;
    private MessageReceiver messageReceiver = new MessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            if (intent != null) {
                String action = intent.getStringExtra("action");
                if (TextUtils.equals("jump", action)) {   //跳转到服务中界面
                    LogUtils.e(TAG, "-------- jump serving -----------------");
                    waitOrServicing(map.get("id"));
                } else if (TextUtils.equals("back", action)) {
                    LogUtils.e(TAG, "-------- jump list -----------------");
                    if (bindresult()) {
                        getHomeContent().switchOrigin(false);
                    }
                }
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wait, null);
        ButterKnife.bind(this, view);
        return view;
    }

    private HashMap<String, String> map;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean isRegister;

    /**
     * "result":"1",
     * "msg":"操作成功",
     * "id":"2",
     * "position":"9号楼3层男浴室",
     * "num":"2",
     * "password":"123456"
     */
    @Override
    public void initView() {
        super.initView();
        map = new HashMap<>();
        Bundle arguments = getArguments();
        if (arguments != null) {
            WaitORService.WaitOrServiceBean bean = (WaitORService.WaitOrServiceBean) arguments.getSerializable("bean");
            map.put("id", bean.id);
            IntentFilter intentFilter = new IntentFilter(Constants.SERVICESTART);
            getContext().registerReceiver(messageReceiver, intentFilter);
            isRegister = true;
            startCountDown(Tools.toInt(bean.countdown));
            waitBathroomid.setText(bean.num);
            waitBathroomlocation.setText(bean.position);
            waitPassword.setText(bean.password);
            if (bean.tips.size() > 0) {
                waitTip1.setText(bean.tips.get(0));
            }
            if (bean.tips.size() > 1) {
                waitTip2.setText(bean.tips.get(1));
            }
            waitServiceRoot.setVisibility(View.VISIBLE);//设置布局可见
            LogUtils.e(TAG, " Receive register success ");
        } else {
            LogUtils.e(TAG, " bundle is null ");
        }
        map.put("type", "1");
        waitHidePwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    waitPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置密码为隐藏的
                    waitPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        LoadingTrAnimDialog.dismissLoadingAnimDialog();
    }

    /**
     * 更新视图
     *
     * @param arguments
     */
    public void updateView(Bundle arguments) {
        if (arguments != null) {
            WaitORService.WaitOrServiceBean bean = (WaitORService.WaitOrServiceBean) arguments.getSerializable("bean");
            map.put("id", bean.id);
            startCountDown(Tools.toInt(bean.countdown));
            waitBathroomid.setText(bean.num);
            waitBathroomlocation.setText(bean.position);
            waitPassword.setText(bean.password);
            if (bean.tips.size() > 0) {
                waitTip1.setText(bean.tips.get(0));
            }
            if (bean.tips.size() > 1) {
                waitTip2.setText(bean.tips.get(1));
            }
            waitServiceRoot.setVisibility(View.VISIBLE);//设置布局可见
        }
        LoadingTrAnimDialog.dismissLoadingAnimDialog();
    }

    /**
     * 更新待服务或者切换服务中
     *
     * @param id
     */
    private void waitOrServicing(final String id) {
        final Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(id)) {
            map.put("id", id);
        }
        LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
        VolleySingleton.post(Neturl.APPOINT_STATUS, "appoint", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                WaitORService waitORService = JsonUtils.parseJsonToBean(result, WaitORService.class);
                if (waitORService != null && waitORService.data != null) {
                    if (TextUtils.equals("1", waitORService.data.vstatus)) {//待服务
                        if (waitBathroomid == null)
                            return;
                        map.put("id", waitORService.data.id);
                        startCountDown(Tools.toInt(waitORService.data.countdown));
                        waitBathroomid.setText(waitORService.data.num);
                        waitBathroomlocation.setText(waitORService.data.position);
                        waitPassword.setText(waitORService.data.password);
                    } else {
                        //切换到服务中
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id);
                        bundle.putSerializable("bean", waitORService.data);
                        if (bindresult())
                            getHomeContent().switchServicing(bundle);
                    }
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    /**
     * 开始倒计时
     *
     * @param time
     */
    private synchronized void startCountDown(int time) {
        this.time = time;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        countdown();
    }

    private int time;

    private synchronized void countdown() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (time > 0) {
                    time--;
                    countdown();
                } else {
                    if (waitTime != null) {
                        waitTime.setText("");
                        if (bindresult()) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getHomeContent().obtainLocation();
                                }
                            }, 3000);

                        }
                    }
                }
            }
        }, 1000);
        waitTime.setText(Tools.formatTime(time));
    }

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

        if (messageReceiver != null && isRegister) {
            getContext().unregisterReceiver(messageReceiver);
            isRegister = false;
        }
        super.onDestroy();
    }


    @OnClick({R.id.wait_cancel, R.id.wait_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wait_cancel:
                Dialog dialog = TipDialog2.show_(getContext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        cancel();
                        isobtain();
                    }
                }, "提示信息", "确认要取消本次服务吗？");
                break;
            case R.id.wait_confirm:
                startService();
                break;
        }
    }

    //*开启服务*/
    private void startService() {
        LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
        VolleySingleton.post(Neturl.REQUEST_START, "request_start", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Result result1 = JsonUtils.parseJsonToBean(result, Result.class);
                if (result1 != null && result1.data != null) {
                    ToastUtils.show(result1.data.msg);
//                    if (bindresult()) {
//                        getHomeContent().switchOrigin(false);
                }
//                } else {
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
//                }
            }
        });
        //设置30秒后刷新一次   就一次多了不刷
        CountDownTimer timer = new CountDownTimer(10000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 倒计时过程中，millisUntilFinished不断变化中
                long sr = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                getHomeContent().obtainLocation();
            }
        };
        timer.start();
    }


    /**
     * 取消预约
     */
    private void cancel() {
        LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
        VolleySingleton.post(Neturl.CANCEL_APPOINT, "cancel", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Result result1 = JsonUtils.parseJsonToBean(result, Result.class);
                if (result1 != null && result1.data != null) {
                    ToastUtils.show(result1.data.msg);
                    if (bindresult()) {
                        getHomeContent().switchOrigin(false);
                    }
                } else {
                    LoadingTrAnimDialog.dismissLoadingAnimDialog();
                }
            }
        });
    }

    /**
     * 判断当前是否在服务中
     *
     * @return
     */
    public void isobtain() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("lat", SPUtils.getString(Constants.LOCATON_LAT, ""));
        map.put("lng", SPUtils.getString(Constants.LOCATON_LNG, ""));
        TelephonyManager TelephonyMgr = (TelephonyManager) getContext().getSystemService(TELEPHONY_SERVICE);
        String deviceId = TelephonyMgr.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = "";
        }
        WifiManager wm = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String mac = wm.getConnectionInfo().getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            map.put("mac", mac);
        }
        map.put("version", android.os.Build.VERSION.RELEASE);
        map.put("os", "0");
        map.put("imei", deviceId);
        map.put("uuid", SPUtils.getString("uuid", ""));
        map.put("type", android.os.Build.MODEL);// 设备型号
        VolleySingleton.postNoDEVICETOKEN(Neturl.INIT_APP, "initapp", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Location location = JsonUtils.parseJsonToBean(result, Location.class);
//                if (location != null && location.data != null) {
//                    SPUtils.saveString("uuid", location.data.uuid);
//                    SPUtils.saveString("kefu", location.data.kefu);
//                }
                if (location != null && location.data != null) {
                    String status = location.data.status;
                    if (TextUtils.equals("0", status) || TextUtils.equals("1", status) || TextUtils.equals("2", status)) {
                        cancel();
                    } else {
                        ToastUtils.show("该订单已开启水控机，无法取消");
                        //刷新当前界面
                        getHomeContent().obtainLocation();
                    }
                }


            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("WaitServiceFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart("WaitServiceFragment");
    }
}
