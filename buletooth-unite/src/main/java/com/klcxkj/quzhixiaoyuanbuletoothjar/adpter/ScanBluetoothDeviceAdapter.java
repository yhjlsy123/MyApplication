package com.klcxkj.quzhixiaoyuanbuletoothjar.adpter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.klcxkj.quzhixiaoyuanbuletooth_unite.R;
import com.klcxkj.quzhixiaoyuanbuletoothjar.MainActivity;
import com.klcxkj.quzhixiaoyuanbuletoothjar.widget.Effectstype;
import com.klcxkj.quzhixiaoyuanbuletoothjar.widget.NiftyDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class ScanBluetoothDeviceAdapter extends BaseAdapter {

	private LayoutInflater mInflator;
	private List<BluetoothDevice> devices;
	private Context mContext;

	private NiftyDialogBuilder dialogBuilder;

	public ScanBluetoothDeviceAdapter(Context context,
                                      List<BluetoothDevice> list, NiftyDialogBuilder niftyDialogBuilder) {
		super();
		mContext = context;
		mInflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		devices = list;

		dialogBuilder = niftyDialogBuilder;
	}

	@Override
	public int getCount() {
		return devices.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return devices.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub

		return position;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {

		ViewHolder viewHolder;
		if (view == null) {
			view = mInflator.inflate(R.layout.scan_bluetoothdevice_item,
					viewGroup, false);
			viewHolder = new ViewHolder();

			viewHolder.address_txt = (TextView) view
					.findViewById(R.id.address_txt);

			viewHolder.mac_txt = (TextView) view.findViewById(R.id.mac_txt);

			viewHolder.bind_btn = (Button) view.findViewById(R.id.bind_btn);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		BluetoothDevice bluetoothDevice = devices.get(i);

		viewHolder.address_txt.setText(bluetoothDevice.getAddress());

		viewHolder.mac_txt.setText(bluetoothDevice.getName());

		viewHolder.bind_btn.setTag(bluetoothDevice);
		viewHolder.bind_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final BluetoothDevice bDevice = (BluetoothDevice) v.getTag();
				dialogBuilder
						.withTitle(mContext.getString(R.string.tips))
						.withMessage(
								mContext.getString(R.string.tips_bluetooth_bind))
						.withEffect(Effectstype.Fadein)
						.isCancelable(false)
						.withButton1Text(
								mContext.getString(R.string.cancel))
						.setButton1Click(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialogBuilder.dismiss();
							}
						})
						.withButton2Text(mContext.getString(R.string.sure))
						.setButton2Click(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialogBuilder.dismiss();
//

								Intent intent = new Intent();
								intent.setClass(mContext, MainActivity.class);
								intent.putExtra("deciveMac",bDevice.getAddress());
								mContext.startActivity(intent);
							}
						}).show();
			}
		});
		return view;
	}

	static class ViewHolder {
		TextView address_txt;
		TextView mac_txt;
		Button bind_btn;
	}

	public void changeData(ArrayList<BluetoothDevice> lists) {
		devices = lists;
		notifyDataSetChanged();

	}

}
