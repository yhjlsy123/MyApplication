package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.entity.GiveEntity;

import java.util.List;

/*转赠类型*/
public class GiveAdapter extends BaseAdapter {

    private Context mContext;
    private List<GiveEntity> mlist;

    public GiveAdapter(Context context, List<GiveEntity> objects) {
        mContext = context;
        mlist = objects;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_give, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tvbathroom.setText(mlist.get(position).getValue());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.change(mlist.get(position).getKey(),mlist.get(position).getAccount(),mlist.get(position).getValue());
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
        void change(String item,String account,String name);
    }

    public class ViewHolder {
        TextView tvbathroom;

        public ViewHolder(View view) {
            tvbathroom = (TextView) view.findViewById(R.id.text_GiveType);

        }
    }
}
