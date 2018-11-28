package com.zhuochi.hydream.bathhousekeeper.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.zhuochi.hydream.bathhousekeeper.config.BathHouseApplication;


/**
 * 单例的吐司
 * Created by and on 2016/11/3.
 */

public class ToastUtils {
    private static Toast mToast = null;

    private ToastUtils() {
    }

    /**
     * 弹出单例的吐司
     *
     * @param text
     */
    public static void show(String text) {
        if (mToast == null)
            synchronized (ToastUtils.class) {
                if (mToast == null)
                    mToast = Toast.makeText(BathHouseApplication.mApplicationContext, "", Toast.LENGTH_SHORT);
            }
        if (TextUtils.isEmpty(text)) {
            mToast.cancel();
            return;
        }
        mToast.setText(text);
        mToast.show();
    }
}
