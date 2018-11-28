package com.zhuochi.hydream.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OpIndexDialog extends Dialog {
    @BindView(R.id.bg)
    ImageView img;
    private Context context;

    /**
     * @param pos: 0 上面 1 下面 2 左面 3 是右面
     */
    public void setPos(int pos) {
        this.pos = pos;
    }


    private int pos;

    public OpIndexDialog(@NonNull Context context) {
        super(context, R.style.CommonDialog);
        this.context = context;

    }

    public OpIndexDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected OpIndexDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.op_index_dialog);
        ButterKnife.bind(this);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        switch (pos) {
            case 0:
                layoutParams.gravity = Gravity.TOP;
                break;
            case 1:
                layoutParams.gravity = Gravity.BOTTOM;
                break;
            case 2:
                layoutParams.gravity = Gravity.LEFT;
                break;
            case 3:
                layoutParams.gravity = Gravity.RIGHT;
                break;
        }

        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);

    }

    public void setBackGround(Object url) {
        Glide.with(context).load(url).error(R.mipmap.op_index).diskCacheStrategy(DiskCacheStrategy.ALL).into(img);

    }

    @Override
    public void show() {
        if (!SPUtils.getBoolean("is_first_enter", true)) {
            return;
        }
        super.show();
        SPUtils.saveBoolean("is_first_enter", false);

    }
}
