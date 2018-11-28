package com.isgala.xishuashua.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.isgala.xishuashua.R;
import com.isgala.xishuashua.base.BAdapter;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.FeedBackHistoryItemBean;
import com.isgala.xishuashua.utils.ImageLoadUtils;

import java.util.List;

/**
 * 意见反馈历史
 * Created by and on 2016/11/19.
 */

public class FeedBackHistoryAdapter extends BAdapter<FeedBackHistoryItemBean> {
    public FeedBackHistoryAdapter(Context context, int layoutId, List<FeedBackHistoryItemBean> list) {
        super(list, layoutId, context);
    }

    @Override
    public void upDateView(int position, ViewHolder holder, FeedBackHistoryItemBean item) {
        holder.getView(R.id.item_history_create, TextView.class).setText(item.created);
        ImageLoadUtils.loadImage(context, holder.getView(R.id.item_history_userphoto, ImageView.class), item.photo);
        holder.getView(R.id.item_history_content, TextView.class).setText(item.content);
        if (TextUtils.isEmpty(item.result)) {
            holder.getView(R.id.item_history_system_root, View.class).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.item_history_system_root, View.class).setVisibility(View.VISIBLE);
            holder.getView(R.id.item_history_feedback_system, TextView.class).setText(item.result);
            ImageLoadUtils.loadImage(context, holder.getView(R.id.item_history_systemphoto, ImageView.class), item.admin_photo);
        }
    }
}
