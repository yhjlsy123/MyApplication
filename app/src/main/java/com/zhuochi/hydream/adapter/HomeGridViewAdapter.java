package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.entity.DeviceTypeEntity;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.ImageLoadUtils;

import java.util.List;

/**
 * @author Cuixc
 * @date on  2018/6/12
 */

public class HomeGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<DeviceTypeEntity> mEntityList;
    private int mPosition;
    private String device_type_key = "";

    public HomeGridViewAdapter(Context context, List<DeviceTypeEntity> entityList, int mposition) {
        mContext = context;
        mEntityList = entityList;
        mPosition = mposition;
    }

    @Override
    public int getCount() {
        return mEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void  setData( List<DeviceTypeEntity> entityList){
        mEntityList = entityList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(R.layout.item_list_home, null);
        TextView textView = (TextView) convertView.findViewById(R.id.tvCity);
        textView.setText(mEntityList.get(position).getDevice_type_name());
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ItemImage);
        String ImageFaultUrl = mEntityList.get(position).getIcon_base_url() + "/" + Constants.MENUDEFAULT;
        String ImageSelectedUrl = mEntityList.get(position).getIcon_base_url() + "/" + Constants.MENUSELECTED;
        ImageLoadUtils.loadImage(mContext, ImageFaultUrl, imageView);
        if (mPosition == position) {
            Common.ICON_BASE_URL = mEntityList.get(position).getIcon_base_url();
            ImageLoadUtils.loadImage(mContext, ImageSelectedUrl, imageView);
            textView.setTextColor(Color.parseColor("#00bdfe"));
        }

        return convertView;
    }

}
