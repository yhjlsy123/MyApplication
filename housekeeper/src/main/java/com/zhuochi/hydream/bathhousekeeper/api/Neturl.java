package com.zhuochi.hydream.bathhousekeeper.api;


/**
 * Created by and on 2016/11/7.
 */

public interface Neturl {
    String HTTP = "http://";
    String HTTPS = "https://";
    //    "39.106.16.87"
    // TODO 上线修改
//    String SERVICE = "hydream.94feel.com";
//    String SERVICE = "ce.hydream.cn";
//    String SERVICE = "ceshi.hydream.cn";
    String SERVICE = "sw.hydream.cn";
    String S = "/";
    String SERVICE_PRO = "api_v1";
    String SERVICE_URL = HTTP + SERVICE + S;


    String WATER_URL = "http://47.95.215.57/appI/api/";
//    String WATER_URL="http://39.106.16.87/appI/api/";

    String VIP = SERVICE_URL + SERVICE_PRO + S + "Vip" + S;
    String INDEX = SERVICE_URL + SERVICE_PRO + S + "Index" + S;
    String OAUTH = SERVICE_URL + SERVICE_PRO + S + "Oauth" + S;
    //    String PUBLIC = SERVICE_URL + SERVICE_PRO + S + "Public" + S;
    String BATHROOM = SERVICE_URL + SERVICE_PRO + S + "Bathroom" + S;
    String PAYMENT = SERVICE_URL + SERVICE_PRO + S + "Payment" + S;
    String VERSION = SERVICE_URL + SERVICE_PRO + S + "Version" + S;
    String WATERCONTROL = SERVICE_URL + SERVICE_PRO + S + "Watercontrol" + S;
    String SENDMQTT = SERVICE_URL + SERVICE_PRO + S + "Sendmqtt" + S;


    String H5 = SERVICE_URL + "w3g/WebView/view?name=";

    /**
     * 用户协议
     */
    String REGISTER_TEXT = H5 + "agreement";

    /**
     * 用户指南
     */
    String USER_GUIDE = H5 + "guide";
    /**
     * 关于我们
     */
    String ABOUT_US = H5 + "about";

    /**
     * 初始化App
     */
    String INIT_APP = INDEX + "locations";


    /**
     * 首页活动弹框数据
     */
    String ACTIVITY_APP = VIP + "activity";

    /**
     * 个人信息
     */
    String USER_INFO = VIP + "user_info";
    /**
     * 修改个人信息
     */
    String MODIFY_USER_INFO = VIP + "modify_all";
    /**
     * 选择校区
     */
    String GET_CAMPUS = VIP + "get_campus";
    /**
     * 修改浴室
     */
    String MODIFY_BATHROOM = VIP + "modify_bathroom";

    /**
     * 获取学校的列表
     */
    String GET_SCHOOL = VIP + "get_school";
    /**
     * 修改学校
     */
    String MODIFY_SCHOOL = VIP + "modify_school";
    /**
     * 消息列表
     */
    String GET_MESSAGE = VIP + "get_message";

    /**
     * 分页的消息列表
     */
    String MESSAGE_LIST = VIP + "message_list";

    /**
     * 排行榜
     */
    String Rank_List = VIP + "ranking_list";
    /**
     * 意见反馈
     */
    String SUB_SUGGEST = VIP + "sub_suggest";

    /**
     * 获取反馈历史
     */
    String SUGGEST_HISTORY = VIP + "get_suggest";

    /**
     * 洗澡消费记录
     */
    String RECORD_LIST = VIP + "consume_log_page";
    /**
     * 实名认证
     */
    String CHECK_AUTHE = VIP + "check_auth";

    /**
     * 普通充值卡
     */
    String GENERAL_CARD = VIP + "new_general_card";

    /**
     * 交押金界面
     */
    String RECHARGE_YAJIN = VIP + "yajin_pay";

    /**
     * 退押金
     */
    String TUI_YAJIN_BACK = VIP + "yajin_back";

    /**
     * 余额退款页面
     */
    String BALANCE_REFUND = VIP + "account_refund";

    /**
     * 余额退款提交
     */
    String BALANCE_REFUND_COMMIT = VIP + "account_refund_submit";


    /**
     * 余额转押金
     */
    String BALANCE_TO_YAJIN = VIP + "balance_yajin";

    /**
     * 获取图形验证码
     */
    String GET_PIC_CODE = OAUTH + "login";
    /**
     * 获取七牛TOKEN
     */
    String QINIU_TOKEN = INDEX + "getToken";
    /**
     * 获取验证码
     */
    String GET_LOGIN_CODE = OAUTH + "api_get_sms";
    /**
     * 登录
     */
    String USER_LOGIN = OAUTH + "login_with_vrcode";

    /**
     * 检查是否拥有开柜密码
     */
    String CHECK_LOCK_PWD = OAUTH + "check_ppwd";

    /**
     * 设置开柜密码
     */
    String SET_LOCK_PWD = OAUTH + "set_ppwd";
    /**
     * 浴室列表
     */
    String SHOWER_LIST = BATHROOM + "shower";

