package com.zhuochi.hydream.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuochi.hydream.R;

/**
 * Created by and on 2016/11/9.
 */

public class SureSchoolChoice extends Dialog {
    public SureSchoolChoice(Context context) {
        super(context);
    }

    public SureSchoolChoice(Context context, int theme) {
        super(context, theme);
    }

    public SureSchoolChoice(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        private Listener listener;

        public void setOnClickListener(Listener listener) {
            this.listener = listener;
        }

        @SuppressWarnings("deprecation")
        @SuppressLint("InflateParams")
        public SureSchoolChoice create(String title) {
            // 使用Theme实例化Dialog
            final SureSchoolChoice dialog = new SureSchoolChoice(activity, R.style.CommonDialog);
            View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_sure_school_choice, null);
            ((TextView) layout.findViewById(R.id.title)).setText(title);
//            ((TextView) layout.findViewById(R.id.sub_title)).setText("，"+sub);
            layout.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.sure();
                    dialog.dismiss();
                }
            });
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return dialog;
        }
    }

    public interface Listener {
        void sure();
    }
}
