package com.zhuochi.hydream.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuochi.hydream.R;

/**
 * 重置账号密码后弹窗，选择是否重新登陆
 * 认证：修改手机号
 * Created by and on 2016/11/10.
 */

public class ResetNumberPWD extends Dialog {

    public ResetNumberPWD(Context context) {
        super(context);
    }

    public ResetNumberPWD(Context context, int theme) {
        super(context, theme);
    }

    public ResetNumberPWD(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Activity activity;
        private OnSuccessListener onSuccessListener;

        public void setOnClickListener(OnSuccessListener onSuccessListener) {
            this.onSuccessListener = onSuccessListener;
        }

        public Builder(Activity activity) {
            this.activity = activity;
        }


        @SuppressWarnings("deprecation")
        @SuppressLint("InflateParams")
/*
* 实例化dialog
* @param type 1.重置账号密码后使用；2.认证时修改手机号成功后使用
* */
        public ResetNumberPWD create(int type) {
            // 使用Theme实例化Dialog
            final ResetNumberPWD dialog = new ResetNumberPWD(activity, R.style.CommonDialog);
            View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_reset_number_pwd, null);
            LinearLayout dialog_bg = layout.findViewById(R.id.dialog_bg);
            TextView dialog_reset_number_title = layout.findViewById(R.id.dialog_reset_number_title);
            TextView dialog_reset_number_context = layout.findViewById(R.id.dialog_reset_number_context);
            Button reset_number_button = layout.findViewById(R.id.reset_number_button);
            switch (type) {
                case 1:
                    dialog_bg.setBackgroundResource(R.mipmap.reset_number_pop_bg);
                    dialog_reset_number_title.setText(R.string.reset_number_title);
                    dialog_reset_number_context.setText(R.string.reset_number_context);
                    break;
                case 2:
                    dialog_bg.setBackgroundResource(R.mipmap.eidt_phone_pop);
                    dialog_reset_number_title.setText(R.string.authentication_phone_title);
                    dialog_reset_number_context.setText(R.string.authentication_phone_context);
                    break;
            }
            reset_number_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSuccessListener != null)
                        onSuccessListener.login();
                    dialog.dismiss();
                }
            });

            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return dialog;
        }
    }

    public interface OnSuccessListener {
        void login();
    }
}
