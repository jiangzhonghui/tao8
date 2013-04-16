package com.tao8.app.ad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import cn.domob.android.ads.DomobSplashAd;
import cn.domob.android.ads.DomobSplashAdListener;
import cn.waps.AppConnect;

import com.tao8.app.R;
import com.tao8.app.TopConfig;
import com.tao8.app.ui.ViewPagerActivity;
import com.tao8.app.util.LogUtil;

public class SplashScreen extends Activity {
	DomobSplashAd splashAd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		/////////////////////////////
		// 初始化统计器，并通过代码设置WAPS_ID, WAPS_PID
		AppConnect.getInstance(TopConfig.WAPS_ID, "WAPS", this);
		 // 使用自定义的OffersWebView
		AppConnect.getInstance(this).setAdViewClassName(this.getPackageName() + ".ad.MyAdView");
		   // 初始化自定义广告数据
    	AppConnect.getInstance(this).initAdInfo();
    	// 初始化插屏广告数据
    	AppConnect.getInstance(this).initPopAd(this);
		////////////////////////////
    	
    	
		splashAd = new DomobSplashAd(this,TopConfig.PUBLISHER_ID);
		splashAd.setSplashAdListener(new DomobSplashAdListener() {
			@Override
			public void onSplashPresent() {
				Log.i("DomobSDKDemo", "onSplashStart");
			}

			@Override
			public void onSplashDismiss() {
				Log.i("DomobSDKDemo", "onSplashClosed");
				jump();
			}

			@Override
			public void onSplashLoadFailed() {
				Log.i("DomobSDKDemo", "onSplashLoadFailed");
			}
		});

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (splashAd.isSplashAdReady()) {
					splashAd.splash(SplashScreen.this);
				} else {
					LogUtil.e(SplashScreen.this.getClass().getName(), "Splash ad is NOT ready.");
					//Toast.makeText(SplashScreen.this, "Splash ad is NOT ready.", Toast.LENGTH_SHORT).show();
					jump();
				}
			}
		}, 1000);
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
