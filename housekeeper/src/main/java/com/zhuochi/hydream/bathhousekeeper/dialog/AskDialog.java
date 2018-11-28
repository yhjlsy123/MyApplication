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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AskDialog extends Dialog {
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.sure)
    Button sure;

    public String getContentValue() {
        return content.getText().toString();
    }

    private String contentValue;
    private View.OnClickListener sUreclickListener;
    private View.OnClickListener rUreclickListener;

    public AskDialog(@NonNull Context context, View.OnClickListener sUreclickListener, View.OnClickListener rUreclickListener) {
        super(context);
        this.sUreclickListener = sUreclickListener;
        this.rUreclickListener = rUreclickListener;
        show();
    }

    public AskDialog(@NonNull Context context, int themeResId) {
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
        sure.setOnClickListener(sUreclickListener);
        cancel.setOnClickListener(rUreclickListener);

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
