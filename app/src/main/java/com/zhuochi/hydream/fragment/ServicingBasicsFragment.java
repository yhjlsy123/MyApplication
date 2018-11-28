package com.zhuochi.hydream.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.activity.PayActivity;
import com.zhuochi.hydream.activity.UnpaidOrderActivity;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.LoadingSpecialDialog;
import com.zhuochi.hydream.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.dialog.StopServiceDialog;
import com.zhuochi.hydream.entity.InServiceEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.pushbean.InitSettingEntity;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.receiver.MessageReceiver;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.LogUtils;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.Tools;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.OnClick;

/**
 * 服务中 除水控机
 * Created by and on 2016/11/10.
 */

public class ServicingBasicsFragment extends BaseHomeFragment {

    private final String TAG = "ServicingFragment";
    private ImageView refreshImage;
    private View mView;
    private TextView servingLocation, servingTime, servingMoney, servingPwd;
    private CheckBox serviceHide;
    private XiRequestParams params;
    private String deviceType = "";
    private String uuid = "";
    private LinearLayout servicerefresh;
    private Button serviceStop;
    private String mPaymentStyle = "";
    private int time;
    //不能结束时
    private RelativeLayout linearLog;
    private TextView tipContent;
    private Button tipOk;

    private MessageReceiver messageReceiver = new MessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            LogUtils.e(TAG, "-------- jump order -----------------");
            try {
                if (intent != null) {
                    String order_id = intent.getStringExtra("order_id");
                    LogUtils.e("ServicingFragment", "order_id : " + order_id);
                    removeHandler();
                    if (TextUtils.isEmpty(order_id)) {
                        startActivity(new Intent(getActivity(), UnpaidOrderActivity.class));
                    }
                }
            } catch (Exception e) {
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                sendEmptyMessageDelayed(100, 995);
                time++;
                if (servingTime != null)
                    servingTime.setText(Tools.formatTime(time));
            } else if (msg.what == 120) {
                if (refreshImage != null)
                    refreshImage.clearAnimation();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_servicing_basics, null);
        params = new XiRequestParams(getActivity());
        IntentFilter intentFilter = new IntentFilter(Constants.SERVICEFINISH);
        getContext().registerReceiver(messageReceiver, intentFilter);
        initView();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();
//        Bundle arguments = getArguments();
        linearLog = (RelativeLayout) mView.findViewById(R.id.linear_log);
        tipContent = (TextView) mView.findViewById(R.id.tip1_content);
        tipOk = (Button) mView.findViewById(R.id.tip1_ok);
        tipOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mInitEntity.getServicePhone()));
                    startActivity(intent);
                } catch (Exception e) {

                }
            }
        });
        servingLocation = (TextView) mView.findViewById(R.id.serving_location);
        servingTime = (TextView) mView.findViewById(R.id.serving_time);
        servingMoney = (TextView) mView.findViewById(R.id.serving_money);
        servingPwd = (TextView) mView.findViewById(R.id.serving_pwd);
        serviceHide = (CheckBox) mView.findViewById(R.id.service_hide);
        refreshImage = (ImageView) mView.findViewById(R.id.refresh_image);
        servicerefresh = (LinearLayout) mView.findViewById(R.id.service_refresh);
        servicerefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bindresult()) {
                    dTime = System.currentTimeMillis();
                    LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
                    animation();
                    getHomeContent().exchangeMsg(1);
                }
            }
        });
        serviceStop = (Button) mView.findViewById(R.id.service_stop);
        serviceStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStopDialog();
            }
        });
        upView(null);
//        LoadingTrAnimDialog.dismissLoadingAnimDialog();
        serviceHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    servingPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置密码为隐藏的
                    servingPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }


    public void upView(Bundle bundle) {
//        if (servingTime != null && arguments != null) {
        bundle = getArguments();
        deviceType = bundle.getString("deviceType");
        uuid = bundle.getString("uuid");
        String startTime = bundle.getString("startTime");
        String serverTime = bundle.getString("serverTime");
//            String times = Tools.getDistanceTime(Long.valueOf(serverTime), Long.valueOf(startTime));
        String times = Common.formatTime(serverTime, startTime);
        InServiceEntity data = (InServiceEntity) bundle.getSerializable("bean");
        servingLocation.setText(data.getSub_devices().get(0));
        if (servingTime.getText().toString().isEmpty()) {
            time = Tools.toInt(times);
            servingTime.setText(Tools.formatTime(time) + "");
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessageDelayed(100, 995);
            }
        }
        String devicePwd = bundle.getString("devicePwd");

        servingPwd.setText(devicePwd);
        mPaymentStyle = bundle.getString("payment_style");
        if (!TextUtils.isEmpty(mPaymentStyle)) {
            if (mPaymentStyle.equals("prepayment")) {//已经扣费、预扣费
                //Todo 预扣费金额从后台获取
                servingMoney.setText("--");


            } else {
                servingMoney.setText("--");
            }
        }
