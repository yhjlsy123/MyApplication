package com.zhuochi.hydream.bean_;

import java.io.Serializable;

/**
 *充值记录
 * Created by and on 2016/11/19.
 */

public class RechargeLog implements Serializable {
    /*
    "rc_id":"1",
                "name":"充值0.03元",
                "give":"送0元",
                "recharge":"0.03",
                "end_time":"2017-08-24",
                "created":"2016-02-15 15:20:04"
     */
    public String rc_id;
    public String name;
    public String give;
    public String recharge;
    public String end_time;
    public String created;
}
