package com.xinglincloud.www.newsuperbath.Util;

import android.util.Log;

import com.xinglincloud.www.newsuperbath.CtrlInformation.CtrlInfo;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomNumUtils {
    private static HashSet hashSet = new HashSet();
    private static final String TAG = "RandomNumUtils";
    public static int getRandomNum(int max) {
        int min = 1;
        Random random = new Random();
        int n;
        if (hashSet.size() < CtrlInfo.boxNum) {
            while (true) {
                n = random.nextInt(max) % (max - min + 1) + min;
                Log.i(TAG, "getRandomNum: 随机取得的数为：" + n);
                if (hashSet.add(n)) {
                    Log.i(TAG, "getRandomNum: 获取的随机数为：" + n);
                    break;
                }
            }
            return n;
        } else {
            return 0;
        }
    }

    /**
     * 移除随机数
     * @param num
     * @return
     */
    public static boolean removeRandomNum(int num) {
        return hashSet.remove(num);
    }

    /**
     * 返回随机数集合的长度
     * @return
     */
    public static int getRandomNumSize() {
        return hashSet.size();
    }
}

