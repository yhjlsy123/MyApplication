package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.entity.SchoolListWithUserNumEntity;

import java.util.List;

public class SchoolListWithUserAdapter extends BaseQuickAdapter<SchoolListWithUserNumEntity,BaseViewHolder> {

    public SchoolListWithUserAdapter(int layoutResId, @Nullable List<SchoolListWithUserNumEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SchoolListWithUserNumEntity item) {
        helper.setText(R.id.school,item.getOrg_name())
                .setText(R.id.area,item.getOrg_area_name())
                .setText(R.id.user_num,item.getNum());
    }
}
