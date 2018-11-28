package com.zhuochi.hydream.ViewHolder;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.entity.RankEntity;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.Tools;
import com.zhuochi.hydream.view.RoundedImageView;

/**
 * Created by ztech on 2018/7/17.
 */

public class RankEntityViewHolder extends BaseViewHolder<RankEntity.RankingListBean> {
    ImageView imgRank;
    TextView textRank;
    RoundedImageView imgHead;
    TextView textName;
    TextView textTime;
    LinearLayout itemRankBg;

    public RankEntityViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_ranking);
        imgRank = $(R.id.img_rank);
        textRank = $(R.id.text_rank);
        imgHead = $(R.id.img_head);
        textName= $(R.id.text_name);
        textTime = $(R.id.text_time);
        itemRankBg = $(R.id.item_rank_bg);
           }

    @Override
    public void setData(final RankEntity.RankingListBean rankEntity) {
           textRank.setText(rankEntity.get_num() + "");
        textName.setText(rankEntity.getBuyer_name());
        textTime.setText(Tools.SToMin(Long.parseLong(rankEntity.getSum_used_time())) + "分钟");
        ImageLoadUtils.loadImage(getContext(), rankEntity.getAvatar(), imgHead);
        switch (rankEntity.get_num()) {
            case 1:
                 textRank.setVisibility(View.GONE);
                itemRankBg.setBackgroundResource(R.mipmap.ranking_rad_bg);
                imgRank.setImageResource(R.mipmap.ranking_one);
//                Glide.with(context).load(R.mipmap.ranking_one).into(imgRank);
                textName.setTextColor(Color.WHITE);
                textTime.setTextColor(Color.WHITE);
                break;
            case 2:
                textRank.setVisibility(View.GONE);
                itemRankBg.setBackgroundResource(R.mipmap.ranking_yellow_bg);
                imgRank.setImageResource(R.mipmap.ranking_two);
                textName.setTextColor(Color.WHITE);
                textTime.setTextColor(Color.WHITE);
                break;
            case 3:
                textRank.setVisibility(View.GONE);
                itemRankBg.setBackgroundResource(R.mipmap.ranking_blue_bg);
                imgRank.setImageResource(R.mipmap.ranking_three);
                textName.setTextColor(Color.WHITE);
                textTime.setTextColor(Color.WHITE);
                break;
            default:
                imgRank.setVisibility(View.GONE);
                break;
        }
    }
}