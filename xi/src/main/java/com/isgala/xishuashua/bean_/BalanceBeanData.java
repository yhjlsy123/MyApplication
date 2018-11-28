package com.isgala.xishuashua.bean_;

import java.io.Serializable;
import java.util.List;

/**
 * Created by and on 2016/11/19.
 */
public class BalanceBeanData implements Serializable{

       /* {
            "month":"2016-10",
            "list":[
                {
                    "log_id":"1280",
                    "text":"充值-微信充值",
                    "week":"周四",
                    "date":"10-27",
                    "price":"0.01",
                    "icon":"weixin"
                }
            ]
        },

     */
    public String month;
    public List<BalanceList> list;
}
