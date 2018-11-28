package com.isgala.xishuashua.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.bean_.Banner;
import com.isgala.xishuashua.bean_.Banners;
import com.isgala.xishuashua.bean_.ShowerList;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.ui.BannerHtmlActivity;
import com.isgala.xishuashua.utils.ImageLoadUtils;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.NetworkUtil;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.isgala.xishuashua.view.AutoHorizontalScrollTextView;
import com.isgala.xishuashua.view.RecyclerViewBanner;
import com.isgala.xishuashua.view.pop.PopHomeWindow;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Handler;


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
    private List<Banners.BannersData.BannerBean> mBannerList = new ArrayList();
    private Banner mBanner;
    private PopHomeWindow mHomeWindow;
    private final int MESSAGEID = 99;
    private final int DELAYEDTIME = 5000;

//    private Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what==MESSAGEID){
//                update(false);
//                if (NetworkUtil.isNetworkAvailable())
//                    handler.sendEmptyMessageDelayed(MESSAGEID, DELAYEDTIME);
//            }
//        }
//    };
//
//    @Override
//    public void onDestroy() {
//        try {
//            if (handler != null) {
//                handler.removeCallbacksAndMessages(null);
//                handler = null;
//            }
//            super.onDestroy();
//        } catch (Exception e) {
//        }
//    }
//    @Override
//    public void onStop() {
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(null);
//        }
//        super.onStop();
//    }
//      public void onResume() {
//        super.onResume();
//        MobclickAgent.onPageStart("OriginFragment");
//        if (NetworkUtil.isNetworkAvailable())
//            handler.sendEmptyMessageDelayed(MESSAGEID, 3000);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_origin_super, null);
//        mView.getFocusables()
        initView(mView);
//        initCarouselText();
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

    private void initView(View view) {
        mButton = (Button) view.findViewById(R.id.btn_onclick);
        mAutoTextView = (AutoHorizontalScrollTextView) view.findViewById(R.id.txt_auto);
        mImgMiddle = (ImageView) view.findViewById(R.id.img_middle);
        recyclerViewBanner = (RecyclerViewBanner) view.findViewById(R.id.rv_banner);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImgMiddle.setVisibility(View.VISIBLE);
                if (mHomeWindow == null) {
                    mHomeWindow = new PopHomeWindow(getActivity());
                    mHomeWindow.init();
                }
                mHomeWindow.showMoreWindow(view, 200);
            }
        });
    }
    /**
     * 获取跑马灯文字信息+
     */
//    public void initCarouselText() {
//        String b_id = SPUtils.getString("yb_id", "");
//        map.put("b_id", b_id);
//        map.put(Constants.LOCATON_LAT, SPUtils.getString(Constants.LOCATON_LAT, ""));
//        map.put(Constants.LOCATON_LNG, SPUtils.getString(Constants.LOCATON_LNG, ""));
//        VolleySingleton.post(Neturl.SHOWER_LIST, "shower_list", map, new VolleySingleton.CBack() {
//            @Override
//            public void runUI(String result) {
//                ShowerList showerList = JsonUtils.parseJsonToBean(result, ShowerList.class);
//                if (showerList != null && showerList.data != null) {
//                    showNotice(showerList.data.notice);
//                }
//            }
//        });
//    }

    /**
     * 显示跑马灯文字信息
     *
     * @param notice
     */
    private void showNotice(List<ShowerList.Notice> notice) {
        if (notice != null && notice.size() > 0) {
            noticeList = new ArrayList();
            noticeList.addAll(notice);
            StringBuffer title = new StringBuffer();
            for (ShowerList.Notice n : noticeList) {
                title.append(n.content + "                    ");
            }
            mAutoTextView.setText(title.toString());
            mAutoTextView.requestFocus();
            mAutoTextView.setVisibility(View.VISIBLE);
        } else {
            mAutoTextView.setVisibility(View.GONE);
        }
    }

    /**
     * 获取跑马灯文字信息+
     */
    public void update(final boolean flag) {
        if (flag)
            LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
        //获取用户上次的选择
        String b_id = SPUtils.getString("yb_id", "");
        map.put("b_id", b_id);
        map.put(Constants.LOCATON_LAT, SPUtils.getString(Constants.LOCATON_LAT, ""));
        map.put(Constants.LOCATON_LNG, SPUtils.getString(Constants.LOCATON_LNG, ""));
        VolleySingleton.post(Neturl.SHOWER_LIST, "shower_list", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                ShowerList showerList = JsonUtils.parseJsonToBean(result, ShowerList.class);
                if (showerList != null && showerList.data != null) {
                    showNotice(showerList.data.notice);
                    if (showerList.data.default_choice != null) {
                        if (TextUtils.isEmpty(map.get("b_id"))) {
                            map.put("b_id", showerList.data.default_choice.b_id);
                        }
                        String s_id = showerList.data.default_choice.s_id;
                        if (!TextUtils.isEmpty(s_id)) {
                            SPUtils.saveString(Constants.S_ID, s_id);
                        }
                    }
                    map.put("num", "");
                    boolean equals = TextUtils.equals("1", showerList.data.is_pay);
                } else {
                    map.put("num", "");//跳转界面时，将已选择的位置置空;
//                    homeListGridview.setVisibility(View.INVISIBLE);
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
//                homeListGridview.setVisibility(View.INVISIBLE);
            }
        });
        getBannerData();
    }

    /**
     * 设置轮播图滚动
     */
    public void setPlayBanner(boolean bool) {
        recyclerViewBanner.setPlaying(bool);
    }

    public Boolean getPlayBanner() {
        return recyclerViewBanner.getIsPlaying();
    }

    /**
     * 获取图片轮播数据
     */
    private void getBannerData() {
        final Map<String, String> map = new HashMap<>();
        String s_id = SPUtils.getString("s_id", "");
        map.put("s_id", s_id);//学校id
        map.put("campus", SPUtils.getString("campus", ""));//校区id
        VolleySingleton.post(Neturl.GET_BANNER, "get_banner", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                try {
                    Banners banners = JsonUtils.parseJsonToBean(result, Banners.class);
                    if (banners!=null&&banners.data != null && banners.data.getBanner().size()>0) {
                        List<Banners.BannersData.BannerBean> dataList = banners.data.getBanner();
                        dealWithApplication(dataList);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

//                Gson gson = new Gson();
//                Banner banner=gson.fromJson(result,Banner.class);
//                Banner banner = JsonUtils.parseJsonToBean(result,Banner.class);
//            if (banner != null && banner.getData() !=null){
//                List<Banner.DataBean> list=banner.getData();
//                dealWithApplication(dataList);
//            }
            }
        });
    }

    private void dealWithApplication(List<Banners.BannersData.BannerBean> banner) {
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
                ImageLoadUtils.loadImage(getActivity(), mBannerList.get(position % mBannerList.size()).getImage(), R.mipmap.authe_name,
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
        intent.putExtra("BannerUrl", mBannerList.get(position).getUrl());
        startActivity(intent);
    }

}
