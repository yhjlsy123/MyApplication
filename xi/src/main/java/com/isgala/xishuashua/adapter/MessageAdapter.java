package com.isgala.xishuashua.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.base.BAdapter;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.Message;

import java.util.List;

/**
 * 消息列表
 * Created by and on 2016/11/8.
 */

public class MessageAdapter extends BAdapter<Message.MessageItem> {
    public MessageAdapter(List<Message.MessageItem> list, int layoutId, Context context) {
        super(list, layoutId, context);
    }

    @Override
    public void upDateView(int position, ViewHolder holder, Message.MessageItem item) {
        holder.getView(R.id.message_time, TextView.class).setText(item.created);
        holder.getView(R.id.message_title, TextView.class).setText(item.title);
        holder.getView(R.id.message_content, TextView.class).setText(item.content);
        if (position == getCount() - 1) {
            holder.getView(R.id.line, ImageView.class).setPadding(0, 0, 0, 0);
        } else {
            holder.getView(R.id.line, ImageView.class).setPadding(20, 0, 0, 0);
        }
    }
}
