package com.tao8.weather;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.tao8.weather.domain.CityCode;
import com.tao8.weather.domain.CityCodeDao;
import com.tao8.weather.domain.Weather;

public final class WeatherAsyncTask extends AsyncTask<Void, Void, Weather> {
	private Context context;
	private String name;
	SharedPreferences sPreferences;

	public WeatherAsyncTask(Context context, String name,
			SharedPreferences sPreferences) {
		this.context = context;
		if (name == null) {
			throw new NullPointerException("name 地址信息不能为空!");
		}
		this.name = name;
		this.sPreferences = sPreferences;
	}

	@Override
	protected Weather doInBackground(Void... params) {
		Weather weather = getWeather();
		return weather;
	}

	@Override
	protected void onPostExecute(Weather weather) {
		if (weather != null) {
			// ///////////////
			Notification notification = new Notification();
			notification.tickerText = weather.weather1;
			notification.icon = R.drawable.weather;
			notification.flags = Notification.FLAG_NO_CLEAR;

			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					1, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, weather.weather1,
					weather.city, pendingIntent);
			notification.contentView = new RemoteViews(
					context.getPackageName(), R.layout.notifition);
			// notification.contentView.setImageViewBitmap(R.id.notifition_iv_today,new
			// BitmapDrawable(getAssets().open("d"+weather)));
			// notification.contentView.setImageViewBitmap(R.id.notifition_iv_tomorrow,
			// bitmap);
			notification.contentView.setTextViewText(R.id.notifition_tv_today,
					weather.city + " 今日天气: " + weather.weather1 + " "
							+ weather.temp1 + " " + weather.wind1);
			notification.contentView.setTextViewText(
					R.id.notifition_tv_tomorrow, weather.city + " 明日天气: "
							+ weather.weather2 + " " + weather.temp2 + " "
							+ weather.wind2);
			mNotificationManager.notify(1, notification);
		}
		super.onPostExecute(weather);
	}

	private Weather getWeather() {
		Weather weather = null;
		try {
			CityCode cityCode = new CityCodeDao(context)
					.getCityCodeByCityName(name);
			if (cityCode != null && cityCode.cityCode != null) {

				String urlString = "http://m.weather.com.cn/data/"
						+ cityCode.cityCode + ".html";
				HttpGet httpget = new HttpGet(urlString);
				HttpParams params = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(params, 10000);
				HttpConnectionParams.setSoTimeout(params, 20000);
				HttpClient httpclient = new DefaultHttpClient(params);
				HttpResponse execute = httpclient.execute(httpget);
				int code = execute.getStatusLine().getStatusCode();
				System.out.println(code);
				if (code == 200) {
					String weatherData = EntityUtils.toString(execute.getEntity(),"UTF-8");
//					InputStream inputStream = execute.getEntity().getContent();
//					ByteArrayOutputStream bout = new ByteArrayOutputStream();
//					byte[] buffer = new byte[1024];
//					int len = 0;
//					while ((len = inputStream.read(buffer)) != -1) {
//						bout.write(buffer, 0, len);
//					}
//					String weatherData = new String(bout.toByteArray(),"UTF-8");
					if (BuildConfig.DEBUG) {
						System.out.println(weatherData);
					}
					if (!TextUtils.isEmpty(weatherData)) {
						JSONObject object = new JSONObject(weatherData);
						JSONObject jsonObject = object
								.getJSONObject("weatherinfo");
						weather = new Weather();
						Field[] fields = Weather.class.getDeclaredFields();
						for (Field field : fields) {
							if ("serialVersionUID".equalsIgnoreCase(field
									.getName())) {
								continue;
							}
							String fieldName = field.getName();
							String string = jsonObject.getString(fieldName);
							field.setAccessible(true);
							field.set(weather, string);
						}

						Editor edit = sPreferences.edit();
						edit.putLong("getWeatherTime", new Date().getTime());
						edit.commit();

					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return weather;
	}
}