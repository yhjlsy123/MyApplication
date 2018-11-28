package com.isgala.xishuashua.bean_;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by and on 2016/11/14.
 */

public class School extends Base {
    public ArrayList<SchoolItem> data;

    public static class SchoolItem implements Serializable {
        public String s_id;
        public String name;
    }
}
