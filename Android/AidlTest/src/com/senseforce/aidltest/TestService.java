package com.senseforce.aidltest;

import java.util.HashMap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class TestService extends Service {

	private HashMap<String, String> mData = new HashMap<String, String>();
	private ITestService.Stub mStub = new ITestService.Stub() {

		@Override
		public String output(String key) throws RemoteException {
			return mData.get(key);
		}

		@Override
		public String input(String key, String value) throws RemoteException {
			return mData.put(key, value);
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		return mStub;
	}

}
