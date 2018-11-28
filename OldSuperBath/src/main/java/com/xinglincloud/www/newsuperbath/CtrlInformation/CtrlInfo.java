package com.xinglincloud.www.newsuperbath.CtrlInformation;

import java.util.HashMap;
import java.util.Map;

public class CtrlInfo {
    public static final String SERVER_URI_V1 = "tcp://mqtt1.94lihai.com:1883";
    public static final String SERVER_URI_V3 = "tcp://mqtt.94lihai.com:1883";

    public static String mode = "none";
    public static final String MODE_BATH ="BATH";
    public static final String MODE_HAIRDRYER ="HAIRDRYER";
    public static final String MODE_MIXED ="MIXED";

    public static String bathMsgSign = "one";
    public static String hairdryerMsgSign = "one";
    public static String mixdMsgSign = "one";
    public static final String None = "None";
    public static final String PWD_Right = "PasswordCorrect";
    public static final String PWD_WRONG = "PasswordMistake";
    public static final String PWD_FINISH = "PasswordCorrectFinish";

    public static final String INSUFFICIENT_BALANCE ="InsufficientBalance";
    public static final String DEVICE_DISABLE ="DeviceDisable";
    public static String device_id = "";

    public static String uuid="0000000000";
    public static boolean uuidCtrl = false;
    public static boolean appCtrl = true;
    public static boolean communication = true;
    public static boolean connectState = true;
    public static boolean publishIsSure = false;
    public static String version = "3.0";


    public static String device_msg_id = "000000";
    public static String bath_request_id = "000000";
    public static String bath_args = "i am ok";
    public static String bath_sub_devices = "faucet_01";
    public static String bath_used = "000000";
    public static String bath_action = "abc";

    public static String hair_dryer_args = "i am ok";
    public static String hair_dryer_request_id = "000000";
    public static String hair_dryer_sub_devices = "blower_01";
    public static String hair_dryer_action = "abc";
    public static String result = "default";

    public static Map<String, String> faucetHashmapInit(int i){
        Map<String,String> uartCMD= new HashMap<>();
        String sumkey ;
        String valuehead = "9803";
        String valuetail = "000000009E";
        String sumvalue;
        for (int j=1; j<=i; j++){
            if(j<16){
                sumkey = "waterselect_"+String.valueOf(j);
                sumvalue = valuehead+"0"+Integer.toHexString(j).toUpperCase()+"00"+valuetail;
                uartCMD.put(sumkey,sumvalue);
                sumkey = "wateropen_"+String.valueOf(j);
                sumvalue = valuehead+"0"+Integer.toHexString(j).toUpperCase()+"02"+valuetail;
                uartCMD.put(sumkey,sumvalue);
                sumkey = "waterclose_"+String.valueOf(j);
                sumvalue = valuehead+"0"+Integer.toHexString(j).toUpperCase()+"03"+valuetail;
                uartCMD.put(sumkey,sumvalue);

            }else {
                sumkey = "waterselect_" + String.valueOf(j);
                sumvalue = valuehead + Integer.toHexString(j).toUpperCase() + "00" + valuetail;
                uartCMD.put(sumkey, sumvalue);
                sumkey = "wateropen_" + String.valueOf(j);
                sumvalue = valuehead + Integer.toHexString(j).toUpperCase() + "02" + valuetail;
                uartCMD.put(sumkey, sumvalue);
                sumkey = "waterclose_" + String.valueOf(j);
                sumvalue = valuehead + Integer.toHexString(j).toUpperCase() + "03" + valuetail;
                uartCMD.put(sumkey, sumvalue);
            }
        }
        return uartCMD;
    }
}