    /**
     * 准备排队
     */
    String START_LINE_UP = BATHROOM + "wait";
    /**
     * 点击排队可自动跳转待服务
     */
    String LINEUP_TO_WAIT = BATHROOM + "wait_new";

    /**
     * 排队信息
     */

    String LINE_UP = BATHROOM + "wait_page";
    /**
     * 等待中的数据
     */
    String APPOINT_STATUS = BATHROOM + "wait_service";
    /**
     * 筛选具体的浴室
     */
    String SHOWERROOM_FILTER = BATHROOM + "getRoom";
    /**
     * 正常预约
     */
    String APPOINT = BATHROOM + "bespeak";
    /**
     * 取消预约
     */
    String CANCEL_APPOINT = BATHROOM + "cancel";
    /**
     * 获取订单
     */
    String PAY_PAGE = BATHROOM + "pay_page";

    /**
     * 请求停止服务
     */
    String REQUEST_STOP = BATHROOM + "request_stop";

    /**
     * 支付结果页
     */
    String PAY_RESULT = BATHROOM + "pay_success";
    /**
     * 余额支付
     */
    String YUE_PAY = PAYMENT + "balance_pay";

    /**
     *
     * 支付宝支付
     */
    String ALIPAY = PAYMENT + "getRechargeOrder";
    /**
     * 微信支付
     */
    String WEIXINPAY = PAYMENT + "unifiedorder";


    /**
     * 支付宝/微信支付回调
     */
    String PAY_RESULT_CALLBACK = PAYMENT + "check_pay";
    /**
     * 获取用户的账户信息
     */
    String GET_UER_WALLET = VIP + "get_user_money";

    /**
     * 余额明细
     */
    String BALANCE_LOG = VIP + "balance_log";

    /**
     * 余额详情
     */
    String BALANCE_DETAIL = VIP + "balance_detail";


    /**
     *
     */
    String RECHARGE_RULE = H5 + "recharge_rule";


    /**
     *
     * 改版
     * 改版
     * 改版
     *
     */

    /**
     * 获取首页 图片轮播数据
     */
    String GET_BANNER = INDEX + "get_banner";
    /**
     * 检测升级
     */
    String GET_UPGRADEAPP = VERSION + "upgradeApp";
    /**
     * 使用密码登陆
     */
    String GET_LOGIN_PASSWORD = OAUTH + "login_with_password";
    /**
     * 忘记密码
     */
    String FORGET_PASSWORD = OAUTH + "forget_password";
    /**
     * 同步登录，暂时用于凯路厂商（趣智校园）
     */
//    String SYNCHROLOGIN = OAUTH + "synchroLogin";
    /**
     * 同步充值，暂时用于凯路厂商（趣智校园）
     */
//    String SYNCHRORECHARGE = OAUTH + "synchroRecharge";
    /**
     * APP中显示各个站长联系方式
     */
    String STATION_CONTACT = OAUTH + "station_contact";
    /**
     * 更换手机号功能
     */
    String EDIT_MOBILE = OAUTH + "edit_mobile";
    /**
     * 用户转赠自己的资金到其他账号
     */
    String TRANSFER_ACCOUNTS = OAUTH + "transfer_accounts";
    /**
     * 凯路水控机洗浴结束后的回调函数
     */
//    String BATHENDORDER = WATERCONTROL + "bathEndOrder";
    /**
     * 凯路水控机洗浴开始的回调函数
     */
//    String BATHBEGINORDER = WATERCONTROL + "bathBeginOrder";
    /**
     * 用于超级澡堂用户和凯路后台用户同步金额
     */
//    String SYNCHROAMOUNT = WATERCONTROL + "synchroAmount";
    /**
     * 获取吹风机设备列表（一般就两个）
     */
    String SHOW_BLOWER_DEVICE = SENDMQTT + "show_blower_device";
    /**
     * 获取吹风机位置列表（筛选器）
     */
    String GET_BLOWER_POSITION = SENDMQTT + "get_blower_position";
    /**
     * 开启吹风机
     */
    String BEGIN_BLOWER = SENDMQTT + "begin_blower";
    /**
     * 一卡通认证
     */
    String GET_ONECARDSOLUTIONAUTH = VIP + "oneCardSolutionAuth";

    /**
     * 获取更多充值方式
     */
    String GET_RECHARGERYPE = PAYMENT + "getRechargeRype";

    /**
     * 一卡通充值
     */
    String GET_ONECARDRECHARGE = PAYMENT + "oneCardRecharge";
    /**
     * 一卡通支付(订单支付)
     */
    String GET_ONECARDPAY = PAYMENT + "oneCardPay";

    /**
     * 开始服务
     */
    String REQUEST_START = BATHROOM + "request_start";
    /**
     * 吹风机消费记录
     */
    String BLOWER_LIST = VIP + "blower_consume_log";
    /**
     * 是否认证一卡通
     */
    String IFAUTHONECARD = VIP + "ifAuthOneCard";
}
