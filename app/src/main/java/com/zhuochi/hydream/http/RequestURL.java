package com.zhuochi.hydream.http;


import com.zhuochi.hydream.utils.SPUtils;

/**
 * @author Cuixc
 * @date on  2018/5/8
 */

public class RequestURL {
    public static String URL = SPUtils.getString("baseUrl", "http://lz.hydream.cn/api/v1/");
//    public static String URL = "http://hx.94lihai.com/api/v1/";
//    生产
//    public static String URL = "http://newdev.gaopintech.cn/api/v1/";
//    测试
//    public static String URL= "http://zaotang.94feel.com/api/v1/";

    //    public static String ICON_URL = "http://newdev.gaopintech.cn";
    public static String ICON_URL = " http://lz.hydream.cn";

    public static String IMAGE_URL = "http://newdev.gaopintech.cn/static/images/payment/";
    // =========================UserApi用户基础操作类====================================
    //账号密码登录
    public static String WIDTHPWD = URL + "UserApi/loginWidthPwd";
    public static String WIDTHPWD_ONLY = "loginWidthPwd";

    //注册 获取验证码
    public static String SENDSMSCODE = URL + "UserApi/sendSMSCode";
    public static String SENDSMSCODE_ONLY = "sendSMSCode";
    //注册
    public static String REG = URL + "UserApi/reg";
    public static String REG_ONLY = "reg";
    //快捷登录
    public static String FASTLOGIN = URL + "UserApi/fastLogin";
    public static String FASTLOGIN_ONLY = "fastLogin";

    //重置账号密码
    public static String FORGETPASSWORD1 = URL + "UserApi/forgetPassword";
    public static String FORGETPASSWORD_ONLY1 = "forgetPassword";

    //获取钱包信息
    public static String GETWALLETINFO = URL + "UserApi/getWalletInfo";
    public static String GETWALLETINFO_ONLY = "getWalletInfo";

    // =========================OauthApi用户鉴权====================================
    //获取Token获取唯一的识别码，可替代本机的唯一设备号 UUID
    public static String GETTOKEN = URL + "OauthApi/getToken";
    public static String GETTOKEN_ONLY = "getToken";
    //获取七牛Token
    public static String GETUPLOADFILETOKEN = URL + "OauthApi/getUploadFileToken";
    public static String GETUPLOADFILETOKEN_ONLY = "getUploadFileToken";
    //推送授权信息  获取推送的秘钥（目前是极光）
    public static String GETPUSHAUTH = URL + "OauthApi/getPushAuth";
    public static String GETPUSHAUTH_ONLY = "getPushAuth";
    //获取用户基础信息
    public static String GETUSERBASEINFO = URL + "MemberApi/getUserBaseInfo";
    public static String GETUSERBASEINFO_ONLY = "getUserBaseInfo";
    //修改密码
    public static String CHANGEPASSWORD = URL + "MemberApi/changePassword";
    public static String CHANGEPASSWORD_ONLY = "changePassword";
    //修改基础信息
    public static String CHANGBASEINFO = URL + "MemberApi/changBaseInfo";
    public static String CHANGBASEINFO_ONLY = "changBaseInfo";

    //获取设备信息（获取浴室信息）
    public static String GETDEVICEINFO = URL + "MemberApi/getDeviceInfo";
    public static String GETDEVICEINFO_ONLY = "getDeviceInfo";
    //获取认证信息  如：学工号、宿舍号、一卡通账号等
    public static String GETOTHERAUTHINFO = URL + "MemberApi/getOtherAuthInfo";
    public static String GETOTHERAUTHINFO_ONLY = "getOtherAuthInfo";
    //认证一卡通
    public static String VALIDATEIDNO = URL + "MemberApi/validateIdNo";
    public static String VALIDATEIDNO_ONLY = "validateIdNo";

