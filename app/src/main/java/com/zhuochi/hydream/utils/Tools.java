package com.zhuochi.hydream.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一些小工具
 *
 * @author and
 */
public class Tools {
    /**
     * 将钱数格式化成(*.00)
     *
     * @param money
     * @return
     */
    public static String formatMoney(String money) {
        if (TextUtils.isEmpty(money)) {
            money = "0";
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(Double.valueOf(money.trim()));
    }


    private static long lastClickTime;

    /**
     * 双击关闭
     *
     * @return 可以关闭返回true
     */
    public synchronized static boolean dblClose() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 3000) {
            return true;
        }
        lastClickTime = time;
        ToastUtils.show("再按一次退出程序");
        return false;
    }

    private static boolean notClick;

    /**
     * 将字符串转成int类型,null返回0;
     */
    public static int toInt(String str) {
        int temp = 0;
        if (!TextUtils.isEmpty(str)) {
            try {
                temp = Integer.valueOf(str);
            } catch (Exception e) {
            }
        }
        return temp;
    }

    /**
     * 将字符串转成float类型,null返回0;
     */
    public static float toFloat(String str) {
        float temp = 0.00f;
        if (!TextUtils.isEmpty(str)) {
            try {
                temp = Float.valueOf(str);
            } catch (Exception e) {
            }
        }
        return temp;
    }

    public static int measureHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将时间格式化：xx′xx″
     *
     * @param s
     * @return
     */
    public static String formatTime(long s) {
        Integer mi = 60;
        long minute = s / mi;
        long second = (s - mi * minute);

        StringBuffer stringBuffer = new StringBuffer();

        if (minute > 0) {
            if (minute < 10) {
                stringBuffer.append(0);
            }
            stringBuffer.append(minute + "′");
        }
        if (minute == 0) {
            if (second > 0) {
                if (second < 10) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(second + "″");
            }
        } else {
            if (second > 0) {
                if (second < 10) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(second + "″");
            } else {
                stringBuffer.append("00″");
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 将时间格式化：xx′xx″
     *
     * @param s
     * @return
     */
    public static String formatTimeLineUp(long s) {
        Integer mi = 60;
        long minute = s / mi;
        long second = (s - mi * minute);

        StringBuffer stringBuffer = new StringBuffer();

        if (minute > 0) {
            if (minute < 10) {
                stringBuffer.append(0);
            }
            stringBuffer.append(minute + ":");
        }
        if (minute == 0) {
            if (second > 0) {
                if (second < 10) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(second + "″");
            }
        } else {
            if (second > 0) {
                if (second < 10) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(second + "″");
            } else {
                stringBuffer.append("00″");
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 将时间格式化：s-->min
     *
     * @param s
     * @return
     */
    public static long SToMin(long s) {
        Integer mi = 60;
        long minute = s / mi;
        return minute;
    }


    /**
     * 将时间格式化：mm.dd hh:mm:ss
     *
     * @param s
     * @return
     */
    public static String ToMDHMS(long s) {
        Date date = new Date(s * 1000);
        String str1 = (new SimpleDateFormat("MM")).format(date) + "." + (new SimpleDateFormat("dd").format(date) + " " + DateFormat.getTimeInstance().format(date));
        return str1;
    }

    /**
     * 将时间格式化： hh:mm:ss
     *
     * @param s
     * @return
     */
    public static String ToHMS(long s) {
        long days = s / 86400;//转换天数
        s = s % 86400;//剩余秒数
        long hours = s / 3600;//转换小时数
        s = s % 3600;//剩余秒数
        long minutes = s / 60;//转换分钟
        s = s % 60;//剩余秒数
        String str = "";
        if (hours < 10) {
            str = "0" + hours;
        } else {
            str = "" + hours;
        }
        str = str + ":";
        if (minutes < 10) {
            str = str + "0" + minutes;
        } else {
            str = str + "" + minutes;
        }
        str = str + ":";
        if (s < 10) {
            str = str + "0" + s;
        } else {
            str = str + "" + s;
        }
        return str;
    }
    /*转换分钟*/
    public static String Tominutes(long s) {
        long minutes = s / 60;//转换分钟
        String str = "";
        str = str + minutes;
        return str+"分钟";
    }

    /**
     * 格式化   时分秒
     *
     * @param second
     * @return
     */
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
            if (d == 0) {
                return s + "秒";
            }
            if (d > 0) {
                if (s == 0) {
                    return d + "分钟";
                } else {
                    return d + "分钟" + s + "秒";
                }
            }
            return d + "分钟" + s + "秒";
        } else {
            return h + "小时" + d + "分钟" + s + "秒";

        }
    }

    /**
     * 格式化   分秒
     *
     * @param second
     * @return
     */
    public static String FormatChange(int second) {
//        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
//            h = second / 3600;
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
//        if (h == 0) {
        if (d == 0) {
            return s + "秒";
        }
        if (d > 0) {
            return d + "分钟" + s + "秒";
        }
        return d + "分钟" + s + "秒";
//        } else {
//            return h + "小时" + d + "分钟" + s + "秒";
//
//        }
    }

    /*
    * 计算time2减去time1的差值 差值只设置 几天 几个小时 或 几分钟
    * 根据差值返回多长之间前或多长时间后
    * */
    public static String getDistanceTime(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;

        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day != 0) return day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
        if (hour != 0) return hour + "小时" + min + "分钟" + sec + "秒";
        if (min != 0) return min + "分钟" + sec + "秒";
        if (sec != 0) return sec + "秒";
        return "0秒";
    }
}
