package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BAdapter;
import com.zhuochi.hydream.base.ViewHolder;
import com.zhuochi.hydream.entity.MessageTypeDataEntity;
import com.zhuochi.hydream.entity.MessageTypeEntity;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.Tools;

import java.util.List;

/**
 * 消息列表
 * Created by and on 2016/11/8.
 */

public class MessageTypeDataAdapter extends BAdapter<MessageTypeDataEntity> {
    public MessageTypeDataAdapter(List<MessageTypeDataEntity> list, int layoutId, Context context) {
        super(list, layoutId, context);
    }

    @Override
    public void upDateView(int position, ViewHolder holder, MessageTypeDataEntity item) {
//        holder.getView(R.id.message_time, TextView.class).setText(item.created);
//        holder.getView(R.id.message_title, TextView.class).setText(item.title);
        holder.getView(R.id.tv_time, TextView.class).setText(Common.getDatedd(item.getCreate_time()));
        holder.getView(R.id.tv_title, TextView.class).setText(item.getTitle());
        holder.getView(R.id.tv_content, TextView.class).setText(item.getDescribe());
//        if (position == getCount() - 1) {
//            holder.getView(R.id.line, ImageView.class).setPadding(0, 0, 0, 0);
//        } else {
//            holder.getView(R.id.line, ImageView.class).setPadding(20, 0, 0, 0);
//        }
    }
}
