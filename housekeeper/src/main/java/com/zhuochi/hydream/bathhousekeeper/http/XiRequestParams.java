package com.zhuochi.hydream.bathhousekeeper.http;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zhuochi.hydream.bathhousekeeper.dialog.LoadingTrAnimDialog;

import java.util.Iterator;
import java.util.Map;

/**
 * HTTP请求 参数及逻辑封装
 *
 * @author Cuixc
 * @date on  2018/5/8
 */

public class XiRequestParams {
    private Context mContext;
    private ResponseListener mCallBack;

    public XiRequestParams(Context context) {
        mContext = context;
    }

    public void addCallBack(ResponseListener callBack) {
        if (callBack instanceof Activity) {
            LoadingTrAnimDialog.showLoadingAnimDialog((Activity) mContext);
        }
        if (callBack instanceof Fragment) {
            Activity at = ((Fragment) callBack).getActivity();
            LoadingTrAnimDialog.showLoadingAnimDialog(at);
        }

        mCallBack = callBack;
    }


    /**
     * 快捷登录
     * 接口描述：
     *
     * @param Map<String,Objecet> map   请求data参数
     * @param String              key   请求键值
     */

    public void comnRequest(String url, Map<String, Object> data) {
        JSONObject jsonObject = new JSONObject();
        Log.e("cxt", JSON.toJSONString(data));
        jsonObject.put("data", JSON.parseObject(JSON.toJSONString(data)));
        RequestController.RequestHttp(RequestURL.URL + url, url, jsonObject, mCallBack);
    }


