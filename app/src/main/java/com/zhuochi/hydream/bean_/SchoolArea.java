package com.zhuochi.hydream.bean_;


import java.io.Serializable;
import java.util.List;

/**
 * 校区列表
 * Created by and on 2016/11/.
 */

public class SchoolArea extends Base {
    public List<SchoolData> data;

    public static class SchoolData implements Serializable {
        public  String s_id;
        public String name;
        public List<Room>room;
    }

}