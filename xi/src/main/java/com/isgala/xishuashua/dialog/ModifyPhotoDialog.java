package com.isgala.xishuashua.dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.utils.DimensUtil;

/**
 * 修改图像的对话框
 * 
 * @author gong
 *
 */
public class ModifyPhotoDialog extends Dialog {

	public ModifyPhotoDialog(Context context) {
		super(context);
	}

	public ModifyPhotoDialog(Context context, int theme) {
		super(context, theme);
	}

	public ModifyPhotoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public static class Builder {
		private Context context;
		private String negativeButtonText;
		private OnClickListener negativeButtonClickListener;

		private String cameraButtonText;
		private String albumButtonText;
		private OnClickListener cameraButtonClickListener;
		private OnClickListener albumButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setCameraButton(String cameraButtonText, OnClickListener listener) {
			this.cameraButtonText = cameraButtonText;
			this.cameraButtonClickListener = listener;
			return this;
		}

		public Builder setAlbumButton(String albumButtonText, OnClickListener listener) {
			this.albumButtonText = albumButtonText;
			this.albumButtonClickListener = listener;
			return this;
		}

		/**
		 * 使用String来设置取消按钮的文字，同时设置监听
		 * 
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		@SuppressLint("InflateParams")
		@SuppressWarnings("deprecation")
		public ModifyPhotoDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// 使用Theme实例化Dialog
			final ModifyPhotoDialog dialog = new ModifyPhotoDialog(context, R.style.ModifyPhotoDialog);
			View layout = inflater.inflate(R.layout.dialog_modify_photo, null);

			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			// 设置拍照按钮
			if (cameraButtonText != null) {
				((Button) layout.findViewById(R.id.bt_dialog_modify_camera)).setText(cameraButtonText);
				if (cameraButtonClickListener != null) {
					((Button) layout.findViewById(R.id.bt_dialog_modify_camera))
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									cameraButtonClickListener.onClick(dialog, DialogInterface.BUTTON1);
								}
							});
				}
			}
			// 设置相册按钮
			if (albumButtonText != null) {
				((Button) layout.findViewById(R.id.bt_dialog_modify_gallery)).setText(albumButtonText);
				if (albumButtonClickListener != null) {
					((Button) layout.findViewById(R.id.bt_dialog_modify_gallery))
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									albumButtonClickListener.onClick(dialog, BUTTON2);
								}
							});
				}
			}
			// 设置取消按钮
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.bt_dialog_modify_cancel)).setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.bt_dialog_modify_cancel))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			}
			Window window = dialog.getWindow();
			window.setGravity(Gravity.BOTTOM);
			window.setWindowAnimations(R.style.ModifyPhotoDialog);
			WindowManager windowManager = window.getWindowManager();
			Display defaultDisplay = windowManager.getDefaultDisplay();
			WindowManager.LayoutParams attributes = window.getAttributes();
			attributes.width = (int) (defaultDisplay.getWidth() - 56 * DimensUtil.getWidthRate());
			window.setAttributes(attributes);
			return dialog;
		}
	}

}
