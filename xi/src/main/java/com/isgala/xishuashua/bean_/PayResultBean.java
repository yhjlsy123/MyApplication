package com.isgala.xishuashua.bean_;

import java.io.Serializable;
import java.util.List;

/**
 * Created by and on 2016/11/18.
 */

public class PayResultBean extends Base {
    public PayResultData data;

    public static class PayResultData implements Serializable {
        /*
            "activity_id": "",
        "balance": "1192.48元",
        "order_id": "108",
        "payable": "0.00",
        "ranking": [
         {
                "nickname": "同学3072",
                "order_id": "54",
                "photo": "http://ogd58ywup.bkt.clouddn.com/ic_launcher.png",
                "top": "1",
                "total_time": "39分",
                "v_id": "1"

        ]],
        "total_pay": "",
        "total_time": "1分",
        "water": "0.000m³"
        "show_bath_type":""用水量
         */
        public String order_id;
        public String payable;
        public String total_time;
        public String water;
        public String show_bath_type;
        public String balance;
        public String total_price;
        public String activity_id;
        public List<RankItem> ranking;
    }
}
