package com.isgala.xishuashua.bean_;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by and on 2016/11/17.
 */

public class WaitORService extends Base implements Serializable{
    public WaitOrServiceBean data;

    public static class WaitOrServiceBean implements Serializable{
        public String result;
        public String msg;
        public String status;
        public String vstatus;
        public String position;
        public String num;
        public String id;
        public String password;
        public String countdown;
        public String time;
        public String water;
        public String money;
        public ArrayList<String> tips;
    }
}
