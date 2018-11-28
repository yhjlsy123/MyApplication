package com.isgala.xishuashua.utils;

import android.content.Intent;
import android.os.Build;

import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.config.Constants;
import com.klcxkj.zqxy.ui.MainUserActivity;

import static com.isgala.xishuashua.api.Neturl.SYNCHROAMOUNT;

/**
 * Created by 唯暮 on 2018/4/8.
 */

public class Common {
    //同步余额
    public void synchroAmount() {
        String device_type = SPUtils.getString(Constants.DEVICE_TYPE, "");
        if (device_type.equals("2")) {
            VolleySingleton.post(SYNCHROAMOUNT, "synchroAmount", null, new VolleySingleton.CBack() {
                @Override
                public void runUI(String result) {
                    Result re = JsonUtils.parseJsonToBean(result, Result.class);

                }
            });
        }
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

}


