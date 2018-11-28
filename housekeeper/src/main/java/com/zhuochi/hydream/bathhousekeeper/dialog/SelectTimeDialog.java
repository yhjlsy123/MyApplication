package com.zhuochi.hydream.bathhousekeeper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.view.datapicker.DatePicker;
import com.zhuochi.hydream.bathhousekeeper.view.datapicker.DialogAlertListener;

import java.util.Calendar;


public class SelectTimeDialog extends Dialog {

	private DialogAlertListener listener;
    DatePicker datePicker;

    TextView wancheng;

    Calendar mCalendar;



	public SelectTimeDialog(Context context,DialogAlertListener listener) {
		super(context, R.style.dialog);

        this.listener = listener;
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_select_layout);
		setCancelable(false);
		setCanceledOnTouchOutside(false);

       init();
	}

    private void init() {
        mCalendar = Calendar.getInstance();
        datePicker = (DatePicker) findViewById(R.id.datePicker);

        wancheng = (TextView) findViewById(R.id.wancheng);

        wancheng.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int year = datePicker.getYear();
                int monthOfYear = datePicker.getMonth();
                int dayOfMonth = datePicker.getDay();
                String years = "";
                String months = "";
                String days = "";
                years = year + "";
                monthOfYear = monthOfYear + 1;
                if (monthOfYear < 10) {
                    months = "0" + monthOfYear;
                } else {
                    months = monthOfYear + "";
                }
                if (dayOfMonth < 10) {
                    days = "0" + dayOfMonth;
                } else {
                    days = dayOfMonth + "";
                }
                String date1 = years + "年" + months + "月" + days + "日";
                String date2 = years + "-" + months + "-" + days;

                if(listener != null) {
                    listener.onDialogOk(SelectTimeDialog.this,date1,date2);
                }
                /*Intent intent = new Intent();
                intent.putExtra("date1", date1);
                intent.putExtra("date2", date2);
                setResult(RESULT_OK, intent);
                finish();*/

            }
        });
    }


}