    //设置设备信息（选择设备信息类似选择浴室）
    public static String SETDEVICEINFO = URL + "MemberApi/setDeviceInfo";
    public static String SETDEVICEINFO_ONLY = "setDeviceInfo";
    //设置头像
    public static String SETAVATAR = URL + "MemberApi/setAvatar";
    public static String SETAVATAR_ONLY = "setAvatar";
    //更换手机号
    public static String CHANGEMOBILE = URL + "MemberApi/changeMobile";
    public static String CHANGEMOBILE_ONLY = "changeMobile";
    //=======================================DeviceAreaApi====================
    //设备选择器   一次返回全部的设备区域信息
    public static String DEVICESELECTOR = URL + "DeviceAreaApi/deviceSelector";
    public static String DEVICESELECTOR_ONLY = "deviceSelector";
    //获取组织机构（学校）
    public static String GETORGS = URL + "DeviceAreaApi/getOrgs";
    public static String GETORGS_ONLY = "getOrgs";
    //获取组织机构区域（校区）
    public static String GETORGAREAS = URL + "DeviceAreaApi/getOrgAreas";
    public static String GETORGAREAS_ONLY = "getOrgAreas";
    //获取楼号列表（楼号）
    public static String GETBUILDINGS = URL + "DeviceAreaApi/getBuildings";
    public static String GETBUILDINGS_ONLY = "getBuildings";
    //获取设备列表（浴室）
    public static String GETDEVICEAREA = URL + "DeviceAreaApi/getDeviceArea";
    public static String GETDEVICEAREA_ONLY = "getDeviceArea";
    //根据服务区域获取设备列表(首页设备列表)
    public static String SELECTDEVICESBYAREAID = URL + "DeviceAreaApi/selectDevicesByAreaId";
    public static String SELECTDEVICESBYAREAID_ONLY = "selectDevicesByAreaId";
    //排队
    public static String QUEUEUP = URL + "DeviceAreaApi/queueUp";
    public static String QUEUEUP_ONLY = "queueUp";
    //取消排队
    public static String CANCELQUEUE = URL + "DeviceAreaApi/cancelQueue";
    public static String CANCELQUEUE_ONLY = "cancelQueue";
    //获取设备类型列表(首页顶部弹出类型)
    public static String SELECTDEVICETYPEBYORGAREAID = URL + "DeviceAreaApi/selectDeviceTypeByOrgAreaId";
    public static String SELECTDEVICETYPEBYORGAREAID_ONLY = "selectDeviceTypeByOrgAreaId";

    //获取首页下拉（原浴室）列表
    public static String SELECTDEVICEAREASWITHDEVICESTATE = URL + "DeviceAreaApi/selectDeviceAreasWithDeviceState";
    public static String SELECTDEVICEAREASWITHDEVICESTATE_ONLY = "selectDeviceAreasWithDeviceState";
    //根据设备类型获取状态
    public static String GETSTATEBYDEVICETYPE = URL + "DeviceAreaApi/getStateByDeviceType";
    public static String GETSTATEBYDEVICETYPE_ONLY = "getStateByDeviceType";
    //根据设备类型获取提示语状态
    public static String GETDEVICEAREABYID = URL + "DeviceAreaApi/getDeviceAreaById";
    public static String GETDEVICEAREABYID_ONLY = "getDeviceAreaById";
    //==========================================FeedbackApi意见反馈===========================================
    //获取意见反馈类型列表
    public static String FEEDBACKTYPELIST = URL + "FeedbackApi/feedbackTypeList";
    public static String FEEDBACKTYPELISTA_ONLY = "feedbackTypeList";
    //反馈意见(首页)
    public static String FEEDBACK = URL + "FeedbackApi/feedback";
    public static String FEEDBACK_ONLY = "feedback";
    //意见反馈列表
    public static String MYFEEDBACKLIST = URL + "FeedbackApi/myFeedbackList";
    public static String MYFEEDBACKLIST_ONLY = "myFeedbackList";
    //意见反馈对话列表
    public static String FEEDBACKDETAILLIST = URL + "FeedbackApi/feedbackDetailList";
    public static String FEEDBACKDETAILLIST_ONLY = "feedbackDetailList";
    //意见反馈对话
    public static String FEEDBACKDIALOG = URL + "FeedbackApi/feedbackDialog";
    public static String FEEDBACKDIALOG_ONLY = "feedbackDialog";

