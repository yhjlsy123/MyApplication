package com.zhuochi.hydream.config;

import com.zhuochi.hydream.utils.Common;

/**
 * Created by and on 2016/11/7.
 */

public interface Constants {
    //首页顶部列表弹窗类型图片
    String MENUDEFAULT = "menuDefault.png";
    String MENUSELECTED = "menuSelected.png";
    //首页洗浴图片类型
    String READY = "ready.png";     //可用
    String OFFLINE = "offline.png"; //离线  不可用
    String RUNNING = "running.png"; //正在使用  不可用
    String SELECTED = "selected.png";//点击状态
    String REPAIRING = "repairing.png";
    String FAULTED = "faulted.png";
    String JUNKED = "junked.png";
    String RESERVED = "reserved.png";

    //支付类型图标
    String ALIPAY = "alipay.png";
    String GENERAL_CARD = "general_card.png";
    String SYS_CASH = "sys_cash.png";
    String WEIXINPAY = "weixinpay.png";

    //用于每次生成json httpDES秘钥
    String hash_key = "q9KswPoz62jQk12W";
    //token 唯一标识
    String TOKEN_ID = "TOKEN_ID";
    //用户ID
    String USER_ID = "USER_ID";
    //手机号
    String MOBILE_PHONE = "MOBILE_PHONE";
    //用户状态;0:禁用,1:正常,2:未验证
    String USER_STATUE = "USER_STATUE";
    //组织机构ID（学校ID）
    String ORG_ID = "ORG_ID";
    //组织机构ID（校区ID）
    String ORG_AREA_ID = "ORG_AREA_ID";
    //组织区域下属的建筑物ID（楼层ID）
    String BUILDING_ID = "BUILDING_ID";
    //当前绑定区域（浴室）
    String DEVICE_AREA_ID = "DEVICE_AREA_ID";
    String USER_OAUTH = "USER_OAUTH";
    String BATH_ID = "BATH_ID";
    //意见反馈照片数量
    public static final int FEED_BACK_PHOTO_NUM = 3;
    public static boolean TIME_POP_STATE = true;

//    String ImageReadyUrl = Common.ICON_BASE_URL + "/" + READY;
//    String ImageOfflineUrl = Common.ICON_BASE_URL + "/" + OFFLINE;//不可用
//    String ImageRunningUrl = Common.ICON_BASE_URL + "/" + RUNNING;
//    String ImageRepairingUrl = Common.ICON_BASE_URL + "/" + REPAIRING;
//    String ImageFaultedUrl = Common.ICON_BASE_URL + "/" + FAULTED;
//    String ImageJunkedUrl = Common.ICON_BASE_URL + "/" + JUNKED;
//    String ImageReservedUrl = Common.ICON_BASE_URL + "/" + RESERVED;

//    String ImageSelectedUrl = Common.ICON_BASE_URL + "/" + SELECTED;


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
     * 刷新总线 消息
     */
    String REFRESHBUS = "RefreshBus";
    /**
     * 定时刷新
     */
    String TIMING_REFRESH = "Timing_Refresh";
    /**
     * 设备类型
     */
    String DEVICE_TYPE = "device_type";
    /**
     * 这里存的是选择完浴室生成的id
     */
    String YB_ID = "yb_id";

    String PHONE_NUMBER = "phone";


}
