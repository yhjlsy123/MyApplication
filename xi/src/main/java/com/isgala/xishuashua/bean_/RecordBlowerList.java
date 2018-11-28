package com.isgala.xishuashua.bean_;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by and on 2016/11/14.
 */

public class RecordBlowerList extends Base {
    public RecordBlowerData data;

    public static class RecordBlowerData implements Serializable {
        public String total_price;
        public String real_payment;
        public String blower_total_time;
        public ArrayList<blower_consume_list> blower_consume_list;
    }

    public static class blower_consume_list implements Serializable {
        public String order_price;
        public String payable;
        public String total_time;
        public String pay_status;
        public String created;
    }

}
