package com.zhuochi.hydream.bathhousekeeper.view.datapicker;

import android.app.Dialog;

public interface DialogAlertListener {


	public void onDialogOk(Dialog dlg, String date1, String date2);

	public void onDialogCancel(Dialog dlg);
	
}
