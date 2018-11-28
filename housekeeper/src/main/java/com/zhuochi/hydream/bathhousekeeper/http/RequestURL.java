package com.zhuochi.hydream.bathhousekeeper.http;

/**
 * @author Cuixc
 * @date on  2018/5/8
 */

public class RequestURL {
    //    测试环境
//    public static String URL = "http://zaotang.94feel.com/investorapi/v1/";
//    旧生产环境
    public static String URL = "http://newdev.gaopintech.cn/investorapi/v1/";
//    新生产环境
//    public static String URL = "http:// lz.hydream.cn/investorapi/v1/";
    //    public static String ICON_URL = "http://zaotang.94feel.com";
//    public static String IMAGE_URL = "http://zaotang.94feel.com/static/images/payment/";
    // =========================UserApi用户基础操作类====================================
    //账号密码登录
    public static String WIDTHPWD = URL + "UserApi/loginWidthPwd";
    public static String WIDTHPWD_ONLY = "loginWidthPwd";
    //1.1 校区列表
    public static String ORGAREALISTSWITHUSERNUM = URL + "Org/orgAreaListsWithUserNum";
    public static String ORGAREALISTSWITHUSERNUM_ONLY = "orgAreaListsWithUserNum";
    //1.2 校区用户列表
    public static String ORGAREAUSERLISTS = URL + "Org/orgAreaUserLists";
    public static String ORGAREAUSERLISTS_ONLY = "orgAreaUserLists";
    //2.1 反馈列表
    public static String SUGGESTIONLISTS = URL + "FeedBack/suggestionLists";
    public static String SUGGESTIONLISTS_ONLY = "suggestionLists";
    //2.2 反馈回复列表
    public static String REPLYLISTS = URL + "FeedBack/replyLists";
    public static String REPLYLISTS_ONLY = "replyLists";
    //2.3 回复反馈
    public static String SENDREPLY = URL + "FeedBack/sendReply";
    public static String SENDREPLY_ONLY = "sendReply";
    //3.1 获取学校/校区列表
    public static String GETORGLISTS = URL + "Org/getOrgLists";
    public static String GETORGLISTS_ONLY = "getOrgLists";
    //3.2 获取学校/校区/浴室
    public static String GETORGBATHROOMLISTS = URL + "Org/getOrgBathroomLists";
    public static String GETORGBATHROOMLISTS_ONLY = "getOrgBathroomLists";
    //3.3 获取学校/校区/浴室
    public static String GETORGBATHROOMPOSITIONLISTS = URL + "Org/getOrgBathroomPositionLists";
    public static String GETORGBATHROOMPOSITIONLISTS_ONLY = "getOrgBathroomPositionLists";
    //4.1 获取唯一识别码
    public static String GETTOKEN = URL + "OauthApi/getToken";
    public static String GETTOKEN_ONLY = "getToken";
    //4.2 获取文件上传的Token
    public static String GETUPLOADFILETOKEN = URL + "OauthApi/getUploadFileToken";
    public static String GETUPLOADFILETOKEN_ONLY = "getUploadFileToken";
    //4.3 获取推送的秘钥
    public static String GETPUSHAUTH = URL + "OauthApi/getPushAuth";
    public static String GETPUSHAUTH_ONLY = "getPushAuth";
    //5.2 发送验证码
    public static String SENDSMSCODE = URL + "UserApi/sendSMSCode";
    public static String SENDSMSCODE_ONLY = "sendSMSCode";
    //5.3 忘记密码
    public static String FORGETPASSWORD = URL + "UserApi/forgetPassword";
    public static String FORGETPASSWORD_ONLY = "forgetPassword";
    //5.4 快捷登录
    public static String FASTLOGIN = URL + "UserApi/fastLogin";
    public static String FASTLOGIN_ONLY = "fastLogin";
    //6.1 获取用户信息
    public static String GETUSERBASEINFO = URL + "MemberApi/getUserBaseInfo";
    public static String GETUSERBASEINFO_ONLY = "getUserBaseInfo";
    //6.2 修改密码
    public static String CHANGEPASSWORD = URL + "MemberApi/changePassword";
    public static String CHANGEPASSWORD_ONLY = "changePassword";
    //6.3 设置头像
    public static String SETAVATAR = URL + "MemberApi/setAvatar";
    public static String SETAVATAR_ONLY = "setAvatar";
    //7.1 校区设备数量列表
    public static String ORGAREALISTSWITHDEVICENUM = URL + "Org/orgAreaListsWithDeviceNum";
    public static String ORGAREALISTSWITHDEVICENUM_ONLY = "orgAreaListsWithDeviceNum";
    //7.2 校区设备列表
    public static String ORGAREADEVICELISTS = URL + "Org/orgAreaDeviceLists";
    public static String ORGAREADEVICELISTS_ONLY = "orgAreaDeviceLists";
    //7.3 设备编辑
    public static String ORGAREADEVICEEDIT = URL + "Org/orgAreaDeviceEdit";
    public static String ORGAREADEVICEEDIT_ONLY = "orgAreaDeviceEdit";
    //  ----------------------公告----------------------------
    //8.1 公告列表页
    public static String NOTICELIST = URL + "NoticeApi/noticeList";
    public static String NOTICELIST_ONLY = "noticeList";
    //8.2 发布公告
    public static String PUTOUTBULLENTIN = URL + "NoticeApi/putOutBullentin";
    public static String PUTOUTBULLENTIN_ONLY = "putOutBullentin";
    //8.3 删除公告
    public static String DELBULLENTIN = URL + "NoticeApi/delBullentin";
    public static String DELBULLENTIN_ONLY = "delBullentin";
    //8.4 获取公告类型
    public static String GETMSGSORT = URL + "NoticeApi/getMsgSort";
    public static String GETMSGSORT_ONLY = "getMsgSort";
    //  ----------------------订单----------------------------

