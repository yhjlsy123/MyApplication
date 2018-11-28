package com.zhuochi.hydream.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.utils.ToastUtils;

/**
 * 设置开柜密码
 * Created by and on 2016/11/10.
 */

public class TipDialogEdit extends Dialog {

    public TipDialogEdit(Context context) {
        super(context);
    }

    public TipDialogEdit(Context context, int theme) {
        super(context, theme);
    }

    public TipDialogEdit(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
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
        public TipDialogEdit create() {
            // 使用Theme实例化Dialog
            final TipDialogEdit dialog = new TipDialogEdit(activity, R.style.CommonDialog);
            View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_tip_edit, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final EditText input_password = (EditText) layout.findViewById(R.id.tip1_content);
//            final EditText again_input_password = (EditText) layout.findViewById(R.id.again_input_password);
            layout.findViewById(R.id.setting_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pwd = input_password.getText().toString().trim();
//                    String againpwd = again_input_password.getText().toString().trim();

                    if (TextUtils.isEmpty(pwd)) {
                        ToastUtils.show("密码不能为空！");
                        return;
                    }
                    if (onClickListener != null) {
                        onClickListener.confirm(pwd);
                        dialog.dismiss();
                    }
                }
            });
            return dialog;
        }
    }

    public interface OnConfirmListener {
        void confirm(String pwd);
    }
}
