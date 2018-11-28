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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.utils.ToastUtils;

/**
 * 设备密码重置后弹窗;
 * 引用： 一卡通认证后，弹窗
 * Created by and on 2016/11/10.
 */

public class ResetEquipmentPWD extends Dialog {

    public ResetEquipmentPWD(Context context) {
        super(context);
    }

    public ResetEquipmentPWD(Context context, int theme) {
        super(context, theme);
    }

    public ResetEquipmentPWD(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        @SuppressWarnings("deprecation")
        @SuppressLint("InflateParams")
        /*
        * 实例化dialog
        * @param type  传入的类型  1.首次修改设备密码，2.修改设备密码；3.一卡通成功后弹窗
        * */
        public ResetEquipmentPWD create(int type) {
            // 使用Theme实例化Dialog
            final ResetEquipmentPWD dialog = new ResetEquipmentPWD(activity, R.style.CommonDialog);
            View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_reset_equipment_pwd, null);
            LinearLayout dialog_bg = layout.findViewById(R.id.dialog_bg);
            TextView dialog_reset_equipment_title = layout.findViewById(R.id.dialog_reset_equipment_title);
            TextView dialog_reset_equipment_context = layout.findViewById(R.id.dialog_reset_equipment_context);
            switch (type) {
                case 1:
                    dialog_reset_equipment_title.setText(R.string.reset_equipment_title_first);
                    dialog_reset_equipment_context.setText(R.string.reset_equipment_context_first);
                    break;
                case 2:
                    dialog_reset_equipment_title.setText(R.string.reset_equipment_title);
                    dialog_reset_equipment_context.setText(R.string.reset_equipment_context);
                    break;
                case 3:
                    dialog_bg.setBackgroundResource(R.mipmap.eidt_card_pop);
                    dialog_reset_equipment_title.setText(R.string.authentication_card_title);
                    dialog_reset_equipment_context.setText(R.string.authentication_card_context);
                    break;
            }

            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return dialog;
        }
    }
}
