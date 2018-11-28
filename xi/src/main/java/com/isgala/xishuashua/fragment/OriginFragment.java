package com.isgala.xishuashua.fragment;

import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.adapter.ListFilterAdapter;
import com.isgala.xishuashua.adapter.ShowerListAdapter;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.OnItemClickListener;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.BathRoomFilter;
import com.isgala.xishuashua.bean_.CheckPWD;
import com.isgala.xishuashua.bean_.LineUpBean;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.bean_.ShowerList;
import com.isgala.xishuashua.bean_.WaitORService;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.dialog.SettingCabinetPWD;
import com.isgala.xishuashua.dialog.ShouldPayDialog;
import com.isgala.xishuashua.ui.AutheActivity;
import com.isgala.xishuashua.ui.PayActivity;
import com.isgala.xishuashua.ui.RechargeYaJinActivity;
import com.isgala.xishuashua.utils.DimensUtil;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.LogUtils;
import com.isgala.xishuashua.utils.NetworkUtil;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.Tools;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.isgala.xishuashua.view.AutoHorizontalScrollTextView;
import com.isgala.xishuashua.view.CustomListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ShowerListAdapter listAdapter;
    private Map<String, String> map = new HashMap<>();
    private final int MESSAGEID = 99;
    private final int DELAYEDTIME = 5000;
    private final String APPOINT = "预 约";
    private final String LINEUP = "排 队";
    //    private int number = -1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            if (msg.what == 199) {
