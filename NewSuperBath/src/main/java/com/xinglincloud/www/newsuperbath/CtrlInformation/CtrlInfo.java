package com.xinglincloud.www.newsuperbath.CtrlInformation;

import java.util.HashMap;
import java.util.Map;


public class CtrlInfo {
    public static final String SERVER_URI_V1 = "tcp://mqtt1.94lihai.com:1883";
    public static final String SERVER_URI_V3 = "tcp://mqtt.94lihai.com:1883";
    public static final String SERVER_URI_V4 = "tcp://emq.94lihai.com:1883";
    public static final String EMQ_USERNAME = "gaopin";
    public static final String EMQ_PASSWORD = "GP##2018";
    public static String mode = "None";
    public static final String MODE_BATH = "BATH";
    public static final String MODE_HAIRDRYER = "HAIRDRYER";
    public static final String MODE_MIXED = "MIXED";
    public static String bathMsgSign = "one";
    public static String hairdryerMsgSign = "one";
    public static String mixdMsgSign = "one";
    public static String result = "default";
    public static final String None = "None";
    public static final String PWD_Right = "PasswordCorrect";
    public static final String PWD_Right_Finish = "PasswordCorrectFinish";
    public static final String PWD_WRONG = "PasswordMistake";
    public static final String INSUFFICIENT_BALANCE = "InsufficientBalance";
    public static final String DEVICE_DISABLE = "DeviceDisable";


    //方法开关
    public static String uuid = "0000000000";
    public static boolean uuidCtrl = false;
    public static boolean appCtrl = true;
    public static boolean communication = true;
    public static boolean connectState = true;
    public static boolean publishIsSure = false;
    public static String version = "3.0";
    public static int boxNum = 0;
    public static int faucetNum = 32;
    public static int faucetNumStart = 1;
    public static int faucetNumEnd = 32;
    public static String openPassword = "default";
    public static String closePassword = "default";
    public static String device_pwd = "default";

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

    public static Map<String, String> faucetHashmapInit(int i) {
        Map<String, String> uartCMD = new HashMap<>();
        String sumkey;
        String valuehead = "9803";
        String valuetail = "000000009E";
        String sumvalue;
        for (int j = 1; j <= i; j++) {
            if (j < 16) {
                sumkey = "waterselect_" + String.valueOf(j);
                sumvalue = valuehead + "0" + Integer.toHexString(j).toUpperCase() + "00" + valuetail;
                uartCMD.put(sumkey, sumvalue);
                sumkey = "wateropen_" + String.valueOf(j);
                sumvalue = valuehead + "0" + Integer.toHexString(j).toUpperCase() + "02" + valuetail;
                uartCMD.put(sumkey, sumvalue);
                sumkey = "waterclose_" + String.valueOf(j);
                sumvalue = valuehead + "0" + Integer.toHexString(j).toUpperCase() + "03" + valuetail;
                uartCMD.put(sumkey, sumvalue);

            } else {
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

    public static Map<String, String> hairDryerHashmapInit() {
        Map<String, String> uartCMD = new HashMap<>();
        String leftopen = "b1";
        String leftclose = "c1";
        String rightopen = "b2";
        String rightclose = "c2";

        uartCMD.put("leftopen", leftopen);
        uartCMD.put("leftclose", leftclose);
        uartCMD.put("rightopen", rightopen);
        uartCMD.put("rightclose", rightclose);

        return uartCMD;
    }

    public static HashMap<String, String> ventilatorHashmapInit() {

        HashMap<String, String> uartCMD = new HashMap<>();
        String low = "010F00000010020100E3B0";
        String medium = "010F00000010020200E340";
        String high = "010F00000010020400E0E0";
        String close = "010F00000010020000E220";

        uartCMD.put("low", low);
        uartCMD.put("medium", medium);
        uartCMD.put("high", high);
        uartCMD.put("close", close);

        return uartCMD;
    }

    public static HashMap<String, String> boxHashmapInit() {
        HashMap<String, String> uartCMD = new HashMap<>();
        uartCMD.put("box_open_1", "680702000000000916");
        uartCMD.put("box_close_1", "680702000100000a16");

        uartCMD.put("box_open_2", "680702010000000a16");
        uartCMD.put("box_close_2", "680702010100000b16");

        uartCMD.put("box_open_3", "680702020000000B16");
        uartCMD.put("box_close_3", "680702020100000C16");

        uartCMD.put("box_open_4", "680702030000000c16");
        uartCMD.put("box_close_4", "680702030100000d16");

        uartCMD.put("box_open_5", "680702040000000d16");
        uartCMD.put("box_close_5", "680702040100000e16");

        uartCMD.put("box_open_6", "680702050000000e16");
        uartCMD.put("box_close_6", "680702050100000f16");

        uartCMD.put("box_open_7", "680702060000000f16");
        uartCMD.put("box_close_7", "680702060100001016");

        uartCMD.put("box_open_8", "680702070000001016");
        uartCMD.put("box_close_8", "680702070100001116");

        uartCMD.put("box_open_9", "680702080000001116");
        uartCMD.put("box_close_9", "680702080100001216");

        uartCMD.put("box_open_10", "680702090000001216");
        uartCMD.put("box_close_10", "680702090100001316");

        uartCMD.put("box_open_11", "6807020a0000001316");
        uartCMD.put("box_close_11", "6807020a0100001416");

        uartCMD.put("box_open_12", "6807020b0000001416");
        uartCMD.put("box_close_12", "6807020b0100001516");

        uartCMD.put("box_open_13", "6807020c0000001516");
        uartCMD.put("box_close_13", "6807020c0100001616");

        uartCMD.put("box_open_14", "6807020d0000001616");
        uartCMD.put("box_close_14", "6807020d0100001716");

        uartCMD.put("box_open_15", "6807020e0000001716");
        uartCMD.put("box_close_15", "6807020e0100001816");

        uartCMD.put("box_open_16", "6807020f0000001816");
        uartCMD.put("box_close_16", "6807020f0100001916");

        return uartCMD;
    }

}
