package com.isgala.xishuashua.dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.receiver.DownloadProgressReceiver;


/**
 * 下载进度对话框
 * 
 * @author gong
 *
 */
public class DownloadDialog extends Dialog {

	private static ProgressBar progress;
	private static TextView percent;

	public DownloadDialog(Context context) {
		super(context);
	}

	public DownloadDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {

		private DownloadProgressReceiver receiver;
		private Context context;
		private int from;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder(Context context, int from) {
			this.context = context;
			this.from = from;
		}

		@SuppressWarnings("deprecation")
		@SuppressLint("InflateParams")
		public DownloadDialog create() {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// 使用Theme实例化Dialog
			final DownloadDialog dialog = new DownloadDialog(context, R.style.UpdateDialog);
			View layout = inflater.inflate(R.layout.dialog_download, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			progress = (ProgressBar) layout.findViewById(R.id.pb_dialog_download_progress);

			percent = (TextView) layout.findViewById(R.id.tv_dialog_download_percent);

			dialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_SEARCH) {
						return true;
					} else {
						return false; // 默认返回 false
					}
				}
			});

			receiver = new DownloadProgressReceiver(progress, percent);
			IntentFilter intentFilter = new IntentFilter("DOWNLOAD");
			context.registerReceiver(receiver, intentFilter);

			dialog.setCancelable(false);
			return dialog;
		}

	}

}
