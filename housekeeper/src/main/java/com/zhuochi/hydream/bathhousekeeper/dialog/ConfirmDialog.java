package com.zhuochi.hydream.bathhousekeeper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmDialog extends Dialog {
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.sure)
    Button sure;
    private String contentValue;
    private View.OnClickListener clickListener;

    public ConfirmDialog(@NonNull Context context, String content, View.OnClickListener clickListener) {
        super(context);
        this.contentValue = content;
        this.clickListener = clickListener;
        show();
    }

    public ConfirmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_dialog);
        ButterKnife.bind(this);
        if (!(TextUtils.isEmpty(contentValue))) {
            content.setText(contentValue);
        }
        sure.setOnClickListener(clickListener);

    }

    @OnClick({R.id.cancel, R.id.sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.sure:
                dismiss();
                break;
        }

    }


}
