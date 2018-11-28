package com.isgala.xishuashua.bean_;

import java.io.Serializable;

/**
 * 余额支付后返回的结果
 * Created by and on 2016/11/18.
 */

public class PayCallBack extends Base {
    /**
     * {"status":"1","msg":"","data":{"result":"1","msg":"\u652f\u4ed8\u6210\u529f","recharge":"366.30","give":"0","by_account":"366.30",
     * "total_fee":"0.00","total_price":"366.30","time":"2016-11-18","order_number":"O16111815069983","pay_type":"\u5e73\u53f0\u4f59\u989d"}}
     */
    public PayCallBackBean data;

    public static class PayCallBackBean implements Serializable {
        public String result;
        public String msg;
        public String recharge;
        public String give;
        public String by_account;
        public String total_fee;
        public String total_price;
        public String time;
        public String order_number;
        public String pay_type;
    }
}