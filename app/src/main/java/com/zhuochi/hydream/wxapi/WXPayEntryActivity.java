package com.zhuochi.hydream.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zhuochi.hydream.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhuochi.hydream.utils.Common;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_pay_result);
        api = WXAPIFactory.createWXAPI(this, Common.WX_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            // 支付成功
            if (resp.errCode == 0) {
                setPayResult(0);
            } else if (resp.errCode == -1) {
                // 展示错误提示Dialog
                setPayResult(-1);
            } else if (resp.errCode == -2) {
                // 展示取消支付的Dialog
                setPayResult(-2);
            }
        }
        // 结束掉自己
        finish();
    }

    private void setPayResult(int payResult) {
        api.unregisterApp();
        Intent intent = new Intent();
        intent.setAction("WXPAYRESULT");
        intent.putExtra("payResult", payResult);
        sendOrderedBroadcast(intent, null);
    }
}