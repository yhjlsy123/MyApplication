package com.isgala.xishuashua.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.isgala.xishuashua.bean_.ShowerList;
import com.isgala.xishuashua.view.BathRoomView;

import java.util.ArrayList;
import java.util.List;

/**
 * 浴室浴头列表
 * Created by and on 2016/11/5.
 */

public class ShowerListAdapter extends BaseAdapter {
    private List<ShowerList.Shower> mShowerList;

    public ShowerListAdapter(List<ShowerList.Shower> shower) {
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

    public void notifyDataSetChanged(List<ShowerList.Shower> shower) {
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
        final BathRoomView bathRoomView;
        if (convertView != null) {
            bathRoomView = (BathRoomView) convertView;
        } else {
            bathRoomView = new BathRoomView(parent.getContext());
        }
        final ShowerList.Shower shower = mShowerList.get(position);
        if (TextUtils.equals("1", shower.status)) {
            if (TextUtils.equals(selectID, shower.id)) {
                if (listener != null)
                    listener.change(shower.num);
                bathRoomView.setStatus(BathRoomView.CHECKED);
            } else {
                bathRoomView.setStatus(BathRoomView.USABLE);
            }
        } else if (TextUtils.equals("2", shower.status)) {//状态==2 不可预约，将选中的相同Id置为"";
            if (TextUtils.equals(selectID, shower.id)) {
                selectID = "";
                if (listener != null)
                    listener.change("");
            }
            bathRoomView.setStatus(BathRoomView.USING);
        }
        bathRoomView.setText(shower.num);
        bathRoomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals("2", shower.status))
                    return;
                if (TextUtils.equals("1", shower.status)) {
                    if (TextUtils.equals(selectID, shower.id)) {
                        selectID = "";
                        if (listener != null)
                            listener.change("");
                    } else {
                        selectID = shower.id;
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
