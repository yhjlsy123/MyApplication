package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.bean.NoticeManageItemBean;

import java.util.List;

public class NoticeManageListAdapter extends BaseItemDraggableAdapter<NoticeManageItemBean, BaseViewHolder> {
    public NoticeManageListAdapter(int layoutResId, @Nullable List<NoticeManageItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeManageItemBean item) {
        helper.setText(R.id.notice_school_name, item.getOrg_name() + "-" + item.getOrg_area_name())
                .setText(R.id.notice_content, Html.fromHtml(item.getContent()));
    }
}
