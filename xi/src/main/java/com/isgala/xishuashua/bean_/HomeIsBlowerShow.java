package com.isgala.xishuashua.bean_;

import java.io.Serializable;

/**
 * @author Cuixc
 * @date on  2018/6/13
 */

public class HomeIsBlowerShow extends Base{
    public Data data;

    public static class Data  implements Serializable {

        public String result;//状态码 0 正常 1 排队中 2 待服务/服务中
        public String is_allow_blower;// 浴位的ID/排队的id
        public String msg;
    }
}
