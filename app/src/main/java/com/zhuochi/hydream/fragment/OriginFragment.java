package com.zhuochi.hydream.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.activity.PayActivity;
import com.zhuochi.hydream.activity.RechargeActivity;
import com.zhuochi.hydream.activity.UnpaidOrderActivity;
import com.zhuochi.hydream.adapter.ListFilterAdapter;
import com.zhuochi.hydream.adapter.SelectTimeAdapter;
import com.zhuochi.hydream.adapter.ShowerAdapter;
import com.zhuochi.hydream.adapter.ShowerListAdapter;
import com.zhuochi.hydream.base.OnItemClickListener;
import com.zhuochi.hydream.base.ViewHolder;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.H5Dialog;
import com.zhuochi.hydream.dialog.LoadingRefreshDialog;
import com.zhuochi.hydream.dialog.LoadingSpecialDialog;
import com.zhuochi.hydream.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.dialog.OpIndexDialog;
import com.zhuochi.hydream.dialog.SettingCabinetPWD;
import com.zhuochi.hydream.dialog.TipDialog2;
import com.zhuochi.hydream.dialog.TipDialogMessage;
import com.zhuochi.hydream.entity.BatheFloorEntity;
import com.zhuochi.hydream.entity.ErrorData;
import com.zhuochi.hydream.entity.HomeShowerListEntity;
import com.zhuochi.hydream.entity.SeesionInfor;
import com.zhuochi.hydream.entity.ShowerOrderEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.exchang.WaitingInfo;
import com.zhuochi.hydream.entity.pushbean.InitSettingEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.RequestURL;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.DialogUtils;
import com.zhuochi.hydream.utils.DimensUtil;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.Tools;
import com.zhuochi.hydream.utils.UserUtils;
import com.zhuochi.hydream.utils.Util;
import com.zhuochi.hydream.view.AutoHorizontalScrollTextView;
import com.zhuochi.hydream.view.CustomListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 首页预约的界面
 * Created by and on 2016/11/10.
 */

public class OriginFragment extends BaseHomeFragment {
    private final String TAG = "OriginFragment";
    @BindView(R.id.home_bathroom_location)
    TextView homeBathroomLocation;
    @BindView(R.id.home_list_gridview)
    GridView homeListGridview;
    @BindView(R.id.home_tip)
    TextView homeTip;
    @BindView(R.id.choiceroom_listview)
    CustomListView choiceroomListview;
    @BindView(R.id.origin_filter_content)
    RelativeLayout originFilterContent;
    @BindView(R.id.origin_next)
    Button originNext;
    @BindView(R.id.upline_line1)
    TextView uplinenumber;//当前排队的人数
    @BindView(R.id.upline_time)
    TextView uplineTime;//预计排队的时间
    @BindView(R.id.upline_status)
    RelativeLayout uplineStatus;//显示排队
    @BindView(R.id.arrow)
    ImageView arrow;
    @BindView(R.id.origin_root)
    View originRoot;
    @BindView(R.id.scrollview_choiceroom)
    ScrollView scrollview;
    @BindView(R.id.vertical_textview)
    Util verticalTextview;
    @BindView(R.id.origin_errbg)
    View errbgView;
    @BindView(R.id.origin_errtip)
    TextView errTipView;
    @BindView(R.id.include_no)
    View inCludeNo;
    @BindView(R.id.line_tip)
    LinearLayout mLineTip;      //判断显示图标提示
    @BindView(R.id.line_tip_not)
    LinearLayout line_tip_not;
    @BindView(R.id.img_Optional)
    ImageView imgOptional;        //可选
    @BindView(R.id.img_Selected)
    ImageView imgSelected;        //已选
    @BindView(R.id.img_not_optional)
    ImageView imgNotOptional;     //不可选
    @BindView(R.id.tv_sort_tip)
    TextView tvSortTip;
    @BindView(R.id.home_choice_bathroomlocation)
    RelativeLayout homeChoiceBathroomlocation;
    @BindView(R.id.home_hint)
    LinearLayout homeHint;
    @BindView(R.id.img_cha)
    ImageView imgCha;
    @BindView(R.id.tv_hin)
    TextView tvHin;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.line_selectTime)
    RelativeLayout lineSelectTime;
    @BindView(R.id.text_line)
    LinearLayout textLine;
    //    @BindView(R.id.vertical_textviews)
