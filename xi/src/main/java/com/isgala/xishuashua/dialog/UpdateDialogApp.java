package com.isgala.xishuashua.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.isgala.xishuashua.R;

/**
 * Created by 唯暮 on 2018/4/13.
 */

public class UpdateDialogApp extends Dialog {
    public UpdateDialogApp(Context context) {
        super(context);
    }

    public UpdateDialogApp(Context context, int theme) {
        super(context, theme);
    }

    public UpdateDialogApp(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Activity activity;
        private OnConfirmListener onClickListener;

        public void setConfirm(OnConfirmListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public Builder(Activity activity) {
            this.activity = activity;
        }


        @SuppressWarnings("deprecation")
        @SuppressLint("InflateParams")
        public OneCardSolutionPWD create(String content, String isShowCancel) {
            // 使用Theme实例化Dialog
            final OneCardSolutionPWD dialog = new OneCardSolutionPWD(activity, R.style.UpdateDialog);
            View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_update_app, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final TextView txtContent = (TextView) layout.findViewById(R.id.content);
            txtContent.setText(content);
            final TextView txtCancel = (TextView) layout.findViewById(R.id.txt_cancel);
            final TextView txtConfirm = (TextView) layout.findViewById(R.id.txt_confirm);
            if (isShowCancel != null && isShowCancel.equals("1")) {
                activity.setFinishOnTouchOutside(false);
                txtCancel.setVisibility(View.GONE);
            }
            dialog.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialo, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.show();
                        return true;
                    } else {
                        return false;
                    }

                }
            });
            txtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        dialog.dismiss();
                    }
                }
            });
            txtConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.confirm();
//                        dialog.dismiss();
                    }
                }
            });
            return dialog;
        }

    }

    public interface OnConfirmListener {
        void confirm();
    }

}
