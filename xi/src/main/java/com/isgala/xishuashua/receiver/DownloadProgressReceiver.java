package com.isgala.xishuashua.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下载进度监听接收者
 * 
 * @author gong
 *
 */
public class DownloadProgressReceiver extends BroadcastReceiver {

	private ProgressBar mProgress;
	private TextView mPercent;

	public DownloadProgressReceiver() {
		super();
	}

	public DownloadProgressReceiver(ProgressBar progress, TextView percent) {
		this.mProgress = progress;
		this.mPercent = percent;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		long progress = intent.getLongExtra("progress", 0);

		long max = intent.getLongExtra("max", 0);

		long percent = (long) ((progress * 1.00 / max * 1.00) * 100);

		mProgress.setMax((int) max);

		mProgress.setProgress((int) progress);

		mPercent.setText(percent + "");

	}

}