    /**
     * 登录接口
     *
     * @param pwd    密码
     * @param mobile 手机号
     */
    public void LoginRequest(String pwd, String mobile) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("pwd", pwd);
            object.put("mobile", mobile);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.WIDTHPWD, RequestURL.WIDTHPWD_ONLY, jsonObject, mCallBack);
    }


    /**
     * 校区列表
     * 接口描述：
     * 校区列表-用户数量 orgAreaListsWithUserNum
     *
     * @param user_id 必须
     * @return array
     */
    public void getSchoolListsWithUserNumRequest(int user_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.ORGAREALISTSWITHUSERNUM, RequestURL.ORGAREALISTSWITHUSERNUM_ONLY, jsonObject, mCallBack);
    }

    /**
     * 校区用户列表
     * 用户列表 orgAreaUserLists
     *
     * @param user_id       必须
     * @param org_id        必须
     * @param org_area_id   必须
     * @param user_nickname 可选
     * @param page          可选
     * @param limit         可选
     * @return array
     */

    public void getOrgAreaUserLists(int user_id, int org_id, int org_area_id, String user_nickname, int page, int limit) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("user_nickname", user_nickname);
            object.put("page", page);
            object.put("limit", limit);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.ORGAREAUSERLISTS, RequestURL.ORGAREAUSERLISTS_ONLY, jsonObject, mCallBack);
    }

    /**
     * 接口描述：
     * 反馈列表 suggestionLists
     *
     * @param user_id     必选 整数
     * @param org_id      可选 整数
     * @param org_area_id 可选 整数
     * @param start_date  可选 格式2018-08-08
     * @param end_date    可选 格式2018-08-08
     * @param page        可选 分页
     * @param limit       可选 每页数量
     * @return array
     */

    public void getSuggestionLists(int user_id, int org_id, int org_area_id, String is_reply, String start_date, String end_date, String page, String limit) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("start_date", start_date);
            object.put("end_date", end_date);
            object.put("is_reply", is_reply);
            object.put("page", page);
            object.put("limit", limit);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.SUGGESTIONLISTS, RequestURL.SUGGESTIONLISTS, jsonObject, mCallBack);
    }

    public void getSuggestionLists(int user_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.SUGGESTIONLISTS, RequestURL.SUGGESTIONLISTS_ONLY, jsonObject, mCallBack);
    }

    /**
     * 反馈回复列表
     * 接口描述：
     * 反馈回复 replyLists
     *
     * @param user_id       必选 整数
     * @param suggestion_id 必选 整数
     * @return array
     */

    public void getreplyLists(int user_id, int suggestion_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("suggestion_id", suggestion_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.REPLYLISTS, RequestURL.REPLYLISTS_ONLY, jsonObject, mCallBack);
    }

    /**
     * 回复反馈
     * 接口描述：
     *
     * @param user_id       必选 整数
     * @param suggestion_id 必选 整数
     * @param feedbackInfo  必选 数组
     * @return array
     * @:回复反馈 sendReply
     */

    public void sendReply(int user_id, int suggestion_id, int feedbackInfo) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("suggestion_id", suggestion_id);
            object.put("feedbackInfo", feedbackInfo);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.SENDREPLY, RequestURL.SENDREPLY_ONLY, jsonObject, mCallBack);
    }
    /*
     *//** 接口描述：
     * 学校校区列表
     * @param user_id 必须
     * @return array
     *//*
    public void getOrgSchoolLists(int user_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.GETORGLISTS, RequestURL.GETORGLISTS_ONLY, jsonObject, mCallBack);
    }*/

    /**
     * 获取学校/校区列表
     * 接口描述：
     * 学校校区列表
     *
     * @param user_id 必须
     * @return array
     */
    public void getSchoolList(int user_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.GETORGLISTS, RequestURL.GETORGLISTS_ONLY, jsonObject, mCallBack);
    }


    /**
     * 获取学校/校区/浴室
     * 接口描述：
     * 学校/校区/浴室列表
     *
     * @param user_id 必须
     * @return array
     */

    public void getOrgBathroomLists(int user_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.GETORGBATHROOMLISTS, RequestURL.GETORGBATHROOMLISTS_ONLY, jsonObject, mCallBack);
    }

    /**
     * 获取学校/校区/浴室/浴位
     * 接口描述：
     * 学校/校区/浴室列表
     *
     * @param user_id 必须
     * @return array
     */

    public void getOrgBathroomPositionLists(int user_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.GETORGBATHROOMPOSITIONLISTS, RequestURL.GETORGBATHROOMPOSITIONLISTS_ONLY, jsonObject, mCallBack);
    }

    /**
     * 获取唯一识别码
     * 接口描述：
     * getToken
     *
     * @todo 获取唯一识别码
     * @author Sorry <1150699887@qq.com>
     * @date 2018-07-04 17:49
     */

    public void getToken() {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.GETTOKEN, RequestURL.GETTOKEN_ONLY, jsonObject, mCallBack);
    }

    /**
     * 获取文件上传的Token
     * 接口描述：
     * getUploadFileToken
     *
     * @param mobile       手机号
     * @param package_name 包名(用于区分不同的APP)
     * @todo 获取文件上传的Token, 目前用的是七牛
     * @author Sorry <1150699887@qq.com>
     * @date 2018-07-04 17:49
     */
    public void getUploadFileToken(String package_name) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
