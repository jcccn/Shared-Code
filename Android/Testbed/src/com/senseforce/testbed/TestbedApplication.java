package com.senseforce.testbed;

import android.app.Application;
import android.preference.PreferenceManager;

public class TestbedApplication extends Application {
	@Override
	public void onCreate() {
		/*
		 * This populates the default values from the preferences XML file. See
		 * {@link DefaultValues} for more details.
		 */
		PreferenceManager.setDefaultValues(this, R.xml.default_values, false);
	}

	@Override
	public void onTerminate() {
	}
}
