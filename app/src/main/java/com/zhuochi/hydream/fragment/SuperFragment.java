package com.zhuochi.hydream.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.klcxkj.zqxy.ui.MainAdminActivity;
import com.klcxkj.zqxy.ui.MainUserActivity;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.activity.BannerHtmlActivity;
import com.zhuochi.hydream.api.Neturl;
import com.zhuochi.hydream.bean_.ShowerList;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.entity.BannerBroadcast;
import com.zhuochi.hydream.entity.BatheFloorEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.UserUtils;
import com.zhuochi.hydream.view.AutoHorizontalScrollTextView;
import com.zhuochi.hydream.view.RecyclerViewBanner;
import com.zhuochi.hydream.view.pop.PopHomeWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 超级澡堂改版
 * Created Cuixc on 2018/3/13.
 */
public class SuperFragment extends BaseHomeFragment implements RecyclerViewBanner.OnRvBannerClickListener {
    private View mView;
    private Button mButton;
    private AutoHorizontalScrollTextView mAutoTextView;
    private ImageView mImgMiddle;
    private Map<String, String> map = new HashMap<>();
    private ArrayList<ShowerList.Notice> noticeList;
    private RecyclerViewBanner recyclerViewBanner;
    private List<String> mBannerList = new ArrayList();
//    private Banner mBanner;
    private PopHomeWindow mHomeWindow;
    private XiRequestParams params;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_origin_super, null);
        params=new XiRequestParams(getActivity());
        initView(mView);
//        initCarouselText();
        getDeviceTip();
        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
     * 根据浴室ID获取提示文字
     *
     */
    public void getDeviceTip() {
        int Device_area_id = SPUtils.getInt(Constants.DEVICE_AREA_ID, 0);
        params.addCallBack(this);
        params.getDeviceAreaById("kalushit");
    }

    /**
     * 获取图片轮播数据
     */
    private void  getAdList(){
        int OrgAreaID = UserUtils.getInstance(getActivity()).getOrgAreaID();
        params.addCallBack(this);
        params.getAdList(OrgAreaID);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag){
            case "getDeviceAreaById":
                if (result.getData().getData().equals("false")){
                    return;
                }
                Map mapNotice = (Map) result.getData().getData();
                BatheFloorEntity msgEntity = new Gson().fromJson(GsonUtils.parseMapToJson(mapNotice), BatheFloorEntity.class);
                showNotice(msgEntity.getReminder());
                break;
            case "getAdList":
                Map map1=(Map) result.getData().getData();
                String  gs=GsonUtils.parseMapToJson(map1);
                BannerBroadcast banners=new Gson().fromJson(gs,BannerBroadcast.class);
                List<String> list=(List<String>) banners.getPicList();
                dealWithApplication(list);
                break;
            case "syncAmount":
                ToastUtils.show(result.getData().getMsg());
                int userType=SPUtils.getInt("user_type",0);
                if (userType==3) {
//                    管理员测试洗澡
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MainAdminActivity.class);
                    intent.putExtra("tellPhone", SPUtils.getString(Constants.PHONE_NUMBER, ""));
                    intent.putExtra("PrjID", "0");
                    intent.putExtra("app_url", Neturl.WATER_URL);
                    startActivity(intent);
                }else {
                    //普通用戶
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MainUserActivity.class);
                    intent.putExtra("tellPhone", SPUtils.getString(Constants.MOBILE_PHONE, ""));
                    intent.putExtra("PrjID", "0");
                    intent.putExtra("app_url", Neturl.WATER_URL);
                    startActivity(intent);
                }
                break;
        }
        super.onRequestSuccess(tag, result);
    }

    private void initView(View view) {
        mButton = (Button) view.findViewById(R.id.btn_onclick);
        mAutoTextView = (AutoHorizontalScrollTextView) view.findViewById(R.id.txt_auto);
        mImgMiddle = (ImageView) view.findViewById(R.id.img_middle);
        recyclerViewBanner = (RecyclerViewBanner) view.findViewById(R.id.rv_banner);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImgMiddle.setVisibility(View.VISIBLE);
                syncAmount();
//                if (mHomeWindow == null) {
//                    mHomeWindow = new PopHomeWindow(getActivity());
//                    mHomeWindow.init();
//                }
//                mHomeWindow.showMoreWindow(view, 200);
            }
        });
        getAdList();
    }

    /**
     * 同步余额
     */
    private void syncAmount() {
        params.addCallBack(this);
        params.syncAmount();
    }
    /**
     * 显示跑马灯文字信息
     *
     * @param notice
     */
    private void showNotice(String notice) {
        if (notice != null &&!TextUtils.isEmpty(notice)) {
//            noticeList = new ArrayList();
//            noticeList.addAll(notice);
//            StringBuffer title = new StringBuffer();
//            for (ShowerList.Notice n : noticeList) {
//                title.append(n.content + "                    ");
//            }
            mAutoTextView.setText(notice);
            mAutoTextView.requestFocus();
            mAutoTextView.setVisibility(View.VISIBLE);
        } else {
            mAutoTextView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置轮播图滚动
     */
    public void setPlayBanner(boolean bool) {
        recyclerViewBanner.setPlaying(bool);
    }

    private void dealWithApplication(List<String> banner) {
        // 图片轮播 数据
        mBannerList.clear();
        mBannerList.addAll(banner);
        // 是否显示指示器
        recyclerViewBanner.isShowIndicatorPoint(false);
        recyclerViewBanner.setRvBannerDatas(mBannerList);
        recyclerViewBanner.setPlaying(false);
        recyclerViewBanner.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
            @Override
            public void switchBanner(int position, ImageView bannerView, TextView title) {
                ImageLoadUtils.loadImage(getActivity(), mBannerList.get(position % mBannerList.size()), R.mipmap.bg_home_banner,
                        bannerView);
//                title.setText(mBannerList.get(position % mBannerList.size()).getTitles());
            }
        });
        recyclerViewBanner.setOnRvBannerClickListener(this);
        // 应用
    }

    /**
     * 播图片的点击事件
     */
    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), BannerHtmlActivity.class);
//        intent.putExtra("BannerUrl", mBannerList.get(position).getUrl());
        startActivity(intent);
    }

}
