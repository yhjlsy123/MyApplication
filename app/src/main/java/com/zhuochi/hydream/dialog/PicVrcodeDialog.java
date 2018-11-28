package com.zhuochi.hydream.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.zhuochi.hydream.R;

/**
 * Created by and on 2016/11/7.
 */

public class PicVrcodeDialog extends Dialog {


    public PicVrcodeDialog(Context context) {
        super(context);
    }

    public PicVrcodeDialog(Context context, int theme) {
        super(context, theme);
    }

    public PicVrcodeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder implements android.view.View.OnClickListener {

        public Context context;
        private EditText inputText;
        private ImageView ivCode;

        public Builder(Context context) {
            this.context = context;
        }

        public ImageView getIvCode() {
            return ivCode;
        }

        @SuppressWarnings("deprecation")
        public PicVrcodeDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final PicVrcodeDialog dialog = new PicVrcodeDialog(context, R.style.PicVrcodeDialog);
            View layout = inflater.inflate(R.layout.dialog_picvrcode, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            inputText = (EditText) layout.findViewById(R.id.et_dialog_pic_vrcode);// 验证码输入框
            inputText.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {// 当用户验证码输入完成自动获取验证码
                    if (s.length() >= 4) {
                        dialog.dismiss();
                        if (vrCodeListener != null)
                            vrCodeListener.getUserInputCode(s.toString());
                    }
                }
            });
            ivCode = (ImageView) layout.findViewById(R.id.iv_dialog_pic_vrcode);
            ivCode.setOnClickListener(this);
            return dialog;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_dialog_pic_vrcode:
                    if (vrCodeListener != null)
                        vrCodeListener.refreshCode(ivCode);
                    break;
            }
        }

        public void setVrCodeListener(VrCodeListener vrCodeListener) {
            this.vrCodeListener = vrCodeListener;
        }

        private VrCodeListener vrCodeListener;
    }

    /**
     * 监听图片验证码的接口
     */
    public interface VrCodeListener {
        /**
         * 用于刷新显示的图形验证码
         */
        void refreshCode(ImageView ivCode);

        /**
         * 获取用户输入的验证码
         *
         * @param userInputCode
         */
        void getUserInputCode(String userInputCode);
    }
}
