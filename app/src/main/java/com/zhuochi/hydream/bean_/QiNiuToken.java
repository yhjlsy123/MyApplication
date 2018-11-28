package com.zhuochi.hydream.bean_;

import java.io.Serializable;

/**
 * 七牛Token实体类
 *
 * @author gong
 */
public class QiNiuToken extends Base {

    public DataBean data;

    public static class DataBean implements Serializable {
        public String domain;
        public String uptoken;
    }
}
