package com.senseforce.FinalizePerfTest.classes;

import java.lang.ref.WeakReference;

import android.util.Log;

import com.senseforce.FinalizePerfTest.AbstractTester;

public class RevivedClassTester extends AbstractTester {

	public RevivedClassTester() {
		
	}

	@Override
	public void runTest() {
		runTest(0);
	}
	
	@Override
	public void runTest(int loopIndex) {
		RevivedClassHost host = new RevivedClassHost();
		RevivedClass ref = new RevivedClass(host, loopIndex);
		
		WeakReference<RevivedClass> weakRef = new WeakReference<RevivedClass>(ref);
		
		ref = null;
		System.gc();
		
		if (weakRef.get() != null) {
			Log.d("RevivedClass", "RevivedClass's instance ref is still alive, loop : " + loopIndex);
		}
		else {
			Log.d("RevivedClass", "RevivedClass's instance ref is not alive, loop : " + loopIndex);
		}
		
		weakRef = null;
	}

	public static class RevivedClass {
		
		public RevivedClassHost host = null;
		private int mTag = -1;
		
		public RevivedClass(RevivedClassHost host) {
			this.host = host;
		}
		
		public RevivedClass(RevivedClassHost host, int tag) {
			this.host = host;
			this.mTag = tag;
		} 
		
		protected void finalize() {
			host.ref = this;
			Log.d("RevivedClass", "RevivedClass finalize, loop : " + mTag);
		}
	}
	
	public static class RevivedClassHost {
		public RevivedClass ref = null;
	}
}
