package com.isgala.xishuashua.bean_;

import java.io.Serializable;
import java.util.List;

/**
 * Created by and on 2016/11/9.
 */

public class ShowerList extends Base {
    /**
     * "default":{"s_id":"1","campus":"2","b_id":"1"}
     * "shower": [
     * {
     * "id": "3",
     * "num": "3",
     * "status": "1"
     * },
     * {
     * "id": "2",
     * "num": "2",
     * "status": "1"
     * },
     * {
     * "id": "1",
     * "num": "1",
     * "status": "1"
     * }
     * ],
     * "fee_scale": "前30min 0.1元/分 30min后0.5元/分"
     */

    public ShowerData data;

    public static class ShowerData implements Serializable {
        public List<Shower> shower;
        public String fee_scale;
        public String fee_scale_notice;
        public String queue_number;
        public String wait_time;
        public String surplus;
        public String show_shower;
        public String show_message;
        public Default default_choice;
        public String is_pay;// 0：没有 1：有
        public List<Notice> notice;

        public static class Default implements Serializable {
            public String s_id;
            public String campus;
            public String b_id;
            public String position;
        }
    }

    public static class Notice implements Serializable {
        public String id;
        public String content;
    }

    public static class Shower implements Serializable {
        public String id;
        public String num;
        public String status;
    }
}
