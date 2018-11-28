package com.zhuochi.hydream.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.bean_.BalanceBean;
import com.zhuochi.hydream.bean_.BalanceLogDetail;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.RequestURL;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.UserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 余额详情
 */
public class BalanceDetailActivity extends BaseAutoActivity {


    @BindView(R.id.img_sign)
    ImageView imgSign;
    @BindView(R.id.tv_balance_type)
    TextView tvBalanceType;
    @BindView(R.id.tv_balance_money)
    TextView tvBalanceMoney;
    @BindView(R.id.tv_balance_state)
    TextView tvBalanceState;
    @BindView(R.id.payment_method)
    TextView paymentMethod;
        @BindView(R.id.commodity_description)
    TextView commodityDescription;
    @BindView(R.id.creation_time)
    TextView creationTime;
    @BindView(R.id.order_number)
    TextView orderNumber;

    @BindView(R.id.activity_record_detial)
    LinearLayout activityRecordDetial;
    XiRequestParams params;
    String Log_id;//余额记录Id


    @BindView(R.id.dafault)
    View dafault;
    @BindView(R.id.lin)
    LinearLayout lin;

    BalanceLogDetail balanceLogDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_detail);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        /*获取上一个界面传过来的BalanceListBean，并取得Log_id*/
        String jsonStr = getIntent().getExtras().get("balance").toString();
        BalanceBean.BalanceListBean balanceListBean = JSON.parseObject(jsonStr, BalanceBean.BalanceListBean.class);
        Log_id = balanceListBean.getId() + "";
        if ((!NetworkUtil.isNetworkAvailable())) {
                  /*没网显示缺省内容*/
            lin.setVisibility(View.GONE);
            dafault.setVisibility(View.VISIBLE);
        } else {
            balanceLogDetail();
        }

    }


    /*余额记录详情*/
    private void balanceLogDetail() {
        params.addCallBack(this);
        params.balanceLogDetail(UserUtils.getInstance(this).getMobilePhone(), UserUtils.getInstance(this).getUserID(), Log_id);

    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "balanceLogDetail":/*余额记录详情*/
                Object object = result.getData().getData();
                balanceLogDetail = JSON.parseObject(JSON.toJSONString(object), BalanceLogDetail.class);
                if (balanceLogDetail != null) {
                    lin.setVisibility(View.VISIBLE);
                    dafault.setVisibility(View.GONE);
                    String ImageFaultUrl = RequestURL.ICON_URL + "/" + balanceLogDetail.getIcon();
                    ImageLoadUtils.loadImage(BalanceDetailActivity.this, ImageFaultUrl, imgSign);
                    tvBalanceType.setText(balanceLogDetail.getPurpose_name());
                    tvBalanceMoney.setText(balanceLogDetail.getBill_amount());
                    paymentMethod.setText(balanceLogDetail.getPay_type_name());
                    tvBalanceState.setText("交易成功");
                    creationTime.setText(Common.getDateToYMDHM(balanceLogDetail.getCreate_time()));
                    orderNumber.setText(balanceLogDetail.getPay_sn());
                    commodityDescription.setText(balanceLogDetail.getPurpose_name());
                } else {/*显示缺省页*/
                    lin.setVisibility(View.GONE);
                    dafault.setVisibility(View.VISIBLE);
                }
                break;

        }
        super.onRequestSuccess(tag, result);
    }

    @OnClick({R.id.userinfo_cancle, R.id.order_doubt})
    public void OnClick(View view) {

        switch (view.getId()) {
            case R.id.userinfo_cancle:
                finish();
                break;
            case R.id.order_doubt:
//                getAboutOrde();
//                showDialog();
                break;
        }
    }

    /*弹出弹窗*/
//    private void showDialog() {
//        OrderDialog.Builder builder = new OrderDialog.Builder(this);
//        builder.create().show();
//        builder.setOnClickListener(new OrderDialog.OrderListener() {
//                                       @Override
//                                       public void submit() {
///*填写要执行的操作*/
//                                       }
//                                   }
//        );
//    }

}
