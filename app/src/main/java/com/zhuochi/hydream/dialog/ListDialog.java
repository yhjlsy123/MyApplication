package com.zhuochi.hydream.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zhuochi.hydream.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListDialog extends Dialog {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.confrim)
    Button confrim;

    public ListDialog(@NonNull Context context) {
        super(context);
    }

    public ListDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_dialog);
        ButterKnife.bind(this);
    }
}