//            object.put("mobile", mobile);
            object.put("package_name", package_name);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.GETUPLOADFILETOKEN, RequestURL.GETUPLOADFILETOKEN_ONLY, jsonObject, mCallBack);
    }

    /**
     * 获取推送的秘钥
     * 接口描述：
     * getPushAuth
     *
     * @param package_name 包名(用于区分不同的APP)
     * @todo 获取推送的秘钥（目前是极光）
     * @author Sorry <1150699887@qq.com>
     * @date 2018-07-04 17:49
     */
    public void getPushAuth(String package_name) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("package_name", package_name);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.GETPUSHAUTH, RequestURL.GETPUSHAUTH_ONLY, jsonObject, mCallBack);
    }

    /**
     * 发送验证码
     * 接口描述：
     * sendSMSCode
     *
     * @param code_type 验证码类型 (必须)
     *                  //login：登录验证码 find_pwd:找回密码 reg:注册验证码 handle_capital 资金处理（转赠，提现）
     * @return array
     * @todo 发送验证码
     * @url mobile 必须 手机号
     */

    public void sendSMSCode(String mobile, String code_type) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", mobile);
            object.put("code_type", code_type);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.SENDSMSCODE, RequestURL.SENDSMSCODE_ONLY, jsonObject, mCallBack);
    }

    /**
     * 忘记密码
     * 接口描述：
     * forgetPassword
     *
     * @param mobile     必须 手机号
     * @param pwd        必须 密码
     * @param verifyCode 必须 短信验证码
     * @return array
     * @todo 忘记密码
     */

    public void forgetPassword(String mobile, String pwd, String verifyCode) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", mobile);
            object.put("pwd", pwd);
            object.put("verifyCode", verifyCode);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.FORGETPASSWORD, RequestURL.FORGETPASSWORD_ONLY, jsonObject, mCallBack);
    }

    /**
     * 快捷登录
     * 接口描述：
     * fastLogin
     *
     * @param mobile     手机号 必须
     * @param verifyCode 短信验证码 必须
     * @return array
     * @todo 快捷登录
     */

    public void getFastLogin(String mobile, String verifyCode) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", mobile);
            object.put("verifyCode", verifyCode);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.FASTLOGIN, RequestURL.FASTLOGIN_ONLY, jsonObject, mCallBack);
    }

    /**
     * 获取用户信息
     * 接口描述：
     *
     * @param user_id 必须 整数 用户id
     * @return array
     * @todo 获取用户信息
     */

    public void getUserBaseInfoRequest(int user_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.GETUSERBASEINFO, RequestURL.GETUSERBASEINFO_ONLY, jsonObject, mCallBack);
    }

    /**
     * 修改密码
     * 接口描述：
     *
     * @param user_id     必须 整数 用户id
     * @param old_pwd     必须 字符串 当前密码
     * @param changed_pwd 必须 字符串 修改后密码
     * @return array
     * @todo 修改密码
     */
    public void changePassword(int user_id, String old_pwd, String changed_pwd) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("old_pwd", old_pwd);
            object.put("changed_pwd", changed_pwd);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.CHANGEPASSWORD, RequestURL.CHANGEPASSWORD_ONLY, jsonObject, mCallBack);
    }

    /**
     * 设置头像
     * 接口描述：
     *
     * @param user_id 必须 整数 用户id
     * @param avatar  必须 字符串 头像地址
     * @return array
     * @ 设置头像
     */

    public void setAvatar(int user_id, String avatar) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("avatar", avatar);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.SETAVATAR, RequestURL.SETAVATAR_ONLY, jsonObject, mCallBack);
    }

    /**
     * 校区设备数量列表
     * 接口描述：
     * 校区列表-设备数量 orgAreaListsWithDeviceNum
     *
     * @param user_id         必须
     * @param device_type_key 必须
     * @return array
     */

    public void getOrgAreaListsWithDeviceNum(int user_id, String device_type_key) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("device_type_key", device_type_key);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.ORGAREALISTSWITHDEVICENUM, RequestURL.ORGAREALISTSWITHDEVICENUM_ONLY, jsonObject, mCallBack);
    }

    /**
     * 校区设备列表
     * 接口描述：
     * 设备列表 orgAreaDeviceLists
     *
     * @param user_id         必须
     * @param org_id          必须
     * @param org_area_id     必须
     * @param device_type_key 必须   	faucet洗浴 blower吹风
     * @param page            可选 分页
     * @param limit           可选 每页数量
     * @return array
     */
    public void getorgAreaDeviceLists(int user_id, int org_id, int org_area_id, String device_type_key, int page, int limit) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("page", page);
            object.put("limit", limit);
            object.put("device_type_key", device_type_key);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.ORGAREADEVICELISTS, RequestURL.ORGAREADEVICELISTS_ONLY, jsonObject, mCallBack);
    }

    /**
     * 设备编辑
     * 接口描述：
     * 设备管理 orgAreaDeviceEdit
     *
     * @param user_id    必须
     * @param device_key 必须
     * @return array
     */

    public void orgAreaDeviceEdit(int user_id, String device_key) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("device_key", device_key);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.ORGAREADEVICEEDIT, RequestURL.ORGAREADEVICEEDIT_ONLY, jsonObject, mCallBack);
    }

    /**
     * 公告列表页
     * 接口描述：
     *
     * @param user_id     string	用户id	是	否
     * @param org_id      string	学校id	否	否	1	1
     * @param org_area_id string	校区id	否	否
     * @param b_time      string	搜索开始时间	否	否
     * @param e_time      string	搜索结束时间	否	否
     * @param page        string	分页	否	否
     * @/param limit    string	每页数量
     */

    public void getNoticeList(int user_id, int org_id, int org_area_id, String b_time, String e_time, int page) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("b_time", b_time);
            object.put("e_time", e_time);
            object.put("user_id", user_id);
            object.put("page", page);
            object.put("limit", 10);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.NOTICELIST, RequestURL.NOTICELIST_ONLY, jsonObject, mCallBack);
    }

    /**
     * 发布公告
     *
     * @param uid         string	用户id
     * @param org_id      string	学校
     * @param org_area_id string
     * @param msg_sort    string	消息分类
     * @param title       string	标题
     * @param describe    string	描述
     * @param content     string	内容
     * @param publish     int	是否发布，1未发布；2已发布
     */

    public void putOutBullentin(String uid, String org_id, String org_area_id, String msg_sort, String describe, String title, String content, int publish) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("uid", uid);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("msg_sort", msg_sort);
            object.put("title", title);
            object.put("describe", describe);
            object.put("title", title);
            object.put("content", content);
            object.put("publish", publish);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.PUTOUTBULLENTIN, RequestURL.PUTOUTBULLENTIN_ONLY, jsonObject, mCallBack);
    }

    /*
     *删除公告
     * @param msg_id*/

    public void DELBULLENTIN(String msg_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("msg_id", msg_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.DELBULLENTIN, RequestURL.DELBULLENTIN_ONLY, jsonObject, mCallBack);
    }

    /**
     * 获取公告类型
     *
     * @param user_id
     */
    public void getMsgSort(String user_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.GETMSGSORT, RequestURL.GETMSGSORT_ONLY, jsonObject, mCallBack);
    }

    /**
     * 订单列表
     * 接口描述：
     *
     * @param user_id        必选 整数
     * @param device_type    必选 默认 洗浴 'faucet'=>"洗浴", 'blower'=>"吹风机"
     * @param order_state    订单状态
     * @param device_key     设备编号
     * @param org_id         可选 整数
     * @param org_area_id    可选 整数
     * @param device_area_id 可选 整数
     * @param start_date     可选 格式2018-08-08
     * @param end_date       可选 格式2018-08-08
     * @param page           可选 分页
     * @param limit          可选 每页数量
     * @return array
     * @订单数据 lists
     */

    public void getOrderLists(int user_id, String device_type, String order_state, int org_id, int org_area_id, int device_area_id, String device_key,
                              String start_date, String end_date, int page, int limit) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("device_type", device_type);
            object.put("order_state", order_state);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("device_area_id", device_area_id);
            object.put("device_key", device_key);

            object.put("start_date", start_date);
            object.put("end_date", end_date);
            object.put("page", page);
            object.put("limit", limit);
            object.put("order_state", order_state);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.ORDERLISTS, RequestURL.ORDERLISTS_ONLY, jsonObject, mCallBack);
    }

    /**
     * 订单详情
     * 接口描述：
     *
     * @param user_id       必选 整数
     * @param order_item_id 必选 订单id
     * @return array
     * @订单详情 detail
     */

    public void getOrderDetail(int user_id, int order_item_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("order_item_id", order_item_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.ORDERDETAIL, RequestURL.ORDERDETAIL_ONLY, jsonObject, mCallBack);
    }

    /**
     * 流水数据
     * 接口描述：
     *
     * @param user_id         必选 整数
     * @param org_id          可选 整数
     * @param org_area_id     可选 整数
     * @param device_area_id  可选 整数
     * @param device_type_key 必选 默认 洗浴 'faucet'=>"洗浴", 'blower'=>"吹风机", 'washer'=>'洗衣机'
     * @param start_date      可选 格式2018-08-08
     * @param end_date        可选 格式2018-08-08
     * @param page            可选 分页
     * @param limit           可选 每页数量
     * @return array
     * @流水数据 flow
     */

    public void getOrderFlow(int user_id, int org_id, int org_area_id, int device_area_id,
                             String device_type_key, String device_key, String start_date, String end_date, int page, int limit) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("device_type_key", device_type_key);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("device_area_id", device_area_id);
            object.put("device_key", device_key);
            object.put("start_date", start_date);
            object.put("end_date", end_date);
            object.put("page", page);
            object.put("limit", limit);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.ORDERFLOW, RequestURL.ORDERFLOW_ONLY, jsonObject, mCallBack);
    }

    /**
     * 流水详情
     * 接口描述：// *
     *
     * @param user_id         必选 整数
     * @param flow_date       必选 整数
     * @param org_id          可选 整数
     * @param org_area_id     可选 整数
     * @param device_area_id  可选 整数
     * @param device_type_key 可选 默认 洗浴 'faucet'=>"洗浴", 'blower'=>"吹风机", 'washer'=>'洗衣机'
     * @return array
     * @流水数据详情 flowDetail
     */

    public void getOrderFlowDetial(int user_id, long flow_date, int org_id, int org_area_id, int device_area_id, String device_type_key) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("flow_date", flow_date);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("device_area_id", device_area_id);
            object.put("device_type_key", device_type_key);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.ORDERFLOWDETAIL, RequestURL.ORDERFLOWDETAIL_ONLY, jsonObject, mCallBack);
    }

    /**
     * 设备类型
     * 接口描述：// *
     *
     * @param user_id 必选 整数
     *                洗浴 'faucet'=>"洗浴", 'blower'=>"吹风机", 'washer'=>'洗衣机'
     * @return array
     * @设备类型 devicetype
     */

    public void getDeviceType(int user_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.DEVICETYPE, RequestURL.DEVICETYPE_ONLY, jsonObject, mCallBack);
    }


    /**
     * 首页
     * 接口描述： //**
     *
     * @param user_id        必选 整数
     * @param org_id         可选 整数 学校
     * @param org_area_id    可选 整数 校区
     * @param device_area_id 可选 整数 浴室
     * @param device_key     可选 整数 浴位
     * @param start_date     可选 格式2018-08-08 开始时间
     * @param end_date       可选 格式2018-08-08 截止时间
     * @return array
     * @:管家首页 index
     */

    public void getHomePage(int user_id, int org_id, int org_area_id, int device_area_id, String device_key, String start_date, String end_date) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("device_area_id", device_area_id);
            object.put("device_key", device_key);
            object.put("start_date", start_date);
            object.put("end_date", end_date);
            jsonObject.put("data", object);
            Log.d("homgPage", jsonObject.toJSONString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.STATISTICSHOME, RequestURL.STATISTICSHOME_ONLY, jsonObject, mCallBack);
    }

    /**
     * 接口描述：/**
     *
     * @param user_id     必选 整数
     * @param org_id      可选 整数
     * @param org_area_id 可选 整数
     * @param start_date  可选 格式2018-08-08
     * @param end_date    可选 格式2018-08-08
     * @param page        可选 分页
     * @return array
     * @T:押金管理 lists
     * @、param limit 可选 每页数量
     */

    public void getDepositList(int user_id, int org_id, int org_area_id, String start_date, String end_date, int page) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("start_date", start_date);
            object.put("end_date", end_date);
            object.put("page", page);
            object.put("limit", 10);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.DEPOSITLISTS, RequestURL.DEPOSITLISTS_ONLY, jsonObject, mCallBack);
    }

    /**
     * 接口描述：/**
     *
     * @param user_id     string	用户id	是	否
     * @param org_id      string	学校id	否	否
     * @param org_area_id string	校区id	否	否
     * @param b_time      string	搜索开始时间	否	否
     * @param e_time      string	搜索结束时间	否	否
     * @param page        string	分页	否	否		1
     * @return array
     * @T:充值卡列表
     * @/param limit    string	每页数量	否	否		10
     */

    public void getRechargeCardList(int user_id, int org_id, int org_area_id, String b_time, String e_time, int page) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("b_time", b_time);
            object.put("e_time", e_time);
            object.put("page", page);
            object.put("limit", 10);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.RECHARGECARDLIST, RequestURL.RECHARGECARDLIST_ONLY, jsonObject, mCallBack);
    }

    /**
     * 退款列表
     * 接口描述：
     *
     * @param user_id     必选 整数
     * @param status      可选 整数 1:处理中 2：退款成功 3：拒绝退款 4：退款失败 5:用户取消
     * @param org_id      可选 整数
     * @param org_area_id 可选 整数
     * @param start_date  可选 格式2018-08-08
     * @param end_date    可选 格式2018-08-08
     * @param page        可选 分页
     * @return array
     * @:退款管理 lists
     * @/param limit 可选 每页数量
     */

    public void getRefundList(int user_id, int status, int org_id, int org_area_id, String start_date, String end_date, int page) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("status", status);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("b_time", start_date);
            object.put("e_time", end_date);
            object.put("page", page);
            object.put("limit", 10);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.REFUNDLISTS, RequestURL.REFUNDLISTS_ONLY, jsonObject, mCallBack);
    }

    /**
     * 数据图表 排行榜
     * user_id	int		是	否
     * org_id	int		否	否		0 或 ""
     * org_area_id	string		否	否		0 或 ""
     * device_area_id	int		否	否		0 或 ""
     * device_key	int		否	否		0 或 ""
     * start_date	string		否	否	2018-12-11	""
     * end_date	string		否	否	2018-01-01	""
     */

    public void getTopChart(int user_id, int org_id, int org_area_id, int device_area_id, String device_key, String start_date, String end_date) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("device_area_id", device_area_id);
            object.put("device_key", device_key);
            object.put("start_date", start_date);
            object.put("end_date", end_date);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.TOPCHART, RequestURL.TOPCHART_ONLY, jsonObject, mCallBack);
    }

    /**
     * 数据图表 人均消费排行
     * user_id	int		是	否
     * org_id	int		否	否		0 或 ""
     * org_area_id	string		否	否		0 或 ""
     * device_area_id	int		否	否		0 或 ""
     * device_key	int		否	否		0 或 ""
     * start_date	string		否	否	2018-12-11	""
     * end_date	string		否	否	2018-01-01	""
     */

    public void getUserAverageConsumptionTopChart(int user_id, int org_id, int org_area_id, int device_area_id, String device_key, String start_date, String end_date) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("device_area_id", device_area_id);
            object.put("device_key", device_key);
            object.put("start_date", start_date);
            object.put("end_date", end_date);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.USERAVERAGECONSUMPTIONTOPCHART, RequestURL.USERAVERAGECONSUMPTIONTOPCHART_ONLY, jsonObject, mCallBack);
    }


    /**
     * 数据图表 洗浴高峰
     * user_id	int		是	否
     * org_id	int		否	否		0 或 ""
     * org_area_id	string		否	否		0 或 ""
     * device_area_id	int		否	否		0 或 ""
     * device_key	int		否	否		0 或 ""
     * start_date	string		否	否	2018-12-11	""
     * end_date	string		否	否	2018-01-01	""
     */

    public void getFrequencyChart(int user_id, int org_id, int org_area_id, int device_area_id, String device_key, String start_date, String end_date) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("device_area_id", device_area_id);
            object.put("device_key", device_key);
            object.put("start_date", start_date);
            object.put("end_date", end_date);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.FREQUENCYCHART, RequestURL.FREQUENCYCHART_ONLY, jsonObject, mCallBack);
    }

    /**
     * 数据图表 洗浴高峰
     * user_id	int		是	否
     * org_id	int		否	否		0 或 ""
     * org_area_id	string		否	否		0 或 ""
     * device_area_id	int		否	否		0 或 ""
     * device_key	int		否	否		0 或 ""
     * start_date	string		否	否	2018-12-11	""
     * end_date	string		否	否	2018-01-01	""
     */

    public void getRatioChart(int user_id, int org_id, int org_area_id, int device_area_id, String device_key, String start_date, String end_date) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("device_area_id", device_area_id);
            object.put("device_key", device_key);
            object.put("start_date", start_date);
            object.put("end_date", end_date);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.RATIOCHART, RequestURL.RATIOCHART_ONLY, jsonObject, mCallBack);
    }

    /**
     * 首页
     * 接口描述：
     *
     * @param user_id        必选 整数
     * @param org_id         可选 整数 学校
     * @param org_area_id    可选 整数 校区
     * @param device_area_id 可选 整数 浴室
     * @param device_key     可选 整数 浴位
     * @param start_date     可选 格式2018-08-08 开始时间
     * @param end_date       可选 格式2018-08-08 截止时间
     * @return array
     * @TO管家首页 index
     */

    public void getConsumptionChart(int user_id, int org_id, int org_area_id, int device_area_id, String device_key, String start_date, String end_date, String url) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("device_area_id", device_area_id);
            object.put("device_key", device_key);
            object.put("start_date", start_date);
            object.put("end_date", end_date);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.URL + url, url, jsonObject, mCallBack);
    }

    /**
     * 意见反馈类型列表
     */
    public void getFeedbackTypeList(int user_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.FEEDBACKTYPELIST, RequestURL.FEEDBACKTYPELISTA_ONLY, jsonObject, mCallBack);
    }

    /**
     * 反馈意见(首页)
     *
     * @param user_id 手机号
     * @param type
     * @param content
     * @param imgs
     * @param phone
     */
    public void feedback(int user_id, int type, String content, String imgs, String phone) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("type_id", type);//问题类型id
            jsonObject1.put("content", content);//意见内容不多于300字(必须)
            jsonObject1.put("imgs", imgs);//上传的图片返回链接，用,分割()
            jsonObject1.put("phone", phone);//预留字段，可为空
            object.put("user_id", user_id);
            object.put("feedbackInfo", jsonObject1);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.FEEDBACK, RequestURL.FEEDBACK_ONLY, jsonObject, mCallBack);
    }

    /**
     * 意见反馈列表
     *
     * @param user_id 手机号
     */
    public void myFeedbackList(int user_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.MYFEEDBACKLIST, RequestURL.MYFEEDBACKLIST_ONLY, jsonObject, mCallBack);
    }

    /**
     * 意见反馈对话列表
     *
     * @param user_id 手機號
     * @param su_id   意见反馈ID
     */
    public void feedbackDetailList(int user_id, String su_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("su_id", su_id);

            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.FEEDBACKDETAILLIST, RequestURL.FEEDBACKDETAILLIST_ONLY, jsonObject, mCallBack);
    }

    /**
     * 意见反馈对话
     *
     * @param user_id
     */
    public void feedbackDialog(int user_id, int relation_su_id, int type_id, String content, String imgs) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            JSONObject innerJsonObject = new JSONObject();
            innerJsonObject.put("relation_su_id", relation_su_id);
            innerJsonObject.put("type_id", type_id);
            innerJsonObject.put("content", content);
            innerJsonObject.put("imgs", imgs);
            object.put("dialogInfo", innerJsonObject);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.FEEDBACKDIALOG, RequestURL.FEEDBACKDIALOG_ONLY, jsonObject, mCallBack);
    }

    /**
     * @param user_id
     * @param org_id
     * @param org_area_id
     * @param start_date
     * @param end_date
     */
    public void getActivityList(int user_id, int org_id, int org_area_id, String start_date, String end_date) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("start_date", start_date);
            object.put("end_date", end_date);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.ACTIVITYLISTS, RequestURL.ACTIVITYLISTS_ONLY, jsonObject, mCallBack);
    }

    /**
     * @param user_id            账号id
     * @param activity_name      活动名称
     * @param client_org_id      学校
     * @param client_org_area_id 校区
     * @param device_type_key    设备类型
     * @param discount_factor    折扣 0-1
     * @param start_time         开始时间
     * @param end_time           结束时间
     * @param status             0待审核；1正常；2禁用；
     * @param activity_tip       提示信息
     * @param description        详情
     */
    public void addActivity(int user_id, String activity_name, int client_org_id, int client_org_area_id, String device_type_key, String discount_factor,
                            String start_time, String end_time, int status, String activity_tip, String description) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            JSONObject innerJsonObject = new JSONObject();
            innerJsonObject.put("activity_name", activity_name);
            innerJsonObject.put("client_org_id", client_org_id);
            innerJsonObject.put("client_org_area_id", client_org_area_id);
            innerJsonObject.put("device_type_key", device_type_key);
            innerJsonObject.put("discount_factor", discount_factor);
            innerJsonObject.put("start_time", start_time);
            innerJsonObject.put("end_time", end_time);
            innerJsonObject.put("status", status);
            innerJsonObject.put("activity_tip", activity_tip);
            innerJsonObject.put("description", description);
            object.put("activityInfo", innerJsonObject);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.ACTIVITYADD, RequestURL.ACTIVITYADD_ONLY, jsonObject, mCallBack);
    }

    /**
     * 获取学校列表
     *
     * @param user_id
     */
    public void getOrg(int user_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.GETORG, RequestURL.GETORG_ONLY, jsonObject, mCallBack);
    }

    /**
     * 获取校区列表
     *
     * @param user_id
     * @param org_id  学校id
     */
    public void getOrgArea(int user_id, int org_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);

            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.GETORGAREA, RequestURL.GETORGAREA_ONLY, jsonObject, mCallBack);
    }

    /**
     * 获取学校校区的浴室
     *
     * @param user_id
     * @param org_id      学校id
     * @param org_area_id 校区id
     */
    public void getBathRoom(int user_id, int org_id, int org_area_id) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.GETBATHROOM, RequestURL.GETBATHROOM_ONLY, jsonObject, mCallBack);
    }

    /**
     * 获取学校校区浴室中的浴位
     *
     * @param user_id
     * @param org_id          学校id
     * @param org_area_id     校区id
     * @param device_area_id  浴室ID
     * @param device_type_key 设备类型
     */
    public void getBathPosition(int user_id, int org_id, int org_area_id, int device_area_id, String device_type_key) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("org_id", org_id);
            object.put("org_area_id", org_area_id);
            object.put("device_area_id", device_area_id);
            object.put("device_type_key", device_type_key);
            jsonObject.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestController.RequestHttp(RequestURL.GETBATHPOSITION, RequestURL.GETBATHPOSITION_ONLY, jsonObject, mCallBack);
    }
}
