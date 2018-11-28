package com.zhuochi.hydream.bathhousekeeper.http;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.SystemUtil;
import com.zhuochi.hydream.bathhousekeeper.utils.UserUtils;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 用户json拼接 HTTP加密请求参数
 *
 * @author Cuixc
 * @date on  2018/5/8
 */

public class JsonSplicingUtil {

    /**
     * 子类
     * @param act 请求方法标识
     * @param jsonObject
     * @param mContext
     * @return
     */
    public static Map<String, String> JsonSplicingSon(String act, JSONObject jsonObject, Context mContext) {
        Map<String, String> hashMap = new HashMap<String, String>();
        String token = UserUtils.getInstance(mContext).getTokenID() ;
        JSONObject object = new JSONObject();
        try {
            object.put("device_type", "android");
            object.put("source_sys_version", SystemUtil.getSystemVersion()); //Android  手机系统版本
            object.put("source_version", SPUtils.getLocalVersion(mContext));      //当前app版本
            object.put("source_model", SystemUtil.getSystemModel());        //手机型号
            object.put("token",token);        //请求的方法
            object.put("act", act);        //请求的方法

            jsonObject.put("header", object);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        hashMap = JsonSplicing(DESCryptogRaphy.string2Unicode(jsonObject.toString()),mContext);

        return hashMap;
    }

    /**
     * Json串拼接封装
     *
     * @param datsObject 内嵌data
     * @return
     */
    private static Map<String, String> JsonSplicing(String datsObject, Context mContext) {
        JSONObject FatherObj = new JSONObject();
        JSONObject SonObj = new JSONObject();
        JSONObject GrandsonObj = new JSONObject();
        try {
            String msgID = SystemUtil.getTimeRandom();
            String imei =  SystemUtil.getIMEI(mContext);
            int length = datsObject.length();
            //des+base64 加密
            String EDSdata = DESCryptogRaphy.encode(datsObject);
            String verify = msgID + imei + length + Constants.hash_key + EDSdata;
            //sha1  加密
            GrandsonObj.put("verify", DESCryptogRaphy.shaEncrypt(verify));//msg_id+uuid+length+hash_key+data=====sha1加密
            GrandsonObj.put("msg_id", msgID);//获取时间戳+随机6位数字
            GrandsonObj.put("length", datsObject.length()); //data长度
            GrandsonObj.put("uuid", imei);
            SonObj.put("head", GrandsonObj);
            SonObj.put("data", EDSdata);
            FatherObj.put("ztechMsg", SonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, String> resultMap = new HashMap<String, String>();
        try {

            Iterator<String> iter =  FatherObj.keySet().iterator();
            String key = null;
            String value = null;

            while (iter.hasNext()) {
                key = iter.next();
                value = FatherObj.get(key).toString();
                resultMap.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}
