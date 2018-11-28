package com.isgala.xishuashua.config;

/**
 * Created by and on 2016/11/7.
 */

public interface Constants {
    String LOCATON_LAT = "lat";
    String LOCATON_LNG = "lng";
    String OAUTH_TOKEN = "oauth_token";
    String OAUTH_TOKEN_SECRET = "oauth_token_secret";
    String LOCATION = "location";
    String DEVICE_TOKEN = "device_token";
    String IS_LOGIN = "login_is";
    /**
     * 存放个人的信息
     */
    String USER_INFO = "user_info";

    /**
     * 校区id
     */
    String CAMPUS = "campus";
    /**
     * 学校id(判断页面跳转的标记)
     */
    String S_ID = "s_id";
    /**
     * /**
     * 排队的广播接收字段(排队界面接收)
     */
    String LINEUP = "wait";

    /**
     * 服务开始的广播字段(等待界面接收)
     */
    String SERVICESTART = "wait_service";

    /**
     * 结束服务的广播字段(服务界面接收)
     */
    String SERVICEFINISH = "service";


    /**
     * 有未支付订单
     */
    String PAY_ORDER = "pay_order";

    /**
     * 查询的订单号
     */
    String ORDER_ID = "out_trade_no";

    /**
     * 切换首页的标记
     */
    String FINISH = "finish";

    /**
     * 新消息
     */
    String NEW_MESSAGE = "new_message";
    /**
     * 设备类型
     */
    String DEVICE_TYPE = "device_type";
    /**
     * 这里存的是选择完浴室生成的id
     */
    String YB_ID = "yb_id";

    String V_ID = "v_id";//用户登录ID
    String PHONE_NUMBER = "phone";
}
