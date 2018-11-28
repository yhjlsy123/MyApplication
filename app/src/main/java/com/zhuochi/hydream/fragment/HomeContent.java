package com.zhuochi.hydream.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.klcxkj.zqxy.databean.MessageEvent;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.activity.MessageActivity;
import com.zhuochi.hydream.activity.MyScanActivity;
import com.zhuochi.hydream.activity.PayActivity;
import com.zhuochi.hydream.activity.PayResult;
import com.zhuochi.hydream.activity.RechargeActivity;
import com.zhuochi.hydream.activity.UserActivity;
import com.zhuochi.hydream.adapter.HomeGridViewAdapter;
import com.zhuochi.hydream.adapter.ShowerAdapter;
import com.zhuochi.hydream.base.BaseFragment;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.LoadingSpecialDialog;
import com.zhuochi.hydream.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.dialog.TipDialog2;
import com.zhuochi.hydream.entity.DeviceTypeEntity;
import com.zhuochi.hydream.entity.InServiceEntity;
import com.zhuochi.hydream.entity.MyMqttMessage;
import com.zhuochi.hydream.entity.ShowerOrderEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.SonDeviceInfo;
import com.zhuochi.hydream.entity.exchang.ExChangDeviceInfo;
import com.zhuochi.hydream.entity.exchang.ExChangMsg;
import com.zhuochi.hydream.entity.exchang.ExChangUserState;
import com.zhuochi.hydream.entity.exchang.QueuingInfo;
import com.zhuochi.hydream.entity.exchang.TurnOff;
import com.zhuochi.hydream.entity.exchang.WaitingInfo;
import com.zhuochi.hydream.entity.pushbean.SettingsEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.receiver.MessageReceiver;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.UserUtils;
import com.zhuochi.hydream.view.pulltorefresh.RefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhuochi.hydream.fragment.OriginFragment.IsImg_load;
import static com.zhuochi.hydream.fragment.OriginFragment.stateTip;


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
    @BindView(R.id.refreshLayout)
    RefreshLayout mPullToRefresh;
    @BindView(R.id.line_home)
    RelativeLayout lineHome;
    @BindView(R.id.line)
    RelativeLayout line;
    @BindView(R.id.line_add)
    LinearLayout lineadd;
    @BindView(R.id.line_title)
    RelativeLayout lineTitle;
    @BindView(R.id.img_Content)
    ImageView imgContent;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private List mFeeTypeList;
    private View contentView;
    /**
     * 是否是第一次进入
     */
    private boolean first;
    private XiRequestParams params;
    private HomeGridViewAdapter adapter;
    private String mDevice_type_key = "";//设备类型
    private int mDevice_type_btn;//显示按钮样式  1:预约 0开始
    private String mShort_tip = "";//显示顶部提示
    private String mPayment_style;//支付类型
    private SonDeviceInfo info;
    private ExChangDeviceInfo deviceInfo;
    /*------------------------------------*/
    private RelativeLayout home_choice_bathroomlocation;
    private RelativeLayout origin_filter_content;
    private ImageView arrow;
    private RelativeLayout origin_errbg;
    public static int DELAYEDTIME_REFRESH = 5000;//刷新时长
    private Bundle mWaitbundle = new Bundle();
    private int QueuingLastID = 0;//排队last id
    private int reservedLastID = 0;//用户状态 last id
    private int deviceStateLastID = 0;//设备状态
    private int orderLastID = 0;//订单信息
    private int settingsLastID = 0;//设置


    public Handler homeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Integer.MAX_VALUE && null != msg.obj && msg.obj instanceof MyMqttMessage) {


            } else {
                if (NetworkUtil.isNetworkAvailable())
                    exchangeMsg(0);
                if (DELAYEDTIME_REFRESH != 0) {
                    homeHandler.sendEmptyMessageDelayed(0, DELAYEDTIME_REFRESH);//启动handler，实现4秒定时循环执行
                }
            }

        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.content_home, null);
        ButterKnife.bind(this, contentView);
        params = new XiRequestParams(getActivity());
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
            BalanceOrder();
        } else if (messageEvent.getEventType().equals("finish")) { //使用结束，采集数据 类型：洗澡/饮水/吹风机
            if (messageEvent.isB()) {
                //自己结束
                //     Todo Event 缺少结束蓝牙水控接口
                getBathEndOrder(messageEvent);
                ToastUtils.show("自己结束");
                Log.d("MainActivity", "自己结束");

            } else {
                //他人结束
                ToastUtils.show("他人结束");
                Log.d("MainActivity", "他人结束");
                //     Todo Event 缺少结束蓝牙水控接口
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
//     Todo Event 缺少开启蓝牙水控接口      getBathBeginOrder(messageEvent);
            getBathBeginOrder(messageEvent);

        } else if (messageEvent.getEventType().equals("finish_washing")) {//使用结束，采集数据 类型：洗衣机
            Log.d("MainActivity", "洗衣机结束");

        } else if (messageEvent.getEventType().equals("start_washing")) { //点击开始  类型：洗衣机
            Log.d("MainActivity", "开始洗衣机");

        }
    }

    /**
     * 结束订单
     *
     * @param event
     */
    private void getBathEndOrder(MessageEvent event) {
        params.addCallBack(this);
        params.bathEndOrder(updateIsSelf(event));
    }

    /**
     * 开始订单
     *
     * @param event
     */
    private void getBathBeginOrder(MessageEvent event) {
        params.addCallBack(this);
        params.bathBeginOrder(updateIsSelf(event));
    }

    private String updateIsSelf(MessageEvent messageEvent) {
        Map<String, String> map = new HashMap<>();
        map.put("eventType", messageEvent.getEventType());
        map.put("is_self", String.valueOf(messageEvent.isB()));

        String updateIs_self = new Gson().toJson(messageEvent);
        String update = "b";
        return updateIs_self.replaceFirst(update, "is_self");
    }

    /*余额不足*/
    private void BalanceOrder() {
        Dialog dialog = TipDialog2.show_(getContext(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent payIntent = new Intent(getActivity(), RechargeActivity.class);
                payIntent.putExtra("PayType", "Recharge");//余额
                startActivity(payIntent);
            }
        }, "提示信息", "余额不足，请前去交余额");

    }

    //获取顶部类型弹窗
    private void getDeviceTypeByOrgAreaId() {
        int OrgAreaID = UserUtils.getInstance(getActivity()).getOrgAreaID();
        params.addCallBack(this);
        params.getDeviceTypeByOrgAreaId(OrgAreaID);
    }


    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "selectDeviceTypeByOrgAreaId"://获取顶部类型弹窗
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                if (jsonArray.size() > 0) {
                    List<DeviceTypeEntity> entityList = JSON.parseArray(JSON.toJSONString(jsonArray), DeviceTypeEntity.class);
                    DeviceTypeEntity deviceTypeEntity = entityList.get(0);
                    if (mFeeTypeList != null) {
                        mFeeTypeList.clear();
                    }
                    mFeeTypeList = deviceTypeEntity.getFee_type_list();
                    setLayoutInflater(entityList, 0);
                }
                break;

            case "exchangeMsg"://实时刷新总线
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
                JSONArray jsonArray2 = new JSONArray((ArrayList) result.getData().getData());
                String gs = JSON.toJSONString(jsonArray2);
                Log.d("ls", gs);
                List<ExChangMsg> msg = JSON.parseArray(JSON.toJSONString(jsonArray2), ExChangMsg.class);
                if (msg.size() == 0) {
                    mPullToRefresh.refreshComplete();
                }
                for (int i = 0; i < msg.size(); i++) {
                    if (msg.get(i).getType().equals("waitingInfo")) {
                        //等待时间
                        Map waitingMap = (Map) msg.get(i).getContent();
                        WaitingInfo waitingInfo = new Gson().fromJson(GsonUtils.parseMapToJson(waitingMap), WaitingInfo.class);
                        mWaitbundle.putSerializable("bean", waitingInfo);
                    }
                    if (msg.get(i).getType().equals("indexTip")) {
                        String contentTip = msg.get(i).getContent().toString();
                        mWaitbundle.putString("content", contentTip);
                    }
                    if (msg.get(i).getType().equals("userState")) {  //用户状态
                        Map map2 = (Map) msg.get(i).getContent();
                        String gson1 = GsonUtils.parseMapToJson(map2);
                        ExChangUserState userState = new Gson().fromJson(gson1, ExChangUserState.class);
                        if (!userState.getDeviceInfo().toString().isEmpty()) {//用户使用设备状态
                            Map map1 = (Map) userState.getDeviceInfo();
                            String devicestr = GsonUtils.parseMapToJson(map1);
                            deviceInfo = new Gson().fromJson(devicestr, ExChangDeviceInfo.class);
                            reservedLastID = deviceInfo.getId();
                        }
                        if (userState.getUserState().equals("normal")) {

                            //正常预约
                            originFrag(mWaitbundle);
//                            isShowTopDeviceType(false);
                        } else if (userState.getUserState().equals("reserved")) {
                            //预约中

                            ShowerOrderEntity entity = new ShowerOrderEntity();
                            entity.setDeviceAreaName(deviceInfo.getDeviceAreaName());
                            entity.setMaxWaitTime(deviceInfo.getMax_wait_time());
                            entity.setDeviceNumber(deviceInfo.getDevice_number());
                            entity.setDeviceTypeKey(deviceInfo.getDevice_type_key());
                            entity.setUserDevicePwd(deviceInfo.getUser_device_password());
                            entity.setUuid(deviceInfo.getUuid());
                            entity.setAllowRemoteControl(userState.getAllowRemoteControl());
                            entity.setRemainTime(userState.getRemainTime());
                            Bundle bundle = new Bundle();
                            bundle.putString("Device_name", deviceInfo.getDevice_name());
                            bundle.putSerializable("bean", entity);
//                            bundle.putInt("remainTime", userState.getRemainTime());
                            switchWait(bundle);
                        } else if (userState.getUserState().equals("running")) {
                            //使用中 服务中
                            stateTip = 0;
//                            WaitServiceFragment.waitStateTip = 0;
                            LoadingSpecialDialog.dismissLoadingAnimDialog();
                            if (null == deviceInfo) {
                                Log.i("lsy", deviceInfo + "");
                                return;
                            }
                            if (mDevice_type_btn == 1) {
                                InServiceEntity entity = new InServiceEntity();
                                String Device_name = deviceInfo.getDevice_name();
                                List<String> re = Arrays.asList(Device_name.split(","));
                                entity.setRequest_id(deviceInfo.getDevice_key());
                                entity.setSub_devices(re);
                                Bundle bundle = new Bundle();
                                bundle.putString("payment_style", mPayment_style);//支付类型
                                bundle.putString("uuid", deviceInfo.getUuid());
                                bundle.putString("deviceType", deviceInfo.getDevice_type_key());
                                bundle.putString("devicePwd", deviceInfo.getUser_device_password());
                                bundle.putSerializable("bean", entity);
                                bundle.putString("serverTime", result.getData().getTime());//服务器时间
                                bundle.putString("startTime", deviceInfo.getStart_time());//开始时间
                                bundle.putString("deviceName", deviceInfo.getDeviceAreaName());//浴室位置
                                switchServicing(bundle);
                            } else {//普通服务中
                                InServiceEntity entity = new InServiceEntity();
                                String Device_name = deviceInfo.getDevice_name();
                                List<String> re = Arrays.asList(Device_name.split(","));
                                entity.setRequest_id(deviceInfo.getDevice_key());
                                entity.setSub_devices(re);
                                Bundle bundle = new Bundle();
                                bundle.putString("payment_style", mPayment_style);//支付类型
                                bundle.putString("uuid", deviceInfo.getUuid());
                                bundle.putString("deviceType", deviceInfo.getDevice_type_key());
                                bundle.putString("devicePwd", deviceInfo.getUser_device_password());
                                bundle.putSerializable("bean", entity);
                                bundle.putString("serverTime", result.getData().getTime());//服务器时间
                                bundle.putString("startTime", deviceInfo.getStart_time());//开始时间
                                switchServicingBasics(bundle);
                            }
                        } else if (userState.getUserState().equals("queuing")) {
                            //排队中
                            Map queuingMap = (Map) userState.getQueueInfo();
                            QueuingInfo queuingInfo = new Gson().fromJson(GsonUtils.parseMapToJson(queuingMap), QueuingInfo.class);
                            QueuingLastID = queuingInfo.getId();//排队id
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("bean", queuingInfo);
                            bundle.putString("serverTime", result.getData().getTime());//服务器时间
                            switchLineUp(bundle);
                        }
                    }
                    if (msg.get(i).getType().equals("turnOffReturn")) {
                        //订单信息
                        LoadingSpecialDialog.dismissLoadingAnimDialog();
                        orderLastID = msg.get(i).getId();
                        Map map1 = (Map) msg.get(i).getContent();
                        TurnOff mTurnOff = new Gson().fromJson(GsonUtils.parseMapToJson(map1), TurnOff.class);
                        if (mTurnOff.getAutoPay() == 1) {//自动扣费
                            Intent intent = new Intent(getContext(), PayResult.class);
                            intent.putExtra("payType", deviceInfo.getDevice_type_key());
                            intent.putExtra("entity", GsonUtils.parseMapToJson(map1));
                            startActivity(intent);
                            homeHandler.removeCallbacksAndMessages(0);
                        } else {//手动扣费
                            Intent intent = new Intent(getContext(), PayActivity.class);
                            intent.putExtra("entity", GsonUtils.parseMapToJson(map1));
                            startActivity(intent);
                            homeHandler.removeCallbacksAndMessages(0);
                        }
                    }

                    if (msg.get(i).getType().equals("deviceState")) {
                        //设备状态
                        deviceStateLastID = msg.get(i).getId();

                    }

                    if (msg.get(i).getType().equals("settings")) {
                        //系统支持设置  保留字段
                        settingsLastID = msg.get(i).getId();
//                        Map settingMap = (Map) msg.get(i).getContent();
//                        SettingsInfo settingsInfo = new Gson().fromJson(GsonUtils.parseMapToJson(settingMap), SettingsInfo.class);
//                        DELAYEDTIME_REFRESH = settingsInfo.getRefreshInterval() * 1000;
                    }
                }
                break;
            case "syncAmount":
                ToastUtils.show(result.getData().getMsg());
                break;
            case "bathBeginOrder"://开始蓝牙水控洗浴
                ToastUtils.show(result.getData().getMsg());
                break;
            case "bathEndOrder"://结束蓝牙水控洗浴
                ToastUtils.show(result.getData().getMsg());
                break;
        }
        super.onRequestSuccess(tag, result);

    }

    private void setLayoutInflater(final List<DeviceTypeEntity> entityList, final int mposition) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_horizontal_home, null);
        final GridView gridView = (GridView) view.findViewById(R.id.grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter = new HomeGridViewAdapter(getActivity(), entityList, position);
                gridView.setAdapter(adapter);
                setCanPull(true);
                mDevice_type_key = entityList.get(position).getDevice_type_key();
//              蓝牙水控
                if (mDevice_type_key.equals("kalushit")) {
                    lineTitle.setVisibility(View.GONE);
                    imgContent.setBackgroundResource(R.mipmap.home_top_nav_n);
                    isRevision();
                    homeHandler.removeCallbacksAndMessages(null);
                    setCanPull(false);
                    isShowTopDeviceType(true, false);
                    return;
                }
                mDevice_type_btn = entityList.get(position).getNeed_reserve();
                mShort_tip = entityList.get(position).getShort_tip();
                mPayment_style = entityList.get(position).getPayment_style();
                mFeeTypeList = entityList.get(position).getFee_type_list();
                ShowerAdapter.selectID = "";
                OriginFragment.mUUid = "";
                OriginFragment.IsImg_load = false;
                Common.ICON_BASE_URL = entityList.get(position).getIcon_base_url();
                if (!mDevice_type_key.equals("kalushit")) {
                    exchangeMsg(0);
                }
                lineTitle.setVisibility(View.GONE);
                imgContent.setBackgroundResource(R.mipmap.home_top_nav_n);
                originFrag(mWaitbundle);
                isShowTopDeviceType(true, false);
            }
        });

        int size = entityList.size();
        int length = 130;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 3) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数
        if (adapter == null) {
            adapter = new HomeGridViewAdapter(getActivity(), entityList, mposition);
            gridView.setAdapter(adapter);
        } else {
            adapter.setData(entityList);
        }
        lineadd.addView(view);
        mDevice_type_key = entityList.get(mposition).getDevice_type_key();
        mDevice_type_btn = entityList.get(mposition).getNeed_reserve();
        mPayment_style = entityList.get(mposition).getPayment_style();
        mShort_tip = entityList.get(mposition).getShort_tip();
        mFeeTypeList = entityList.get(mposition).getFee_type_list();
        if (mDevice_type_key.equals("kalushit")) {
//            LoadingRefreshDialog.dismissLoadingAnimDialog("Refresh");
            lineTitle.setVisibility(View.GONE);
            isRevision();
            homeHandler.removeCallbacksAndMessages(null);
            setCanPull(false);
            isShowTopDeviceType(true, false);
            return;
        }
        Common.ICON_BASE_URL = entityList.get(mposition).getIcon_base_url();
