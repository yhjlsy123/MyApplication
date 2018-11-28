package com.zhuochi.hydream.ViewHolder;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.entity.RecordEntity;
import com.zhuochi.hydream.http.RequestURL;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.Tools;

import java.text.ParseException;


/**
 * Created by Inga on 2018/7/17.
 */

public class RecordEntityViewHolder extends BaseViewHolder<RecordEntity.ConsumptionListBean> {
    ImageView imgSign;
    TextView textRecordLength;
    TextView textRecordMoney;
    TextView textRecordTime;
    TextView textRecordState;
    LinearLayout itemRankBg;

    public RecordEntityViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_record);
        imgSign = $(R.id.img_sign);
        textRecordLength = $(R.id.text_record_length);
        textRecordMoney = $(R.id.text_record_money);
        textRecordTime = $(R.id.text_record_time);
        textRecordState = $(R.id.text_record_state);
        itemRankBg = $(R.id.item_rank_bg);

    }

    @Override
    public void setData(final RecordEntity.ConsumptionListBean recordEntity) {

        String ImageFaultUrl = RequestURL.ICON_URL + "/" + recordEntity.getIcon();
        ImageLoadUtils.loadImage(getContext(), ImageFaultUrl, imgSign);
        textRecordLength.setText("时长：" + Tools.SToMin(recordEntity.getTotal_used_time()) + "分钟");
        textRecordTime.setText(Tools.ToMDHMS(recordEntity.getStart_time()));
        textRecordMoney.setText(recordEntity.getCash_amount() + "元");
        switch (recordEntity.getOrder_state()) {
//            0:使用中|1(默认):未付款|2:已付款|3:已发货|4:已收货|5:已退款|6:已取消|7:退款审核中
            case "1":
                textRecordState.setText("未付款");
                break;
            case "2":
                textRecordState.setText("已完成");
                textRecordState.setTextColor(Color.RED);
                break;
        }

    }
}