package com.tao8.app.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.tao8.app.TopConfig;
import com.taobao.top.android.Installation;

public class TopStatParmUtil {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ttid;
	}
	public static String getDefaultSid(Context context){
		String id = Installation.id(context);
		return Md5Util.EncoderPwdByMd5(id);
	}
}