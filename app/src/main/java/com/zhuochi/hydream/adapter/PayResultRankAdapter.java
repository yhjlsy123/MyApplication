package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BAdapter;
import com.zhuochi.hydream.base.ViewHolder;
import com.zhuochi.hydream.bean_.RankItem;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.Tools;

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
        holder.getView(R.id.item_rank_number, TextView.class).setText(item._num);
        ImageLoadUtils.loadImage(context, holder.getView(R.id.item_rank_photo, ImageView.class), item.avatar);
        holder.getView(R.id.item_rank_name, TextView.class).setText(item.buyer_name);
        holder.getView(R.id.item_rank_usetime, TextView.class).setText(Tools.Tominutes(item.sum_used_time));
    }
}
