package com.isgala.xishuashua.bean_;

import java.io.Serializable;
import java.util.List;

/**
 * 浴室选择
 * Created by and on 2016/11/11.
 */

public class BathRoomFilter extends Base {
    public List<Filter> data;

    public static class Filter implements Serializable {
        public String b_id;
        public String name;
        public String b_name;
        public String ratio;
    }
}
