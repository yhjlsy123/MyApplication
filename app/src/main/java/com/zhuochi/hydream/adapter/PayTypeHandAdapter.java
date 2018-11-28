package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.entity.PayTypeList;
import com.zhuochi.hydream.utils.ImageLoadUtils;

import java.util.List;

/**
 * 支付类型
 * Created by and on 2016/12/9.
 */
public class PayTypeHandAdapter extends BaseAdapter {
    private String selectRc_id;
    private Context mContext;
    private List<PayTypeList> mlist;
    private int mPosition = 0;

    public PayTypeHandAdapter(Context context, List<PayTypeList> list) {
        mContext = context;
        mlist = list;
    }


    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    ViewHolder holder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_paytype, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tvbathroom.setText(mlist.get(position).getMethod_name());
        String imgUrl = mlist.get(position).getIco();
        if (mPosition == position) {
            holder.icon.setBackgroundResource(R.mipmap.pay_check);
        } else {
            holder.icon.setBackgroundResource(R.mipmap.pay_uncheck);
        }
//        if (listener != null) {
            listener.change(mlist.get(mPosition).getMethod());
//        }
        final ViewHolder finalViewHolder = holder;

        ImageLoadUtils.loadImage(mContext, imgUrl, holder.imageView);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position;
                listener.change(mlist.get(position).getMethod());
//                if (mPosition != position) {
//                    finalViewHolder.icon.setBackgroundResource(R.mipmap.pay_uncheck);
//                }
                if (mPosition == position) {
                    finalViewHolder.icon.setBackgroundResource(R.mipmap.pay_check);
                }
                notifyDataSetChanged();
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
        ImageView icon;

        public ViewHolder(View view) {
            tvbathroom = (TextView) view.findViewById(R.id.tv_payType);
            imageView = (ImageView) view.findViewById(R.id.img);
            icon = (ImageView) view.findViewById(R.id.icon);

        }
    }
}
