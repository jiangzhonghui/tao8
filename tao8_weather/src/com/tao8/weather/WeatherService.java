package com.tao8.weather;

import java.io.IOException;
import java.util.Date;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.tao8.weather.domain.CityCode;
import com.tao8.weather.domain.CityCodeDao;

public class WeatherService extends Service {
	private LocationClient mLocClient;
	private WeatherAsyncTask weatherAsyncTask;
	private String address;
	public MyLocationListenner myListener = new MyLocationListenner();
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (weatherAsyncTask==null) {
					weatherAsyncTask = weatherAsyncTask = new WeatherAsyncTask(WeatherService.this,msg.obj.toString(),preferences);
				}
				Long getWeatherTime  = preferences.getLong("getWeatherTime", 0L);
				//NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				weatherAsyncTask.execute(null);
				break;
			default:
				break;
			}
		};
	};
	private SharedPreferences preferences;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (BuildConfig.DEBUG) {
			System.out.println("...........onCreate.....................");
			Toast.makeText(getApplicationContext(), "oncreate", 1).show();
		}
		preferences = getSharedPreferences("config",Context.MODE_PRIVATE);
		((Location)getApplication()).mLocationClient.registerLocationListener( myListener );
		mLocClient = ((Location)getApplication()).mLocationClient;
		setLocationOption();
		mLocClient.start();
		new Thread(){
			public void run() {
				CityCodeDao codeDao = new CityCodeDao(getApplicationContext());
				CityCode cityCodeByCityName = codeDao.getCityCodeByCityName("朝阳");
				if (cityCodeByCityName==null) {
					try {
						codeDao.addAllCityCode();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
//		do {
//			SystemClock.sleep(3000);
//			System.out.println("mLocClient.requestLocation();");
//			mLocClient.requestLocation();
//		} while (true);
	}
	//设置相关参数
		private void setLocationOption(){
			LocationClientOption option = new LocationClientOption();
			//option.setOpenGps(mGpsCheck.isChecked());				//打开gps
			option.setCoorType("bd09ll");		//设置坐标类型
			option.setServiceName("com.baidu.location.service_v2.9");
			option.setPoiExtraInfo(true);	
			option.setAddrType("all");
			option.setScanSpan(3000*60*60*12);
			option.setPoiNumber(10);
			option.disableCache(true);		
			mLocClient.setLocOption(option);
		}
		/**
		 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
		 */
		public class MyLocationListenner implements BDLocationListener {
			

			private static final String TAG = "MyLocationListenner";
			

			@Override
			public void onReceiveLocation(BDLocation location) {
				System.out.println("onReceiveLocation"+location);
				if (location == null)
					return ;
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(location.getDistrict());
				address = stringBuilder.toString().replace("省", "").replace("市", "").replace("区", "").replace("县", "");
				Message msg = new Message();
				msg.what = 1;
				msg.obj = address;
				handler.sendMessage(msg);
				///////////////
				StringBuffer sb = new StringBuffer(256);
				sb.append("time : ");
				sb.append(location.getTime());
				sb.append("\nerror code : ");
				sb.append(location.getLocType());
				sb.append("\nlatitude : ");
				sb.append(location.getLatitude());
				sb.append("\nlontitude : ");
				sb.append(location.getLongitude());
				sb.append("\nradius : ");
				sb.append(location.getRadius());
				if (location.getLocType() == BDLocation.TypeGpsLocation){
					sb.append("\nspeed : ");
					sb.append(location.getSpeed());
					sb.append("\nsatellite : ");
					sb.append(location.getSatelliteNumber());
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
					sb.append("\n省：");
					sb.append(location.getProvince());
					sb.append("\n市：");
					sb.append(location.getCity());
					sb.append("\n区/县：");
					sb.append(location.getDistrict());
					sb.append("\naddr : ");
					sb.append(location.getAddrStr());
				}
				sb.append("\nsdk version : ");
				sb.append(((Location)getApplication()).mLocationClient.getVersion());
				sb.append("\nisCellChangeFlag : ");
				sb.append(location.isCellChangeFlag());
				if (BuildConfig.DEBUG) {
					System.out.println(sb.toString());
					Log.i(TAG, sb.toString());
				}
			}
			
			public void onReceivePoi(BDLocation poiLocation) {
				if (poiLocation == null){
					return ; 
				}
				StringBuffer sb = new StringBuffer(256);
				sb.append("Poi time : ");
				sb.append(poiLocation.getTime());
				sb.append("\nerror code : "); 
				sb.append(poiLocation.getLocType());
				sb.append("\nlatitude : ");
				sb.append(poiLocation.getLatitude());
				sb.append("\nlontitude : ");
				sb.append(poiLocation.getLongitude());
				sb.append("\nradius : ");
				sb.append(poiLocation.getRadius());
				if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
					sb.append("\naddr : ");
					sb.append(poiLocation.getAddrStr());
				} 
				if(poiLocation.hasPoi()){
					sb.append("\nPoi:");
					sb.append(poiLocation.getPoi());
				}else{				
					sb.append("noPoi information");
				}
			}
		}
		@Override
		public void onDestroy() {
			mLocClient.stop();
			super.onDestroy();
		}
		
		@Override
		public void onStart(Intent intent, int startId) {
			if (BuildConfig.DEBUG) {
				Log.e(WeatherService.class.getName(), "onStart");
			}
			if (!mLocClient.isStarted()) {
				mLocClient.start();
			}
			int i = 3;
			do {
				SystemClock.sleep(1000*2);
				mLocClient.requestLocation();	
				i--;
			} while (i>=0);
			super.onStart(intent, startId);
		}
}