//    Util utils;
    private ShowerListAdapter listAdapter;
    private List<HomeShowerListEntity> mList;
    private final int MESSAGEID = 99;
    private int DELAYEDTIME = 5000;
    private final String START = "开 始";
    private final String APPOINTMENT = "预 约";

    private final String LINEUP = "排 队";
    private XiRequestParams params;
    private List<BatheFloorEntity> entityList;
    private String mDeviceTypeKey = "";
    private int mDeviceTypeBtn = -1;
    private List mFeeTypeList;
    private String mShort_tip = "";//显示顶部提示
    private SelectTimeAdapter mSelectTimeAdapter;//选择时间适配器
    private String mSelectPrice = "";//选中价格
    private String mSelectTime = "";//选中时间
    public static String mUUid = "";
    public static boolean IsImg_load = false;
    private String mNotice = "";//判断字符串
    /**
     * 弹窗是否显示
     */
    private boolean ishow = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGEID) {
                if (NetworkUtil.isNetworkAvailable())
                    if (getHomeContent() != null) {
                        getHomeContent().exchangeMsg(0);
                    }
                DELAYEDTIME = HomeContent.DELAYEDTIME_REFRESH;
                handler.sendEmptyMessageDelayed(MESSAGEID, DELAYEDTIME);
            }
        }
    };
    private View mView;
    private H5Dialog waterDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_origin_new, null);
        ButterKnife.bind(this, mView);

