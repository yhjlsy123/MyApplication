package com.zhuochi.hydream.bean_;


import java.io.Serializable;

/**
 *
 * Created by and on 2016/11/9.
 */

public class Result extends Base {
    public ResultData data;
    public static class ResultData implements Serializable {
        public String result;
        public String msg;
        public String status;
        public int flag;
        public String tip;
        public String recharge_type;
        public String card_no;
        public String is_checked;
    }
}
