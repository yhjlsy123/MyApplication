package com.xinglincloud.www.newsuperbath.Util;

import com.xinglincloud.www.newsuperbath.CtrlInformation.CtrlInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.myapache.commons.codec.digest.DigestUtils;
import java.util.Random;

public class JsonUtil {
    private static final String TAG = "JsonUtil";
    //DES_KEY和HASH_KEY
    private static String DES_KEY = "5yoOxt9w";
    private static String HASH_KEY = "q9KswPoz62jQk12W";


//    private static String msg_id, uuid, length, verify;
//    private static String desDataStr, sha1Verify;
//    private static String reString;

    private static String encryptionJson(JSONObject jsonData,boolean is) {
        JSONObject jsonHead = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        Random rand = new Random();
        String msg_id = String.valueOf(System.currentTimeMillis()) + "_" + (rand.nextInt(899999) + 100000);
        //构建msg_id作为判断凭证
        if(is){
            CtrlInfo.device_msg_id = msg_id;
        }
        //将data的value值加密，DES的cbc模式
        String desDataStr = DESUtils.encode(DES_KEY, string2Unicode(jsonData.toString())).trim();
        //构建head的value值,获取length，构建verify
        String length = String.valueOf(string2Unicode(jsonData.toString()).length());
        String verify = msg_id + CtrlInfo.uuid + length + HASH_KEY + desDataStr;//此处注意jsonData
        String sha1Verify = DigestUtils.sha1Hex(verify);
        try {
            jsonHead.put("msg_id", msg_id);
            jsonHead.put("uuid", CtrlInfo.uuid);
            jsonHead.put("length", length);
            jsonHead.put("verify", sha1Verify);
            jsonHead.put("verify", sha1Verify);
            jsonHead.put("ver", CtrlInfo.version);

//            将head和data放入main  构成最终的json
            jsonMain.put("head", jsonHead);
            jsonMain.put("data", desDataStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonMain.toString();
    }

    //1/设备端主动请求服务器端开启，password为输入的密码
    public static String enSendOpenRequestBath(String password) {
        JSONObject jsonData = new JSONObject();
        try {
            //构建data的value值
            jsonData.put("request_dev_type", "controller");
            jsonData.put("target_dev_type", "faucet");
            jsonData.put("mode", "reserved");
            jsonData.put("action", "turn_on_request");
            jsonData.put("pwd", password);
            LogUtil.d(TAG, "enSendOpenRequestBath: 加密之前data的值(toString)：--" + jsonData + "---");
        } catch (JSONException e) {
            e.printStackTrace();
        }
            return encryptionJson(jsonData,true);
    }

    //    2、设备主动请求后，服务器端给予开启的控制指令，设备端向服务器返回开启成功，
//    sub_devices设备列表
    public static String enSendOpenResponseBath(String err) {
        JSONObject jsonData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
//            构建data的value值
            jsonArray.put(CtrlInfo.bath_sub_devices);
            jsonData.put("sub_devices", jsonArray);
            jsonData.put("action", "turn_on_response");
            jsonData.put("err", err);
            jsonData.put("request_id", CtrlInfo.bath_request_id);
            jsonData.put("args", CtrlInfo.bath_args);
            LogUtil.d(TAG, "enSendOpenResponseBath: 构建的data：" + jsonData);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encryptionJson(jsonData,false);
    }

    //    3、设备端请求关闭，
//{
//    "head":{...},
//    "data":
//    { //红色部分传输时为加密数据，文档中仅用于演示
//        "pwd":"[洗澡密码]",
//        "action":"turn_off_request"
//    }
//}
    public static String enSendCloseRequestBath(String pwd) {
        JSONObject jsonData = new JSONObject();
//        构建msg_id，uuid
        try {
//            构建data的value值
            jsonData.put("request_dev_type", "controller");
            jsonData.put("target_dev_type", "faucet");
            jsonData.put("mode", "reserved");
            jsonData.put("action", "turn_off_request");
            jsonData.put("pwd", pwd);
            LogUtil.d(TAG, "enSendCloseRequestBath: jsonData加密之前的值" + jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encryptionJson(jsonData,true);
    }

    //4、设备关闭响应，发送到服务器
    public static String enSendCloseResponseBath(String err, String used) {
        JSONObject jsonData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
//            构建data的value值

            jsonData.put("action", "turn_off_response");
            jsonData.put("request_id", CtrlInfo.bath_request_id);
            jsonData.put("err", err);
            jsonData.put("used", used);
            jsonData.put("args", CtrlInfo.bath_args);
            jsonArray.put(CtrlInfo.bath_sub_devices);
            jsonData.put("sub_devices", jsonArray);
            LogUtil.d(TAG, "enSendCloseResponseBath: jsonData加密之前的值" + jsonData);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encryptionJson(jsonData,false);
    }

    /*
     * 吹风机
     * 1.设备端向服务器发起开启吹风机请求
     * */
    public static String enSendOpenRequestHAIRDRYER(String device,String mobile,String pwd,String time){
        JSONObject jsonData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            //构建data的value值
            jsonData.put("request_dev_type", "controller");
            jsonData.put("target_dev_type", "blower");
            jsonData.put("mode", "locale");
            jsonData.put("action", "turn_on_request");
            jsonArray.put(device);
            jsonData.put("sub_devices", jsonArray);
            jsonData.put("mobile", mobile);
            jsonData.put("pwd", pwd);
            jsonData.put("time", time);

            LogUtil.d(TAG, "enSendOpenRequestHAIRDRYER: 加密之前data的值(toString)：--" + jsonData + "---");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encryptionJson(jsonData,true);

    }
    /*
     * 吹风机
     * 2.设备端向服务器回复吹风机开启结果
     * */
    public static String enSendOpenResponseHAIRDRYER(String device,String err){
        JSONObject jsonData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            //构建data的value值
            jsonArray.put(device);
            jsonData.put("sub_devices", jsonArray);
            jsonData.put("action", "turn_on_response");
            jsonData.put("err", err);
            jsonData.put("request_id", CtrlInfo.hair_dryer_request_id);
            jsonData.put("args", CtrlInfo.hair_dryer_args);

            LogUtil.d(TAG, "enSendOpenRequestHAIRDRYER: 加密之前data的值(toString)：--" + jsonData + "---");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encryptionJson(jsonData,false);

    }

    /*
     * 吹风机
     * 3.设备端向服务器回复吹风机关闭结果
     * */
    public static String enSendCloseResponseHAIRDRYER(String device,String err){
        JSONObject jsonData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            //构建data的value值
            jsonArray.put(device);
            jsonData.put("sub_devices", jsonArray);
            jsonData.put("action", "turn_off_response");
            jsonData.put("err", err);
            jsonData.put("request_id", CtrlInfo.hair_dryer_request_id);
            jsonData.put("args", CtrlInfo.hair_dryer_args);

            LogUtil.d(TAG, ": 加密之前data的值(toString)：--" + jsonData + "---");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encryptionJson(jsonData,false);
    }
    /*
    *心跳、开机、关机、重连、异常断开
    * 发送给device_heartbeat
    * */
    public static String heartBeatString(String string){
        JSONObject jsonData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            //构建data的value值
            jsonData.put("reason",string);

            LogUtil.d(TAG, "enSendOpenRequestHAIRDRYER: 加密之前data的值(toString)：--" + jsonData + "---");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encryptionJson(jsonData,false);
    }

    public static String heartBeatString(String string,String data){
        JSONObject jsonData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            //构建data的value值
            jsonData.put("reason",string);
            jsonData.put("data",data);

            LogUtil.d(TAG, "enSendOpenRequestHAIRDRYER: 加密之前data的值(toString)：--" + jsonData + "---");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encryptionJson(jsonData,false);
    }

    public static String heartBeatStrings(String string,String linkListNum,String connectState,String isok,String alive,String i,String j){
        JSONObject jsonData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            //构建data的value值
            jsonData.put("reason",string);
            jsonData.put("linkListNum",linkListNum);
            jsonData.put("connectState",connectState);
            jsonData.put("isok",isok);
            jsonData.put("threadqueue",alive);
            jsonData.put("pollnum",i);
            jsonData.put("revnum",j);
            LogUtil.d(TAG, "enSendOpenRequestHAIRDRYER: 加密之前data的值(toString)：--" + jsonData + "---");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encryptionJson(jsonData,false);
    }
    /*
     *
     * */
    public static String UpdateRsponseString(String err){
        JSONObject jsonData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            //构建data的value值
            jsonData.put("action","version_update_response");
            jsonData.put("err",err);

            LogUtil.i(TAG, "UpdateRsponseString: 加密之前data的值(toString)：--" + jsonData + "---");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encryptionJson(jsonData,false);
    }
    /**
     * 函数名：public static String deStr(String head,String data)
     * 参数：String head 放入解析后的head，String data 放入解析后的data
     * 函数作用：消息解析函数，对放入的head和data解析和解密，返回给串口处理队列
     * 返回值：1、如果消息不对，错误的数据是：Sha1Erro和DesErro,代表文字解析出错了，
     * 2、如果都是正确的，返回修改后的jsonData字符串，：
     */
    public static String deStr(String head, String data) {
        String reString = "err";
        try {
            String base64data = data;
//            解密data的value值
            data = DESUtils.decode(DES_KEY, data);
            String length = String.valueOf(data.length());
            JSONObject jsonData = new JSONObject(data);

            if (jsonData.has("action")) {
                LogUtil.i(TAG, "deStr: 解码后的jsdata值" + jsonData);
                JSONObject jsonHead = new JSONObject(head);
//            getSHA1为校验文件正确与否的方法
                if (getSha1(jsonHead, length, base64data)) {
                    /*  在这里就要检查是什么类型的消息
                     *   1、turn_on:设备请求开启后，从服务器返回到设备端，开启设备
                     *   2、turn_on_response:设备端开启后发送信息到服务器，服务器返回给客户端
                     *   3、turn_off：设备请求关闭后，从服务器返回到设备端，关闭设备
                     *   4、turn_off_response：设备端关闭后发送信息到服务器，服务器返回给客户端
                     */
                    if (jsonData.has("action")) {
                        jsonData.put("msg_id", jsonHead.getString("msg_id"));
                         reString = jsonData.toString();
                    } else {
                        LogUtil.d(TAG, "deStr:解码后action数据错误 ");
                        reString = "DesErro";
                    }
                } else {
                    LogUtil.d(TAG, "deStr: 消息SHA1校验未通过！");
                    reString = "Sha1Erro";
                }
            } else {
                LogUtil.d(TAG, "deStr: 没有action，无效数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return reString;
        }
        return reString;
    }
    /*
     * 函数名：public static boolean getSha1(JSONObject jsonHead,String length,String data)
     * 参数：
     * 描述：文件Sha1校验
     * 返回值：正确返回true，不正确返回false
     */

    private static boolean getSha1(JSONObject jsonHead, String length, String data) {
        Boolean isok = false;
        try {
            String gmsg_id = jsonHead.get("msg_id").toString();
            String guuid = jsonHead.get("uuid").toString();
            String gverify = jsonHead.getString("verify");
//            Log.i(TAG, "getSha1: 传递过来的SHA1："+gverify);
//            Log.i(TAG, "getSha1: 获取的长度："+length);
            String sumverify = gmsg_id + guuid + length + HASH_KEY + data;
//            Log.i(TAG, "getSha1: 构建之前的sha1字符串："+sumverify);
//            Log.i(TAG, "getSha1: 构建新的SHA1："+DigestUtils.sha1Hex(sumverify));

            if (gverify.equals(DigestUtils.sha1Hex(sumverify))) {
                LogUtil.d(TAG, "getSha1: Sha1一样，返回true");
                isok = true;
            } else {
                isok = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isok;
    }

    /**
     * utf-8 转换成 unicode
     */
    public static String utf8ToUnicode(String inStr) {
        char[] myBuffer = inStr.toCharArray();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inStr.length(); i++) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(myBuffer[i]);
            if (ub == Character.UnicodeBlock.BASIC_LATIN) {
                //英文及数字等
                sb.append(myBuffer[i]);
            } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                //全角半角字符
                int j = (int) myBuffer[i] - 65248;
                sb.append((char) j);
            } else {
                //汉字
                short s = (short) myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 字符串转换unicode
     */
    private static String string2Unicode(String string) {
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            if (c >= 0x4E00 && c <= 0x9FA5) {
                // 转换为unicode
                unicode.append("\\u").append(Integer.toHexString(c));
            } else {
                unicode.append(c);
            }

        }
        return unicode.toString();
    }
    public static String JSONTokener(String in) {
        // consume an optional byte order mark (BOM) if it exists
        if (in != null && in.startsWith("\ufeff")) {
            in = in.substring(1);
        }
        return in;
    }
//    public static String spGetStr(String key){
//    }
//    public static void spSetStr(String key,String value){
//    }
}
