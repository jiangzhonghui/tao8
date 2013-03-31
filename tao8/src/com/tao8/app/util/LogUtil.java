package com.tao8.app.util;

import android.util.Log;

public class LogUtil {
	public static final int LEVEL = 6;
	public static void i(String tag,String msg){
		if (LEVEL>3) {
			Log.i(tag, msg);
		}
	}
	public static void i(String tag,String msg,Throwable tr){
		if (LEVEL>3) {
			Log.i(tag, msg,tr);
		}
	}
	public static void w(String tag,String msg){
		if (LEVEL>4) {
			Log.w(tag, msg);
		}
	}
	public static void w(String tag,String msg,Throwable tr){
		if (LEVEL>4) {
			Log.w(tag, msg,tr);
		}
	}
	public static void e(String tag,String msg){
		if (LEVEL>5) {
			Log.e(tag, msg);
		}
	}
	public static void e(String tag,String msg,Throwable tr){
		if (LEVEL>5) {
			Log.e(tag, msg,tr);
		}
	}
	public static void v(String tag,String msg){
		if (LEVEL>1) {
			Log.v(tag, msg);
		}
	}
	public static void v(String tag,String msg,Throwable tr){
		if (LEVEL>1) {
			Log.v(tag, msg,tr);
		}
	}
	public static void d(String tag,String msg){
		if (LEVEL>2) {
			Log.d(tag, msg);
		}
	}
	public static void d(String tag,String msg,Throwable tr){
		if (LEVEL>2) {
		Log.d(tag, msg,tr);
		}
	}
}
