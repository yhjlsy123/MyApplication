package com.klcxkj.quzhixiaoyuanbuletoothjar;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jooronjar.BluetoothService;
import com.klcxkj.quzhixiaoyuanbuletooth_unite.R;
import com.klcxkj.quzhixiaoyuanbuletoothjar.adpter.ScanBluetoothDeviceAdapter;
import com.klcxkj.quzhixiaoyuanbuletoothjar.util.Common;
import com.klcxkj.quzhixiaoyuanbuletoothjar.widget.NiftyDialogBuilder;

import java.util.ArrayList;
/**
 * autor:OFFICE-ADMIN
 * time:2018/4/17
 * email:yinjuan@klcxkj.com
 * description:搜索蓝牙设备类
 */
public class SearchDeviceActivity extends Activity {

	private TextView start_search;
	private TextView end_search;

	private LinearLayout device_layout;
	private ListView device_listview;
	private TextView close_txt;

	private BluetoothAdapter mBtAdapter;

	private ScanBluetoothDeviceAdapter scanBluetoothDeviceAdapter;
	private ArrayList<BluetoothDevice> mBluetoothDevices;
	private ArrayList<String> mBluetoothName;



	private NiftyDialogBuilder dialogBuilder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_bule);

		dialogBuilder = NiftyDialogBuilder.getInstance(this);
		initView();

		mBtAdapter = BluetoothAdapter.getDefaultAdapter();



		mBluetoothDevices = new ArrayList<BluetoothDevice>();
		mBluetoothName = new ArrayList<String>();
		scanBluetoothDeviceAdapter = new ScanBluetoothDeviceAdapter(this, mBluetoothDevices, dialogBuilder);

		device_listview.setAdapter(scanBluetoothDeviceAdapter);

		// 设置广播信息过滤
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

		this.registerReceiver(receiver, intentFilter);
		//权限申请
	//	checkPermission();
	}
	public static final int PERMISSION_REQUEST_CODE = 1;
	private void checkPermission() {
		String[] permissions = new String[] {Manifest.permission.BLUETOOTH,
				Manifest.permission.BLUETOOTH_ADMIN,
				Manifest.permission.BLUETOOTH_ADMIN,
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.WAKE_LOCK};
		boolean isAllPermissionGranted = true;
		for (int i=0; i< permissions.length; i++) {
			if (!selfPermissionGranted(permissions[i])) {
				isAllPermissionGranted = false;
				break;
			}
		}
		if (!isAllPermissionGranted) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				requestPermissions(permissions, PERMISSION_REQUEST_CODE);
			}
		} else {
			//Toast.makeText(mContext, "应用已经获得需要的权限", Toast.LENGTH_SHORT).show();
		}
	}

	private boolean selfPermissionGranted(String permission) {
		boolean result = true;
		int targetSdkVersion = 0;
		try {
			final PackageInfo info = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0);
			targetSdkVersion = info.applicationInfo.targetSdkVersion;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (targetSdkVersion >= Build.VERSION_CODES.M) {
				result = this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
			} else {
				result = PermissionChecker.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
			}
		}
		return result;
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_CODE:
			{
				if (grantResults.length > 0) {
					boolean bAllPermissionGranted = true;
					for (int i=0; i<grantResults.length; i++) {
						if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
							bAllPermissionGranted = false;
						}
					}
					if (bAllPermissionGranted) {
						//Toast.makeText(mContext, "应用已经获得需要的权限", Toast.LENGTH_SHORT).show();
					} else {
						//Toast.makeText(mContext, "请在权限管理中允许本应用的所有权限", Toast.LENGTH_SHORT).show();
					}

				} else {
					//Toast.makeText(mContext, "请在权限管理中允许本应用的所有权限", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			default:
				break;
		}
	}

	private void initView() {
		start_search = (TextView) findViewById(R.id.start_search);

		start_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startSearch();
			}
		});

		end_search = (TextView) findViewById(R.id.end_search);

		end_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				endSearch();
			}
		});

		device_layout = (LinearLayout) findViewById(R.id.device_layout);
		device_listview = (ListView) findViewById(R.id.device_listview);
		close_txt = (TextView) findViewById(R.id.close_txt);

		device_layout.setVisibility(View.GONE);

		close_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				device_layout.setVisibility(View.GONE);
			}
		});

	}

	protected void endSearch() {
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
	}

	protected void startSearch() {
		mBluetoothDevices.clear();
		scanBluetoothDeviceAdapter.changeData(mBluetoothDevices);
		mBluetoothName.clear();
//		StringBuilder stringBuilder = new StringBuilder();
//		// 获取所有已经绑定的蓝牙设备
//		Set<BluetoothDevice> devices = mBtAdapter.getBondedDevices();
//		if (devices.size() > 0) {
//			device_layout.setVisibility(View.VISIBLE);
//			for (BluetoothDevice bluetoothDevice : devices) {
//				stringBuilder.append(bluetoothDevice.getName() + ":"
//						+ bluetoothDevice.getAddress() + "\n\n");
//				mBluetoothDevices.add(bluetoothDevice);
//				scanBluetoothDeviceAdapter.changeData(mBluetoothDevices);
//			}
//		}

		doDiscovery();

	}

	private void doDiscovery() {
		// If we're already discovering, stop it
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}

		// Request discover from BluetoothAdapter
		mBtAdapter.startDiscovery();
	}

	@Override
	protected void onDestroy() {
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}
		super.onDestroy();

		// Make sure we're not doing discovery anymore


		// Unregister broadcast listeners
		this.unregisterReceiver(receiver);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// 获取查找到的蓝牙设备
				device_layout.setVisibility(View.VISIBLE);
				BluetoothDevice bluetoothDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				BluetoothDevice mDevice = mBtAdapter
						.getRemoteDevice(bluetoothDevice.getAddress());

				if (mDevice != null && mDevice.getAddress() != null) {
					
//					mBtAdapter.cancelDiscovery(); 
					String address = mDevice.getAddress();
					Log.e("water",
							"ACTION_FOUND device = " + address);
					
					if (!mBluetoothName.contains(address)) {
						mBluetoothDevices.add(mDevice);
						mBluetoothName.add(address);
						scanBluetoothDeviceAdapter.changeData(mBluetoothDevices);
					}

					
				}

			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				// 状态改变的广播
				BluetoothDevice bluetoothDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				BluetoothDevice mDevice = mBtAdapter
						.getRemoteDevice(bluetoothDevice.getAddress());

				// if (mDevice != null && mDevice.getName() != null) {
				// Log.e("water", "device = " + mDevice.getAddress());
				// // if (device.getName().equalsIgnoreCase(name)) {
				// int connectState = mDevice.getBondState();
				// switch (connectState) {
				// case BluetoothDevice.BOND_NONE:
				// break;
				// case BluetoothDevice.BOND_BONDING:
				// break;
				// case BluetoothDevice.BOND_BONDED:
				// mbtService.connect(mDevice);
				// break;
				// }
				// // }
				// }

			}
		}
	};

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BluetoothService.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					Common.showToast(SearchDeviceActivity.this,
							R.string.connected_device, Gravity.CENTER);

					Intent intent = new Intent();
					intent.setClass(SearchDeviceActivity.this, MainActivity.class);
					startActivity(intent);

					break;
				case BluetoothService.STATE_CONNECTING:
					Common.showToast(SearchDeviceActivity.this,
							R.string.connecting_device, Gravity.CENTER);
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					if (!SearchDeviceActivity.this.isFinishing()) {
						Common.showToast(SearchDeviceActivity.this,
								R.string.unanble_connect_device, Gravity.CENTER);
					}

					break;
				}
				break;
			case BluetoothService.MESSAGE_WRITE:
				Log.e("water:", "MESSAGE_READ");
				// byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				// String writeMessage = new String(writeBuf);
				// mConversationArrayAdapter.add("Me:  " + writeMessage);
				break;
			case BluetoothService.MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				// String readMessage = new String(readBuf, 0, msg.arg1);
				// _buttonConnect.setText("Key1");
				Log.e("water:", "Bb:" + readBuf[0] + ' ' + readBuf[1] + ' '
						+ readBuf[2] + ' ' + readBuf[3] + ' ' + readBuf[4]);

				break;
			}
		}
	};

}
