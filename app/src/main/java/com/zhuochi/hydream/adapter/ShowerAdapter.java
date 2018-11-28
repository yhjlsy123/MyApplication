package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.entity.HomeShowerListEntity;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zhuochi.hydream.config.Constants.FAULTED;
import static com.zhuochi.hydream.config.Constants.JUNKED;
import static com.zhuochi.hydream.config.Constants.OFFLINE;
import static com.zhuochi.hydream.config.Constants.READY;
import static com.zhuochi.hydream.config.Constants.REPAIRING;
import static com.zhuochi.hydream.config.Constants.RESERVED;
import static com.zhuochi.hydream.config.Constants.RUNNING;
import static com.zhuochi.hydream.config.Constants.SELECTED;

/**
 * 浴室浴头列表
 * Created by and on 2016/11/5.
 */

public class ShowerAdapter extends BaseAdapter {
    private List<HomeShowerListEntity> mShowerList;
    private Context mContext;
    /**
     * 标记选中的位置
     */
    public static String selectID = "";
    public ShowerAdapter(List<HomeShowerListEntity> shower, Context context) {
        mShowerList = new ArrayList<>();
        mContext = context;
        mShowerList.addAll(shower);
    }

    private String TAG = "ShowerListAdapter";

    @Override
    public int getCount() {
        return mShowerList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void notifyDataSetChanged(List<HomeShowerListEntity> shower) {
        mShowerList.clear();
        mShowerList.addAll(shower);
        notifyDataSetChanged();
    }


    ViewHolder holder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.blower_view, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tvbathroom.setText(mShowerList.get(position).getDevice_number());
        String imgUrl = mShowerList.get(position).getDevice_status();
        switch (imgUrl) {
            case "ready":
                holder.tvbathroom.setTextColor(Color.BLACK);
                ImageLoadUtils.loadImage(mContext, Common.ICON_BASE_URL + "/" + READY, holder.imageView);
                break;
            case "offline"://离线
                holder.tvbathroom.setTextColor(Color.WHITE);
                ImageLoadUtils.loadImage(mContext, Common.ICON_BASE_URL + "/" + OFFLINE, holder.imageView);
                break;
            case "running":
                holder.tvbathroom.setTextColor(Color.WHITE);
                ImageLoadUtils.loadImage(mContext, Common.ICON_BASE_URL + "/" + RUNNING, holder.imageView);
                break;
//            case "repairing":
//                holder.tvbathroom.setTextColor(Color.WHITE);
//                ImageLoadUtils.loadImage(mContext,  Common.ICON_BASE_URL + "/" + REPAIRING, holder.imageView);
//                break;
//            case "faulted":
//                holder.tvbathroom.setTextColor(Color.WHITE);
//                ImageLoadUtils.loadImage(mContext,Common.ICON_BASE_URL + "/" + FAULTED, holder.imageView);
//                break;
//            case "junked":
//                holder.tvbathroom.setTextColor(Color.WHITE);
//                ImageLoadUtils.loadImage(mContext, Common.ICON_BASE_URL + "/" + JUNKED, holder.imageView);
//                break;
            case "reserved":
                holder.tvbathroom.setTextColor(Color.WHITE);
                ImageLoadUtils.loadImage(mContext,  Common.ICON_BASE_URL + "/" + RESERVED, holder.imageView);
                break;
        }
        if (TextUtils.equals(selectID, mShowerList.get(position).getDevice_key())) {
            if (listener != null)
                listener.change(mShowerList.get(position).getDevice_key());
            ImageLoadUtils.loadImage(mContext, Common.ICON_BASE_URL + "/" + SELECTED, holder.imageView);
            holder.tvbathroom.setTextColor(Color.WHITE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    if (mShowerList.get(position).getDevice_status().equals("ready")) {
                        String ImageFaultUrl = Common.ICON_BASE_URL + "/" + SELECTED;
                        ImageLoadUtils.loadImage(mContext, ImageFaultUrl, holder.imageView);
                        selectID = mShowerList.get(position).getDevice_key();
                        listener.change(mShowerList.get(position).getDevice_key());

                    } else {
                        listener.change("");
                    }
            }
        });
        return convertView;
    }

    private ItemClickListener listener;

    /**
     * 设置点击条目后的回调
     *
     * @param listener
     */
    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 获取选中条目的数据
     */
    public interface ItemClickListener {
        void change(String item);
    }

    public class ViewHolder {
        TextView tvbathroom;
        ImageView imageView;

        public ViewHolder(View view) {
            tvbathroom = (TextView) view.findViewById(R.id.bathroom_status);
            imageView = (ImageView) view.findViewById(R.id.img);
        }
    }
}
