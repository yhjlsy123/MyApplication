package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BAdapter;
import com.zhuochi.hydream.base.ViewHolder;
import com.zhuochi.hydream.bean_.Message;
import com.zhuochi.hydream.entity.MessageTypeEntity;
import com.zhuochi.hydream.utils.ImageLoadUtils;

import java.util.List;

/**
 * 消息列表
 * Created by and on 2016/11/8.
 */

public class MessageAdapter extends BAdapter<MessageTypeEntity> {
    public MessageAdapter(List<MessageTypeEntity> list, int layoutId, Context context) {
        super(list, layoutId, context);
    }

    @Override
    public void upDateView(int position, ViewHolder holder, MessageTypeEntity item) {
        ImageLoadUtils.loadImage(context, holder.getView(R.id.img_title, ImageView.class), item.getIcon());
        holder.getView(R.id.message_content, TextView.class).setText(item.getType_name());
        if (position == getCount() - 1) {
            holder.getView(R.id.line, ImageView.class).setPadding(0, 0, 0, 0);
        } else {
            holder.getView(R.id.line, ImageView.class).setPadding(20, 0, 0, 0);
        }
    }
}
