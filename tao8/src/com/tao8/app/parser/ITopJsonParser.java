package com.tao8.app.parser;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopApiListener;


public interface  ITopJsonParser<T> {
	static final String LOG_TAG = "ITopJsonParser";
	public T parserJson(String jsonStr)throws JSONException;
}
