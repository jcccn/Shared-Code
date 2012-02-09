package com.senseforce.FinalizePerfTest;

import com.senseforce.FinalizePerfTest.classes.RevivedClassTester;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	private final static int LOOP_COUNT = 100;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        AbstractTester tester = new RevivedClassTester();
        for (int loop = 0; loop < LOOP_COUNT; loop ++) {
        	tester.runTest(loop);
        }
    }
}