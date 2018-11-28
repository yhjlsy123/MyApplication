package com.zhuochi.hydream.bean_;

import java.io.Serializable;
import java.util.List;

/**
 * Created by and on 2016/11/19.
 */

public class Wallet extends Base {
    /*{
    "status":"1",
    "msg":"",
    "data":{
        "cost":"700.04",
        "account":"480.04",
        "sub_account":"10.01",
        "balance":"490.05",
        "hd":[
            {
                "rc_id":"1",
                "name":"充值0.03元",
                "give":"送0元",
                "recharge":"0.03",
                "end_time":"2017-08-24",
                "created":"2016-02-15 15:20:04"
            }
        ]
    }
}
     *
     */
    public WalletData data;

    public static class WalletData implements Serializable {
        public String cost;
        public String account;
        public String sub_account;
        public String balance;
        public String deposit;
        public String yajin_notice;
        public String status;
        public String could_recharge;
        public String recharge_notice;
        public String show_deposit;
        public String refund;
        public String account_notice;
        public String recharge_handle_type;//1：本金退款 2：转赠 3：不允许退款和转赠（皆不可）
        public List<RechargeLog> hd;
    }
}
