package com.zhuochi.hydream.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.adapter.ErrorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErroInforDialog extends Dialog {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.confrim)
    Button confrim;
    private Map<String,Object> data;
    private Context context;
    private ArrayList<String> label;
    private ArrayList<Object> content;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    break;
                case  1:
                    ErrorAdapter adapter=new ErrorAdapter(context,label,content);
                    list.setAdapter(adapter);
                    break;
            }
        }
    };



    public ErroInforDialog(@NonNull Context context,Map<String,Object> data) {
        super(context);
        this.context=context;
        this.data=data;
    }

    public ErroInforDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ErroInforDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_dialog);
        ButterKnife.bind(this);
        dataDeal();
        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErroInforDialog.this.dismiss();
            }
        });

    }
    public void dataDeal(){
        new Thread(){
            @Override
            public void run() {
                super.run();
             label=new ArrayList<String>();
            content=new ArrayList<Object>();
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    label.add( entry.getKey() );
                    content.add(entry.getValue());
                }
                handler.sendEmptyMessage(1);
                onStop();
            }
        }.run();

    }

}