//        ShowerListAdapter = null;
        params = new XiRequestParams(getActivity());

        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        try {
            stateTip = 0;
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }
            super.onDestroy();
        } catch (Exception e) {
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


    @Override
    public void onStop() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        stateTip = 0;
        super.onStop();
    }

    /**
     * 隐藏浴室下拉列表
     */
    private void dismissBathroomList() {
        originFilterContent.setVisibility(View.GONE);
        arrow.setImageResource(R.mipmap.arrow_down);
        ishow = false;
        if (bindresult())
            getHomeContent().setCanPull(true);
    }


    //获取首页下拉（原浴室）列表
    public void getDeviceAreasWithDeviceState(String typeKey, int device_type_btn, String short_tip, List feeTypeList) {
        if (params == null) {
            params = new XiRequestParams(getActivity());
        }
        mShort_tip = short_tip;
        mDeviceTypeKey = typeKey;
        mDeviceTypeBtn = device_type_btn;
        mFeeTypeList = feeTypeList;
        params.addCallBack(this);
        params.getDeviceAreasWithDeviceState(UserUtils.getInstance(getActivity()).getOrgAreaID(), typeKey);
    }

    //获取首页列表
    public void getDevicesByAreaId(int areald, String deviceTypeKey) {
        params.addCallBack(this);
        params.selectDevicesByAreaId(areald, deviceTypeKey);
    }

    /**
     * 根据浴室ID获取提示文字
     *
     * @param deviceAreaId
     */
    public void getDeviceTip(int deviceAreaId) {
        params.addCallBack(this);
        params.getDeviceAreaById(deviceAreaId);
    }


    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "selectDeviceAreasWithDeviceState"://获取首页下拉
                if (TextUtils.equals("washer", mDeviceTypeKey)) {
                    mDeviceTypeBtn = -1;
                }
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                if (jsonArray.size() > 0) {
                    entityList = JSON.parseArray(JSON.toJSONString(jsonArray), BatheFloorEntity.class);
                    String Device_area_name = SPUtils.getString("Device_area_name", "");
                    int Device_area_id = SPUtils.getInt("Device_area_id", 0);
                    if (Common.MAIN_REFRESH == 1) {
                        Common.MAIN_REFRESH = 0;
                        homeBathroomLocation.setText(entityList.get(0).getDevice_area_name());
                        SPUtils.saveString("Device_area_name", entityList.get(0).getDevice_area_name());
                        SPUtils.saveInt("Device_area_id", entityList.get(0).getDevice_area_id());
                        getDevicesByAreaId(entityList.get(0).getDevice_area_id(), mDeviceTypeKey);
                        getDeviceTip(entityList.get(0).getDevice_area_id());
                    } else {
                        if (!Device_area_name.isEmpty() && Device_area_id != 0) {
                            homeBathroomLocation.setText(Device_area_name);
                            getDevicesByAreaId(Device_area_id, mDeviceTypeKey);
                            getDeviceTip(Device_area_id);
                        } else {
                            homeBathroomLocation.setText(entityList.get(0).getDevice_area_name());
                            getDevicesByAreaId(entityList.get(0).getDevice_area_id(), mDeviceTypeKey);
                            getDeviceTip(entityList.get(0).getDevice_area_id());
                        }
                    }
                    if (stateTip == 1) {
                        if (lineSelectTime.getVisibility() == View.VISIBLE) {
                            onBackClick();
                        }
                        stateTip = 0;
                    }

                }
                break;
            case "selectDevicesByAreaId"://获取首页列表数据
                OpIndexDialog dialog = new OpIndexDialog(getActivity());
                dialog.setPos(0);
                dialog.show();
                JSONArray jsonArray1 = new JSONArray((ArrayList) result.getData().getData());
                if (jsonArray1.size() > 0) {
                    mList = JSON.parseArray(JSON.toJSONString(jsonArray1), HomeShowerListEntity.class);
                    updateList(mList);
                    inCludeNo.setVisibility(View.GONE);

                } else {
                    if (listAdapter != null) {
                        listAdapter.notifyDataSetInvalidated();
                        listAdapter = null;
                        homeListGridview.setAdapter(listAdapter);
                    }
                    inCludeNo.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(mShort_tip)) {
                        tvSortTip.setVisibility(View.VISIBLE);
                        tvSortTip.setText(mShort_tip);
                    }
                    if (mDeviceTypeBtn != 1) {
                        originNext.setText(START);//开始
                    }

                    StartDevice();
                }
                break;
            case "reserve"://预约
                ToastUtils.show(result.getData().getMsg());
