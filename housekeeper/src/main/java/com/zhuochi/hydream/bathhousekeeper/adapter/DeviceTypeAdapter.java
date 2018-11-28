package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.bean.DeviceTypBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceTypeAdapter extends BaseAdapter {
    private Context context;
    List<DeviceTypBean> data;
    private ViewHolder viewHolder;
    private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();

    public void setMapValue(Integer pos, Boolean value) {
        map.clear();
        map.put(pos, true);
    }

    public DeviceTypeAdapter(Context context, List<DeviceTypBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_text, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.areaName.setText(data.get(position).getName_text());
        if (map.containsKey(position)) {
            viewHolder.arrow.setChecked(true);
        } else {
            viewHolder.arrow.setChecked(false);
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.area_name)
        TextView areaName;
        @BindView(R.id.arrow)
        CheckBox arrow;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
