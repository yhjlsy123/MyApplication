package com.isgala.xishuashua.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.bean_.PayResultBean;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.bean_.WaitORService;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.dialog.StopServiceDialog;
import com.isgala.xishuashua.receiver.MessageReceiver;
import com.isgala.xishuashua.ui.PayActivity;
import com.isgala.xishuashua.ui.PayResult;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.LogUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.Tools;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 服务中
 * Created by and on 2016/11/10.
 */

public class ServicingFragment extends BaseHomeFragment {
    @BindView(R.id.serving_time)
    TextView servingTime;
    @BindView(R.id.serving_money)
    TextView servingMoney;
    @BindView(R.id.serving_pwd)
    TextView servingPwd;
    @BindView(R.id.serving_tip)
    TextView servingTip;
    @BindView(R.id.serving_location)
    TextView servingLocation;
    @BindView(R.id.service_hide)
    CheckBox serviceHide;
    @BindView(R.id.service_stop)
    Button serviceStop;
    private final String TAG = "ServicingFragment";
    @BindView(R.id.service_root)
    LinearLayout serviceRoot;
    @BindView(R.id.refresh_image)
    ImageView refreshImage;

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
                        startActivity(new Intent(getActivity(), PayActivity.class));
                    } else {
                        jumpPayOrderResult(order_id);
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
        View view = inflater.inflate(R.layout.fragment_servicing, null);
        ButterKnife.bind(this, view);
        IntentFilter intentFilter = new IntentFilter(Constants.SERVICEFINISH);
        getContext().registerReceiver(messageReceiver, intentFilter);
        return view;
    }

    private HashMap<String, String> map;

    @Override
    public void initView() {
        super.initView();
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

    @Override
    public void loadData() {
        super.loadData();
        map = new HashMap<>();
        Bundle arguments = getArguments();
        upView(arguments);
        LoadingTrAnimDialog.dismissLoadingAnimDialog();
    }

    private int time;

    public void upView(Bundle arguments) {
        if (servingTime != null && arguments != null) {
            WaitORService.WaitOrServiceBean data = (WaitORService.WaitOrServiceBean) arguments.getSerializable("bean");
            map.put("id", data.id);
            time = Tools.toInt(data.time);
            servingLocation.setText(data.num);
            servingTime.setText(Tools.formatTime(time));
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessageDelayed(100, 995);
            }
            servingMoney.setText(data.money);
            servingPwd.setText(data.password);
            if (data.tips.size() > 0)
                servingTip.setText(data.tips.get(0));
            serviceRoot.setVisibility(View.VISIBLE);
        } else {
            LogUtils.e(TAG, "  bundle  is  null ");
        }
        if (refreshImage != null) {
            long tempTime = System.currentTimeMillis() - dTime;
            if (handler != null)
                handler.sendEmptyMessageDelayed(120, 1000 - tempTime < 0 ? 0 : 1000 - tempTime);
        }
    }

    /**
     * 跳转到订单结果界面
     */
    private void jumpPayOrderResult(String order_id) {
        payResult(order_id);
    }

    /**
     * 服务完后跳转支付结果页
     *
     * @param orderId
     */
    private void payResult(String orderId) {
        LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
        HashMap<String, String> map = new HashMap<>();
        map.put("order_id", orderId);
        VolleySingleton.post(Neturl.PAY_RESULT, "pay_result", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                PayResultBean payResultBean = JsonUtils.parseJsonToBean(result, PayResultBean.class);
                if (payResultBean != null && TextUtils.equals("1", payResultBean.status)) {
                    Intent intent = new Intent(getActivity(), PayResult.class);
                    intent.putExtra("bean", result);
                    startActivity(intent);
                }
            }
        });
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
                    getHomeContent().obtainLocation();
                }
                break;
        }
    }

    private void animation() {
//        RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
//                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setDuration(2000);//设置动画持续时间
//        animation.setRepeatCount(100);//设置重复次数
////        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
//        refreshImage.setAnimation(animation);
//        animation.startNow();
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

    /**
     * 停止洗澡计费
     */
    private void stop() {
        LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
        VolleySingleton.post(Neturl.REQUEST_STOP, "stop", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Result result1 = JsonUtils.parseJsonToBean(result, Result.class);
                if (result1 != null) {
                    if (TextUtils.equals("1", result1.status)) {
                        ToastUtils.show(result1.data.msg);
                        serviceStop.setClickable(false);
                        serviceStop.setBackgroundResource(R.drawable.shape_bg_corner_gray999);
                        serviceStop.setText(result1.data.msg);
                    } else {
                        ToastUtils.show(result1.msg);
                    }
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        });
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ServicingFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart("ServicingFragment");
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
