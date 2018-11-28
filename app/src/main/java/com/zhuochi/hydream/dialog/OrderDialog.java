package com.zhuochi.hydream.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.utils.ToastUtils;

/**
 * Created by and on 2016/11/9.
 */

public class OrderDialog extends Dialog {
    public OrderDialog(Context context) {
        super(context);
    }

    public OrderDialog(Context context, int theme) {
        super(context, theme);
    }

    public OrderDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        private OrderListener listener;

        public void setOnClickListener(OrderListener listener) {
            this.listener = listener;
        }

        @SuppressWarnings("deprecation")
        @SuppressLint("InflateParams")
        public OrderDialog create() {
            // 使用Theme实例化Dialog
            final OrderDialog dialog = new OrderDialog(activity, R.style.CommonDialog);
            View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_order, null);
            final EditText editText = (EditText) layout.findViewById(R.id.edit_tip);
            layout.findViewById(R.id.order_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (listener != null)
//                        listener.cancel();
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.order_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (TextUtils.isEmpty(editText.getText().toString())){
                            ToastUtils.show("请输入您要反馈的内容");
                        }else {
                            listener.submit(editText.getText().toString());
                            dialog.dismiss();
                        }
                    }

                }
            });
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return dialog;
        }
    }

    public interface OrderListener {
//        void cancel();

        void submit(String reason);
    }
}
