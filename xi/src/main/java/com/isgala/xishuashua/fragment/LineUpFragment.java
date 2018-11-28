package com.isgala.xishuashua.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.isgala.xishuashua.bean_.LineUpBean;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.dialog.TipDialog;
import com.isgala.xishuashua.dialog.TipDialog2;
import com.isgala.xishuashua.receiver.MessageReceiver;
import com.isgala.xishuashua.ui.SchoolList;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.LogUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.isgala.xishuashua.R.id.lineup_time_except;


/**
 * 排队(可以跳转到预约列表、待服务)
 * Created by and on 2016/11/10.
 */

public class LineUpFragment extends BaseHomeFragment {

    @BindView(R.id.lineup_pwd)
    TextView lineupPwd;
    @BindView(R.id.service_wait_hide)
    CheckBox serviceWaitHide;
    @BindView(R.id.lineup_location)
    TextView lineupLocation;
    @BindView(lineup_time_except)
    TextView lineupTimeExcept;
    @BindView(R.id.lineup_bathroomlocation)
    TextView lineupBathroomlocation;
    @BindView(R.id.lineup_tip1)
    TextView lineup_tip1;
    @BindView(R.id.lineup_tip2)
    TextView lineup_tip2;

    private final String TAG = "LineUpFragment";
    @BindView(R.id.lineup_root)
    LinearLayout lineupRoot;

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
                } else if (TextUtils.equals("self", action)) {//更新自己的界面
                    lineUp();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lineup, null);
        ButterKnife.bind(this, view);
        return view;
    }

    private HashMap<String, String> map;

    @Override
    public void initView() {
        super.initView();
        map = new HashMap<>();
        serviceWaitHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lineupPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置密码为隐藏的
                    lineupPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
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
            LineUpBean.Lineup bean = (LineUpBean.Lineup) arguments.getSerializable("bean");
            if (bean != null) {
                updateView(bean);
            }
            IntentFilter intentFilter = new IntentFilter(Constants.LINEUP);
            getContext().registerReceiver(messageReceiver, intentFilter);
            lineUp();
            LogUtils.e(TAG, " Receive register success ");
        } else {
            LogUtils.e(TAG, "  bundle  is  null ");
        }
        LoadingTrAnimDialog.dismissLoadingAnimDialog();
        //防止注册广播之前，数据发生变化.确保实时性
        if (bindresult()) {
            getHomeContent().obtainLocation();
        }
    }

    /**
     * 更新数据
     *
     * @param arguments
     */
    public void loadView(Bundle arguments) {
        if (arguments != null) {
            String id = arguments.getString("id");
            if (!TextUtils.isEmpty(id)) {//注：此处 排队id特别重要！
                map.put("id", id);
            }
            LineUpBean.Lineup bean = (LineUpBean.Lineup) arguments.getSerializable("bean");
            if (bean != null) {
                updateView(bean);
            } else {
                lineUp();
            }
        }
        LoadingTrAnimDialog.dismissLoadingAnimDialog();
    }

    /**
     * 更新排队信息
     */
    private void lineUp() {
        VolleySingleton.post(Neturl.LINE_UP, "line_up", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                LineUpBean lineUpBean = JsonUtils.parseJsonToBean(result, LineUpBean.class);
                if (lineUpBean != null && lineUpBean.data != null) {
                    updateView(lineUpBean.data);
                }
            }
        });
    }

    /**
     * 切换待服务或者服务中
     *
     * @param id
     */
    private void next(String id) {
        if (bindresult()) {
            getHomeContent().waitOrServicing(id);
        }
    }

    private void updateView(LineUpBean.Lineup bean) {
        lineupLocation.setText(bean.queue_number);
        lineupTimeExcept.setText(bean.wait_time);
        lineupBathroomlocation.setText(bean.position);
        lineupPwd.setText(bean.password);
        if (bean.tips.size() > 0) {
            lineup_tip1.setText(bean.tips.get(0));
        }
        if (bean.tips.size() > 1) {
            lineup_tip2.setText(bean.tips.get(1));
        }
        lineupRoot.setVisibility(View.VISIBLE);
    }

    /**
     * 取消排队
     */
    private void cancel() {
        LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
        map.put("type", "2");
        VolleySingleton.post(Neturl.CANCEL_APPOINT, "cancel", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Result bean = JsonUtils.parseJsonToBean(result, Result.class);
                if (bean != null && bean.data != null) {
                    ToastUtils.show(bean.data.msg);
                    if (getHomeContent() != null) {
                        getHomeContent().switchOrigin(false);
                    }
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        });
    }

    @OnClick(R.id.lineup_cancel)
    public void onClick() {
        Dialog dialog = TipDialog2.show_(getContext(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        }, "提示信息", "确认要取消本次服务吗？");

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("LineUpFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart("LineUpFragment");
    }

    @Override
    public void onDestroy() {
        if (messageReceiver != null) {
            getContext().unregisterReceiver(messageReceiver);
        }
        super.onDestroy();
    }
}
