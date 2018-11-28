package com.zhuochi.hydream.entity;

import com.zhuochi.hydream.bean_.Base;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by and on 2016/11/14.
 */

public class SchoolEntity  {
        public int org_id;
        public String org_name;

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }
}