//                number++;
//                verticalTextview.setText(noticeList.get(number%noticeList.size()).content);
//                handler.sendEmptyMessageDelayed(199, 3000);
//            } else
            if (msg.what == MESSAGEID) {
                update(false, false);
                if (NetworkUtil.isNetworkAvailable())
                    handler.sendEmptyMessageDelayed(MESSAGEID, DELAYEDTIME);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_origin_new, null);
        ButterKnife.bind(this, view);
        listAdapter = null;
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

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

    /**
     * 更新主界面
     *
     * @param flag   是否显示未支付订单的弹窗
     * @param notify 是否更新公告
     */
    public void update(final boolean flag, final boolean notify) {
        if (flag)
            LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
        //获取用户上次的选择
        String b_id = SPUtils.getString("b_id", "");
        if (!TextUtils.isEmpty(b_id) && !TextUtils.equals(b_id, map.get("b_id"))) {
            map.put("b_id", b_id);
            dismissBathroomList();
        }
        String bathroomLoaction = SPUtils.getString("bathroomLoaction", "");
        if (!TextUtils.isEmpty(bathroomLoaction)) {
            homeBathroomLocation.setText(bathroomLoaction);
        }
        map.put(Constants.LOCATON_LAT, SPUtils.getString(Constants.LOCATON_LAT, ""));
        map.put(Constants.LOCATON_LNG, SPUtils.getString(Constants.LOCATON_LNG, ""));
        VolleySingleton.post(Neturl.SHOWER_LIST, "shower_list", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                ShowerList showerList = JsonUtils.parseJsonToBean(result, ShowerList.class);
                if (showerList != null && showerList.data != null) {
                    if (homeTip == null)//防止频繁刷新出现问题
                        return;
                    homeTip.setText(showerList.data.fee_scale_notice);
                    if (Tools.toInt(showerList.data.surplus) > 0) {//有浴位
                        uplineStatus.setVisibility(View.GONE);
                        originNext.setText(APPOINT);
                    } else {
                        uplinenumber.setText(showerList.data.queue_number);
                        uplineTime.setText(showerList.data.wait_time);
                        uplineStatus.setVisibility(View.VISIBLE);
                        originNext.setText(LINEUP);
                    }

//                    showShower(showerList.data.show_shower, showerList.data.show_message);


                    showNotice(showerList.data.notice, notify);
                    if (showerList.data.default_choice != null) {
                        if (TextUtils.isEmpty(map.get("b_id"))) {
                            map.put("b_id", showerList.data.default_choice.b_id);
                        }
                        String s_id = showerList.data.default_choice.s_id;
                        if (!TextUtils.isEmpty(s_id)) {
                            SPUtils.saveString(Constants.S_ID, s_id);
                        }
                        map.put("campus", showerList.data.default_choice.campus);
                        if (TextUtils.isEmpty(homeBathroomLocation.getText().toString().trim()))
                            homeBathroomLocation.setText(showerList.data.default_choice.position);
                    }
                    map.put("num", "");
                    if (showerList.data.shower != null) {
                        updateList(showerList.data.shower);
                    }
                    if (originRoot.getVisibility() != View.VISIBLE)
                        originRoot.setVisibility(View.VISIBLE);
                    if (homeListGridview.getVisibility() != View.VISIBLE)
                        homeListGridview.setVisibility(View.VISIBLE);
                    //是否有未支付订单
                    boolean equals = TextUtils.equals("1", showerList.data.is_pay);
                    if (flag && equals) {
                        showShouldPayDialog();
                    }
                } else {
                    map.put("num", "");//跳转界面时，将已选择的位置置空;
                    homeListGridview.setVisibility(View.INVISIBLE);
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                homeListGridview.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 设置距离过远/没有认证之类
     *
     * @param shower
     * @param message
     */
    private void showShower(String shower, String message) {
        if (TextUtils.equals("0", shower)) {//显示遮罩
            errbgView.setVisibility(View.VISIBLE);
            errbgView.setOnClickListener(null);
            errTipView.setText(message);
            if (bindresult()) {
                getHomeContent().setCanPull(false);
            }
        } else {
            errbgView.setVisibility(View.GONE);
            if (bindresult()) {
                getHomeContent().setCanPull(true);
            }
        }
    }

    private ArrayList<ShowerList.Notice> noticeList;

    /**
     * 显示公告
     *
     * @param notice
     */
    private void showNotice(List<ShowerList.Notice> notice, boolean notify) {
        if (!notify)
            return;
        //公告
        if (notice != null && notice.size() > 0) {
            noticeList = new ArrayList();
            noticeList.addAll(notice);
            StringBuffer title = new StringBuffer();
            for (ShowerList.Notice n : noticeList) {
                title.append(n.content + "                    ");
            }
            verticalTextview.setText(title.toString());
            verticalTextview.requestFocus();
//            if (handler != null)
//                handler.sendEmptyMessage(199);
            verticalTextview.setVisibility(View.VISIBLE);
        } else {
            verticalTextview.setVisibility(View.GONE);
        }
    }

    /**
     * 检查是否有未支付订单，之后跳转
     */
    public void checkPayOrder() {
        VolleySingleton.post(Neturl.SHOWER_LIST, "shower_list", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                ShowerList showerList = JsonUtils.parseJsonToBean(result, ShowerList.class);
                if (showerList != null && showerList.data != null) {
                    //是否有未支付订单
                    boolean equals = TextUtils.equals("1", showerList.data.is_pay);
                    if (equals) {
                        showShouldPayDialog();
                        LoadingTrAnimDialog.dismissLoadingAnimDialog();
                        return;
                    }
                    checkLockPWD();
                }
            }
        });
    }

    public void updateList(final List<ShowerList.Shower> shower) {
        if (listAdapter == null) {
            listAdapter = new ShowerListAdapter(shower);
            homeListGridview.setAdapter(listAdapter);
            listAdapter.setItemClickListener(new ShowerListAdapter.ItemClickListener() {
                @Override
                public void change(String item) {
                    map.put("num", item);
                }
            });
        } else {
            listAdapter.notifyDataSetChanged(shower);
        }
    }


    @OnClick({R.id.home_choice_bathroomlocation, R.id.filter_footer, R.id.origin_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_choice_bathroomlocation:
                if (ishow) {
                    originFilterContent.setVisibility(View.GONE);
                    arrow.setImageResource(R.mipmap.arrow_down);
                    ishow = false;
                    if (bindresult()) {
                        getHomeContent().setCanPull(true);
                    }
                } else {
                    filterBathRoom();
                }
                break;
            case R.id.filter_footer:
                if (originFilterContent.getVisibility() == View.VISIBLE) {
                    dismissBathroomList();
                }
                break;
            case R.id.origin_next:
                LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
                checkPayOrder();
                break;
        }
    }

    /**
     * 弹出下拉浴室列表
     */
    private synchronized void filterBathRoom() {
        VolleySingleton.post(Neturl.SHOWERROOM_FILTER, "filter", new HashMap<String, String>(), new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                BathRoomFilter bathRoomFilter = JsonUtils.parseJsonToBean(result, BathRoomFilter.class);
                if (bathRoomFilter != null && bathRoomFilter.data != null) {
                    showBathRoomFilter(bathRoomFilter.data);
                }
            }
        });
    }

    /**
     * 检查是否拥有开柜密码(包含了认证、押金、退款、设置密码的验证)
     */
    private void checkLockPWD() {
        VolleySingleton.post(Neturl.CHECK_LOCK_PWD, "checklock", new HashMap<String, String>(), new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                CheckPWD bean = JsonUtils.parseJsonToBean(result, CheckPWD.class);
                if (bean != null && bean.data != null) {
                    if (bean.data.isallowappoint != null && bean.data.isallowappoint.result.equals("0")) {
                        ToastUtils.show(bean.data.isallowappoint.msg);
                        LoadingTrAnimDialog.dismissLoadingAnimDialog();
                        return;
                    }
                    if (TextUtils.equals("1", bean.data.need_auth) && TextUtils.equals("0", bean.data.auth.result)) {//1.检查是否需要认证
                        Intent intent = new Intent(getContext(), AutheActivity.class);
                        intent.putExtra("from", "home");
                        startActivity(intent);
                        LoadingTrAnimDialog.dismissLoadingAnimDialog();
                        // 判断是否需要交押金
                    } else if (TextUtils.equals("1", bean.data.need_yajin) && TextUtils.equals("0", bean.data.yajin.result)) {//判断是否需要交押金
                        LoadingTrAnimDialog.dismissLoadingAnimDialog();
                        Intent intent = new Intent(getActivity(), RechargeYaJinActivity.class);
                        intent.putExtra("from", "OriginFragment");
                        startActivity(intent);
                    } else if (TextUtils.equals("1", bean.data.need_yajin) && TextUtils.equals("2", bean.data.yajin.result)) {//正在退押金中
                        LoadingTrAnimDialog.dismissLoadingAnimDialog();
                        ToastUtils.show(bean.data.yajin.msg);
                    } else if (TextUtils.equals("2", bean.data.account_refund.result)) {//正在退款
                        LoadingTrAnimDialog.dismissLoadingAnimDialog();
                        ToastUtils.show(bean.data.account_refund.msg);
                    } else if (TextUtils.equals("1", bean.data.password.result)) {//已经设置过
                        if (TextUtils.isEmpty(map.get("num")) && !TextUtils.equals(originNext.getText(), LINEUP)) {
                            LoadingTrAnimDialog.dismissLoadingAnimDialog();
                            ToastUtils.show("请先选择浴位");
                            return;
                        }
                        next();
                    } else {
                        LoadingTrAnimDialog.dismissLoadingAnimDialog();
                        showSettingPwd();
                    }
                } else {
                    LoadingTrAnimDialog.dismissLoadingAnimDialog();
                }
            }
        });
    }

    /**
     * 排队(当有浴位时直接自动跳转至预约界面)
     */
    private void lineUp() {
        LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
        VolleySingleton.post(Neturl.LINEUP_TO_WAIT, "startlineup", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                LineUpBean lineUpBean = JsonUtils.parseJsonToBean(result, LineUpBean.class);
                if (lineUpBean != null && lineUpBean.data != null) {
                    if (TextUtils.equals("1", lineUpBean.data.jump)) {//已经有位置可以预约，跳转到预约界面
                        toAppoint(result);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("bean", lineUpBean.data);
                        bundle.putString("id", lineUpBean.data.id);
                        if (getHomeContent() != null) {
                            getHomeContent().switchLineUp(bundle);
                        }
                    }
                } else {
                    LoadingTrAnimDialog.dismissLoadingAnimDialog();
                }
            }
        });
    }

    /**
     * 预约
     */
    private void appoint() {
        LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
        VolleySingleton.post(Neturl.APPOINT, "appoint", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                toAppoint(result);
            }
        });
    }

    /**
     * 跳转待服务界面
     *
     * @param json
     */
    private void toAppoint(String json) {
        WaitORService waitORService = JsonUtils.parseJsonToBean(json, WaitORService.class);
        if (waitORService != null && waitORService.data != null) {
            ToastUtils.show(waitORService.data.msg);
            Bundle bundle = new Bundle();
            bundle.putString("id", waitORService.data.id);
            bundle.putSerializable("bean", waitORService.data);
            if (bindresult())
                getHomeContent().switchWait(bundle);
        } else {
            LoadingTrAnimDialog.dismissLoadingAnimDialog();
        }
    }

    /**
     * 弹窗是否显示
     */
    private boolean ishow = false;

    public void showBathRoomFilter(List<BathRoomFilter.Filter> list) {
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
        final ListFilterAdapter filterAdapter = new ListFilterAdapter(list, R.layout.item_choice_bathroom, getActivity(), map.get("b_id"));
        choiceroomListview.setAdapter(filterAdapter);
        filterAdapter.setOnItemClickListener(new OnItemClickListener<BathRoomFilter.Filter>() {
            @Override
            public void onItemClick(BathRoomFilter.Filter item, ViewHolder holder, int position) {
                SPUtils.saveString("b_id", item.b_id);
                SPUtils.saveString("bathroomLoaction", item.name);
                filterAdapter.setBID(item.b_id);
                LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
                update(false, true);
//                quitAnimation(choiceroomListview);
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
     * 预约/排队
     */
    private void next() {
        if (TextUtils.equals(originNext.getText(), APPOINT)) {
            appoint();
        } else {
            lineUp();
        }
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
                HashMap<String, String> map = new HashMap<>();
                map.put("ppwd", pwd);
                VolleySingleton.post(Neturl.SET_LOCK_PWD, "set_pwd", map, new VolleySingleton.CBack() {
                    @Override
                    public void runUI(String result) {
                        Result resultBean = JsonUtils.parseJsonToBean(result, Result.class);
                        if (resultBean != null && resultBean.data != null) {
                            ToastUtils.show(resultBean.data.msg);
                            next();
                        }
                    }
                });
            }
        });
    }

    /**
     * 当有未支付订单时，弹此框
     */
    public synchronized void showShouldPayDialog() {
        ShouldPayDialog.Builder builder = new ShouldPayDialog.Builder(getActivity());
        builder.create().show();
        builder.setOnClickListener(new ShouldPayDialog.ShouldPayListener() {
            @Override
            public void pay() {
                LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
                startActivity(new Intent(getContext(), PayActivity.class));
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("OriginFragment");
        if (NetworkUtil.isNetworkAvailable())
            handler.sendEmptyMessageDelayed(MESSAGEID, 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart("OriginFragment");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
