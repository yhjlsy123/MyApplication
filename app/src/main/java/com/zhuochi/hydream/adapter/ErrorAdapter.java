package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuochi.hydream.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErrorAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder viewHolder;
    List<String> label;
    List<Object> content;

    public ErrorAdapter(Context context,  List<String>  label, List<Object> content) {
        this.context = context;
        this.label = label;
        this.content=content;
    }

    @Override
    public int getCount() {
        return label.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_give, null);
            viewHolder= new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.textGiveType.setText(label.get(position)+":\t\t"+content.get(position));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.text_GiveType)
        TextView textGiveType;
        @BindView(R.id.line_GiveType)
        LinearLayout lineGiveType;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
