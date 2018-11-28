package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.ViewHolder;
import com.zhuochi.hydream.bathhousekeeper.entity.SchoolEntity;

import java.util.List;

/**
 * 学校列表
 * Created by and on 2016/11/7.
 */

public class SchoolOrgListAdapter extends BAdapter<SchoolEntity.OrgAreaBean> {
    public SchoolOrgListAdapter(List<SchoolEntity.OrgAreaBean> list, int layoutId, Context context) {
        super(list, layoutId, context);
    }

    @Override
    public void upDateView(int position, ViewHolder holder, SchoolEntity.OrgAreaBean item) {
        if (position == getCount()-1) {
            holder.getView(R.id.school_downline, ImageView.class).setPadding(0, 0, 0, 0);
        } else {
            holder.getView(R.id.school_downline, ImageView.class).setPadding(20, 0, 0, 0);
        }
        holder.getView(R.id.school_name, TextView.class).setText(item.getOrg_area_name());
    }
}
