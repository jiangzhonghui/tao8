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

public class SearchItemParser extends TopJsonParser<List<SearchItem>> {

	@Override
	public List<SearchItem> parserJson(String jsonStr) {
			List<SearchItem> parseArray = null;
			try {
				if (jsonStr==null) {
					return null;
				}
				//{"error_response":{"code":41,"msg":"Invalid arguments:cid"}}
				
				JSONObject jsonObject = new JSONObject(jsonStr);
				JSONObject jsonObject2 = (JSONObject) jsonObject.get("tbk_items_get_response");
				JSONObject reJsonObject = (JSONObject) jsonObject2.get("tbk_items");
				JSONArray jsonArray = (JSONArray) reJsonObject.get("tbk_item");
				parseArray = JSON.parseArray(jsonArray.toString(),SearchItem.class);
			} catch (JSONException e) {
				AppException.json(e);
				if (BuildConfig.DEBUG) {
					Log.e(LOG_TAG, e.getLocalizedMessage(),e);
				}
			}
			return parseArray;
		}
	}

