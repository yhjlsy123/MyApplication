package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.adapter.BlowerListAdapter;
import com.zhuochi.hydream.api.Neturl;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.bean_.BathRoomFilter;
import com.zhuochi.hydream.bean_.BathendOrder;
import com.zhuochi.hydream.bean_.ShowerList;
import com.zhuochi.hydream.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.http.VolleySingleton;
import com.zhuochi.hydream.utils.DimensUtil;
import com.zhuochi.hydream.utils.JsonUtils;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.Tools;
import com.zhuochi.hydream.view.AutoHorizontalScrollTextView;
import com.zhuochi.hydream.view.CustomListView;
import com.zhuochi.hydream.view.pulltorefresh.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 吹风机首页预约的界面
 * Created by and on 2016/11/10.
 */

public class BlowerActivity extends BaseAutoActivity implements RefreshLayout.OnRefreshListener {
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
    RelativeLayout uplineStatus;
    @BindView(R.id.arrow)
    ImageView arrow;
    @BindView(R.id.origin_root)
    View originRoot;
    @BindView(R.id.scrollview_choiceroom)
    ScrollView scrollview;
    @BindView(R.id.vertical_textview)
    AutoHorizontalScrollTextView verticalTextview;
    @BindView(R.id.origin_errbg)
    View errbgView;
    @BindView(R.id.origin_errtip)
    TextView errTipView;
    @BindView(R.id.input_pwd)
    EditText input_pwd;
    @BindView(R.id.refreshLayout)
    RefreshLayout mPullToRefresh;
    private RadioButton radioTimeSmall;//选择最小时长
    private RadioButton radioTimeMax;//选择最大时长
    private RadioGroup mRadioGroup;
    private RelativeLayout mBathroomlocation, mlineShowPwd;
    private BlowerListAdapter listAdapter;
    private Map<String, String> map = new HashMap<>();
    private final int MESSAGEID = 99;
    private final int DELAYEDTIME = 10000;
    private final String APPOINT = "预 约";
    private final String LINEUP = "排 队";
    private String blowerLoaction;
    private String mNUM = "";//吹风机 选择标签
    private String mCurrentUUid = "";//吹风机 uuid
    private int Time = 300;//
    private int mPosition = 0;
    private int state = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGEID) {
                update();
                if (NetworkUtil.isNetworkAvailable())

                    handler.sendEmptyMessageDelayed(MESSAGEID, DELAYEDTIME);
            }
        }
    };

    @Override
    public void onDestroy() {
        try {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }
            super.onDestroy();
        } catch (Exception e) {
        }
    }

    @Override
    public void onStop() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onStop();
    }

    public void onResume() {
        super.onResume();
        if (NetworkUtil.isNetworkAvailable())
            handler.sendEmptyMessageDelayed(MESSAGEID, 3000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blower);
        ButterKnife.bind(this);
        mPullToRefresh.setRefreshListener(BlowerActivity.this);
        InitView();

    }

    /**
     * 设置是否支持下拉刷新数据
     *
     * @param canPull
     */
    public void setCanPull(boolean canPull) {
        mPullToRefresh.setEnabled(canPull);
    }

    private void InitView() {
        radioTimeSmall = (RadioButton) findViewById(R.id.radio_time_small);
        radioTimeMax = (RadioButton) findViewById(R.id.radio_time_max);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.radio_time_small == checkedId) {
//                    ToastUtils.show("选择5分钟");
                    Time = 3 * 60;
                } else if (R.id.radio_time_max == checkedId) {
//                    ToastUtils.show("选择10分钟");
                    Time = 5 * 60;
                }
            }
        });
        mBathroomlocation = (RelativeLayout) findViewById(R.id.home_choice_bathroomlocation);
        mBathroomlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBathRoom(true);
            }
        });
        SPUtils.saveString("b_id", "");
        filterBathRoom(false);
        mlineShowPwd = (RelativeLayout) findViewById(R.id.line_show_pwd);

    }


    /**
     * 获取主界面吹风机列表
     */
    public void update() {
        //获取用户上次的选择
        String b_id = SPUtils.getString("b_id", "");
        map.put("b_id", b_id);//吹风机总控ID
//        VolleySingleton.post(SHOW_BLOWER_DEVICE, "show_blower_device", map, new VolleySingleton.CBack() {
//            @Override
//            public void runUI(String result) {
//                BathendOrder showerList = JsonUtils.parseJsonToBean(result, BathendOrder.class);
//                if (showerList != null && showerList.data.getDeviceLlist() != null) {
//                    if (showerList.data.getDefault_choice() != null) {
//                        mCurrentUUid = showerList.data.getDefault_choice().getUuid();
//                        String tips = showerList.data.getDefault_choice().getTips();
//                        homeTip.setText(tips);
//                    }
//
//                    updateList(showerList.data.getDeviceLlist());
//                    if (state == 1) {
//                        ToastUtils.show("开启吹风机成功！");
//                        LoadingTrAnimDialog.dismissLoadingAnimDialog();
//                    }
//                    state --;
//                }
//                mPullToRefresh.refreshComplete();
//            }
//        });
    }


    private ArrayList<ShowerList.Notice> noticeList;


    //给当前  界面set值
    public void updateList(final List<BathendOrder.BathendOrderData.DeviceLlistBean> shower) {
        if (listAdapter == null) {
            listAdapter = new BlowerListAdapter(shower);
            homeListGridview.setAdapter(listAdapter);
            listAdapter.setItemClickListener(new BlowerListAdapter.ItemClickListener() {
                @Override
                public void change(String item) {
                    mNUM = item;
                    map.put("num", item);
                }
            });
        } else {
            listAdapter.notifyDataSetChanged(shower);
        }
    }

    /**
     * 弹出下拉浴室列表
     */
    private synchronized void filterBathRoom(final boolean ishows) {
//        VolleySingleton.post(Neturl.GET_BLOWER_POSITION, "get_blower_position", new HashMap<String, String>(), new VolleySingleton.CBack() {
//            @Override
//            public void runUI(String result) {
//                BathRoomFilter bathRoomFilter = JsonUtils.parseJsonToBean(result, BathRoomFilter.class);
//                if (bathRoomFilter != null && bathRoomFilter.data != null) {
//                    showBathRoomFilter(bathRoomFilter.data, ishows);
//                    String b_id = SPUtils.getString("b_id", "");
//                    if (b_id.isEmpty()) {
//                        homeBathroomLocation.setText(bathRoomFilter.data.get(mPosition).b_name);
//                        SPUtils.saveString("b_id", bathRoomFilter.data.get(mPosition).b_id);
//                        update();
//                    }
//
//                }
//
//            }
//        });
    }

    /**
     * 检查是否拥有开柜密码(包含了认证、押金、退款、设置密码的验证)
     */
    private void checkLockPWD() {
//        VolleySingleton.post(Neturl.CHECK_LOCK_PWD, "checklock", new HashMap<String, String>(), new VolleySingleton.CBack() {
//            @Override
//            public void runUI(String result) {
//                CheckPWD bean = JsonUtils.parseJsonToBean(result, CheckPWD.class);
//                if (bean != null && bean.data != null) {
//                    if (TextUtils.equals("1", bean.data.need_auth) && TextUtils.equals("0", bean.data.auth.result)) {//1.检查是否需要认证
//                        Intent intent = new Intent(BlowerActivity.this, AutheActivity.class);
//                        intent.putExtra("from", "home");
//                        startActivity(intent);
//                        LoadingTrAnimDialog.dismissLoadingAnimDialog();
//                        // 判断是否需要交押金
//                    } else if (TextUtils.equals("1", bean.data.need_yajin) && TextUtils.equals("0", bean.data.yajin.result)) {//判断是否需要交押金
//                        LoadingTrAnimDialog.dismissLoadingAnimDialog();
//                        Intent intent = new Intent(BlowerActivity.this, RechargeYaJinActivity.class);
//                        intent.putExtra("from", "OriginFragment");
//                        startActivity(intent);
//                    } else if (TextUtils.equals("1", bean.data.need_yajin) && TextUtils.equals("2", bean.data.yajin.result)) {//正在退押金中
//                        LoadingTrAnimDialog.dismissLoadingAnimDialog();
//                        ToastUtils.show(bean.data.yajin.msg);
//                    } else if (TextUtils.equals("2", bean.data.account_refund.result)) {//正在退款
//                        LoadingTrAnimDialog.dismissLoadingAnimDialog();
//                        ToastUtils.show(bean.data.account_refund.msg);
//                    } else if (TextUtils.equals("1", bean.data.password.result)) {//已经设置过
//                        if (TextUtils.isEmpty(map.get("num")) && !TextUtils.equals(originNext.getText(), LINEUP)) {
//                            LoadingTrAnimDialog.dismissLoadingAnimDialog();
////                            ToastUtils.show("请先选择浴位");
//                            return;
//                        }
//                        StartUse();
//                    } else {
//                        LoadingTrAnimDialog.dismissLoadingAnimDialog();
//                        showSettingPwd();
//                    }
//                } else {
//                    LoadingTrAnimDialog.dismissLoadingAnimDialog();
//                }
//            }
//        });
    }


    @OnClick({R.id.filter_footer, R.id.origin_next, R.id.wallet_back, R.id.txt_confirm, R.id.txt_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.home_choice_bathroomlocation:
//                filterBathRoom();
//                if (ishow) {
//                    originFilterContent.setVisibility(View.GONE);
//                    arrow.setImageResource(R.mipmap.arrow_down);
//                    ishow = false;
//                    if (bindresult()) {
//                        getHomeContent().setCanPull(true);
//                    }
//                } else {
//                    filterBathRoom();
//                }
//                break;
            case R.id.filter_footer:
                if (originFilterContent.getVisibility() == View.VISIBLE) {
                    dismissBathroomList();
                }
                break;
            case R.id.origin_next:
                if (mNUM.isEmpty()) {
                    ToastUtils.show("请选择吹风机");
                } else {
                    checkLockPWD();

//                    mlineShowPwd.setVisibility(View.VISIBLE);

                }
                break;
            case R.id.wallet_back:
                finish();
                break;
            case R.id.txt_cancel:
                mlineShowPwd.setVisibility(View.GONE);
                break;
            case R.id.txt_confirm:
                if (input_pwd.getText().toString().isEmpty()) {
                    ToastUtils.show("请输入吹风机密码！");
                    return;
                }
                StartUse();

                break;
        }
    }

    /**
     * 开始使用
     */
    private void StartUse() {
        mlineShowPwd.setVisibility(View.GONE);
        Map<String, String> startMap = new HashMap<>();
//        startMap.put("phone", SPUtils.getString(Constants.PHONE_NUMBER, ""));//当前手机号
//        startMap.put("when_long", String.valueOf(Time));//选择使用时间  5分钟 10分钟
//        startMap.put("imei", mCurrentUUid);//UUID
//        startMap.put("device_seat", mNUM);//吹风机num唯一标识
//        startMap.put("device_pwd", "88888888");//吹风机开启密码
//        VolleySingleton.post(BEGIN_BLOWER, "begin_blower", startMap, new VolleySingleton.CBack() {
//            @Override
//            public void runUI(String result) {
//                Result resultBean = JsonUtils.parseJsonToBean(result, Result.class);
//                if (resultBean != null && resultBean.data != null) {
//                    ToastUtils.show(resultBean.data.msg);
////                    update();
//
//                    if (resultBean.data.status.equals("1")){
//                        LoadingTrAnimDialog.showLoadingAnimDialog(BlowerActivity.this);
//                        state = 1;
//                    }
//
//                }
//            }
//        });
    }


    /**
     * 跳转待服务界面
     *
     * @param json
     */
    private void toAppoint(String json) {
//        WaitORService waitORService = JsonUtils.parseJsonToBean(json, WaitORService.class);
//        if (waitORService != null && waitORService.data != null) {
//            ToastUtils.show(waitORService.data.msg);
//            Bundle bundle = new Bundle();
//            bundle.putString("id", waitORService.data.id);
//            bundle.putSerializable("bean", waitORService.data);
//        }
    }

    /**
     * 弹窗是否显示
     */
    private boolean ishow = false;

    //顶部下拉选项
    public void showBathRoomFilter(List<BathRoomFilter.Filter> list, boolean isShow) {
//        if (bindresult())
//            getHomeContent().setCanPull(false);
        if (isShow) {
            originFilterContent.setVisibility(View.VISIBLE);
        } else {
            originFilterContent.setVisibility(View.GONE);
        }

        if (list.size() > 5) {
            ViewGroup.LayoutParams layoutParams = scrollview.getLayoutParams();
            layoutParams.height = DimensUtil.dpToPixel(BlowerActivity.this, 50) * 5;
            scrollview.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = scrollview.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            scrollview.setLayoutParams(layoutParams);
        }
//        final ListBlowerAdapter filterAdapter = new ListBlowerAdapter(list, R.layout.item_choice_bathroom, this, map.get("b_id"));
//        choiceroomListview.setAdapter(filterAdapter);
//        filterAdapter.setOnItemClickListener(new OnItemClickListener<BathRoomFilter.Filter>() {
//            @Override
//            public void onItemClick(BathRoomFilter.Filter item, ViewHolder holder, int position) {
//                mPosition = position;
//                SPUtils.saveString("b_id", item.b_id);
////                SPUtils.saveString("blowerLoaction", item.b_name);
//                filterAdapter.setBID(item.b_id);
//                homeBathroomLocation.setText(item.b_name);
//                LoadingTrAnimDialog.showLoadingAnimDialog(BlowerActivity.this);
//                update();
////                quitAnimation(choiceroomListview);
//                dismissBathroomList();
//            }
//        });
        enterAnimation(scrollview);
    }

    /**
     * 隐藏浴室下拉列表
     */
    private void dismissBathroomList() {
        originFilterContent.setVisibility(View.GONE);
        arrow.setImageResource(R.mipmap.arrow_down);
        ishow = false;
//        if (bindresult())
//            getHomeContent().setCanPull(true);
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
     * 设置开柜密码
     */
    public void showSettingPwd() {
//        SettingCabinetPWD.Builder builder = new SettingCabinetPWD.Builder(this);
//        builder.create().show();
//        builder.setConfirm(new SettingCabinetPWD.OnConfirmListener() {
//            @Override
//            public void confirm(String pwd) {
//                HashMap<String, String> map = new HashMap<>();
//                map.put("ppwd", pwd);
//                VolleySingleton.post(Neturl.SET_LOCK_PWD, "set_pwd", map, new VolleySingleton.CBack() {
//                    @Override
//                    public void runUI(String result) {
//                        Result resultBean = JsonUtils.parseJsonToBean(result, Result.class);
//                        if (resultBean != null && resultBean.data != null) {
//                            ToastUtils.show(resultBean.data.msg);
//                            StartUse();
//                        }
//                    }
//                });
//            }
//        });
    }

    /**
     * 当有未支付订单时，弹此框
     */
    public synchronized void showShouldPayDialog() {
//        ShouldPayDialog.Builder builder = new ShouldPayDialog.Builder(this);
//        builder.create().show();
//        builder.setOnClickListener(new ShouldPayDialog.ShouldPayListener() {
//            @Override
//            public void pay() {
//                LoadingTrAnimDialog.showLoadingAnimDialog(BlowerActivity.this);
//                startActivity(new Intent(BlowerActivity.this, PayActivity.class));
//            }
//        });
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRefresh() {
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
//        filterBathRoom(false);
        update();
    }
}
