package com.isgala.xishuashua.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BAdapter;
import com.isgala.xishuashua.base.OnItemClickListener;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.BalanceBeanData;
import com.isgala.xishuashua.bean_.BalanceList;
import com.isgala.xishuashua.ui.BalanceLogDetail;
import com.isgala.xishuashua.ui.PayActivity;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.VolleySingleton;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * 余额历史的月份item
 * Created by and on 2016/11/19.
 */

public class BalanceMonthAdapter extends BAdapter<BalanceBeanData> {
    public BalanceMonthAdapter(List<BalanceBeanData> list, int layoutId, Context context) {
        super(list, layoutId, context);
    }

    @Override
    public void upDateView(int position, ViewHolder holder, BalanceBeanData item) {
        if (position != 0 && TextUtils.equals(item.month, getListData().get(position - 1).month)) {//不是第一条，且头(月份)相同，不显示头(月份)信息
            holder.getView(R.id.month_balance, TextView.class).setVisibility(View.GONE);
            holder.getView(R.id.month_topline, View.class).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.month_topline, View.class).setVisibility(View.GONE);
            holder.getView(R.id.month_balance, TextView.class).setVisibility(View.VISIBLE);
        }
        holder.getView(R.id.month_balance, TextView.class).setText(item.month);
        ListView dayListView = holder.getView(R.id.day_listview, ListView.class);
        final BalanceDayAdapter balanceDayAdapter = new BalanceDayAdapter(context, R.layout.item_day_balance, item.list);
        dayListView.setAdapter(balanceDayAdapter);
        balanceDayAdapter.setOnItemClickListener(new OnItemClickListener<BalanceList>() {
            @Override
            public void onItemClick(BalanceList item, ViewHolder holder, int position) {
                HashMap<String, String> map = new HashMap<>();
                map.put("log_id", item.log_id);
                VolleySingleton.post(Neturl.BALANCE_DETAIL, "detail", map, new VolleySingleton.CBack() {
                    @Override
                    public void runUI(String result) {
                        com.isgala.xishuashua.bean_.BalanceLogDetail balanceLogDetail = JsonUtils.parseJsonToBean(result, com.isgala.xishuashua.bean_.BalanceLogDetail.class);
                        if (balanceLogDetail != null && balanceLogDetail.data != null) {
                            if (TextUtils.equals("0", balanceLogDetail.data.operate))
                                return;
                            Intent intent = new Intent(context, BalanceLogDetail.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("json", result);
                            context.startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}
