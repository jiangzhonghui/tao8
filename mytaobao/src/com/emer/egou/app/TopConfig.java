package com.emer.egou.app;

import com.taobao.top.android.TopAndroidClient;

public class TopConfig {

	public static final String APPKEY = "21362041";
	public static final String APPSECRET = "9f3da7f0eeff0528fd372d1cea22ca8e";
	public static final String redirectURI = "com.emar.egou://authorize";
	public static TopAndroidClient client = TopAndroidClient
			.getAndroidClientByAppKey(TopConfig.APPKEY);
	public static Long userId;
	public static final String DEFAULT_SESSION = "6102b016f14feeeda88d6ec8c5a37b17f60d99f667f7ac1362114751";
	


}
