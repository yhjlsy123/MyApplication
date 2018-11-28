package com.isgala.xishuashua.dialog;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.isgala.xishuashua.R;

/**
 * 加载动画对话框
 */
public class LoadingAnimDialog extends Dialog {

    private static LoadingAnimDialog dialog;

    public LoadingAnimDialog(Context context) {
        super(context);
    }

    public LoadingAnimDialog(Context context, int theme) {
        super(context, theme);
    }

    public LoadingAnimDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        @SuppressWarnings("deprecation")
        @SuppressLint("InflateParams")
        public LoadingAnimDialog create() {
            // 使用Theme实例化Dialog
            dialog = new LoadingAnimDialog(activity, R.style.LoadingAnimDialog);
            View layout = LayoutInflater.from(activity).inflate(R.layout.loading_dialog, null);
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
    public static void showLoadingAnimDialog(final Activity activity) {
        if (dialog != null)
            return;
        try {
            dialog = new LoadingAnimDialog.Builder(activity).create();
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
