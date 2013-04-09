package com.tao8.app.util;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.tao8.app.TopConfig;
import com.taobao.top.android.Installation;
import com.taobao.top.android.JNIUtils;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.api.TaobaoUtils;
import com.taobao.top.android.auth.AccessToken;

public class CommonUtil {

	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	public static String getRootFilePath() {
		if (hasSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/";// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath:
																				// /data/data/
		}
	}

	/**
	 * 查看当前网络是否可用
	 * 
	 * @param context
	 * @return 为true，则网络可用
	 */
	public static boolean checkNetState(Context context) {
		boolean netstate = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						netstate = true;
						break;
					}
				}
			}
		}
		return netstate;
	}

	public static void showToask(Context context, String tip) {
		Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
	}

	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	public static int dip2px(Context context, float dipValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static String generateTopClickUri(String click_uri, Context context,
			AccessToken accessToken) {

		StringBuilder sb = new StringBuilder();
		sb.append(click_uri);
		sb.append("&unid=");
		sb.append(Installation.id(context));
		sb.append("&ttid=");
		sb.append(getTTID(context));
		sb.append("&sid=");
		sb.append(accessToken == null ? "" : accessToken
				.getAdditionalInformation().get(AccessToken.KEY_MOBILE_TOKEN));
		// click_uri = click_uri
		// + "&unid="
		// + Installation.id(context)
		// + "&ttid="
		// + getTTID(context)
		// + "&sid="
		// + accessToken==null?"":accessToken.getAdditionalInformation().get(
		// AccessToken.KEY_MOBILE_TOKEN);
		return sb.toString();
	}

	public static String getTTID(Context context) {
		// ttid=400000_12450255@taofen8_android_2.2.1111
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		String ttid = "";
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			String version = packInfo.versionName;
			ttid = "400000" + "_" + TopConfig.APPKEY + "@淘8" + "_" + "android"
					+ "_" + packInfo.versionName;
			return ttid;
		} catch (NameNotFoundException e) {
			LogUtil.e("NameNotFoundException", e.getLocalizedMessage(), e);
		}
		return ttid;

	}
}
