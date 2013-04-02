package com.tao8.app.parser;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tao8.app.AppException;
import com.tao8.app.BuildConfig;
import com.tao8.app.domain.SearchItem;

public class ConvertTaokeItemsParser extends TopJsonParser<List<SearchItem>>{

	private static final String TAG = "ConvertTaokeItemsParser";

	@Override
	public List<SearchItem> parserJson(String jsonStr) {
		List<SearchItem> parseArray = null;
		// TODO Auto-generated method stub
		if (jsonStr==null) {
			return null;
		}
		try {
		JSONObject jsonObject = new JSONObject(jsonStr);
		JSONObject jsonObject2 = (JSONObject) jsonObject.get("taobaoke_widget_items_convert_response");
		JSONObject reJsonObject = (JSONObject) jsonObject2.get("taobaoke_items");
		JSONArray jsonArray = (JSONArray) reJsonObject.get("taobaoke_item");
			parseArray = JSON.parseArray(jsonArray.toString(),SearchItem.class);
		} catch (JSONException e) {
			AppException.json(e);
			if (BuildConfig.DEBUG) {
				Log.e(TAG, e.getMessage(), e);
				
			}
		}
		return parseArray;
	}

}
