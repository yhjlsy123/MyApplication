package com.zhuochi.hydream.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.zhuochi.hydream.R;

/**
 * 加载动画(透明背景)
 */
public class LoadingRefreshDialog extends Dialog {

    private static LoadingRefreshDialog dialog;
    private static String mIdentification = "";

    public LoadingRefreshDialog(Context context) {
        super(context);
    }

    public LoadingRefreshDialog(Context context, int theme) {
        super(context, theme);
    }

    public LoadingRefreshDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Activity mActivity;

        public Builder(Activity ativity) {
            this.mActivity = ativity;
        }

        @SuppressWarnings("deprecation")
        @SuppressLint("InflateParams")
        public LoadingRefreshDialog create() {
            // 使用Theme实例化Dialog
            dialog = new LoadingRefreshDialog(mActivity, R.style.LoadingAnimDialog_translucent);
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

    public static void showLoadingAnimDialog(Activity activity, String Identification) {
        if (dialog != null)
            return;
        try {
            dialog = new Builder(activity).create();
            mIdentification = Identification;
        } catch (Exception e) {
        }
    }
    public static void dismissLoadingAnimDialog(String Identification) {
        if (mIdentification.equals(Identification)){
            try {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
            } catch (Exception e) {
            }
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

