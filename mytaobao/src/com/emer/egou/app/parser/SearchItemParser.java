package com.emer.egou.app.parser;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.emer.egou.app.AppException;
import com.emer.egou.app.BuildConfig;
import com.emer.egou.app.domain.SearchItem;

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
				JSONObject jsonObject2 = (JSONObject) jsonObject.get("taobaoke_items_get_response");
				JSONObject reJsonObject = (JSONObject) jsonObject2.get("taobaoke_items");
				JSONArray jsonArray = (JSONArray) reJsonObject.get("taobaoke_item");
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

