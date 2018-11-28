package com.zhuochi.hydream.bean_;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/19.
 */

public class BackPasswordEntity extends Base{
    public BackPasswordData data;

    public static class BackPasswordData implements Serializable {
        public String status;
        public String msg;
    }
}
