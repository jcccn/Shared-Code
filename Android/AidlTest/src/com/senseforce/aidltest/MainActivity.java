package com.senseforce.aidltest;

import java.util.Date;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private Button button0;
	private Button button1;
	private Button button2;

	private ITestService mService = null;
	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ITestService.Stub.asInterface(service);
			display("bind success");
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		button0 = (Button) findViewById(R.id.button_0);
		button1 = (Button) findViewById(R.id.button_1);
		button2 = (Button) findViewById(R.id.button_2);

		button0.setOnClickListener(this);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
	}

	@Override
	public void onDestroy() {
		button0.setOnClickListener(null);
		button1.setOnClickListener(null);
		button2.setOnClickListener(null);
		super.onDestroy();
	}

	private boolean mBound = false;
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button_0:
			if (mBound) {
				unbindService(mServiceConnection);
			}
			Intent intent = new Intent("com.senseforce.aidltest.ITestService");
			mBound = bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
			break;

		case R.id.button_1:
			String timeString = new Date().toLocaleString();
			if (mService != null) {
				try {
					mService.input("time", timeString);
					display("put:" + timeString);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			else {
				display("not bound");
			}
			break;

		case R.id.button_2:
			if (mService != null) {
				try {
					display("got:" + mService.output("time"));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			else {
				display("not binded");
			}
			break;

		default:
			break;
		}
	}
	
	private void display(String result) {
		((TextView)findViewById(R.id.output_textview)).setText(result);
	}
}