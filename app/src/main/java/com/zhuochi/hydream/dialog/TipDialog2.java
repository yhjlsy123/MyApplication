package com.zhuochi.hydream.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zhuochi.hydream.R;


/**
 * 提示用户检查定位权限是否开放（或者网络）
 */
public class TipDialog2 extends Dialog {


    public TipDialog2(Context context) {
        super(context);
    }

    public TipDialog2(Context context, int theme) {
        super(context, theme);
    }

    public TipDialog2(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {

        public Context context;
        private View layout;

        public Builder(Context context) {
            this.context = context;
        }


        @SuppressWarnings("deprecation")
        public TipDialog2 create(final View.OnClickListener listener, String title, String content) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final TipDialog2 dialog = new TipDialog2(context, R.style.LoadingAnimDialog);
            layout = inflater.inflate(R.layout.dialog_tip2, null);
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
                    if (listener != null)
                    dialog.dismiss();
                }
            });
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        }
    }


    public static class BuilderTwo {

        public Context context;
        private View layout;

        public BuilderTwo(Context context) {
            this.context = context;
        }


        @SuppressWarnings("deprecation")
        public TipDialog2 create(final View.OnClickListener listener, final View.OnClickListener listenerCancel, String title, String content) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final TipDialog2 dialog = new TipDialog2(context, R.style.LoadingAnimDialog);
            layout = inflater.inflate(R.layout.dialog_tip2, null);
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
                    if (listenerCancel != null)
                        listenerCancel.onClick(v);
                    dialog.dismiss();
                }
            });
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        }
    }


    private static TipDialog2 tipDialog;


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

    /**
     * 获取单例提示框
     */
    public static Dialog show_two(Context context, View.OnClickListener listener, View.OnClickListener listenerCancel, String title, String content) {
        try {
            if (tipDialog == null)
                tipDialog = new BuilderTwo(context).create(listener, listenerCancel, title, content);
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
