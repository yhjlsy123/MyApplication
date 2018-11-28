package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BAdapter;
import com.zhuochi.hydream.base.ViewHolder;
import com.zhuochi.hydream.entity.RankEntity;

import java.util.List;

import com.zhuochi.hydream.utils.*;
import com.zhuochi.hydream.view.RoundedImageView;

/**
 * 排行榜适配器
 * Created by ztech on 2018/7/5.
 */

public class RankingAdapter extends BAdapter<RankEntity.RankingListBean> {
    public RankingAdapter(List<RankEntity.RankingListBean> list, int layoutId, Context context) {
        super(list, layoutId, context);
    }

    @Override
    public void upDateView(int position, ViewHolder holder, final RankEntity.RankingListBean item) {
        ImageView imgRank = holder.getView(R.id.img_rank, ImageView.class);
        TextView textRank = holder.getView(R.id.text_rank, TextView.class);
        RoundedImageView imgHead = holder.getView(R.id.img_head, RoundedImageView.class);
        TextView textName = holder.getView(R.id.text_name, TextView.class);
        TextView textTime = holder.getView(R.id.text_time, TextView.class);
        LinearLayout itemRankBg = holder.getView(R.id.item_rank_bg, LinearLayout.class);

        textRank.setText(item.get_num() + "");
        textName.setText(item.getBuyer_name());
        if (item.getSum_used_time() != null) {
            textTime.setText(Tools.SToMin(Long.parseLong(item.getSum_used_time())) + "分钟");
        } else {
            textTime.setText("0分钟");
        }
        if (item.getAvatar().length() > 0) {
            ImageLoadUtils.loadImage(context, item.getAvatar(), imgHead);
        } else {
            imgHead.setImageResource(R.mipmap.defaut_user_photo);
        }
        switch (item.get_num()) {
            case 1:
                textRank.setVisibility(View.GONE);
                itemRankBg.setBackgroundResource(R.mipmap.ranking_rad_bg);
                imgRank.setVisibility(View.VISIBLE);
                imgRank.setImageResource(R.mipmap.ranking_one);
//                Glide.with(context).load(R.mipmap.ranking_one).into(holder.imgRank);
                textName.setTextColor(Color.WHITE);
                textTime.setTextColor(Color.WHITE);
                break;
            case 2:
                textRank.setVisibility(View.GONE);
                itemRankBg.setBackgroundResource(R.mipmap.ranking_yellow_bg);
                imgRank.setVisibility(View.VISIBLE);
                imgRank.setImageResource(R.mipmap.ranking_two);
                textName.setTextColor(Color.WHITE);
                textTime.setTextColor(Color.WHITE);
                break;
            case 3:
                textRank.setVisibility(View.GONE);
                itemRankBg.setBackgroundResource(R.mipmap.ranking_blue_bg);
                imgRank.setVisibility(View.VISIBLE);
                imgRank.setImageResource(R.mipmap.ranking_three);
                textName.setTextColor(Color.WHITE);
                textTime.setTextColor(Color.WHITE);
                break;
            default:
                itemRankBg.setBackgroundResource(R.color.white);
                imgRank.setVisibility(View.GONE);
                textRank.setVisibility(View.VISIBLE);
                textName.setTextColor((ColorStateList) context.getResources().getColorStateList(R.color.black_rank_text));
                textTime.setTextColor((ColorStateList) context.getResources().getColorStateList(R.color.black_rank_text));
                break;
        }

    }
}




