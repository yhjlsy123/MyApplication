package com.zhuochi.hydream.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuochi.hydream.R;

/**
 * Created by and on 2016/11/9.
 */

public class ShouldPayDialog extends Dialog {
    public ShouldPayDialog(Context context) {
        super(context);
    }

    public ShouldPayDialog(Context context, int theme) {
        super(context, theme);
    }

    public ShouldPayDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        private ShouldPayListener listener;

        public void setOnClickListener(ShouldPayListener listener) {
            this.listener = listener;
        }

        @SuppressWarnings("deprecation")
        @SuppressLint("InflateParams")
        public ShouldPayDialog create() {
            // 使用Theme实例化Dialog
            final ShouldPayDialog dialog = new ShouldPayDialog(activity, R.style.CommonDialog);
            View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_shouldpay, null);
            layout.findViewById(R.id.shouldpay_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (listener != null)
//                        listener.cancel();
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.shouldpay_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.pay();
                    }
                    dialog.dismiss();
                }
            });
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return dialog;
        }
    }

    public interface ShouldPayListener {
//        void cancel();

        void pay();
    }
}