//                Map map = (Map) result.getData().getData();
                try {
//                    String gson = GsonUtils.parseMapToJson(map);
//                    ShowerOrderEntity entity = new Gson().fromJson(gson, ShowerOrderEntity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id", "");
//                    bundle.putSerializable("bean", entity);
                    if (bindresult())
                        getHomeContent().exchangeMsg(0);
//                        getHomeContent().switchWait(bundle);
                } catch (Exception e) {
                    e.printStackTrace();

                }
                break;

            case "setDevicePwd"://设置 设备密码
                ToastUtils.show(result.getData().getMsg());
                break;
            case "queueUp"://排队成功
                ToastUtils.show(result.getData().getMsg());
                if (result.getData().getData() instanceof Map) {
                    Map map1 = (Map) result.getData().getData();
                    String gson = GsonUtils.parseMapToJson(map1);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean", gson);
                    bundle.putString("id", "");
                }
                if (getHomeContent() != null) {
                    getHomeContent().exchangeMsg(0);
                }
                break;
            case "getDeviceAreaById"://获取浴室提示文字
                Map mapNotice = (Map) result.getData().getData();
                BatheFloorEntity msgEntity = new Gson().fromJson(GsonUtils.parseMapToJson(mapNotice), BatheFloorEntity.class);
                if (!mNotice.equals(msgEntity.getReminder())) {
                    showNotice(msgEntity.getReminder());
                }

                break;
            case "turnOn"://开始服务
                if (getHomeContent() != null) {
                    getHomeContent().exchangeMsg(1);
                }
                break;
            case "UserApi/getNewSessionId":
                if (result.getData().getCode() == 200) {
                    SeesionInfor data = JSON.parseObject(JSON.toJSONString(result.getData().getData()), SeesionInfor.class);
//                    PHPSESSID=vc2gp11shifvtif73i3tq7vdkg&uuid=aaabbbccc
                    waterDialog = new H5Dialog(getActivity(), "http://lz.hydream.cn/apipage/device/washer?" + data.getSessionName() + "=" + data.getSessionId() + "&uuid=" + mUUid);

                    waterDialog.show();
                    DialogUtils.bottomMatchWidhtScreen(waterDialog);
                }
                break;
        }

        super.onRequestSuccess(tag, result);
    }


    /**
     * 显示公告
     */
    private void showNotice(String noticeTitle) {

        textLine.setVisibility(View.VISIBLE);
        //公告
        if (noticeTitle != null && !TextUtils.isEmpty(noticeTitle)) {
            textLine.removeAllViews();
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.view_incode_tip, null);
            AutoHorizontalScrollTextView textView = (AutoHorizontalScrollTextView) view.findViewById(R.id.vertical_textview);
            textView.setText(noticeTitle);
            mNotice = noticeTitle;
            textLine.addView(view);
        } else {
            textLine.removeAllViews();
            mNotice = "";
            textLine.setVisibility(View.GONE);
        }
    }


    public void updateList(final List<HomeShowerListEntity> shower) {
        if (listAdapter == null) {
            listAdapter = new ShowerListAdapter(shower, getActivity());
            homeListGridview.setAdapter(listAdapter);
            listAdapter.setItemClickListener(new ShowerListAdapter.ItemClickListener() {
                @Override
                public void change(String item, String deviceTypekey) {
                    if (!(TextUtils.isEmpty(deviceTypekey))) {
                        originNext.setTag(R.id.origin_next, deviceTypekey);
                    }
                    mUUid = item;
                    originNext.setBackgroundResource(R.drawable.selector_bg_corner_blue);
                }
            });
        } else {
            listAdapter.notifyDataSetChanged(shower);
        }
        originNext.setText(LINEUP);//排队
        for (int i = 0; i < shower.size(); i++) {
            if (shower.get(i).getDevice_status().equals("ready")) {
                originNext.setText(APPOINTMENT);//预约
            }
        }
        if (!TextUtils.isEmpty(mShort_tip)) {
            tvSortTip.setVisibility(View.VISIBLE);
            tvSortTip.setText(mShort_tip);
        }
        if (mDeviceTypeBtn != 1) {
            originNext.setText(START);//开始
        }
        uplineStatus.setVisibility(View.GONE);
        if (originNext.getText().toString().equals(APPOINTMENT)) {//预约
            if (!TextUtils.isEmpty(mUUid)) {//已选中
                mLineTip.setVisibility(View.GONE);
                line_tip_not.setVisibility(View.VISIBLE);
                Bundle bundle = getArguments();
                String tip = bundle.getString("content");
                homeTip.setText(tip);
            } else {
                showBottomStyle();
                line_tip_not.setVisibility(View.GONE);
            }
        } else if (originNext.getText().toString().equals(LINEUP)) {//排队
            Bundle bundle = getArguments();
            mLineTip.setVisibility(View.GONE);
            uplineStatus.setVisibility(View.VISIBLE);
            line_tip_not.setVisibility(View.VISIBLE);
            WaitingInfo waitingInfo = (WaitingInfo) bundle.getSerializable("bean");
            uplinenumber.setText(waitingInfo.getQueuingNumber());
            int waitingTime = waitingInfo.getExpectedWaitingTime() / 60;
            uplineTime.setText(String.valueOf(waitingTime));
            homeTip.setText(waitingInfo.getMessage());

        } else {//开始
            StartDevice();

        }
    }

    /*
     * 开始
     * */
    private void StartDevice() {
        if (!TextUtils.isEmpty(mUUid)) {//已选中
            mLineTip.setVisibility(View.GONE);
            line_tip_not.setVisibility(View.VISIBLE);
            Bundle bundle = getArguments();
            String tip = bundle.getString("content");
            homeTip.setText(tip);
        } else {
            showBottomStyle();
            line_tip_not.setVisibility(View.GONE);
        }
    }


    /*显示底部按钮样式*/
    private void showBottomStyle() {
        Bundle bundle = getArguments();
        uplineStatus.setVisibility(View.GONE);
        String tip = bundle.getString("content");
        homeTip.setText(tip);
        mLineTip.setVisibility(View.VISIBLE);
        if (!IsImg_load) {
            IsImg_load = true;
            String ImagereadyUrl = Common.ICON_BASE_URL + "/" + Constants.READY;//可选
            ImageLoadUtils.loadImage(getActivity(), ImagereadyUrl, imgOptional);
            String ImageNotSelectUrl = Common.ICON_BASE_URL + "/" + Constants.RUNNING;
            ImageLoadUtils.loadImage(getActivity(), ImageNotSelectUrl, imgNotOptional);//不可选
            String ImageSelectedUrl = Common.ICON_BASE_URL + "/" + Constants.SELECTED;//已选
            ImageLoadUtils.loadImage(getActivity(), ImageSelectedUrl, imgSelected);
        }
    }


    @OnClick({R.id.home_choice_bathroomlocation, R.id.filter_footer, R.id.origin_next, R.id.btn_cancel, R.id.btn_submit, R.id.img_cha})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_choice_bathroomlocation:
                if (ishow) {
                    originFilterContent.setVisibility(View.GONE);
                    arrow.setImageResource(R.mipmap.arrow_down);
                    ishow = false;
                } else {
//                    filterBathRoom();
                    if (entityList != null) {
                        showBathRoomFilter(entityList);
                    }
                }
                break;
            case R.id.filter_footer:
                if (originFilterContent.getVisibility() == View.VISIBLE) {

                    dismissBathroomList();
                }
                break;
            case R.id.origin_next:  //点击开始预约
                switch (originNext.getTag(R.id.origin_next).toString()) {
                    case "blower":
                    case "faucet":
//                        洗浴和吹风机的业务逻辑
                        if (TextUtils.equals(originNext.getText(), APPOINTMENT)) {
                            //预约
                            if (mUUid.isEmpty()) {
                                ToastUtils.show("请选择设备！！");
                                return;
                            }
                            for (int i = 0; i < mList.size(); i++) {
                                //为了防止不在同一浴室执行同一操作
                                if (mList.get(i).getDevice_key().equals(mUUid)) {
                                    mUUid = mList.get(i).getDevice_key();
                                    reserve();
                                    return;
                                }
                            }
                            ToastUtils.show("请选择设备！！");
                        } else if (TextUtils.equals(originNext.getText(), LINEUP)) {
                            //预约//点击开始排队
                            lineUp();
                        } else if (TextUtils.equals(originNext.getText(), START)) {
                            //开始使用
                            if (mUUid.isEmpty()) {
                                ToastUtils.show("请选择设备！！");
                                return;
                            }
                            for (int i = 0; i < mList.size(); i++) {
                                //为了防止不在同一浴室执行同一操作
                                if (mList.get(i).getDevice_key().equals(mUUid)) {
                                    mUUid = mList.get(i).getDevice_key();
                                    //显示吹风机选择时长
                                    ShowSelectTime();
                                    return;
                                }
                            }
                            ToastUtils.show("请选择设备！！");
                        }
                        break;
                    default:
//                            洗衣机等其它设备的业务逻辑
                        openWaterDialog();

                        break;


                }


                break;
            case R.id.btn_cancel:
            case R.id.img_cha:
                lineSelectTime.setVisibility(View.GONE);
                break;
            case R.id.btn_submit:
                if (!TextUtils.isEmpty(mSelectPrice)) {
                    //"turnOn"//开始服务
                    params.addCallBack(this);
                    params.turnOn(mUUid, mDeviceTypeKey, "time", mSelectPrice);
//                    btnSubmit.setClickable(false);
//                    btnSubmit.setBackgroundResource(R.drawable.shape_bg_corner_gray999);
                    loadDatas();
                } else {
                    ToastUtils.show("请选择使用时长！！");
                }

                break;

        }
    }

    private void openWaterDialog() {
        params.addCallBack(this);
        Map<String, Object> pram = new HashMap<String, Object>();
        pram.put("token", UserUtils.getInstance(getActivity()).getTokenID());
        params.comonRequest(pram, "UserApi/getNewSessionId");
    }

    public static int stateTip = 0;
    private TimerTask task;
    private InitSettingEntity mInitEntity;

    private void loadDatas() {
        LoadingSpecialDialog.showLoadingAnimDialog(getActivity());
        task = new TimerTask() {
            @Override
            public void run() {
                stateTip = 1;
            }
        };
        String mGson = SPUtils.getString("initSetting", "");
        if (!TextUtils.isEmpty(mGson)) {
            mInitEntity = new Gson().fromJson(mGson, InitSettingEntity.class);
            Timer timer = new Timer();
            timer.schedule(task, mInitEntity.getDeviceTimeout() * 1000);
//            timer.schedule(task, 10 * 1000);

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
                stateTip = 0;
                lineSelectTime.setVisibility(View.GONE);
            }

        }, "", mInitEntity.getDeviceTimeoutStartTip());

    }

    /*显示选择时间弹窗*/
    private void ShowSelectTime() {
        lineSelectTime.setVisibility(View.VISIBLE);
        if (mSelectTimeAdapter == null) {
            mSelectTimeAdapter = new SelectTimeAdapter(getActivity(), mFeeTypeList);
            listView.setAdapter(mSelectTimeAdapter);
            mSelectTimeAdapter.setItemClickListener(new SelectTimeAdapter.ItemClickListener() {
                @Override
                public void change(String price, String time) {
                    mSelectPrice = price;
                    mSelectTime = time;
                }
            });
        } else {
            mSelectTimeAdapter.notifyDataSetChanged();
        }
    }

    /*排队*/
    private void lineUp() {
        int device_area_id = SPUtils.getInt("Device_area_id", 0);
        params.addCallBack(this);
        params.queueUp(device_area_id, mDeviceTypeKey);
    }

    //点击开始预约
    private void reserve() {
        params.addCallBack(this);
        params.reserve(mUUid);//9019df64-ff05-433f-a8c0-51ce63d36265_faucet_01
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        LoadingSpecialDialog.dismissLoadingAnimDialog();
        stateTip = 0;
        String str = new Gson().toJson(s).toString();
        ErrorData errorData = new Gson().fromJson(str, ErrorData.class);
        int code = errorData.getCode();
        switch (tag) {
            case "reserve"://预约时错误
            case "turnOn":
            case "queueUp"://排队时错误
                if (code == 132) {//您尚未设置设备密码
                    ToastUtils.show(errorData.getMsg());
                    showSettingPwd();
                } else if (code == 137) {//请不要使用简单密码
                    ToastUtils.show(errorData.getMsg());
                } else if (code == 130 || code == 170) {//您尚未预约任何设备
                    ToastUtils.show(errorData.getMsg());
                } else if (code == 122) {//设备 被占用
                    ToastUtils.show(errorData.getMsg());
                } else if (code == 211) {  //跳转未完成订单界面
                    UnpaidOrder();
                } else if (code == 212) { //押金不足
                    DepositOrder();
                } else if (code == 281) {//余额不足
                    BalanceOrder();
                } else {
                    ToastUtils.show(errorData.getMsg());
                }
                break;
        }
        super.onRequestFailure(tag, s);
    }

    /*跳转未完成订单界面*/
    private void UnpaidOrder() {
        Dialog dialog = TipDialog2.show_(getContext(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UnpaidOrderActivity.class));
            }
        }, "提示信息", "您有未支付的订单，请支付？");
    }

    /*押金不足*/
    private void DepositOrder() {
        Dialog dialog = TipDialog2.show_(getContext(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent payIntent = new Intent(getActivity(), RechargeActivity.class);
                payIntent.putExtra("PayType", "deposit");//押金
                startActivity(payIntent);
            }
        }, "提示信息", "请前去交押金");

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


    public void showBathRoomFilter(List<BatheFloorEntity> list) {
        if (bindresult())
            getHomeContent().setCanPull(false);
        originFilterContent.setVisibility(View.VISIBLE);
        if (list.size() > 5) {
            ViewGroup.LayoutParams layoutParams = scrollview.getLayoutParams();
            layoutParams.height = DimensUtil.dpToPixel(getContext(), 50) * 5;
            scrollview.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = scrollview.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            scrollview.setLayoutParams(layoutParams);
        }
        final ListFilterAdapter filterAdapter = new ListFilterAdapter(list, R.layout.item_choice_bathroom, getActivity(), SPUtils.getInt("Device_area_id", 0));
        choiceroomListview.setAdapter(filterAdapter);
        filterAdapter.setOnItemClickListener(new OnItemClickListener<BatheFloorEntity>() {
            @Override
            public void onItemClick(BatheFloorEntity item, ViewHolder holder, int position) {
                SPUtils.saveString("Device_area_name", item.getDevice_area_name());
                SPUtils.saveInt("Device_area_id", item.getDevice_area_id());
//                SPUtils.saveInt(Constants.DEVICE_AREA_ID, item.getDefault_device_area_id());//当前绑定区域（浴室）
                SPUtils.saveInt(Constants.DEVICE_AREA_ID, item.getDevice_area_id());//当前绑定区域（浴室）
                homeBathroomLocation.setText(item.getDevice_area_name());
                mUUid = "";
                getDeviceTip(item.getDevice_area_id());
                filterAdapter.setBID(item.getDevice_area_id());
                LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
                getDevicesByAreaId(item.getDevice_area_id(), mDeviceTypeKey);
                dismissBathroomList();
            }
        });
        enterAnimation(scrollview);
    }


    /**
     * 进入动画
     *
     * @param v
     */
    private void enterAnimation(View v) {
        int from = Tools.measureHeight(v);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -from, 0);
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        translateAnimation.setDuration(200);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                arrow.setImageResource(R.mipmap.arrow_up);
                ishow = true;
            }
        });
        v.clearAnimation();
        v.setAnimation(translateAnimation);
    }

    /**
     * 退出动画
     *
     * @param v
     */
    private void quitAnimation(final View v) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -Tools.measureHeight(v));
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        translateAnimation.setDuration(200);
        v.clearAnimation();
        v.setAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismissBathroomList();
            }
        });
    }

    /**
     * 设置开柜密码
     */
    public void showSettingPwd() {
        SettingCabinetPWD.Builder builder = new SettingCabinetPWD.Builder(getActivity());
        builder.create().show();
        builder.setConfirm(new SettingCabinetPWD.OnConfirmListener() {
            @Override
            public void confirm(String pwd) {
                params.addCallBack(OriginFragment.this);
                params.setDevicePwd(pwd);
            }
        });
    }

    public void onResume() {
        super.onResume();
        if (NetworkUtil.isNetworkAvailable())
            handler.sendEmptyMessageDelayed(MESSAGEID, DELAYEDTIME);
    }

    @Override
    public void onPause() {
        if (task != null) {
            task.cancel();
        }
        stateTip = 0;
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        if (task != null) {
            task.cancel();
        }
        stateTip = 0;
        super.onDestroyView();
    }
}
