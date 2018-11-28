package com.zhuochi.hydream.bathhousekeeper.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import com.zhuochi.hydream.bathhousekeeper.config.BathHouseApplication;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static com.zhuochi.hydream.api.Neturl.SYNCHROAMOUNT;

/**
 * Created by 唯暮 on 2018/4/8.
 */

public class Common {
    public static String ICON_BASE_URL = "";
    public static String WX_APPID = "";
    private static Context context = BathHouseApplication.mApplicationContext;

    /**
     * 判断网络连接是否可用
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
            if (allNetworkInfo != null) {
                for (int i = 0; i < allNetworkInfo.length; i++) {
                    // 并且不是E网: && telephonyManager.getNetworkType() !=
                    // TelephonyManager.NETWORK_TYPE_EDGE
                    if (allNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        ToastUtils.show("网络不可用,请稍后再试");
        return false;
    }

    /*
     * 将秒数转为时分秒
     * */
    public static String change(int second) {
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        if (h == 0) {
            return d + "分" + s + "秒";
        }
        if (h == 0 && d == 0) {
            return s + "秒";
        }
        return h + "时" + d + "分" + s + "秒";
    }


    /**
     * 判断android版本是否小于23（android 6.0）
     */
    public static Boolean isAbove22SDKVersion() {
        return Build.VERSION.SDK_INT >= 23;
    }

    /**
     * 判断android版本是否小于18（android 4.3）
     *
     * @return 低于18true否则false
     */
    public static Boolean SDKVersionBelow18() {
        return Build.VERSION.SDK_INT <= 17;
    }

    /**
     * 判断手机号码是否合理
     *
     * @return
     */
    public static boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11) && isMobileNo(phoneNums)) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     *
     * @param phoneNums
     * @return
     */
    public static boolean isMobileNo(String phoneNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）、166（蚂蚁宝卡）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][3456789]\\d{9}";// "[1]"代表第1位为数字1，"[34758]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phoneNums))
            return false;
        else
            return phoneNums.matches(telRegex);
    }

    /* 获取当前应用程序的包名
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

    /*时间戳转换成字符串*/
    public static String getDateToString(long time) {
        Date date = new Date(time * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(date);
    }

    public static String getDateToYMDHM(long time) {
        Date date = new Date(time * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sf.format(date);
    }

    public static String getDateToMDHMS(long time) {
        Date date = new Date(time * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd日 HH:mm:ss");
        return sf.format(date);
    }

    //十位时间戳字符串转小时分钟秒
    public static String Hourmin(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    //把位时间戳字符串转小时分钟秒
    public static String ymdToString(String time) {
        if (time.length() == 8) {
            String valDate = time.substring(0, 4) +
                    "-" +
                    time.substring(4, 6) +
                    "-" +
                    time.substring(6, 8);
            return valDate;
        } else if (time.length() == 6) {
            String valDate = time.substring(0, 4) + "-" + time.substring(4, 6);
            return valDate;
        }
        return null;
    }


    /**
     * 格式化时间
     */
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    /**
     * 将bitmap写成字节数组
     *
     * @param bm
     * @return
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    private static Map<String, Object> pram;

    public static Map<String, Object> intancePram() {
        if (null == pram) {
            pram = new HashMap<String, Object>();
        } else {
            pram.clear();
        }
        return pram;
    }
}


