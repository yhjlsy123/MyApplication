package com.isgala.xishuashua.bean_;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by and on 2016/11/14.
 */

public class RecordList extends Base {
    public RecordData data;

    public static class RecordData implements Serializable {
        public String count;
        public String nowPage;
        public String totalPage;
        public Total total;
        public ArrayList<LogRecord> result;
    }

    public static class LogRecord implements Serializable {
        public String created;
        public String total_time;
        public String total_time2;
        public String water;
        public String payable;
        public String pay_status;
        public String order_id;
        public String show_bath_type;
    }

    public static class Total implements Serializable {
        public String payable;
        public String water;
        public String total_time;
    }
}