    //订单数据
    public static String ORDERLISTS = URL + "Order/lists";
    public static String ORDERLISTS_ONLY = "Orderlists";
    //订单详情
    public static String ORDERDETAIL = URL + "Order/detail";
    public static String ORDERDETAIL_ONLY = "Orderdetail";

    //流水数据
    public static String ORDERFLOW = URL + "Order/flow";
    public static String ORDERFLOW_ONLY = "Orderflow";

    //流水数据
    public static String ORDERFLOWDETAIL = URL + "Order/flowDetail";
    public static String ORDERFLOWDETAIL_ONLY = "OrderflowDetail";
    //设备类型
    public static String DEVICETYPE = URL + "device/type";
    public static String DEVICETYPE_ONLY = "devicetype";
    //首页
    public static String STATISTICSHOME = URL + "Statistics/home";
    public static String STATISTICSHOME_ONLY = "Statisticshome";
    //押金管理
    public static String DEPOSITLISTS = URL + "Deposit/lists";
    public static String DEPOSITLISTS_ONLY = "Depositlists";
    //押金管理
    public static String RECHARGECARDLIST = URL + "BankrollApi/rechargeCardList";
    public static String RECHARGECARDLIST_ONLY = "rechargeCardList";
    //退款管理
    public static String REFUNDLISTS = URL + "Refund/lists";
    public static String REFUNDLISTS_ONLY = "Refundlists";

    //数据图表  排行榜
    public static String TOPCHART = URL + "Statistics/topChart";
    public static String TOPCHART_ONLY = "topChart";
    //数据图表  人均消费排行
    public static String USERAVERAGECONSUMPTIONTOPCHART = URL + "Statistics/userAverageConsumptionTopChart";
    public static String USERAVERAGECONSUMPTIONTOPCHART_ONLY = "userAverageConsumptionTopChart";
    //数据图表  洗浴高峰
    public static String FREQUENCYCHART = URL + "Statistics/frequencyChart";
    public static String FREQUENCYCHART_ONLY = "frequencyChart";
    //数据图表  比率
    public static String RATIOCHART = URL + "Statistics/ratioChart";
    public static String RATIOCHART_ONLY = "ratioChart";
    //首页 消费记录图表
    public static String CONSUMPTIONCHART = URL + "Statistics/consumptionChart";
    public static String CONSUMPTIONCHART_ONLY = "consumptionChart";

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

    //活动管理
    public static String ACTIVITYLISTS = URL + "Activity/lists";
    public static String ACTIVITYLISTS_ONLY = "lists";

    //添加活动
    public static String ACTIVITYADD = URL + "Activity/add";
    public static String ACTIVITYADD_ONLY = "add";
    //
//
    //==========================================学校、校区===========================================
    //获取学校列表
    public static String GETORG = URL + "Org/getOrg";
    public static String GETORG_ONLY = "getOrg";
    //获取校区列表
    public static String GETORGAREA = URL + "Org/getOrgArea";
    public static String GETORGAREA_ONLY = "getOrgArea";
    //获取学校校区的浴室
    public static String GETBATHROOM = URL + "Org/getBathRoom";
    public static String GETBATHROOM_ONLY = "getBathRoom";
    //获取学校校区浴室中的浴位
    public static String GETBATHPOSITION = URL + "Org/getBathPosition";
    public static String GETBATHPOSITION_ONLY = "getBathPosition";
}


