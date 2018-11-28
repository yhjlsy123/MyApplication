package com.isgala.xishuashua.dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.bean_.Location;

/**
 * 更新对话框
 * 
 * @author gong
 *
 */
public class UpdateDialog extends Dialog {

	public UpdateDialog(Context context) {
		super(context);
	}

	public UpdateDialog(Context context, int theme) {
		super(context, theme);
	}

	public UpdateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public static class Builder {

		private Context context;
		private int from;
		private Location.Version version;
		private boolean exits;
		private OnClickListener positiveButtonClickListener;
		private OnClickListener negativeButtonClickListener;
		private Button mYes;
		private Button mNo;

		public Builder(Context context, int from, Location.Version version, boolean exits) {
			this.context = context;
			this.from = from;
			this.version = version;
			this.exits = exits;
		}

		/**
		 * 设置立即更新按钮的监听
		 * 
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(OnClickListener listener) {
			this.positiveButtonClickListener = listener;
			return this;
		}

		/**
		 * 设置下次再说按钮的监听
		 * 
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(OnClickListener listener) {
			this.negativeButtonClickListener = listener;
			return this;
		}

		@SuppressWarnings("deprecation")
		@SuppressLint("InflateParams")
		public UpdateDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// 使用Theme实例化Dialog
			final UpdateDialog dialog = new UpdateDialog(context, R.style.UpdateDialog);
			View layout = inflater.inflate(R.layout.dialog_update, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			// 设置title
			((TextView) layout.findViewById(R.id.tv_dialog_update_title)).setText("超级澡堂v" + version.now + "诚邀您体验");

			// 设置更新说明
			((ListView) layout.findViewById(R.id.lv_dialog_update_description)).setAdapter(new DescriptionAdapter());

			// 设置确定按钮
			if (positiveButtonClickListener != null) {
				mYes = (Button) layout.findViewById(R.id.dialog_update_yes);
				if (exits) {
					mYes.setText("(已下载)立即安装");
				} else {
					mYes.setText("立即下载");
				}
				mYes.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
					}
				});
			}

			// 设置取消按钮
			if (negativeButtonClickListener != null) {
				mNo = (Button) layout.findViewById(R.id.dialog_update_no);
				ImageView line = (ImageView) layout.findViewById(R.id.iv_dialog_update_line);
				if (from == 1) {
					dialog.setCancelable(false);
					line.setVisibility(View.GONE);
					mNo.setVisibility(View.GONE);
				} else {
					mNo.setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
						}
					});
				}

			}
			return dialog;

		}

		private class DescriptionAdapter extends BaseAdapter {

			@Override
			public int getCount() {
				return version.update.size();
			}

			@Override
			public Object getItem(int position) {
				return version.update.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder = null;
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = View.inflate(parent.getContext(), R.layout.item_update_description, null);
					holder.number = (TextView) convertView.findViewById(R.id.tv_item_update_description_number);
					holder.description = (TextView) convertView.findViewById(R.id.tv_item_update_description);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				holder.number.setText(position + 1 + ".");
				holder.description.setText(version.update.get(position));

				return convertView;
			}

		}

		public static class ViewHolder {

			public TextView number;
			public TextView description;

		}

	}

}
