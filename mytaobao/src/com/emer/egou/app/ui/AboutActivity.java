package com.emer.egou.app.ui;

import com.emar.escore.banner.BannerSDK;
import com.emar.escore.sdk.view.bannerView;
import com.emer.egou.app.R;

import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;

public class AboutActivity extends BaseActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		bannerView bannerView = BannerSDK.getInstance(this).getBanner();
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.banner_linear);
		linearLayout.addView(bannerView);
		BannerSDK.getInstance(AboutActivity.this).showBanner(bannerView);
	}
}
