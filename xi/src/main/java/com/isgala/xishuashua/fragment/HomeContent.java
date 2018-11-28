package com.isgala.xishuashua.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseFragment;
import com.isgala.xishuashua.bean_.HomeIsBlowerShow;
import com.isgala.xishuashua.bean_.Location;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.bean_.WaitORService;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.receiver.MessageReceiver;
import com.isgala.xishuashua.ui.BlowerActivity;
import com.isgala.xishuashua.ui.MessageActivity;
import com.isgala.xishuashua.ui.WalletActivity;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.isgala.xishuashua.view.pulltorefresh.RefreshLayout;
import com.klcxkj.zqxy.databean.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * 主体内容
 * Created by and on 2016/11/3.
 */

public class HomeContent extends BaseFragment implements RefreshLayout.OnRefreshListener {
    private final String TAG = "HomeContent";
    @BindView(R.id.home_info_icon)
    ImageView homeInfoIcon;
    @BindView(R.id.home_message_icon)
    ImageView homeMessageIcon;
    @BindView(R.id.content_home)
    LinearLayout contentHome;
    //    @BindView(R.id.home_pulltorefresh)
//    SwipeRefreshLayout mPullToRefresh;
    @BindView(R.id.refreshLayout)
    RefreshLayout mPullToRefresh;
    @BindView(R.id.home_blower)
    ImageView homeBlower;
    private View contentView;

