package com.isgala.xishuashua.bean_;

import java.io.Serializable;

/**
 * 排行榜条目
 * Created by and on 2016/11/11.
 */

public class RankItem implements Serializable {
    /**
     * {
     * "order_id": "399",
     * "total_time": "40分00秒",
     * "v_id": "8",
     * "nickname": "天使yet 0050",
     * "photo": "http://7xrlwm.com2.z0.glb.qiniucdn.com/20161028140534814.jpg",
     * "top": 2
     * }
     */
    public String order_id;
    public String total_time;
    public String v_id;
    public String nickname;
    public String photo;
    public String top;
}
