package com.isgala.xishuashua.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.isgala.xishuashua.R;
import com.isgala.xishuashua.adapter.RankAdaper;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.Rank;
import com.isgala.xishuashua.bean_.RankItem;
import com.isgala.xishuashua.dialog.LoadingAnimDialog;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.utils.ImageLoadUtils;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.isgala.xishuashua.view.CustomListView;
import com.isgala.xishuashua.view.CustomerScrollView;
import com.isgala.xishuashua.view.RoundedImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 排行榜
 * Created by and on 2016/11/10.
 */

public class RankingActivity extends BaseAutoActivity {
    @BindView(R.id.rank_user_photo)
    RoundedImageView rankUserPhoto;
    @BindView(R.id.rank_listview)
    CustomListView rankListview;
    @BindView(R.id.rank_me_name)
    TextView rankMeName;
    @BindView(R.id.rank_me_top)
    TextView rankMeTop;
    @BindView(R.id.rank_me_usetime)
    TextView rankMeUsetime;
    @BindView(R.id.rank_me_love)
    TextView rankMeLove;
    @BindView(R.id.rank_activity_scrollview)
    CustomerScrollView rankActivityScrollview;
    @BindView(R.id.topline)
    View topLine;
    private RankAdaper rankAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        LoadingAnimDialog.showLoadingAnimDialog(this);
        VolleySingleton.post(Neturl.Rank_List, "ranklist", new HashMap<String, String>(), new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Rank rank = JsonUtils.parseJsonToBean(result, Rank.class);
                if (rank != null && rank.data != null) {
                    updateList(rank.data.list);
                    updateMe(rank.data.my);
                    rankActivityScrollview.setVisibility(View.VISIBLE);
                }
                LoadingAnimDialog.dismissLoadingAnimDialog();
            }
        });
    }

    private void updateMe(RankItem item) {
        if (item == null || TextUtils.isEmpty(item.nickname)) {
            findViewById(R.id.rank_me_p).setVisibility(View.GONE);
            return;
        } else {
            findViewById(R.id.rank_me_p).setVisibility(View.VISIBLE);
            rankMeName.setText(item.nickname);
            rankMeTop.setText(item.top);
            rankMeUsetime.setText(item.total_time);
            ImageLoadUtils.loadImage(this,rankUserPhoto,item.photo);
        }
    }

    private void updateList(List<RankItem> list) {
        if (list.size() > 0) {
            topLine.setVisibility(View.VISIBLE);
        } else {
            topLine.setVisibility(View.INVISIBLE);
        }
        if (rankAdaper == null) {
            rankAdaper = new RankAdaper(list, R.layout.item_rank_list, this);
            rankListview.setAdapter(rankAdaper);
        } else {
            rankAdaper.notifyDataSetChanged(list);
        }
    }

    @OnClick(R.id.rank_back)
    public void onClick() {
        finish();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("RankingActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("RankingActivity");
        MobclickAgent.onPause(this);
    }
}
