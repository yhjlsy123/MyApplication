package com.isgala.xishuashua.dialog;

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

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.utils.ToastUtils;

/**
 * 设置开柜密码
 * Created by and on 2016/11/10.
 */

public class SettingCabinetPWD extends Dialog {

    public SettingCabinetPWD(Context context) {
        super(context);
    }

    public SettingCabinetPWD(Context context, int theme) {
        super(context, theme);
    }

    public SettingCabinetPWD(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
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
        public SettingCabinetPWD create() {
            // 使用Theme实例化Dialog
            final SettingCabinetPWD dialog = new SettingCabinetPWD(activity, R.style.CommonDialog);
            View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_setting_cabinet_pwd, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final EditText showTextView = (EditText) layout.findViewById(R.id.show_pwd_textview);
            final EditText editText = (EditText) layout.findViewById(R.id.setting_pwd);
            layout.findViewById(R.id.setting_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pwd = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(pwd) || pwd.length() < 8) {
                        ToastUtils.show("请确保密码是8-10位");
                        return;
                    }
                    if (onClickListener != null) {
                        onClickListener.confirm(pwd);
                        dialog.dismiss();
                    }
                }
            });
            showTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editText.setText(s);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            final CheckBox check = (CheckBox) layout.findViewById(R.id.show_hide_pwd);
            // 点击后焦点变化，输入类型改变
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String editpwd = editText.getText().toString().trim();
                    int length = editpwd.length();
                    if (isChecked) {
                        showTextView.setText(editpwd);
                        showTextView.setSelection(length);
                        editText.setVisibility(View.INVISIBLE);
                        showTextView.setVisibility(View.VISIBLE);
                    } else {
                        editText.setSelection(length);
                        showTextView.setVisibility(View.INVISIBLE);
                        editText.setVisibility(View.VISIBLE);
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
