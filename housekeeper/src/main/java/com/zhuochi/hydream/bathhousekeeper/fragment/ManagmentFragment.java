package com.zhuochi.hydream.bathhousekeeper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.activity.AreaListsDeviceNumActivity;
import com.zhuochi.hydream.bathhousekeeper.activity.DepositManageActivity;
import com.zhuochi.hydream.bathhousekeeper.activity.ExerciseActivity;
import com.zhuochi.hydream.bathhousekeeper.activity.FeedbackManageActivity;
import com.zhuochi.hydream.bathhousekeeper.activity.NoticeManageActivity;
import com.zhuochi.hydream.bathhousekeeper.activity.RechargeableCardManageActivity;
import com.zhuochi.hydream.bathhousekeeper.activity.RefundsManageActivity;
import com.zhuochi.hydream.bathhousekeeper.activity.UserManageActivity;
import com.zhuochi.hydream.bathhousekeeper.adapter.DeviceManageAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseFragment;
import com.zhuochi.hydream.bathhousekeeper.bean.DeviceManageItemBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/*管理*/
public class ManagmentFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.home_list_recyclerView)
    RecyclerView recyclerView;
    private List<DeviceManageItemBean> mDataList;
    private XiRequestParams params;
    private DeviceManageAdapter adapter;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == view)
            view = inflater.inflate(R.layout.fragment_administration, container, false);
        unbinder = ButterKnife.bind(this, view);
        params = new XiRequestParams(getActivity());
        initAdapter();
        return view;
    }


    public void initAdapter() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mDataList = new ArrayList<>();
        adapter = new DeviceManageAdapter(R.layout.device_manage_item, mDataList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        loadDataFromNet();
    }


    public void loadDataFromNet() {
        params.addCallBack(this);
        params.getDeviceType(SPUtils.getInt(Constants.USER_ID, 0));
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
        if (jsonArray.size() > 0) {
            mDataList = JSON.parseArray(JSON.toJSONString(jsonArray), DeviceManageItemBean.class);
            adapter.setNewData(mDataList);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_use_manage, R.id.tv_Notice_manage, R.id.tv_activity_manage, R.id.tv_feedback_manage, R.id.tv_deposit_manage
            , R.id.tv_Rechargeable_card_manage, R.id.tv_Refunds_manage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_feedback_manage://反馈对话
                startActivity(new Intent(getActivity(), FeedbackManageActivity.class));
                break;
            case R.id.tv_use_manage://用户管理
                startActivity(new Intent(getActivity(), UserManageActivity.class));
                break;
            case R.id.tv_Notice_manage://公告管理
                startActivity(new Intent(getActivity(), NoticeManageActivity.class));
                break;
            case R.id.tv_activity_manage://活动管理
                startActivity(new Intent(getActivity(), ExerciseActivity.class));
                break;
            case R.id.tv_deposit_manage://押金管理
                startActivity(new Intent(getActivity(), DepositManageActivity.class));
                break;
            case R.id.tv_Rechargeable_card_manage://充值卡管理
                startActivity(new Intent(getActivity(), RechargeableCardManageActivity.class));
                break;
            case R.id.tv_Refunds_manage://退款管理
                startActivity(new Intent(getActivity(), RefundsManageActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getActivity(), AreaListsDeviceNumActivity.class);
        intent.putExtra("deviceType", mDataList.get(position).getName());
        intent.putExtra("deviceName", mDataList.get(position).getName_text());

        startActivity(intent);
    }
}
