package com.isgala.xishuashua.bean_;

import java.io.Serializable;

/**
 * 订单页面
 * Created by and on 2016/11/18.
 */

public class Order extends Base {
    public OrderDetail data;

    public static class OrderDetail implements Serializable {
        /*
         * {
         "status":"1",
         "msg":"",
         "data":{
         "order_id":"578",
         "payable":"50.00",
         "total_time":"1200",
         "water":"12㎡",
         "account":"490.05"
         }
         }
         */
        public String order_id;
        public String payable;
        public String total_time;
        public String water;
        public String account;
        public String hide;
    }
}
