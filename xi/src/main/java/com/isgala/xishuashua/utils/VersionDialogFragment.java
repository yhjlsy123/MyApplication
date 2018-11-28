package com.isgala.xishuashua.utils;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.isgala.xishuashua.R;
/**
 * 作者： 叶应是叶
 * 时间： 2017/3/23 12:36
 * 描述：
 */
public class VersionDialogFragment extends DialogFragment {

    private static final String TITLE = "title";

    private static final String DESCRIPTION = "description";

    private View.OnClickListener positiveCallback;
    private String title;
    private setOnclickCancel setOnclickCancel;
    private String description;

    public static VersionDialogFragment getInstance(String title, String description,String url) {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putString(DESCRIPTION, description);
        VersionDialogFragment versionDialogFragment = new VersionDialogFragment();
        versionDialogFragment.setArguments(bundle);
        return versionDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        title = bundle.getString(TITLE);
        description = bundle.getString(DESCRIPTION);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    public void show(FragmentManager fragmentManager, View.OnClickListener positiveCallback,setOnclickCancel setOnclick) {
        this.positiveCallback = positiveCallback;
        setOnclickCancel=setOnclick;
        show(fragmentManager, "VersionDialogFragment");
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.view_upgrade_prompt, null);
//        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_description = (TextView) view.findViewById(R.id.txt_content);
        Button btn_upgrade = (Button) view.findViewById(R.id.btn_submit);
       final Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
//        tv_title.setText(title);
        tv_description.setText(description);
        btn_upgrade.setOnClickListener(positiveCallback);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  btn_cancel.setBackgroundColor(Color.parseColor("#108ee9"));
                setOnclickCancel.OnclickCancel();

            }
        });
//        btn_upgrade.setBackground(getColor(R.color.black333));
        builder.setView(view);
        return builder.create();
    }
    public   interface setOnclickCancel{
        void OnclickCancel();
    }

}
