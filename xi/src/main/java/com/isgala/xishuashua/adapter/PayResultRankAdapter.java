package com.isgala.xishuashua.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.isgala.xishuashua.R;
import com.isgala.xishuashua.base.BAdapter;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.RankItem;
import com.isgala.xishuashua.utils.ImageLoadUtils;

import java.util.List;

/**
 * 支付结果页的排行榜
 * Created by and on 2016/11/11.
 */

public class PayResultRankAdapter extends BAdapter<RankItem> {
    public PayResultRankAdapter(List<RankItem> list, @Nullable int layoutId, Context context) {
        super(list, layoutId, context);
    }

    @Override
    public void upDateView(int position, ViewHolder holder, RankItem item) {
        holder.getView(R.id.item_rank_number, TextView.class).setText(item.top);
        ImageLoadUtils.loadImage(context, holder.getView(R.id.item_rank_photo, ImageView.class), item.photo);
        holder.getView(R.id.item_rank_name, TextView.class).setText(item.nickname);
        holder.getView(R.id.item_rank_usetime, TextView.class).setText(item.total_time);
    }
}
