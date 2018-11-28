package com.zhuochi.hydream.bathhousekeeper.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by and on 2016/11/9.
 */

public class ViewHolder {
    private SparseArray<View>mViews;
    private View mConvertView;
    private  ViewHolder(Context context, ViewGroup parent,int layoutId){
        this.mViews=new SparseArray<>();
        mConvertView= LayoutInflater.from(context).inflate(layoutId,parent,false);
        mConvertView.setTag(this);
    }

    public static ViewHolder getHolder(View mConvertView, ViewGroup parent, int layoutId){
        if(mConvertView==null){
            return new ViewHolder(parent.getContext(),parent,layoutId);
        }
        return (ViewHolder) mConvertView.getTag();
    }
    public View getConvertView(){
        return mConvertView;
    }

    /**
     * 查找指定的控件
     * @param viewId
     * @param tClass
     * @param <T>
     * @return
     */
    public <T extends View >T getView(int viewId,Class<T>tClass){
        View view=mViews.get(viewId);
        if(view==null){
            view=mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }
}
