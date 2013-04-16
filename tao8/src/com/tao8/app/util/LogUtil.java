package com.tao8.app.util;

import com.tao8.app.BuildConfig;

import android.util.Log;

public class LogUtil {
	private static final int LEVEL = 6;
	private static final boolean DEBUG = BuildConfig.DEBUG;
	public static void i(String tag,String msg){
		if (LEVEL>3&&DEBUG) {
			Log.i(tag, msg);
		}
	}
	public static void i(String tag,String msg,Throwable tr){
		if (LEVEL>3&&DEBUG) {
			Log.i(tag, msg,tr);
		}
	}
	public static void w(String tag,String msg){
		if (LEVEL>4&&DEBUG) {
			Log.w(tag, msg);
		}
	}
	public static void w(String tag,String msg,Throwable tr){
		if (LEVEL>4&&DEBUG) {
			Log.w(tag, msg,tr);
		}
	}
	public static void e(String tag,String msg){
		if (LEVEL>5&&DEBUG) {
			Log.e(tag, msg);
		}
	}
	public static void e(String tag,String msg,Throwable tr){
		if (LEVEL>5&&DEBUG) {
			Log.e(tag, msg,tr);
		}
	}
	public static void v(String tag,String msg){
		if (LEVEL>1&&DEBUG) {
			Log.v(tag, msg);
		}
	}
	public static void v(String tag,String msg,Throwable tr){
		if (LEVEL>1&&DEBUG) {
			Log.v(tag, msg,tr);
		}
	}
	public static void d(String tag,String msg){
		if (LEVEL>2&&DEBUG) {
			Log.d(tag, msg);
		}
	}
	public static void d(String tag,String msg,Throwable tr){
		if (LEVEL>2&&DEBUG) {
		Log.d(tag, msg,tr);
		}
	}
}