//        } else {
//            LogUtils.e(TAG, "  bundle  is  null ");
//        }
        if (refreshImage != null) {
            long tempTime = System.currentTimeMillis() - dTime;
            if (handler != null)
                handler.sendEmptyMessageDelayed(120, 1000 - tempTime < 0 ? 0 : 1000 - tempTime);
        }

        if (null != deviceType && TextUtils.equals(deviceType, "blower")) {
            serviceStop.setVisibility(View.GONE);
        } else {
            serviceStop.setVisibility(View.VISIBLE);
        }

    }


    private long dTime;

    @OnClick({R.id.service_stop, R.id.service_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.service_stop:
                showStopDialog();
                break;
            case R.id.service_refresh:
                if (bindresult()) {
                    dTime = System.currentTimeMillis();
                    LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
                    animation();
                    getHomeContent().exchangeMsg(1);
                }
                break;
        }
    }

    private void animation() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_refresh);
        animation.setRepeatCount(100);
        refreshImage.startAnimation(animation);
    }


    /**
     * 弹出停止服务框
     */
    private synchronized void showStopDialog() {
        StopServiceDialog.Builder builder = new StopServiceDialog.Builder(getActivity());
        builder.setOnClickListener(new StopServiceDialog.RefundListener() {
            @Override
            public void refund() {
                stop();
            }
        });
        builder.create().show();
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "turnOff"://停止计费
//                ToastUtils.show(result.getData().getMsg());
                ToastUtils.show("系统确认中，请稍等");
                serviceStop.setClickable(false);
                serviceStop.setBackgroundResource(R.drawable.shape_bg_corner_gray999);
                if (bindresult()) {
                    getHomeContent().exchangeMsg(0);
                }
                break;
        }
        super.onRequestSuccess(tag, result);
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        switch (tag) {
            case "turnOff":

                break;
        }
        super.onRequestFailure(tag, s);
    }

    private TimerTask mTask;
    private InitSettingEntity mInitEntity;

    /**
     * 停止计费
     */
    private void stop() {
        LoadingSpecialDialog.showLoadingAnimDialog(getActivity());
        params.addCallBack(this);
        params.turnOff(uuid, deviceType);
        serviceStop.setClickable(false);
        serviceStop.setBackgroundResource(R.drawable.shape_bg_corner_gray999);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {//在子线程更新UI
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         *要执行的操作
                         */
                        LoadingSpecialDialog.dismissLoadingAnimDialog();
                        serviceStop.setClickable(true);
                        serviceStop.setBackgroundResource(R.drawable.selector_bg_corner_blue);
                    }
                });

            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 15000);//15秒后执行TimeTask的run方法
        //若服务器一直未响应
        mTask = new TimerTask() {
            @Override
            public void run() {//在子线程更新UI
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadingSpecialDialog.dismissLoadingAnimDialog();
                        if (linearLog != null) {
                            linearLog.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        };
        String mGson = SPUtils.getString("initSetting", "");
        if (!TextUtils.isEmpty(mGson)) {
            mInitEntity = new Gson().fromJson(mGson, InitSettingEntity.class);
            Timer timers = new Timer();
            timers.schedule(mTask, mInitEntity.getDeviceTimeout() * 1000);
            tipContent.setText(mInitEntity.getDeviceTimeoutEndTip());
        }

    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mTask != null) {
            mTask.cancel();
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mTask != null) {
            mTask.cancel();
        }
        super.onDestroyView();
    }

    /**
     * 移除计时
     */
    private synchronized void removeHandler() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onDestroy() {
        if (messageReceiver != null) {
            getContext().unregisterReceiver(messageReceiver);
            messageReceiver = null;
        }
        removeHandler();
        super.onDestroy();
    }
}
