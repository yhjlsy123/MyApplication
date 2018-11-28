package com.isgala.xishuashua.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.isgala.xishuashua.R;
import com.isgala.xishuashua.base.BAdapter;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.RankItem;
import com.isgala.xishuashua.utils.ImageLoadUtils;
import com.isgala.xishuashua.view.RoundedImageView;

import java.util.List;

/**
 * 排行榜
 * Created by and on 2016/11/10.
 */

public class RankAdaper extends BAdapter<RankItem> {
    public RankAdaper(List<RankItem> list, int layoutId, Context context) {
        super(list, layoutId, context);
    }

    @Override
    public void upDateView(int position, ViewHolder holder, RankItem item) {
        int paddingleft = 20;
        if (position == getCount() - 1) {
            paddingleft = 0;
        }
        holder.getView(R.id.rank_item_line, ImageView.class).setPadding(paddingleft, 0, 0, 0);
        holder.getView(R.id.rank_item_number, TextView.class).setText(item.top);
        holder.getView(R.id.rank_name, TextView.class).setText(item.nickname);
//        holder.getView(R.id.rank_love_count, TextView.class).setText(item.v_id);
        holder.getView(R.id.rank_use_time, TextView.class).setText(item.total_time);
        ImageLoadUtils.loadImage(context, holder.getView(R.id.rank_user_photo, ImageView.class), item.photo);
    }
}
