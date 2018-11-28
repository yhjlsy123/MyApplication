package com.zhuochi.hydream.bathhousekeeper.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.activity.OrderDetailActivity;
import com.zhuochi.hydream.bathhousekeeper.activity.ReplyListsActivity;
import com.zhuochi.hydream.bathhousekeeper.adapter.FeedbackListManageAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseFragment;
import com.zhuochi.hydream.bathhousekeeper.bean.FeedbackManageItemBean;
import com.zhuochi.hydream.bathhousekeeper.bean.OrderListItemBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.dialog.ConfirmDialog;
import com.zhuochi.hydream.bathhousekeeper.dialog.SureSchoolChoice;
import com.zhuochi.hydream.bathhousekeeper.dialog.TipDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.DialogUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;
import com.zhuochi.hydream.bathhousekeeper.view.HeightSelectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FeedBackListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedBackListFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, HeightSelectView.SelcetCallBack {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IsReply = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mIsReply;
    private String mParam2;
    @BindView(R.id.headViewContaner)
    LinearLayout headViewContaner;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private XiRequestParams params;
    private HeightSelectView mHeightSelectView;
    private FeedbackListManageAdapter adapter;
    private List<FeedbackManageItemBean> entity;
    private int page = 1;
    private int org_id;
    private int org_area_id;
    private String start_date;
    private String end_date;
    private Unbinder unbinder;
    private ConfirmDialog dd;


    public FeedBackListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedBackListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedBackListFragment newInstance(String param1, String param2) {
        FeedBackListFragment fragment = new FeedBackListFragment();
        Bundle args = new Bundle();
        args.putString(IsReply, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsReply = getArguments().getString(IsReply);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed_back_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        refrushData();
        if (getUserVisibleHint()) {
            //解决第一个fragment无法加载数据问题
            initData();
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "FeedBack/refund":
                if (result.getData().getCode() == 200) {
                    ToastUtils.show(result.getData().getMsg());
                    initData();
                }

                break;
            default:
                refreshLayout.setRefreshing(false);
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                if (jsonArray.size() > 0) {

                    if (1 == page) {
                        entity.clear();
                    }
               /*     List<FeedbackManageItemBean> lData = JSON.parseArray(JSON.toJSONString(jsonArray), FeedbackManageItemBean.class);
                    for (FeedbackManageItemBean it : lData) {
                        switch (mIsReply) {
                            case "yes":
                                if (TextUtils.equals("yes", it.getHas_reply())) {
                                    entity.add(it);
                                }

                                break;
                            case "no":
                                if (TextUtils.equals("no", it.getHas_reply())) {
                                    entity.add(it);
                                }

                                break;
                        }
                    }*/
                    entity.addAll(JSON.parseArray(JSON.toJSONString(jsonArray), FeedbackManageItemBean.class));
                    adapter.setNewData(entity);
                    adapter.loadMoreComplete();
                    adapter.setEnableLoadMore(true);
                } else if (jsonArray.size() == 0) {
                    adapter.loadMoreEnd(true);
                }
                break;
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisible()) {//视图可见并且控件准备好了，每次都会调用
            if (null == entity || entity.size() == 0) {//如果数据为空了，则需要重新联网请求
                initData();
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getActivity(), ReplyListsActivity.class);
        String id = String.valueOf(entity.get(position).getSuggestion_id());
        intent.putExtra("id", id);
        startActivity(intent);
    }


    @Override
    public void CallBackSelect(int org_id, int org_area_id, int boothroom_id, String device_key, String StartTime, String EndTime) {
        this.org_id = org_id;
        this.org_area_id = org_area_id;
        start_date = StartTime;
        end_date = EndTime;
        page = 1;
        initData();
    }

    private void initData() {
        adapter.setOnItemClickListener(this);
        params.addCallBack(this);
        params.getSuggestionLists(SPUtils.getInt(Constants.USER_ID, 0), org_id, org_area_id, mIsReply, start_date, end_date, page + "", 10 + "");
    }

    private void refrushData() {
        params = new XiRequestParams(getActivity());
        mHeightSelectView = new HeightSelectView(getActivity(), this, 1, getResources().getString(R.string.select_school_campus));
        headViewContaner.addView(mHeightSelectView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        entity = new ArrayList<>();
        adapter = new FeedbackListManageAdapter(R.layout.item_feedback_manage, entity);
        recyclerView.setAdapter(adapter);
        //下拉刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                initData();
                refreshLayout.setRefreshing(false);
                adapter.setEnableLoadMore(true);
            }
        });
        //上拉加载
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page += 1;
                initData();

            }
        }, recyclerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.user_mobile:
                        if (TextUtils.isEmpty(entity.get(position).getUser_mobile())) {
                            break;
                        }
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + entity.get(position).getUser_mobile()));
                        startActivity(intent);
                        break;
                    case R.id.order_detail:
                        intent = new Intent(getActivity(), OrderDetailActivity.class);
                        intent.putExtra("order_item_id", entity.get(position).getSuggestion_id());
                        startActivity(intent);
                        break;
                    case R.id.re_money:
                        dd = new ConfirmDialog(getActivity(), "确认要返款吗？", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dd.dismiss();
                                returnMoney(entity.get(position).getOrder_sn(), entity.get(position).getSuggestion_id() + "");

                            }
                        });
                        break;
                }
            }
        });
    }

    private void returnMoney(String order_sn, String suggestion_id) {
        params.addCallBack(this);
        Map<String, Object> pram = new HashMap<String, Object>();
        pram.put("order_sn", order_sn);
        pram.put("suggestion_id", suggestion_id);

        params.comnRequest("FeedBack/refund", pram);
    }


}
