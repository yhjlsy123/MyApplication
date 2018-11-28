package com.zhuochi.hydream.bean_;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 排队的bean
 * Created by and on 2016/11/17.
 */

public class LineUpBean extends Base {
    public  Lineup data;
    public static class Lineup implements Serializable{
        public String result;
        public String msg;
        public String id;
        public String status;
        public String queue_number;
        public String wait_time;
        public String position;
        public String password;
        public String jump;
        public ArrayList<String> tips;
    }
}
