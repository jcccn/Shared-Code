package com.senseforce.skintest;

import java.util.Locale;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class HomeActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	TextView title_textview = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		title_textview = (TextView) findViewById(R.id.title_textview);

		findViewById(R.id.button_theme_1).setOnClickListener(this);
		findViewById(R.id.button_theme_2).setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_theme_1:
			changeLanguage(Locale.SIMPLIFIED_CHINESE);
			title_textview.setText(R.string.bar_title_day);
			break;

		case R.id.button_theme_2:
			changeLanguage(Locale.ENGLISH);
			title_textview.setText(R.string.bar_title_night);
			break;

		default:
			break;
		}

	}

	private void changeLanguage(Locale newLocale) {
		Resources resources = getResources();
		Configuration config = resources.getConfiguration();
		DisplayMetrics dm = resources.getDisplayMetrics();
		config.locale = newLocale;
		resources.updateConfiguration(config, dm);

		this.onCreate(null); // used to refresh the activity
	}
}