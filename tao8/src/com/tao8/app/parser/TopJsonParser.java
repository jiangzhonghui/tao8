package com.tao8.app.parser;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopApiListener;

public abstract  class TopJsonParser<T> implements ITopJsonParser<T> {

	public  Object handleApiResponse(String jsonStr)
			throws JSONException {
		JSONObject json = new JSONObject(jsonStr);
		ApiError error = parseError(json);
		if (error != null) {// failed
			Log.e(LOG_TAG, jsonStr);
			return error;
		} else {
			return parserJson(jsonStr);
		}
	}
	
	public ApiError parseError(JSONObject json) throws JSONException {
		JSONObject resp = json.optJSONObject("error_response");
		if (resp == null) {
			return null;
		}
		String code = resp.optString("code");
		String msg = resp.optString("msg");
		String sub_code = resp.optString("sub_code");
		String sub_msg = resp.optString("sub_msg");
		ApiError error = null;
		if (!TextUtils.isEmpty(code) || !TextUtils.isEmpty(sub_code)) {
			error = new ApiError();
			error.setErrorCode(code);
			error.setMsg(msg);
			error.setSubCode(sub_code);
			error.setSubMsg(sub_msg);
		}
		return error;
	}
}
