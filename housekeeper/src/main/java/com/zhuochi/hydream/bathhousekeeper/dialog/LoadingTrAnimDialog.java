package com.zhuochi.hydream.bathhousekeeper.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.zhuochi.hydream.bathhousekeeper.R;


/**
 * 加载动画(透明背景)
 */
public class LoadingTrAnimDialog extends Dialog {

    private static LoadingTrAnimDialog dialog;

    public LoadingTrAnimDialog(Context context) {
        super(context);
    }

    public LoadingTrAnimDialog(Context context, int theme) {
        super(context, theme);
    }

    public LoadingTrAnimDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Activity mActivity;

        public Builder(Activity ativity) {
            this.mActivity = ativity;
        }

        @SuppressWarnings("deprecation")
        @SuppressLint("InflateParams")
        public LoadingTrAnimDialog create() {
            // 使用Theme实例化Dialog
            dialog = new LoadingTrAnimDialog(mActivity, R.style.LoadingAnimDialog_translucent);
            View layout = LayoutInflater.from(mActivity).inflate(R.layout.loading_dialog, null);
            dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnKeyListener(new OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//                    activity.finish();
//                }
                    return true;
                }
            });
            dialog.show();
            return dialog;
        }

    }

    /**
     * 弹出加载动画对话框
     *
     * @param activity 必须是Activity的context
     */
    public static void showLoadingAnimDialog(Activity activity) {
        if (dialog != null)
            return;
        try {
            dialog = new Builder(activity).create();
        } catch (Exception e) {
        }
    }

    /**
     * 隐藏加载动画对话框
     */
    public static void dismissLoadingAnimDialog() {
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
        }
    }
}

