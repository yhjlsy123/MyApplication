package com.isgala.xishuashua.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.isgala.xishuashua.R;
import com.isgala.xishuashua.utils.DimensUtil;
import com.isgala.xishuashua.utils.ImageLoadUtils;
import com.isgala.xishuashua.utils.LogUtils;

import static com.isgala.xishuashua.R.id.rank_user_photo;


/**
 * 活动弹框
 *
 * @author and
 */
public class AppActivityDialog extends Dialog {

    public AppActivityDialog(Context context) {
        super(context);
    }

    public AppActivityDialog(Context context, int theme) {
        super(context, theme);
    }

    public AppActivityDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context context;
        private OnClickListener positiveButtonClickListener;
        private View picCoupon;

        public Builder(Context context) {
            this.context = context;
        }


        /**
         * 设置确认按钮的监听
         *
         * @param listener
         * @return
         */
        public Builder setPositiveButton(OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }


        @SuppressLint("InflateParams")
        public AppActivityDialog create(int width, int height, String imageurl) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 使用Theme实例化Dialog
            final AppActivityDialog dialog = new AppActivityDialog(context, R.style.CommonDialog);
            View layout = inflater.inflate(R.layout.dialog_app_activity, null);
            dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            layout.findViewById(R.id.iv_close_dialog).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.dialog_app_activity_root).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            picCoupon = layout.findViewById(R.id.iv_coupon);
            picCoupon.measure(0, 0);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) picCoupon.getLayoutParams();
            if (width > 1080 || height > 1920) {// 图片大于6P屏幕尺寸
                if (DimensUtil.get6PHeightRate() >= DimensUtil.get6PWidthRate()) {//高大于等于宽,取宽
                    layoutParams.width = DimensUtil.getScreenWidth();
                    layoutParams.height = (int) (height / (width * 1.0f / layoutParams.width));
                } else {
                    layoutParams.height = (int) (DimensUtil.getScreenHeight() - 40 * DimensUtil.getHeightRate());
                    layoutParams.width = (int) (width / (height * 1.0f / layoutParams.height));
                }
            } else {
                width = width + 40;
                layoutParams.width = (int) (width * DimensUtil.get6PWidthRate());
                layoutParams.height = (int) (height * DimensUtil.get6PHeightRate());
            }
            picCoupon.setLayoutParams(layoutParams);
            ImageLoadUtils.loadImage(context, (ImageView) picCoupon, imageurl);

            // 设置确定按钮
            if (positiveButtonClickListener != null) {
                picCoupon.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (positiveButtonClickListener != null)
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }

            Window window = dialog.getWindow();
            window.setWindowAnimations(0);
            android.view.WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = (int) (width * DimensUtil.get6PWidthRate());
            window.setGravity(Gravity.CENTER);
            // 透明度的范围为：0.0f-1.0f;0.0f表示完全透明,1.0f表示完全不透明(系统默认的就是这个)。
            window.setAttributes(attributes);

            return dialog;
        }
    }
}
