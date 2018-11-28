package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.activity.OneCardInfoActivity;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.entity.PayTypeList;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.exchang.TurnOffPayType;
import com.zhuochi.hydream.http.RequestURL;
import com.zhuochi.hydream.http.ResponseListener;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.SPUtils;

import java.util.List;

/**
 * 支付类型
 * Created by and on 2016/12/9.
 */
public class PayTypeGainAdapter extends BaseAdapter implements ResponseListener {
    private String selectRc_id;
    private Context mContext;
    private List<TurnOffPayType.PayTypeBean> mlist;
    private int mPosition = 0;
    private XiRequestParams params;

    public PayTypeGainAdapter(Context context, List<TurnOffPayType.PayTypeBean> list) {
        mContext = context;
        mlist = list;
    }

    public int getmPosition() {
        return mPosition;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
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
        holder.tvbathroom.setText(mlist.get(position).getTypeName());
        String imgUrl = RequestURL.IMAGE_URL + mlist.get(position).getTypeImage();
        if (mPosition == position) {
            holder.icon.setBackgroundResource(R.mipmap.pay_check);
        } else {
            holder.icon.setBackgroundResource(R.mipmap.pay_uncheck);
        }
//        if (listener != null) {
        listener.change(mlist.get(mPosition).getTypeKey());
//        }
        final ViewHolder finalViewHolder = holder;

        ImageLoadUtils.loadImage(mContext, imgUrl, holder.imageView);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position;
                listener.change(mlist.get(position).getTypeKey());
//                if (mPosition != position) {
//                    finalViewHolder.icon.setBackgroundResource(R.mipmap.pay_uncheck);
//                }
                if (mPosition == position) {
                    finalViewHolder.icon.setBackgroundResource(R.mipmap.pay_check);
                }
                if (mlist.get(position).getTypeKey().equals("onecard")) {
                    params = new XiRequestParams(mContext);
                    params.addCallBack(PayTypeGainAdapter.this);
                    getOtherAuthInfo();

                } else {
                    notifyDataSetChanged();
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
        ImageView icon;

        public ViewHolder(View view) {
            tvbathroom = (TextView) view.findViewById(R.id.tv_payType);
            imageView = (ImageView) view.findViewById(R.id.img);
            icon = (ImageView) view.findViewById(R.id.icon);

        }
    }


    /*获取认证信息*/
    private void getOtherAuthInfo() {
        params.addCallBack(this);
        params.getOtherAuthInfo(SPUtils.getString(Constants.MOBILE_PHONE, ""));
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getOtherAuthInfo":
                if (result.getData().getCode() == 100) {
                    Intent intent = new Intent(mContext, OneCardInfoActivity.class);
                    mContext.startActivity(intent);
                    mPosition = 0;

                } else if (result.getData().getCode() == 200) {
                    notifyDataSetChanged();
                }
                notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onRequestFailure(String tag, Object s) {

    }
}
