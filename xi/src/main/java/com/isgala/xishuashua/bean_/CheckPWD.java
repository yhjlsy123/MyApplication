package com.isgala.xishuashua.bean_;

import java.io.Serializable;

/**
 * Created by and on 2016/11/26.
 * <p>
 * 通过判断 password 中的 result 确认是否已经有了密码。
 * 通过判断 school 中的 result 判断是否选择了学校
 * auth中的result表示当前用户是否已经认证了。
 * 通过判断need_auth判断本人是否需要进行认证----1表示  弹窗填写认证信息。
 *
 * need_yajin 判断是否需要交押金
 * yajin - result 判断是否交押金
 */

public class CheckPWD extends Base {
    public CheckPWDData data;

    public static class CheckPWDData implements Serializable {
        public Result school;
        public Result auth;
        public String need_auth;
        public Result password;
        public String need_yajin;
        public Result yajin;
        public Result account_refund;
        public Result isallowappoint;
        public String device_type;

    }

    public static class Result implements Serializable{
        public String msg;
        public String result;
    }
}
