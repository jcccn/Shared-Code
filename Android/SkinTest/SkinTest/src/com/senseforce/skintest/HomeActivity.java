package com.senseforce.skintest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.senseforce.skintest.utils.ZipUtil;

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
		findViewById(R.id.button_theme_3).setOnClickListener(this);
		
		initSkinChoices();
	}
	
	private void initSkinChoices() {
		LinearLayout frame = (LinearLayout)findViewById(R.id.frame);
		ArrayList<PackageInfo> skinList = getAllSkinPackages();
		for (PackageInfo skinInfo : skinList) {
			Context newContext = null;
			try {
				newContext = createPackageContext(skinInfo.packageName, Context.CONTEXT_IGNORE_SECURITY);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			if (newContext == null) {
				continue;
			}
			Button button = new Button(this);
			button.setText(skinInfo.packageName);
			button.setBackgroundResource(R.drawable.button_bg_day);
			button.setTextColor(getResources().getColor(R.color.white));
			button.setTextSize(16);
			final Context skinContext = newContext;
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onSkinChanged(skinContext);
				}
			});
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(10, 10, 10, 10);
			frame.addView(button, params);
		}
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
			
		case R.id.button_theme_3:
			changeSkinWithFiles();
			title_textview.setText(R.string.bar_title_main);
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

//		this.onCreate(null); // used to refresh the activity.It's not a good way.
		onSkinChanged(getApplicationContext());
	}
	
	private void changeSkinWithFiles() {
		String skinDirectoryPath = "/data/data/com.senseforce.skintest/files/skin/res/"; 
		ZipUtil zipUtil = new ZipUtil(2049);
		try {
			zipUtil.unZip(createPackageContext("com.senseforce.skintest.asset1", Context.CONTEXT_IGNORE_SECURITY), "skin.zip", skinDirectoryPath);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		//TODO It will leak here. fix it!
		BitmapDrawable bitmapDrawable = new BitmapDrawable(this.getResources(), skinDirectoryPath + "background_img.png");
		if (bitmapDrawable.getBitmap() == null) {
			Toast.makeText(this, "The skin may be broken", Toast.LENGTH_SHORT).show();
		}
		findViewById(R.id.frame).setBackgroundDrawable(bitmapDrawable);
		title_textview.setText(this.getString(R.string.app_name));
		title_textview.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.title_bar_bg));
	}
	
	private void onSkinChanged(Context skinContext) {
		title_textview.setText(skinContext.getString(R.string.app_name));
		title_textview.setBackgroundDrawable(skinContext.getResources().getDrawable(R.drawable.title_bar_bg));
		findViewById(R.id.frame).setBackgroundDrawable(skinContext.getResources().getDrawable(R.drawable.color_gloal_background));
		View topView = findViewById(R.id.frame);
		if (topView instanceof ViewGroup) {
			ViewGroup frame = (ViewGroup)topView;
			int childrenCount = frame.getChildCount();
			for (int childIndex = 0; childIndex < childrenCount; childIndex++) {
				View button = frame.getChildAt(childIndex);
				if (button instanceof Button) {
					button.setBackgroundDrawable(skinContext.getResources().getDrawable(R.drawable.button_bg_day));
				}
			}
		}
		
	}
	
	private ArrayList<PackageInfo> getAllSkinPackages() {
		ArrayList<PackageInfo> skinList = new ArrayList<PackageInfo>();
		List<PackageInfo> allPackages = getPackageManager().getInstalledPackages(PackageManager.SIGNATURE_MATCH);
		for (PackageInfo packageInfo : allPackages) {
			if (isSkinPackage(packageInfo)) {
				skinList.add(packageInfo);
			}
		}
		return skinList;
	}
	
	private boolean isSkinPackage(PackageInfo packageInfo) {
		String regularExpressionPattern = "com.senseforce.skintest.skin\\w";
		Pattern pattern = Pattern.compile(regularExpressionPattern);
		Matcher matcher = pattern.matcher(packageInfo.packageName);
		return matcher.find();
	}

	@Override
	protected void onDestroy() {
		findViewById(R.id.button_theme_1).setOnClickListener(null);
		findViewById(R.id.button_theme_2).setOnClickListener(null);
		findViewById(R.id.button_theme_3).setOnClickListener(null);
		
		super.onDestroy();
	}
	
	
}