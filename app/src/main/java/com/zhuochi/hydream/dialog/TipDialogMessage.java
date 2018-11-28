package com.zhuochi.hydream.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuochi.hydream.R;


/**
 * 提示用户检查定位权限是否开放（或者网络）
 */
public class TipDialogMessage extends Dialog {


    public TipDialogMessage(Context context) {
        super(context);
    }

    public TipDialogMessage(Context context, int theme) {
        super(context, theme);
    }

    public TipDialogMessage(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {

        public Context context;

        public Builder(Context context) {
            this.context = context;
        }


        @SuppressWarnings("deprecation")
        public TipDialogMessage create(final View.OnClickListener listener, String title, String content) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final TipDialogMessage dialog = new TipDialogMessage(context, R.style.LoadingAnimDialog);
            View layout = inflater.inflate(R.layout.dialog_tip_message, null);
            ((TextView) layout.findViewById(R.id.tip1_content)).setText(content);
            layout.findViewById(R.id.tip1_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onClick(v);
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.tip1_canel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

    private static TipDialogMessage tipDialog;

    /**
     * 获取单例提示框
     */
    public static Dialog show_(Context context, View.OnClickListener listener, String title, String content) {
        try {
            if (tipDialog == null)
                tipDialog = new Builder(context).create(listener, title, content);
            tipDialog.show();
        } catch (Exception e) {

        }
        return tipDialog;
    }

    public static void dismiss_() {
        if (tipDialog != null)
            tipDialog.dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        tipDialog = null;
    }
}
