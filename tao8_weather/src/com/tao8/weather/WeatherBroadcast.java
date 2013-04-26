package com.tao8.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class WeatherBroadcast extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intentService = new Intent();
		intentService.setClass(context, WeatherService.class);
		intentService.setAction("com.tao8.weather.WeatherService");
		String action = intent.getAction();
		Toast.makeText(context, action, 0).show();
		if (BuildConfig.DEBUG) {
			Toast.makeText(context, action, 0).show();
		}
		if (action.equalsIgnoreCase(Intent.ACTION_SCREEN_OFF)) {
			context.stopService(intentService);
		}else {
			context.startService(intentService);
		}
	}
}
