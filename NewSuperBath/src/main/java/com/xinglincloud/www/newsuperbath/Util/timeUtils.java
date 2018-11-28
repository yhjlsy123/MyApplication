package com.xinglincloud.www.newsuperbath.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class timeUtils {

    public static boolean between(String beginTimes,String endTimes) {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        //现在的时间
        Date now = null;
        //开始的时间
        Date beginTime = null;
        //结束的时间
        Date endTime = null;
        try {
            now = sf.parse(sf.format(new Date()));
            beginTime = sf.parse(beginTimes);
            endTime = sf.parse(endTimes);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return belongCalendar(now, beginTime, endTime);
    }


    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }

    }
}