    //=========================================MessageApi消息=======================================
    //消息类型列表
    public static String MESSAGETYPE = URL + "MessageApi/messageType";
    public static String MESSAGETYPE_ONLY = "messageType";
    //指定消息类型的消息列表
    public static String MESSAGELISTBYTYPE = URL + "MessageApi/messageListByType";
    public static String MESSAGELISTBYTYPE_ONLY = "messageListByType";
    //消息详情
    public static String MESSAGEDETAIL = URL + "MessageApi/messageDetail";
    public static String MESSAGEDETAIL_ONLY = "messageDetail";
    //=======================================SettingApi联系站长======================================
    //联系站长
    public static String CONTACTMANAGER = URL + "SettingApi/contactManager";
    public static String CONTACTMANAGER_ONLY = "contactManager";
    //注册协议
    public static String REGISTRATIONAGREEMENT = URL + "SettingApi/registrationAgreement";
    public static String REGISTRATIONAGREEMENT_ONLY = "registrationAgreement";
    //常见问题
    public static String COMMONPROBLEM = URL + "SettingApi/commonProblem";
    public static String COMMONPROBLEM_ONLY = "commonProblem";
    //初始化设置
    public static String GETINIT = URL + "SettingApi/getInit";
    public static String GETINIT_ONLY = "getInit";
    //获取广告列表
    public static String GETADLIST = URL + "SettingApi/getAdList";
    public static String GETADLIST_ONLY = "getAdList";
    //=======================================DeviceApi设备相关接口===================================
    //预约设备
    public static String RESERVE = URL + "DeviceApi/reserve";
    public static String RESERVE_ONLY = "reserve";
    //开启设备
    public static String TURNON = URL + "DeviceApi/turnOn";
    public static String TURNON_ONLY = "turnOn";
    //关闭设备
    public static String TURNOFF = URL + "DeviceApi/turnOff";
    public static String TURNOFF_ONLY = "turnOff";
    //取消预约
    public static String CANCELRESERVE = URL + "DeviceApi/cancelReserve";
    public static String CANCELRESERVE_ONLY = "cancelReserve";
    //获取错误信息
    public static String SHOWERRORCODES = URL + "DeviceApi/showErrorCodes";
    public static String SHOWERRORCODES_ONLY = "showErrorCodes";
    //设置设备密码
    public static String SETDEVICEPWD = URL + "DeviceApi/setDevicePwd";
    public static String SETDEVICEPWD_ONLY = "setDevicePwd";
    //修改设备密码
    public static String CHANGEDEVICEPWD = URL + "DeviceApi/changeDevicePwd";
    public static String CHANGEDEVICEPWD_ONLY = "changeDevicePwd";

