package com.zhuochi.hydream.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.activity.RankingActivity;
import com.zhuochi.hydream.adapter.RankingAdapter;
import com.zhuochi.hydream.base.BaseFragment;
import com.zhuochi.hydream.dialog.TipDialog2;
import com.zhuochi.hydream.entity.RankEntity;
import com.zhuochi.hydream.entity.RankingTypeEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.UserInfoEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.interfaceimp.ImgProblemClick;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.Tools;
import com.zhuochi.hydream.utils.UserUtils;
import com.zhuochi.hydream.view.RoundedImageView;
import com.zhuochi.hydream.view.pulltorefresh2.RefreshListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankMonthFragment extends BaseFragment {
    private XiRequestParams params;
    Serializable obj;
    RankingTypeEntity rankingTypeEntity;

    @BindView(R.id.record_listview)
    RefreshListView recordListview;

    @BindView(R.id.dafault)
    View dafault;

    private View headview;//加载的顶部视图
    RoundedImageView imgHead;
    TextView textRanking;
    TextView rankName;
    TextView rankTime;
    String rankingStr = "";
    String nameStr = "";
    String timeStr = "";
    String imgStr = "";


    private RefreshListView.State mCurrentState;//刷新状态
    private int page;//当前页
    private int limit = 10;//每页条数
    private int position = 0;//list显示位置


    List<RankEntity.RankingListBean> rankingListBean = new ArrayList<>();
    private boolean isViewCreated;//Fragment的View加载完毕的标记
    private boolean isUIVisible; //Fragment对用户可见的标记
    public static String RANK_MESSAGE = "";
    final Handler handler = new Handler() { //刷新顶部视图
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (imgStr.length() > 0) {
                    ImageLoadUtils.loadImage(getActivity(), imgHead, imgStr);
                } else {
                    imgHead.setImageResource(R.mipmap.defaut_user_photo);
                }
                textRanking.setText(rankingStr);
                rankName.setText(nameStr);
                rankTime.setText(timeStr);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obj = getArguments().getSerializable("getRankingType");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_month, null);
        isViewCreated = true;
        ButterKnife.bind(this, view);
        params = new XiRequestParams(getActivity());
        if ((!NetworkUtil.isNetworkAvailable())) {
                  /*没网显示缺省内容*/
            recordListview.setVisibility(View.GONE);
            dafault.setVisibility(View.VISIBLE);
        } else {
            init_Data();/*加载界面*/
        }
        return view;
    }

    /*加载页面*/
    private void init_Data() {
        if (isViewCreated && isUIVisible) {
            initHead();/*渲染顶部*/
            init_View();/*渲染listview*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (rankingStr.length() == 0 && obj != null) {
                        /*获取上一个页面传递的对象rankingTypeEntity，获取排行数据，获取个人信息*/
                        String jsonString = (String) obj;
                        rankingTypeEntity = JSON.parseObject(jsonString, RankingTypeEntity.class);
                        getRankingListByType();
                        getBaseInfo();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;

        }


    }

    /*获取排行数据*/
    public void getRankingListByType() {
        params.addCallBack(this);
        params.getRankingListByType(UserUtils.getInstance(getActivity()).getMobilePhone(), rankingTypeEntity.getRanking_type(), page + "", limit + "");
    }

    /*获取个人信息*/
    private void getBaseInfo() {
        params.addCallBack(this);
        params.getUserBaseInfo(UserUtils.getInstance(getActivity()).getMobilePhone());
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getRankingListByType"://获取排行数据
                Object objects = result.getData().getData();
                try {
                    if (objects != null) {
                        RankEntity rankEntity = JSON.parseObject(JSON.toJSONString(objects), RankEntity.class);
                        RANK_MESSAGE = rankEntity.getFAQ();
                        RankEntity.MyRankingBean myRankingBean = rankEntity.getMyRanking();
                        init_UserInfo(myRankingBean);
                        if (rankEntity.getRankingList().size() > 0) {//有数据时
                            rankingListBean.addAll(rankEntity.getRankingList());
                            RankingAdapter rankingAdapter = new RankingAdapter(rankingListBean, R.layout.item_ranking, getActivity());
                            recordListview.setAdapter(rankingAdapter);
                            if (position != 0) {
                                recordListview.setSelection(position + 2);
                            } else {
                                recordListview.setSelection(position);
                            }
//                            if (rankEntity.getRankingList() == null || rankEntity.getRankingList().size() < limit) {// 当条数小于
                                 /*已加载完*/
                            recordListview.setPullLoadEnable(false);//去掉刷新
                            recordListview.setPullRefreshEnable(false);
//                            } else {
//                                /*未加载完*/
//                                recordListview.setPullLoadEnable(true);
//                                page++;
//                            }
//                            /*若状态为下拉刷新，则刷新完成；若状态为上拉加载，则停止刷新*/
//                            if (mCurrentState == RefreshListView.State.PULL) {
//                                finishLoad();
//                            } else if (mCurrentState == RefreshListView.State.PUSH) {
//                                stopLoad();
//                            }
                        }
                    }
                } catch (Exception e) {
                }/*若数据为空，显示缺省页*/
                if (rankingListBean.size() == 0) {
                    dafault.setVisibility(View.VISIBLE);
                    recordListview.setVisibility(View.GONE);
                }

                break;
            case "getUserBaseInfo":/*获取个人信息*/
                Map map = (Map) result.getData().getData();
                try {
                    String gson = GsonUtils.parseMapToJson(map);
                    UserInfoEntity mEntity = new Gson().fromJson(gson, UserInfoEntity.class);
                    imgStr = mEntity.getAvatar();
                    nameStr = mEntity.getUser_nickname();
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        super.onRequestSuccess(tag, result);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /*个人信息赋值*/
    private void init_UserInfo(RankEntity.MyRankingBean myRankingBean) {
        rankingStr = "第" + myRankingBean.getNumber() + "名";
        if (myRankingBean.getSum_used_time() != null) {
            timeStr = Tools.SToMin(Long.parseLong(myRankingBean.getSum_used_time())) + "分钟";
        } else {
            timeStr = "0分钟";
        }
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);

    }

    /*渲染顶部*/
    private void initHead() {
        headview = LayoutInflater.from(getActivity()).inflate(R.layout.header_rank_month, null);
        imgHead = headview.findViewById(R.id.img_head);
        textRanking = headview.findViewById(R.id.text_ranking);
        rankName = headview.findViewById(R.id.rank_name);
        rankTime = headview.findViewById(R.id.rank_time);
    }

    private void init_View() {
        mCurrentState = RefreshListView.State.NONE;/*设置刷新状态*/
        recordListview.addHeadView(headview);/*绑定顶部View*/
        recordListview.setXListViewListener(new RefreshListView.IXListViewListener() {/*设置监听*/
            @Override
            public void onRefresh() {
                  /*上拉刷新，将page,position都设为0，从0开始*/
                if (mCurrentState == RefreshListView.State.NONE) {
                    mCurrentState = RefreshListView.State.PULL;
                    page = 0;
                    position = 0;
                    rankingListBean = new ArrayList<>();
                    LoadData();
                    return;
                }
                stopLoad();
            }

            @Override
            public void onLoadMore() {
                   /*下拉加载,根据网络状态，判断是否是最后一页，执行操作，*/
                if (mCurrentState == RefreshListView.State.NONE) {
                    if (!NetworkUtil.isNetworkAvailable()) {
                        recordListview.errLoadMore();
                        return;
                    }
                    if (rankingListBean.size() % limit == 0) {
                        mCurrentState = RefreshListView.State.PUSH;
                        position = recordListview.getFirstVisiblePosition();
                        LoadData();
                    } else {
                        recordListview.showNOMore();
                    }
                }
            }
        });
    }

    /**
     * 停止刷新，
     */
    private void stopLoad() {
        recordListview.stopRefresh();
        recordListview.stopLoadMore();
        mCurrentState = RefreshListView.State.NONE;
    }

    /**
     * 刷新完成
     */
    private void finishLoad() {
        recordListview.finishRefresh();
        mCurrentState = RefreshListView.State.NONE;
    }

    private void LoadData() {
        getBaseInfo();
        getRankingListByType();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isUIVisible = true;
            init_Data();
        } else {
            isUIVisible = false;
        }
    }


}
