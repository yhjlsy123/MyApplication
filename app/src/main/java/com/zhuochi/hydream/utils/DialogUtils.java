package com.zhuochi.hydream.utils;


import android.app.Dialog;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/10/12 0012.
 */
public class DialogUtils {
    public static void bottomMatchWidhtScreen(Dialog dialog) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
    }

    public static void rightMatchWidhtScreen(Dialog dialog) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        Display display = dialogWindow.getWindowManager().getDefaultDisplay();
        lp.width = new BigDecimal(display.getWidth() * 0.4).intValue();
        lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.RIGHT;
        dialogWindow.setAttributes(lp);
    }

    /**
     * 居中宽度等于屏幕宽度
     */
    public static void centerMatchWidthScreen(Dialog dialog, int space) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = dialog.getWindow().getWindowManager().getDefaultDisplay().getWidth() - space;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }


    /**
     * 居中宽度等于屏幕宽度
     *
     * @pram wpercient 宽百分比 最大100 适应屏幕宽度
     * @pram hpercient 高的百分比 最大100 适应屏幕高度
     */
    public static void centerMatchWidthScreen(Dialog dialog, int wpercient, int hPercient) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = dialog.getWindow().getWindowManager().getDefaultDisplay().getWidth() * wpercient / 100;
        lp.height = dialog.getWindow().getWindowManager().getDefaultDisplay().getHeight() * hPercient / 100;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

    /**
     * 居中宽度等于屏幕宽度
     */
    public static void centerRelayoutMatchWidthScreen(Dialog dialog, int space) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = dialog.getWindow().getWindowManager().getDefaultDisplay().getWidth() - space;
        lp.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

}
