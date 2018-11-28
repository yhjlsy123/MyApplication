package com.zhuochi.hydream.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhuochi.hydream.bean_.BathendOrder;
import com.zhuochi.hydream.view.BlowerRoomView;

import java.util.ArrayList;
import java.util.List;

/**
 * 吹风机列表
 * Created by and on 2016/11/5.
 */

public class BlowerListAdapter extends BaseAdapter {
    private List<BathendOrder.BathendOrderData.DeviceLlistBean> mShowerList;

    public BlowerListAdapter(List<BathendOrder.BathendOrderData.DeviceLlistBean> shower) {
        mShowerList = new ArrayList<>();
        mShowerList.addAll(shower);
    }

    private String TAG = "ShowerListAdapter";

    @Override
    public int getCount() {
        return mShowerList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void notifyDataSetChanged(List<BathendOrder.BathendOrderData.DeviceLlistBean> shower) {
        mShowerList.clear();
        mShowerList.addAll(shower);
        notifyDataSetChanged();
    }

    /**
     * 标记选中的位置
     */
    private String selectID;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final BlowerRoomView bathRoomView;
        if (convertView != null) {
            bathRoomView = (BlowerRoomView) convertView;
        } else {
            bathRoomView = new BlowerRoomView(parent.getContext());
        }
        final BathendOrder.BathendOrderData.DeviceLlistBean shower = mShowerList.get(position);
        if (TextUtils.equals("1", shower.getStatus())) {
            if (TextUtils.equals(selectID, shower.getId())) {
                if (listener != null)
                    listener.change(shower.getNum());
                bathRoomView.setStatus(BlowerRoomView.CHECKED);
            } else {
                bathRoomView.setStatus(BlowerRoomView.USABLE);
            }
        } else if (TextUtils.equals("2", shower.getStatus())) {//状态==2 不可预约，将选中的相同Id置为"";
            if (TextUtils.equals(selectID, shower.getId())) {
                selectID = "";
                if (listener != null)
                    listener.change("");
            }
            bathRoomView.setStatus(BlowerRoomView.USING);
        }
        bathRoomView.setText(shower.getNum());
        bathRoomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals("2", shower.getStatus()))
                    return;
                if (TextUtils.equals("1", shower.getStatus())) {
                    if (TextUtils.equals(selectID, shower.getId())) {
                        selectID = "";
                        if (listener != null)
                            listener.change("");
                    } else {
                        selectID = shower.getId();
                    }
                    notifyDataSetChanged();
                }
            }
        });
        return bathRoomView;
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
}
