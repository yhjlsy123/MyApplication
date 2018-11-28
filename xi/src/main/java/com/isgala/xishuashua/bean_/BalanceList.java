package com.isgala.xishuashua.bean_;

import java.io.Serializable;

/**
 * Created by and on 2016/11/19.
 */
public class BalanceList implements Serializable{
    /*
    {
                    "log_id":"1280",
                    "text":"充值-微信充值",
                    "week":"周四",
                    "date":"10-27",
                    "price":"0.01",
                    "icon":"weixin"
                }
     */
    public String log_id;
    public String text;
    public String week;
    public String price;
    public String date;
    public String icon;
}
