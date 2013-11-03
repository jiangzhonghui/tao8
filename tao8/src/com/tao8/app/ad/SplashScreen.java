package com.tao8.app.ad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tao8.app.R;
import com.tao8.app.ui.ViewPagerActivity;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		jump();
	}

	private void jump() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(SplashScreen.this, ViewPagerActivity.class));
				finish();
			}
		}, 1000);
	}
}