    private DrawerLayout mDrawer;
    /**
     * 是否是第一次进入
     */
    private boolean first;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.content_home, null);
        ButterKnife.bind(this, contentView);
        initi();
        mPullToRefresh.setRefreshListener(this);
        //增加水控机功能
        EventBus.getDefault().register(this);
        return contentView;
    }

    /**
     * 水控机结束回调
     *
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messEvent(MessageEvent messageEvent) {
        if (messageEvent.getEventType().equals("money")) { //余额不足
            Log.d("MainActivity", "余额不足");
            startActivity(new Intent(getContext(), WalletActivity.class));
        } else if (messageEvent.getEventType().equals("finish")) { //使用结束，采集数据 类型：洗澡/饮水/吹风机
            if (messageEvent.isB()) {
                //自己结束
                getBathEndOrder(messageEvent);
                ToastUtils.show("自己结束");
                Log.d("MainActivity", "自己结束");

            } else {
                //他人结束
                ToastUtils.show("他人结束");
                Log.d("MainActivity", "他人结束");
                getBathEndOrder(messageEvent);
            }
        } else if (messageEvent.getEventType().equals("start")) { //点击开始 类型：洗澡/饮水/吹风机
            ToastUtils.show("点击开始");
            Log.d("MainActivity", "点击开始");
            Log.d("MainActivity", "事件类型：" + messageEvent.getEventType());
            Log.d("MainActivity", "订单开始时间：" + messageEvent.getTimeid());
            Log.d("MainActivity", "用户账号:" + messageEvent.getMaccountid());
            Log.d("MainActivity", "设备ID:" + messageEvent.getMdeviceid());
            Log.d("MainActivity", "项目编号:" + messageEvent.getMproductid());
            Log.d("MainActivity", "预扣金额" + messageEvent.getYkmoneyString());
            Log.d("MainActivity", "使用次数:" + messageEvent.getUsercount());
            Log.d("MainActivity", "设备mac地址" + messageEvent.getMacString());
            Log.d("MainActivity", "用户类型：" + messageEvent.getAccounttypeString());
            getBathBeginOrder(messageEvent);

        } else if (messageEvent.getEventType().equals("finish_washing")) {//使用结束，采集数据 类型：洗衣机
            Log.d("MainActivity", "洗衣机结束");

        } else if (messageEvent.getEventType().equals("start_washing")) { //点击开始  类型：洗衣机
            Log.d("MainActivity", "开始洗衣机");

        }
//        if (messageEvent.isB()){
        //自己结束
//        }else {
        //他人结束
//        }
    }

    private String updateIsSelf(MessageEvent messageEvent) {
        Map<String, String> map = new HashMap<>();
        map.put("eventType", messageEvent.getEventType());
        map.put("is_self", String.valueOf(messageEvent.isB()));

        String updateIs_self = new Gson().toJson(messageEvent);
        String update = "b";
        return updateIs_self.replaceFirst(update, "is_self");
    }

    /**
     * 水控机洗浴结束
     */
    private void getBathEndOrder(final MessageEvent messageEvent) {
        Map<String, String> map = new HashMap<>();
        map.put("order_info", updateIsSelf(messageEvent));

        VolleySingleton.post(Neturl.BATHENDORDER, "bathEndOrder", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Result result1 = JsonUtils.parseJsonToBean(result, Result.class);
                if (result1.data.flag == -1) {
                    getBathEndOrder(messageEvent);
                }
            }
        });
    }

    /**
     * 水控机洗浴开始
     */
    private void getBathBeginOrder(final MessageEvent messageEvent) {
        Map<String, String> map = new HashMap<>();
        map.put("order_info", updateIsSelf(messageEvent));
        VolleySingleton.post(Neturl.BATHBEGINORDER, "bathBeginOrder", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                String string = result;
                Result result1 = JsonUtils.parseJsonToBean(result, Result.class);
                if (result1.data.flag == -1) {
                    getBathBeginOrder(messageEvent);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        obtainLocation();//为了保证实时性，故每次都要请求最新的
    }

    /**
     * 设置是否支持下拉刷新数据
     *
     * @param canPull
     */
    public void setCanPull(boolean canPull) {
        mPullToRefresh.setEnabled(canPull);
    }


    /**
     * 初始化操作
     */
    private void initi() {
        first = true;
//        SPUtils.saveString("b_id", "");
//        SPUtils.saveString("bathroomLoaction", "");
        homeInfoIcon.setVisibility(View.VISIBLE);
        String string = SPUtils.getString(Constants.LOCATION, "");
        if (!TextUtils.isEmpty(string)) {
            Location location = JsonUtils.parseJsonToBean(string, Location.class);
            switchFragment(location);
        }
        getIsBlowerShow();
        IntentFilter intentFilter = new IntentFilter(Constants.NEW_MESSAGE);
        getContext().registerReceiver(messageReceiver, intentFilter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("not first", true);
    }

    /**
     * 切换状态 正常0、排队中1、等待中/服务中2(vstatus 1 2 )
     */
    private void switchFragment(Location location) {

        messageState();
        String device_type = SPUtils.getString(Constants.DEVICE_TYPE, "");
        if (device_type.equals("2")) {
            homeBlower.setVisibility(View.GONE);
            isRevision();
            return;
        }
        SPUtils.saveString("b_id", "");
        SPUtils.saveString("bathroomLoaction", "");
        if (location != null && location.data != null) {
            String status = location.data.status;
            if (TextUtils.equals("0", status)) {
                switchOrigin(first);
            } else if (TextUtils.equals("1", status)) {
                Bundle bundle = new Bundle();
                bundle.putString("id", location.data.id);
                switchLineUp(bundle);
            } else if (TextUtils.equals("2", status)) {
                waitOrServicing(location.data.id);
            } else {
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        } else {
            LoadingTrAnimDialog.dismissLoadingAnimDialog();
        }
    }

    private void messageState() {
        if (SPUtils.getBoolean("new_message", false)) {
            homeMessageIcon.setImageResource(R.drawable.selector_new_message);
        } else {
            homeMessageIcon.setImageResource(R.drawable.selector_message);
        }
    }

    private void getIsBlowerShow() {
        Map<String, String> pram = new HashMap<String, String>();
        pram.put("oauth_token", SPUtils.getString(Constants.OAUTH_TOKEN, null));
        pram.put("oauth_token_secret", SPUtils.getString(Constants.OAUTH_TOKEN_SECRET, null));
        VolleySingleton.postNoDEVICETOKEN(Neturl.EXISTS_BLOWER, "exists_blower", pram, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                HomeIsBlowerShow isBlowerShow = JsonUtils.parseJsonToBean(result, HomeIsBlowerShow.class);
                if (null != isBlowerShow && !TextUtils.isEmpty(isBlowerShow.data.is_allow_blower) && isBlowerShow.data.is_allow_blower.equals("1")) {
                    homeBlower.setVisibility(View.VISIBLE);
                } else {
                    homeBlower.setVisibility(View.GONE);
                }
            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                mPullToRefresh.refreshComplete();
            }
        });
    }

    /**
     * 切换待服务或者服务中
     *
     * @param id
     */
    public void waitOrServicing(final String id) {
        final Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(id)) {
            map.put("id", id);
        } else {
            throw new RuntimeException(" id is null ");
        }
        VolleySingleton.post(Neturl.APPOINT_STATUS, "waitOrServicing", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                WaitORService waitORService = JsonUtils.parseJsonToBean(result, WaitORService.class);
                if (waitORService != null && waitORService.data != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    bundle.putSerializable("bean", waitORService.data);
                    if (TextUtils.equals("1", waitORService.data.vstatus)) {//待服务
                        switchWait(bundle);
                    } else {//服务中
                        switchServicing(bundle);
                    }
                } else {
                    LoadingTrAnimDialog.dismissLoadingAnimDialog();
                }
            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
            }
        });
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void loadData() {
        super.loadData();
    }

    /**
     * 绑定侧拉菜单
     *
     * @param drawer
     */
    public void setUp(DrawerLayout drawer) {
        mDrawer = drawer;
    }

    @OnClick({R.id.home_info_icon, R.id.home_message_icon, R.id.home_blower})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_info_icon:
                if (mDrawer != null)
                    mDrawer.openDrawer(GravityCompat.START, true);
                break;
            case R.id.home_message_icon:
                Intent intent = new Intent(getContext(), MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.home_blower:
                Intent intent1 = new Intent(getContext(), BlowerActivity.class);
                startActivity(intent1);
                break;
        }
    }

    /**
     * 判断当前是否切换到超级澡堂改版页面
     *
     * @return
     */
    public synchronized void isRevision() {
        try {
            SuperFragment superFragment;
            superFragment = (SuperFragment) getChildFragmentManager().findFragmentByTag("superTag");
            if (superFragment == null) {
                superFragment = new SuperFragment();
                superFragment.binding(this);
                getChildFragmentManager().beginTransaction().replace(R.id.content_home_content, superFragment, "superTag").commitNowAllowingStateLoss();
                superFragment.update(true);
                superFragment.setPlayBanner(true);
            }
            homeInfoIcon.setVisibility(View.VISIBLE);
            if (mDrawer != null)
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mPullToRefresh.refreshComplete();
        } catch (Exception e) {
            mPullToRefresh.refreshComplete();
        }

    }

    /**
     * 切换到初始页
     *
     * @param flag 是否显示未支付订单的弹窗
     */
    public synchronized void switchOrigin(boolean flag) {
        try {
            first = false;
            OriginFragment originFragment;
            originFragment = (OriginFragment) getChildFragmentManager().findFragmentByTag("origin");
            if (originFragment == null) {
                originFragment = new OriginFragment();
                originFragment.binding(this);
                getChildFragmentManager().beginTransaction().replace(R.id.content_home_content, originFragment, "origin").commitNowAllowingStateLoss();
                originFragment.update(flag, true);

            } else {//第二次调用 当前页面默认 不提示是否有未支付订单
                originFragment.update(false, true);
            }
            homeInfoIcon.setVisibility(View.VISIBLE);
            if (mDrawer != null)
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mPullToRefresh.refreshComplete();
        } catch (Exception e) {
            mPullToRefresh.refreshComplete();
        }
    }

    /**
     * 切换到等待界面
     */
    public void switchWait(Bundle bundle) {
        WaitServiceFragment waitServiceFragment;
        waitServiceFragment = (WaitServiceFragment) getChildFragmentManager().findFragmentByTag("waitservice");
        if (waitServiceFragment == null) {
            waitServiceFragment = new WaitServiceFragment();
            if (bundle != null)
                waitServiceFragment.setArguments(bundle);
            waitServiceFragment.binding(this);
            getChildFragmentManager().beginTransaction().replace(R.id.content_home_content, waitServiceFragment, "waitservice").commitNowAllowingStateLoss();
            homeInfoIcon.setVisibility(View.GONE);
        } else {
            waitServiceFragment.updateView(bundle);
        }
        if (mDrawer != null)
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mPullToRefresh.refreshComplete();
    }

    public void switchServicing(Bundle bundle) {
        ServicingFragment servicingFragment;
        servicingFragment = (ServicingFragment) getChildFragmentManager().findFragmentByTag("Servicing");
        if (servicingFragment == null) {
            servicingFragment = new ServicingFragment();
            if (bundle != null)
                servicingFragment.setArguments(bundle);
            servicingFragment.binding(this);
            getChildFragmentManager().beginTransaction().replace(R.id.content_home_content, servicingFragment, "" +
                    "Servicing").commitNowAllowingStateLoss();
            homeInfoIcon.setVisibility(View.GONE);
        } else {
            servicingFragment.upView(bundle);
        }
        if (mDrawer != null)
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mPullToRefresh.refreshComplete();
    }

    /**
     * 切换到排队界面
     */
    public void switchLineUp(Bundle bundle) {
        LineUpFragment lineUpFragment;
        lineUpFragment = (LineUpFragment) getChildFragmentManager().findFragmentByTag("lineup");
        if (lineUpFragment == null) {
            lineUpFragment = new LineUpFragment();
            if (bundle != null) {
                lineUpFragment.setArguments(bundle);
            }
            lineUpFragment.binding(this);
            getChildFragmentManager().beginTransaction().replace(R.id.content_home_content, lineUpFragment, "lineup").commitNowAllowingStateLoss();
        } else {
            lineUpFragment.loadView(bundle);
        }
        homeInfoIcon.setVisibility(View.GONE);
        if (mDrawer != null)
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mPullToRefresh.refreshComplete();
    }

    /**
     * 获取用户的设备信息
     */
    public void obtainLocation() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("lat", SPUtils.getString(Constants.LOCATON_LAT, ""));
        map.put("lng", SPUtils.getString(Constants.LOCATON_LNG, ""));
        TelephonyManager TelephonyMgr = (TelephonyManager) getContext().getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String deviceId = TelephonyMgr.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = "";
        }
        WifiManager wm = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String mac = wm.getConnectionInfo().getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            map.put("mac", mac);
        }
        map.put("version", Build.VERSION.RELEASE);
        map.put("os", "0");
        map.put("imei", deviceId);
        map.put("uuid", SPUtils.getString("uuid", ""));
        map.put("type", Build.MODEL);// 设备型号
        VolleySingleton.postNoDEVICETOKEN(Neturl.INIT_APP, "initapp", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                SPUtils.saveString(Constants.LOCATION, result);
                Location location = JsonUtils.parseJsonToBean(result, Location.class);
                if (location != null && location.data != null) {
                    SPUtils.saveString("uuid", location.data.uuid);
                    SPUtils.saveString("kefu", location.data.kefu);
                }
                switchFragment(location);
            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                mPullToRefresh.refreshComplete();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Handler homeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onRefresh() {
        LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
        obtainLocation();
    }


    private MessageReceiver messageReceiver = new MessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            SPUtils.saveBoolean("new_message", true);
            messageState();
        }
    };

    @Override
    public void onDestroy() {
        if (messageReceiver != null) {
            getContext().unregisterReceiver(messageReceiver);
            messageReceiver = null;
        }
        //水控机 干掉多余资源
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        null.unbind();
    }
}