//        getStateByDeviceType();
        originFrag(mWaitbundle);
        isShowTopDeviceType(true, false);
    }

    /**
     * 同步余额
     */
    public void syncAmount() {
        params.addCallBack(this);
        params.syncAmount();
    }


    /**
     * 实时刷新当前界面
     */
    public void exchangeMsg(int isManual) {
        Log.d("lsy", "exchangeMsg");
        int[] sortList = {QueuingLastID, reservedLastID, deviceStateLastID, orderLastID, settingsLastID};
        int device_area_id = SPUtils.getInt("Device_area_id", 0);
        try {
            int lastid = Common.getMaxNum(sortList);
            if (!TextUtils.isEmpty(mDevice_type_key)) {
                params.addCallBack(this);
                params.exchangeMsg(device_area_id, mDevice_type_key, lastid, isManual);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        //为了保证实时性，故每次都要请求最新的
        if (!mDevice_type_key.equals("kalushit")) {
            if (DELAYEDTIME_REFRESH != 0) {
                homeHandler.sendEmptyMessageDelayed(0, DELAYEDTIME_REFRESH);//启动handler，实现定时循环执行
            }
        }


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

        getDeviceTypeByOrgAreaId();
//        isRevision(); todo 跳转只有水控机页面
        first = true;
        homeInfoIcon.setVisibility(View.VISIBLE);
        homeMessageIcon.setVisibility(View.VISIBLE);
        imgContent.setVisibility(View.VISIBLE);

        //新消息显示
        IntentFilter intentFilter = new IntentFilter(Constants.REFRESHBUS);
        //注册监听
        getContext().registerReceiver(messageReceiver, intentFilter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("not first", true);
    }

    private void messageState() {
        if (SPUtils.getBoolean("new_message", false)) {
            homeMessageIcon.setImageResource(R.mipmap.home_new);
        } else {
            homeMessageIcon.setImageResource(R.mipmap.home_new_no);
        }
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void loadData() {
        super.loadData();
    }


    @OnClick({R.id.home_info_icon, R.id.home_message_icon, R.id.img_Content})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_info_icon:
                Intent intent1 = new Intent(getContext(), UserActivity.class);
                startActivity(intent1);
                break;
            case R.id.home_message_icon:
                Intent intent = new Intent(getContext(), MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.img_Content:
                if (lineTitle.getVisibility() == View.GONE) {
                    lineTitle.setVisibility(View.VISIBLE);
                    imgContent.setBackgroundResource(R.mipmap.home_top_nav_s);
                    isShowTopDeviceType(true, true);
                } else {
                    lineTitle.setVisibility(View.GONE);
                    imgContent.setBackgroundResource(R.mipmap.home_top_nav_n);
                    isShowTopDeviceType(true, false);
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        homeHandler.removeCallbacksAndMessages(null);
    }


    public void originFrag(Bundle bundle) {
        try {
            first = false;
            OriginFragment originFragment;
            originFragment = (OriginFragment) getChildFragmentManager().findFragmentByTag("origin");
            if (originFragment == null) {
                originFragment = new OriginFragment();
                if (bundle != null) {
                    originFragment.setArguments(bundle);
                }
                originFragment.binding(this);
                getChildFragmentManager().beginTransaction().replace(R.id.content_home_content, originFragment, "origin").commitNowAllowingStateLoss();
                originFragment.getDeviceAreasWithDeviceState(mDevice_type_key, mDevice_type_btn, mShort_tip, mFeeTypeList);
                IsImg_load = false;
                homeInfoIcon.setVisibility(View.VISIBLE);
                homeMessageIcon.setVisibility(View.VISIBLE);
                imgContent.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.GONE);
                mPullToRefresh.refreshComplete();
            } else {//第二次调用 当前页面默认 不提示是否有未支付订单
                originFragment.getDeviceAreasWithDeviceState(mDevice_type_key, mDevice_type_btn, mShort_tip, mFeeTypeList);
                if (bundle != null) {
                    originFragment.setArguments(bundle);
                }
                mPullToRefresh.refreshComplete();
            }

        } catch (Exception e) {
            mPullToRefresh.refreshComplete();
        }
    }

    private void isShowTopDeviceType(boolean isNewChild, boolean state) {
        if (isNewChild) {//当前fragment 获取子类控件，因此布局 若有替换 必须重新new一遍
            if (null == getChildFragmentManager().findFragmentById(R.id.content_home_content)) {
                return;
            }
            home_choice_bathroomlocation = (RelativeLayout) getChildFragmentManager().findFragmentById(R.id.content_home_content).getView().findViewById(R.id.home_choice_bathroomlocation);
            origin_filter_content = (RelativeLayout) getChildFragmentManager().findFragmentById(R.id.content_home_content).getView().findViewById(R.id.origin_filter_content);
            arrow = (ImageView) getChildFragmentManager().findFragmentById(R.id.content_home_content).getView().findViewById(R.id.arrow);
            origin_errbg = (RelativeLayout) getChildFragmentManager().findFragmentById(R.id.content_home_content).getView().findViewById(R.id.origin_errbg);
        }
        if (state) {
            home_choice_bathroomlocation.setVisibility(View.GONE);
            origin_filter_content.setVisibility(View.GONE);
            arrow.setImageResource(R.mipmap.arrow_down);
            origin_errbg.setVisibility(View.VISIBLE);
            origin_errbg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lineTitle.setVisibility(View.GONE);
                    imgContent.setBackgroundResource(R.mipmap.home_top_nav_n);
                    originFrag(mWaitbundle);
                    isShowTopDeviceType(false, false);
                }
            });
        } else {
            arrow.setImageResource(R.mipmap.arrow_up);
            home_choice_bathroomlocation.setVisibility(View.VISIBLE);
            origin_errbg.setVisibility(View.GONE);
        }
        if (mDevice_type_key.equals("kalushit")) {
            if (home_choice_bathroomlocation != null) {
                home_choice_bathroomlocation.setVisibility(View.GONE);
            }
            if (origin_filter_content != null) {
                origin_filter_content.setVisibility(View.GONE);
            }
            if (arrow != null) {
                arrow.setVisibility(View.GONE);
            }
            if (origin_errbg != null) {
                origin_errbg.setVisibility(View.GONE);
            }
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
//                superFragment.update(true);
//                superFragment.setPlayBanner(true);

            }
            homeInfoIcon.setVisibility(View.VISIBLE);
            homeMessageIcon.setVisibility(View.VISIBLE);
            imgContent.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.GONE);
        } catch (Exception e) {
            mPullToRefresh.refreshComplete();
        }

    }

    /**
     * 预约等待界面
     */
    public void switchWait(Bundle bundle) {
        FrameLayout fragment = (FrameLayout) contentView.findViewById(R.id.content_home_content);
        WaitServiceFragment waitServiceFragment;
        waitServiceFragment = (WaitServiceFragment) getChildFragmentManager().findFragmentByTag("waitservice");
        if (waitServiceFragment == null) {
            waitServiceFragment = new WaitServiceFragment();
            if (bundle != null)
                waitServiceFragment.setArguments(bundle);
            waitServiceFragment.binding(this);
            fragment.removeAllViews();
            getChildFragmentManager().beginTransaction().replace(R.id.content_home_content, waitServiceFragment, "waitservice").commitNowAllowingStateLoss();
            homeInfoIcon.setVisibility(View.GONE);
            imgContent.setVisibility(View.GONE);
            homeMessageIcon.setVisibility(View.GONE);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText("预约");
        } else {
            waitServiceFragment.updateView(bundle);
        }
        mPullToRefresh.refreshComplete();
    }

    /**
     * 服务中
     *
     * @param bundle
     */
    public void switchServicing(Bundle bundle) {
        ServicingFragment servicingFragment;
        servicingFragment = (ServicingFragment) getChildFragmentManager().findFragmentByTag("Servicing");
        if (servicingFragment == null) {
            servicingFragment = new ServicingFragment();
            if (bundle != null)
                servicingFragment.setArguments(bundle);
            servicingFragment.binding(this);
            getChildFragmentManager().beginTransaction().replace(R.id.content_home_content, servicingFragment, "Servicing").commitNowAllowingStateLoss();
            homeInfoIcon.setVisibility(View.GONE);
            homeMessageIcon.setVisibility(View.GONE);
            imgContent.setVisibility(View.GONE);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText("服务");
        } else {
            if (bundle != null)
                servicingFragment.setArguments(bundle);
            servicingFragment.upView(bundle);
        }
        mPullToRefresh.refreshComplete();
    }

    /*普通服务中*/
    public void switchServicingBasics(Bundle bundle) {
        ServicingBasicsFragment servicingBasFragment;
        servicingBasFragment = (ServicingBasicsFragment) getChildFragmentManager().findFragmentByTag("Servicing");
        if (servicingBasFragment == null) {
            servicingBasFragment = new ServicingBasicsFragment();
            if (bundle != null)
                servicingBasFragment.setArguments(bundle);
            servicingBasFragment.binding(this);
            getChildFragmentManager().beginTransaction().replace(R.id.content_home_content, servicingBasFragment, "Servicing").commitNowAllowingStateLoss();
            homeInfoIcon.setVisibility(View.GONE);
            homeMessageIcon.setVisibility(View.GONE);
            imgContent.setVisibility(View.GONE);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText("服务");
        } else {
            if (bundle != null)
                servicingBasFragment.setArguments(bundle);
            servicingBasFragment.upView(bundle);
        }
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
            if (bundle != null)
                lineUpFragment.setArguments(bundle);
            lineUpFragment.binding(this);
            getChildFragmentManager().beginTransaction().replace(R.id.content_home_content, lineUpFragment, "lineup").commitNowAllowingStateLoss();
        } else {
            lineUpFragment.loadView(bundle);
        }
        homeInfoIcon.setVisibility(View.GONE);
        homeMessageIcon.setVisibility(View.GONE);
        imgContent.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("排队");
        mPullToRefresh.refreshComplete();
    }

