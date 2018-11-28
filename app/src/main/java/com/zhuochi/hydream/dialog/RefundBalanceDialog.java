package com.zhuochi.hydream.dialog;

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

import com.zhuochi.hydream.R;

/**
 * 退款(余额)
 * Created by and on 2016/11/9.
 */

public class RefundBalanceDialog extends Dialog {
    public RefundBalanceDialog(Context context) {
        super(context);
    }

    public RefundBalanceDialog(Context context, int theme) {
        super(context, theme);
    }

    public RefundBalanceDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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
        public RefundBalanceDialog create(String string) {
            // 使用Theme实例化Dialog
            final RefundBalanceDialog dialog = new RefundBalanceDialog(activity, R.style.CommonDialog);
            View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_balancerefund, null);
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
            ((TextView) layout.findViewById(R.id.refund_title)).setText(string);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return dialog;
        }

        private void setTextColor(TextView tv) {
            String text = tv.getText().toString().trim();
            SpannableString spannableString = new SpannableString(text);
            ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
            spannableString.setSpan(span, text.length() - 4, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(spannableString);
        }
    }


    public interface RefundListener {
        void refund();
    }
}
