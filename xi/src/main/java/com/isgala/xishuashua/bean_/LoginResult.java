package com.isgala.xishuashua.bean_;

import java.io.Serializable;

/**
 * Created by and on 2016/11/7.
 */

public class LoginResult extends Base {

    public DataBean data;

    public static class DataBean implements Serializable {
        public String status;
        public String message;
        public String oauth_token;
        public String oauth_token_secret;
        public String v_id;
        public String nickname;
        public String sex;
        public String phone;
        public String s_id;
        public String integral;
        public String campus;
        public String photo;
        public String b_id;
        //新增
        public String device_type;
    }
}
