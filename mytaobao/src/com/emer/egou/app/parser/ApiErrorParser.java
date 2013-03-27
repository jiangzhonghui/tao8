package com.emer.egou.app.parser;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.emer.egou.app.AppException;
import com.emer.egou.app.BuildConfig;
import com.taobao.top.android.api.ApiError;

public class ApiErrorParser implements ITopJsonParser<ApiError>{

	@Override
	public ApiError parserJson(String jsonStr) {
		JSONObject json;
		ApiError error = null;
		try {
			json = new JSONObject(jsonStr);
			JSONObject resp = json.optJSONObject("error_response");
			if (resp == null) {
				return null;
			}
			String code = resp.optString("code");
			String msg = resp.optString("msg");
			String sub_code = resp.optString("sub_code");
			String sub_msg = resp.optString("sub_msg");
			
			if (!TextUtils.isEmpty(code) || !TextUtils.isEmpty(sub_code)) {
				error = new ApiError();
				error.setErrorCode(code);
				error.setMsg(msg);
				error.setSubCode(sub_code);
				error.setSubMsg(sub_msg);
			}
		} catch (JSONException e) {
			AppException.json(e);
			if (BuildConfig.DEBUG) {
				Log.e(LOG_TAG, e.getMessage(),e);
			}
		}
		return error;
	}

}
