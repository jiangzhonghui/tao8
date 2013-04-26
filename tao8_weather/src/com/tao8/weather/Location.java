package com.tao8.weather;

import android.app.Application;
import android.os.Process;
import android.util.Log;

import com.baidu.location.LocationClient;

public class Location extends Application {

	public LocationClient mLocationClient = null;
	public static String TAG = "LocTestDemo";
	
	@Override
	public void onCreate() {
		mLocationClient = new LocationClient( this );
		
		super.onCreate(); 
		Log.d(TAG, "... Application onCreate... pid=" + Process.myPid());
	}
	
	
}