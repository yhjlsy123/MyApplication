package com.zhuochi.hydream.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.LoadingSpecialDialog;
import com.zhuochi.hydream.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.dialog.TipDialog2;
import com.zhuochi.hydream.dialog.TipDialogMessage;
import com.zhuochi.hydream.entity.ShowerOrderEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.pushbean.InitSettingEntity;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.receiver.MessageReceiver;
import com.zhuochi.hydream.utils.LogUtils;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.Tools;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhuochi.hydream.fragment.RankMonthFragment.RANK_MESSAGE;

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

    @BindView(R.id.wait_hide_pwd)
    CheckBox waitHidePwd;
    private final String TAG = "WaitServiceFragment";
    @BindView(R.id.wait_service_root)
    LinearLayout waitServiceRoot;
    @BindView(R.id.wait_cancel)
    Button waitCancel;
    @BindView(R.id.wait_confirm)
    Button waitConfirm;
    @BindView(R.id.home_tip)
    TextView mTip;
    @BindView(R.id.refresh_image)
    ImageView refreshImage;
    @BindView(R.id.service_refresh)
    LinearLayout serviceRefresh;
    private XiRequestParams params;
    private String UUID = "";
    private String deviceType = "";//要开启的设备类型
    private int mRemainTime;
    private MessageReceiver messageReceiver = new MessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            if (intent != null) {
                String action = intent.getStringExtra("action");
                if (TextUtils.equals("jump", action)) {   //跳转到服务中界面
                    LogUtils.e(TAG, "-------- jump serving -----------------");
//                    waitOrServicing(map.get("id"));
                } else if (TextUtils.equals("back", action)) {
                    LogUtils.e(TAG, "-------- jump list -----------------");
                    if (bindresult()) {
                        getHomeContent().exchangeMsg(0);
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
        params = new XiRequestParams(getActivity());
        return view;
    }


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
        waitServiceRoot.setVisibility(View.VISIBLE);//设置布局可见
        Bundle arguments = getArguments();
        if (arguments != null) {
//            mRemainTime = arguments.getInt("remainTime");
            ShowerOrderEntity bean = (ShowerOrderEntity) arguments.getSerializable("bean");

//            time = bean.getMaxWaitTime();
            time = bean.getRemainTime();
            UUID = bean.getUuid();

            deviceType = bean.getDeviceTypeKey();
            IntentFilter intentFilter = new IntentFilter(Constants.SERVICESTART);
            getContext().registerReceiver(messageReceiver, intentFilter);
            isRegister = true;
            startCountDown(time);
            mTip.setText("请尽快到达浴室，超时" + Tools.change(bean.getMaxWaitTime()) + getString(R.string.wait_tip));
            waitBathroomid.setText(bean.getDeviceNumber());
            waitBathroomlocation.setText(bean.getDeviceAreaName());
            waitPassword.setText(bean.getUserDevicePwd());

            //是否允许遥控
            if (bean.getAllowRemoteControl() == 0) {//都不显示
                waitCancel.setVisibility(View.GONE);
                waitConfirm.setVisibility(View.GONE);
            } else if (bean.getAllowRemoteControl() == 1) {//关闭
                waitCancel.setVisibility(View.VISIBLE);

            } else if (bean.getAllowRemoteControl() == 2) {//开启
                waitConfirm.setVisibility(View.VISIBLE);
            } else if (bean.getAllowRemoteControl() == 3) {//都显示
                waitCancel.setVisibility(View.VISIBLE);
                waitConfirm.setVisibility(View.VISIBLE);

            }
            LogUtils.e(TAG, " Receive register_bg success ");
        } else {
            LogUtils.e(TAG, " bundle is null ");
        }
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

    @Override

    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "turnOn"://开始服务
                ToastUtils.show(result.getData().getMsg());
                LoadingSpecialDialog.showLoadingAnimDialog(getActivity());
                if (bindresult())
                    getHomeContent().exchangeMsg(0);
                break;
            case "cancelReserve"://取消预约
                ToastUtils.show(result.getData().getMsg());
                if (bindresult()) {
                    getHomeContent().exchangeMsg(0);

                }
                break;

        }
        super.onRequestSuccess(tag, result);
    }

    /**
     * 更新视图
     *
     * @param arguments
     */
    public void updateView(Bundle arguments) {
        if (arguments != null) {
//            WaitORService.WaitOrServiceBean bean = (WaitORService.WaitOrServiceBean) arguments.getSerializable("bean");
//            map.put("id", bean.id);
            ShowerOrderEntity bean = (ShowerOrderEntity) arguments.getSerializable("bean");
//            mRemainTime = arguments.getInt("remainTime");
            time = bean.getRemainTime();
            waitBathroomid.setText(bean.getDeviceNumber());
            waitBathroomlocation.setText(bean.getDeviceAreaName());
            waitPassword.setText(bean.getUserDevicePwd());
            waitServiceRoot.setVisibility(View.VISIBLE);//设置布局可见
            if (refreshImage != null) {
                long tempTime = System.currentTimeMillis() - dTime;
                if (handler != null)
                    handler.sendEmptyMessageDelayed(120, 1000 - tempTime < 0 ? 0 : 1000 - tempTime);
            }
        }
        LoadingTrAnimDialog.dismissLoadingAnimDialog();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                sendEmptyMessageDelayed(100, 995);
                time++;
                if (waitTime != null)
                    waitTime.setText(Tools.formatTime(time));
            } else if (msg.what == 120) {
                if (refreshImage != null)
                    refreshImage.clearAnimation();
            }
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

    private int time = 0;

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
                                    cancelService();
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
        if (task!=null) {
            task.cancel();
        }
        if (messageReceiver != null && isRegister) {
            getContext().unregisterReceiver(messageReceiver);
            isRegister = false;
        }
        super.onDestroy();
    }

    private long dTime;

    @OnClick({R.id.wait_cancel, R.id.wait_confirm, R.id.service_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wait_cancel:
                Dialog dialogs = TipDialog2.show_(getContext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelService();
                    }
                }, "提示信息", "确认要取消本次服务吗？");
                break;
            case R.id.wait_confirm:
                Dialog dialog = TipDialog2.show_(getContext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startService();
                    }
                }, "提示信息", "确认要开启本次服务吗？");

                break;
            case R.id.service_refresh:
                if (bindresult()) {
                    dTime = System.currentTimeMillis();
                    LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
//                    LoadingSpecialDialog.showLoadingAnimDialog(getActivity());
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



    private  TimerTask task;
    private InitSettingEntity mInitEntity;
    //*开启服务*/
    private void startService() {
        params.addCallBack(this);
        params.turnOn(UUID, deviceType);
        waitConfirm.setClickable(false);
        waitConfirm.setBackgroundResource(R.drawable.shape_bg_corner_gray999);
         task = new TimerTask() {
            @Override
            public void run() {
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBackClick();
                        waitConfirm.setClickable(true);
                        waitConfirm.setBackgroundResource(R.drawable.selector_bg_corner_blue);
                    }
                });

            }

        };
        String mGson = SPUtils.getString("initSetting", "");
        if (!TextUtils.isEmpty(mGson)) {
             mInitEntity = new Gson().fromJson(mGson, InitSettingEntity.class);
            Timer timer = new Timer();
//            timer.schedule(task, 10* 1000);
            timer.schedule(task, mInitEntity.getDeviceTimeout() * 1000);

        }


    }

    public void onBackClick() {
        LoadingSpecialDialog.dismissLoadingAnimDialog();
        Dialog dialog = TipDialogMessage.show_(getContext(), new View.OnClickListener() {
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
        }, "", mInitEntity.getDeviceTimeoutStartTip());

    }


    /**
     * 取消服务
     *
     * @return
     */
    public void cancelService() {
        LoadingSpecialDialog.dismissLoadingAnimDialog();
        params.addCallBack(this);
        params.cancelReserve(UUID, deviceType);
//        params.cancelReserve("9019df64-ff05-433f-a8c0-51ce63d36265","faucet");
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        if (task!=null) {
            task.cancel();
        }
        super.onPause();
    }
}
