package com.isgala.xishuashua.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isgala.xishuashua.R;

/**
 * 结束服务
 * Created by and on 2016/11/9.
 */

public class StopServiceDialog extends Dialog {
    public StopServiceDialog(Context context) {
        super(context);
    }

    public StopServiceDialog(Context context, int theme) {
        super(context, theme);
    }

    public StopServiceDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        private RefundListener listener;

        public void setOnClickListener(RefundListener listener) {
            this.listener = listener;
        }

        @SuppressWarnings("deprecation")
        @SuppressLint("InflateParams")
        public StopServiceDialog create() {
            // 使用Theme实例化Dialog
            final StopServiceDialog dialog = new StopServiceDialog(activity, R.style.CommonDialog);
            View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_stopservice, null);
            layout.findViewById(R.id.refund_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.refund_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.refund();
                    }
                    dialog.dismiss();
                }
            });
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return dialog;
        }
    }


    public interface RefundListener {
        void refund();
    }
}