    //=======================================BankrollApi个人中心支付相关接口===================================
    //转赠
    public static String TRANSFER = URL + "BankrollApi/transfer";
    public static String TRANSFER_ONLY = "transfer";
    //转赠类型
    public static String ALLOWTRANSFERTYPE = URL + "BankrollApi/allowTransferType";
    public static String ALLOWTRANSFERTYPE_ONLY = "allowTransferType";
    // 允许的退款类型
    public static String ALLOWREFUNDTYPE = URL + "BankrollApi/allowRefundType";
    public static String ALLOWREFUNDTYPE_ONLY = "allowRefundType";
    // 充值卡列表
    public static String RECHARGECARDS = URL + "BankrollApi/rechargeCards";
    public static String RECHARGECARDS_ONLY = "rechargeCards";
    // 充值方式
    public static String ALLOWRECHARGEMETHOD = URL + "BankrollApi/allowRechargeMethod";
    public static String ALLOWRECHARGEMETHOD_ONLY = "allowRechargeMethod";
    // 请求退款
    public static String APPLICATIONFORREFUND = URL + "BankrollApi/applicationForRefund";
    public static String APPLICATIONFORREFUND_ONLY = "applicationForRefund";
    // 退款
    public static String DOAPPLICATIONFORREFUND = URL + "BankrollApi/doApplicationForRefund";
    public static String DOAPPLICATIONFORREFUND_ONLY = "doApplicationForRefund";
    // 取消退款
    public static String USERCANCELREFUND = URL + "BankrollApi/userCancelRefund";
    public static String USERCANCELREFUND_ONLY = "userCancelRefund";
    // 获取资金处理方式
    public static String GETHANDLETYPE = URL + "BankrollApi/getHandleType";
    public static String GETHANDLETYPE_ONLY = "getHandleType";
    //=======================================PaymentApi余额相关接口===================================
    // 余额充值(用于测试)
    public static String DORECHARGE = URL + "PaymentApi/doRecharge";
    public static String DORECHARGE_ONLY = "doRecharge";
    // 订单支付接口
    public static String ORDERPAY = URL + "PaymentApi/orderPay";
    public static String ORDERPAY_ONLY = "orderPay";
    //根据订单编号查询支持的支付类型
    public static String GETALLOWEDPAYTYPE = URL + "PaymentApi/getAllowedPayType";
    public static String GETALLOWEDPAYTYPE_ONLY = "getAllowedPayType";
    //获取押金支付类型
    public static String GETDEPOSITINFO = URL + "PaymentApi/getDepositInfo";
    public static String GETDEPOSITINFO_ONLY = "getDepositInfo";
    //支付押金
    public static String PAYDEPOSIT = URL + "PaymentApi/payDeposit";
    public static String PAYDEPOSIT_ONLY = "payDeposit";
    //=======================================BusApi相关接口===================================
    // APP数据总线
    public static String EXCHANGEMSG = URL + "BusApi/exchangeMsg";
    public static String EXCHANGEMSG_ONLY = "exchangeMsg";
    //=======================================RankingListApi相关接口===================================
    //  获取排行类型
    public static String GETRANKINGTYPE = URL + "RankingListApi/getRankingType";
    public static String GETRANKINGTYPE_ONLY = "getRankingType";
    //  根据类型获取排行榜列表
    public static String GETRANKINGLISTBYTYPE = URL + "RankingListApi/getRankingListByType";
    public static String GETRANKINGLISTBYTYPE_ONLY = "getRankingListByType";
    //  获取支付成功后的排行榜
    public static String GETORDERRANKINGLIST = URL + "RankingListApi/getOrderRankingList";
    public static String GETORDERRANKINGLIST_ONLY = "getOrderRankingList";
    //=======================================ConsumptionApi相关接口===================================
    //  消费明细
    public static String CONSUMPTIONLOG = URL + "ConsumptionApi/consumptionLog";
    public static String CONSUMPTIONLOG_ONLY = "consumptionLog";
    //  消费详情
    public static String CONSUMPTIONDETAILED = URL + "ConsumptionApi/consumptionDetailed";
    public static String CONSUMPTIONDETAILED_ONLY = "consumptionDetailed";

    //=======================================CapitalApi相关接口===================================
    //余额记录
    public static String BALANCELOG = URL + "CapitalApi/balanceLog";
    public static String BALANCELOG_ONLY = "balanceLog";
    //余额记录
    public static String BALANCELOGDETAIL = URL + "CapitalApi/balanceLogDetail";
    public static String BALANCELOGDETAIL_ONLY = "balanceLogDetail";
    //=======================================OrderApi相关接口===================================
    //获取未完成订单
    public static String GETOUTSTANDINGORDER = URL + "OrderApi/getOutstandingOrder";
    public static String GETOUTSTANDINGORDER_ONLY = "getOutstandingOrder";
    //对订单有疑问
    public static String DOUBTABOUTORDER = URL + "OrderApi/doubtAboutOrder";
    public static String DOUBTABOUTORDER_ONLY = "doubtAboutOrder";
    //=======================================UserApi App版本更新===================================
    //App版本更新
    public static String UPGRADEAPP = URL + "UserApi/upgradeApp";
    public static String UPGRADEAPP_ONLY = "upgradeApp";
    //=======================================KailuApi 凯路水控机===================================
    //凯路水控机同步余额
    public static String SYNCAMOUNT = URL + "KailuApi/syncAmount";
    public static String SYNCAMOUNT_ONLY = "syncAmount";
    //凯路水控机洗浴开始
    public static String BATHBEGINORDER = URL + "KailuApi/bathBeginOrder";
    public static String BATHBEGINORDER_ONLY = "bathBeginOrder";
    //凯路水控机洗浴结束
    public static String BATHENDORDER = URL + "KailuApi/bathEndOrder";
    public static String BATHENDORDER_ONLY = "bathEndOrder";
}


