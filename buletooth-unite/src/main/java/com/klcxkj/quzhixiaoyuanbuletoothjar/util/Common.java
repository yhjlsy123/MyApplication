package com.klcxkj.quzhixiaoyuanbuletoothjar.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.klcxkj.quzhixiaoyuanbuletooth_unite.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

	public static final String USER_IS_FIRST = "user_is_first";

	public static final String TIME_ID = "time_id";
	public static final String PRODUCT_ID = "product_id";
	public static final String DEVICE_ID = "device_id";
	public static final String ACCOUNT_ID = "account_id";
	public static final String USER_COUNT = "user_count";

	/**
	 * 显示toast
	 */
	public static String getMacMode(String mac) {
		String regex = "(.{2})";
		String input = mac.toUpperCase().replaceAll(regex, "$1:");
		String resultString = input.substring(0, input.length() - 1);
		return resultString;
	}

	public static String[] getSubString(String string, String index) {
		String[] tagsStrings = string.split(index);
		return tagsStrings;
	}

	private static Toast mToast;

	public static void showToast(Context context, int resid, int position) {

		if (mToast == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View layout = inflater.inflate(R.layout.toast_layout, null);
			TextView title = (TextView) layout.findViewById(R.id.toast_text);
			title.setText(resid);
			mToast = new Toast(context);
			// toast.setGravity(Gravity.RIGHT | Gravity.TOP, 0, 0);
			mToast.setView(layout);

		} else {
			View view = mToast.getView();
			TextView textView = (TextView) view.findViewById(R.id.toast_text);
			textView.setText(resid);
		}
		if (position == Gravity.BOTTOM) {
			mToast.setGravity(position, 0, 80);
		} else {
			mToast.setGravity(position, 0, 0);
		}

		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.show();

	}

	public static void showToast(Context context, String string, int position) {

		if (mToast == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View layout = inflater.inflate(R.layout.toast_layout, null);
			TextView title = (TextView) layout.findViewById(R.id.toast_text);
			title.setText(string);
			mToast = new Toast(context);
			// toast.setGravity(Gravity.RIGHT | Gravity.TOP, 0, 0);
			mToast.setView(layout);

		} else {
			View view = mToast.getView();
			TextView textView = (TextView) view.findViewById(R.id.toast_text);
			textView.setText(string);
		}
		mToast.setGravity(position, 0, 0);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.show();

	}

	/**
	 * 判断是不是手机号码
	 */
	public static boolean isPhoneNum(String phone) {
		boolean result = false;
		if (phone != null && phone.length() == 11 && phone.startsWith("1")) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	/*
	 * 验证号码 手机号 固话均可
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;

		String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
		CharSequence inputStr = phoneNumber;

		Pattern pattern = Pattern.compile(expression);

		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches()) {
			isValid = true;
		}

		return isValid;

	}

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(true);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;
	}

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param
	 * @return
	 */
	public static Dialog createDingdanDialog(Context context, String timeid,
                                             String productid, String deviceid, String accountid,
                                             String accounttype, String usercount, String ykmoney, String consumemone, String rate, String mac) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.xiaofeizhangdan_dialog, null);// 得到加载view

		EditText timeid_edit = (EditText) v.findViewById(R.id.timeid_edit);
		EditText productid_edit = (EditText) v
				.findViewById(R.id.productid_edit);
		EditText deviceid_edit = (EditText) v.findViewById(R.id.deviceid_edit);
		EditText accountid_edit = (EditText) v
				.findViewById(R.id.accountid_edit);
		EditText accounttype_edit = (EditText) v
				.findViewById(R.id.accounttype_edit);
		EditText usercount_edit = (EditText) v
				.findViewById(R.id.usercount_edit);
		EditText ykmoney_edit = (EditText) v.findViewById(R.id.ykmoney_edit);
		EditText consumeMone_edit = (EditText) v
				.findViewById(R.id.consumeMone_edit);
		EditText rate_edit = (EditText) v.findViewById(R.id.rate_edit);
		EditText mac_edit = (EditText) v.findViewById(R.id.mac_edit);
		
		timeid_edit.setText(timeid);
		productid_edit.setText(productid);
		deviceid_edit.setText(deviceid);
		accountid_edit.setText(accountid);
		accounttype_edit.setText(accounttype);
		usercount_edit.setText(usercount);
		ykmoney_edit.setText(ykmoney);
		consumeMone_edit.setText(consumemone);
		rate_edit.setText(rate);
		mac_edit.setText(mac);

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
		loadingDialog.setCancelable(true);// 不可以用“返回键”取消
		loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;
	}
}