//    public void getStateByDeviceType() {
//        params.addCallBack(this);
//        params.getStateByDeviceType(mDevice_type_key);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRefresh() {
        LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
        if (!mDevice_type_key.equals("kalushit")) {
            Log.d("lsy", "onRefresh");
            exchangeMsg(1);
        }
    }


    /**
     * 推送回调广播接收者
     * 此回调处理 1.主线定时刷新时长 2.刷新当前主线一次
     */
    private MessageReceiver messageReceiver = new MessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            SPUtils.saveBoolean("new_message", true);
            String settingEntiy = intent.getStringExtra("entity");
            if (!TextUtils.isEmpty(settingEntiy) && settingEntiy != null) {
                SettingsEntity settingsEntity = new Gson().fromJson(settingEntiy, SettingsEntity.class);
                DELAYEDTIME_REFRESH = settingsEntity.getRefreshInterval() * 1000;
                homeHandler.removeCallbacksAndMessages(0);
                onResume();
            } else {
                messageState();
                if (NetworkUtil.isNetworkAvailable())
                    exchangeMsg(0);
            }
        }
    };


    public void mqtChangeUserStatus(ExChangMsg msg) {
        if (msg.getType().equals("turnOffReturn")) {
            //订单信息
            LoadingSpecialDialog.dismissLoadingAnimDialog();
            orderLastID = msg.getId();
            Map map1 = (Map) msg.getContent();
            TurnOff mTurnOff = new Gson().fromJson(GsonUtils.parseMapToJson(map1), TurnOff.class);
            if (mTurnOff.getAutoPay() == 1) {//自动扣费
                Intent intent = new Intent(getContext(), PayResult.class);
                intent.putExtra("payType", deviceInfo.getDevice_type_key());
                intent.putExtra("entity", GsonUtils.parseMapToJson(map1));
                startActivity(intent);
                homeHandler.removeCallbacksAndMessages(0);
            } else {//手动扣费
                Intent intent = new Intent(getContext(), PayActivity.class);
                intent.putExtra("entity", GsonUtils.parseMapToJson(map1));
                startActivity(intent);
                homeHandler.removeCallbacksAndMessages(0);
            }
        }

        if (msg.getType().equals("userState")) {  //用户状态
            Map map2 = (Map) msg.getContent();
            String gson1 = GsonUtils.parseMapToJson(map2);
            ExChangUserState userState = new Gson().fromJson(gson1, ExChangUserState.class);
            if (!userState.getDeviceInfo().toString().isEmpty()) {//用户使用设备状态
                Map map1 = (Map) userState.getDeviceInfo();
                String devicestr = GsonUtils.parseMapToJson(map1);
                deviceInfo = new Gson().fromJson(devicestr, ExChangDeviceInfo.class);
                reservedLastID = deviceInfo.getId();
            }
            if (userState.getUserState().equals("normal")) {//正常预约
                originFrag(mWaitbundle);
//                            isShowTopDeviceType(false);
            } else if (userState.getUserState().equals("reserved")) {
                //预约中

                ShowerOrderEntity entity = new ShowerOrderEntity();
                entity.setDeviceAreaName(deviceInfo.getDeviceAreaName());
                entity.setMaxWaitTime(deviceInfo.getMax_wait_time());
                entity.setDeviceNumber(deviceInfo.getDevice_number());
                entity.setDeviceTypeKey(deviceInfo.getDevice_type_key());
                entity.setUserDevicePwd(deviceInfo.getUser_device_password());
                entity.setUuid(deviceInfo.getUuid());
                entity.setAllowRemoteControl(userState.getAllowRemoteControl());
                entity.setRemainTime(userState.getRemainTime());
                Bundle bundle = new Bundle();
                bundle.putString("Device_name", deviceInfo.getDevice_name());
                bundle.putSerializable("bean", entity);
//                            bundle.putInt("remainTime", userState.getRemainTime());
                switchWait(bundle);
            } else if (userState.getUserState().equals("running")) {
                //使用中 服务中
                stateTip = 0;
//                            WaitServiceFragment.waitStateTip = 0;
                LoadingSpecialDialog.dismissLoadingAnimDialog();
                if (mDevice_type_btn == 1) {
                    InServiceEntity entity = new InServiceEntity();
                    String Device_name = deviceInfo.getDevice_name();
                    List<String> re = Arrays.asList(Device_name.split(","));
                    entity.setRequest_id(deviceInfo.getDevice_key());
                    entity.setSub_devices(re);
                    Bundle bundle = new Bundle();
                    bundle.putString("payment_style", mPayment_style);//支付类型
                    bundle.putString("uuid", deviceInfo.getUuid());
                    bundle.putString("deviceType", deviceInfo.getDevice_type_key());
                    bundle.putString("devicePwd", deviceInfo.getUser_device_password());
                    bundle.putSerializable("bean", entity);
                    bundle.putString("serverTime", msg.getTime());//服务器时间
                    bundle.putString("startTime", deviceInfo.getStart_time());//开始时间
                    bundle.putString("deviceName", deviceInfo.getDeviceAreaName());//浴室位置
                    switchServicing(bundle);
                } else {//普通服务中
                    InServiceEntity entity = new InServiceEntity();
                    String Device_name = deviceInfo.getDevice_name();
                    List<String> re = Arrays.asList(Device_name.split(","));
                    entity.setRequest_id(deviceInfo.getDevice_key());
                    entity.setSub_devices(re);
                    Bundle bundle = new Bundle();
                    bundle.putString("payment_style", mPayment_style);//支付类型
                    bundle.putString("uuid", deviceInfo.getUuid());
                    bundle.putString("deviceType", deviceInfo.getDevice_type_key());
                    bundle.putString("devicePwd", deviceInfo.getUser_device_password());
                    bundle.putSerializable("bean", entity);
                    bundle.putString("serverTime", msg.getTime());//服务器时间
                    bundle.putString("startTime", deviceInfo.getStart_time());//开始时间
                    switchServicingBasics(bundle);
                }
            } else if (userState.getUserState().equals("queuing")) {
                //排队中
                Map queuingMap = (Map) userState.getQueueInfo();
                QueuingInfo queuingInfo = new Gson().fromJson(GsonUtils.parseMapToJson(queuingMap), QueuingInfo.class);
                QueuingLastID = queuingInfo.getId();//排队id
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", queuingInfo);
                bundle.putString("serverTime", msg.getTime());//服务器时间
                switchLineUp(bundle);
            }
        }


    }


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

    }

}