package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhuochi.hydream.R;

import com.zhuochi.hydream.activity.RecordDetialActivity;
import com.zhuochi.hydream.activity.UnpaidOrderActivity;
import com.zhuochi.hydream.base.BAdapter;
import com.zhuochi.hydream.base.ViewHolder;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.entity.RecordEntity;
import com.zhuochi.hydream.http.RequestURL;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.Tools;


import java.util.List;

public class RecordAdapter extends BAdapter<RecordEntity.ConsumptionListBean> {
    public RecordAdapter(List<RecordEntity.ConsumptionListBean> list, int layoutId, Context context) {
        super(list, layoutId, context);
    }

    @Override/*获取控件并赋值*/
    public void upDateView(int position, ViewHolder holder, final RecordEntity.ConsumptionListBean item) {
        ImageView imgSign = holder.getView(R.id.img_sign, ImageView.class);
        TextView textRecordLength = holder.getView(R.id.text_record_length, TextView.class);
        TextView textRecordMoney = holder.getView(R.id.text_record_money, TextView.class);
        TextView textRecordTime = holder.getView(R.id.text_record_time, TextView.class);
        final TextView textRecordState = holder.getView(R.id.text_record_state, TextView.class);
        LinearLayout itemRankBg = holder.getView(R.id.item_rank_bg, LinearLayout.class);
        String ImageFaultUrl = RequestURL.ICON_URL + "/" + item.getIcon();
        ImageLoadUtils.loadImage(context, ImageFaultUrl, imgSign);
        textRecordLength.setText("时长：" + Tools.change(item.getTotal_used_time()));
        textRecordTime.setText(Tools.ToMDHMS(item.getStart_time()));
        textRecordMoney.setText(item.getCash_amount() + "元");
        Glide.with(context).load(RequestURL.ICON_URL + item.getIcon()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.mipmap.ic_launcher).into(imgSign);
        final String state = item.getOrder_state();

        switch (state) {
//            0:使用中|1(默认):未付款|2:已付款|3:已发货|4:已收货|5:已退款|6:已取消|7:退款审核中
            case "1":
                textRecordState.setText("未付款");
                textRecordState.setTextColor(Color.RED);
                break;
            default:
                textRecordState.setText("已完成");
                textRecordState.setTextColor(Color.parseColor("#00bdfe"));
                break;
        }
        itemRankBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tagtag", "onclick");
                switch (state) {
                    case "1":
                        Intent intent = new Intent(context, UnpaidOrderActivity.class);
//                        intent.putExtra("order", JSON.toJSONString(item));
                        context.startActivity(intent);
                        break;
                    default:
                        Intent intent1 = new Intent(context, RecordDetialActivity.class);
                        intent1.putExtra("order", JSON.toJSONString(item));
                        context.startActivity(intent1);
                        break;
                }

            }
        });

    }
}


