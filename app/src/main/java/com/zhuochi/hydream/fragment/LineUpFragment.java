package com.zhuochi.hydream.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.dialog.TipDialog2;
import com.zhuochi.hydream.entity.LineUpEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.exchang.QueuingInfo;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.receiver.MessageReceiver;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.Tools;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 排队(可以跳转到预约列表、待服务)
 * Created by and on 2016/11/10.
 */

public class LineUpFragment extends BaseHomeFragment {


    private final String TAG = "LineUpFragment";
    @BindView(R.id.lineup_root)
    RelativeLayout lineupRoot;
    @BindView(R.id.tv_lineupState)
    TextView tvLineupState;
    @BindView(R.id.tv_lineupNumber)
    TextView tvLineupNumber;
    @BindView(R.id.tv_lineupTime)
    TextView tvLineupTime;
    @BindView(R.id.line_title)
    LinearLayout lineTitle;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.img_animation)
    ImageView imgAnimation;
    @BindView(R.id.tv_lineupWaitTime)
    TextView tvLineupWaitTime;
    @BindView(R.id.home_tip)
    TextView homeTip;
    @BindView(R.id.lineup_cancel)
    Button lineupCancel;
    private XiRequestParams params;
    private AnimationDrawable animationDrawable;
    private View mView;
    private String deviceTypeKey = "";
    private int deviceAreaId;
    private int time;
    private MessageReceiver messageReceiver = new MessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            if (intent != null) {

                String action = intent.getStringExtra("action");
                String id = intent.getStringExtra("id");
                if (TextUtils.equals("jump", action)) {//跳转
                    if (!TextUtils.isEmpty(id)) {
                        next(id);
                    } else {
                        throw new RuntimeException(" push line up id is null ");
                    }
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lineup, null);
        ButterKnife.bind(this, view);
        params = new XiRequestParams(getActivity());
        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                sendEmptyMessageDelayed(100, 995);
                time++;
                if (tvLineupWaitTime != null)
                    tvLineupWaitTime.setText(Tools.formatTime(time));
            } else if (msg.what == 120) {
                if (tvLineupWaitTime != null)
                    tvLineupWaitTime.clearAnimation();
            }
        }
    };
    private HashMap<String, String> map;

    @Override
    public void initView() {
        super.initView();
        map = new HashMap<>();
        imgAnimation.setBackgroundResource(R.drawable.animation_list);
        animationDrawable = (AnimationDrawable) imgAnimation.getBackground();
        animationDrawable.setOneShot(false);
        if (animationDrawable.isRunning()) {

        }
        animationDrawable.start();
    }

    @Override
    public void loadData() {
        super.loadData();
        Bundle arguments = getArguments();
        if (arguments != null) {
            String id = arguments.getString("id");
            if (!TextUtils.isEmpty(id)) {//注：此处 排队id特别重要！
                map.put("id", id);
            }
            String serverTime = arguments.getString("serverTime");
            QueuingInfo bean = (QueuingInfo) arguments.getSerializable("bean");
            deviceTypeKey = bean.getDevice_type_key();
            tvLineupState.setText(bean.getDevice_area_name());
            deviceAreaId = bean.getDevice_area_id();
            tvLineupNumber.setText(bean.getQueuingNumber() + "");
            int waitingTime = bean.getExpectedWaitingTime() / 60;
            tvLineupTime.setText(String.valueOf(waitingTime));
            homeTip.setText(bean.getMessage());
            String times = Common.formatTime(serverTime, bean.getCreate_time());
            if (tvLineupWaitTime.getText().toString().isEmpty()) {
                time = Tools.toInt(times);
                tvLineupWaitTime.setText(Tools.formatTime(time) + "");
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                    handler.sendEmptyMessageDelayed(100, 995);
                }
            }
        }
        IntentFilter intentFilter = new IntentFilter(Constants.LINEUP);
        getContext().registerReceiver(messageReceiver, intentFilter);
//            lineUp();
//            LogUtils.e(TAG, " Receive register_bg success ");
//        } else {
//            LogUtils.e(TAG, "  bundle  is  null ");
//        }
//        LoadingTrAnimDialog.dismissLoadingAnimDialog();
//        //防止注册广播之前，数据发生变化.确保实时性
        if (bindresult()) {
            getHomeContent().exchangeMsg(0);
        }
    }

    /**
     * 更新数据
     *
     * @param arguments
     */
    public void loadView(Bundle arguments) {
        if (arguments != null) {
//            String id = arguments.getString("id");
//            if (!TextUtils.isEmpty(id)) {//注：此处 排队id特别重要！
//                map.put("id", id);
//            }
            QueuingInfo bean = (QueuingInfo) arguments.getSerializable("bean");
            if (bean != null) {
                updateView(bean);
//            loadData();
            }
        }
        LoadingTrAnimDialog.dismissLoadingAnimDialog();
    }


    /**
     * 切换待服务或者服务中
     *
     * @param id
     */
    private void next(String id) {
        if (bindresult()) {
//            getHomeContent().waitOrServicing(id);
        }
    }

    private void updateView(QueuingInfo bean) {
        tvLineupNumber.setText(bean.getQueuingNumber() + "");
        int waitingTime = bean.getExpectedWaitingTime() / 60;
        tvLineupTime.setText(String.valueOf(waitingTime));
        homeTip.setText(bean.getMessage());

    }

    /**
     * 取消排队
     */
    private void cancel() {
        params.addCallBack(this);
        params.cancelQueue(deviceAreaId, deviceTypeKey);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "cancelQueue":
                ToastUtils.show(result.getData().getMsg());
                if (bindresult()) {
                    LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
                    getHomeContent().exchangeMsg(0);
                }
                break;
        }

        super.onRequestSuccess(tag, result);
    }

    @OnClick(R.id.lineup_cancel)
    public void onClick() {
        Dialog dialog = TipDialog2.show_(getContext(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        }, "提示信息", "确认要取消排队吗？");

    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
        }
        removeHandler();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
