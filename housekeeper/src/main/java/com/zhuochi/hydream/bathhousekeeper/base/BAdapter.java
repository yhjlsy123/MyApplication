package com.zhuochi.hydream.bathhousekeeper.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by and on 2016/11/9.
 */

public abstract class BAdapter<T> extends BaseAdapter {
    private List<T> mDataList;
    private int mLayoutId;
    protected OnItemClickListener mItemClickListener;
    public Context context;

    public BAdapter(List<T> list, @Nullable int layoutId, @Nullable Context context) {
        mDataList = new ArrayList<>();
        if (list != null)
            mDataList.addAll(list);
        this.mLayoutId = layoutId;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public T getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder = ViewHolder.getHolder(convertView, parent, mLayoutId);
        upDateView(position, holder, getItem(position));
        convertView = holder.getConvertView();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(getItem(position), holder, position);
            }
        });
        return convertView;
    }

    /**
     * 设置点击事件
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    /**
     * 设置控件的具体显示
     *
     * @param position
     * @param holder
     * @param item
     */
    public abstract void upDateView(int position, ViewHolder holder, T item);

    /**
     * add操作
     *
     * @param data
     */
    public void notifyDataSetChanged(List<T> data) {

        if (data != null && data.size() > 0) {
            mDataList.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 先clear,后add操作
     *
     * @param data
     */
    public void notifyDataSetChanged2(List<T> data) {
        mDataList.clear();
        if (data != null && data.size() > 0) {
            mDataList.addAll(data);
        }
        notifyDataSetChanged();
    }

    public List<T> getListData() {
        return mDataList;
    }
}
