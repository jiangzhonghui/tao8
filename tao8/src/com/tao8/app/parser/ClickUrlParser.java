package com.tao8.app.parser;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.tao8.app.domain.ClickUrl;

public class ClickUrlParser extends TopJsonParser<List<ClickUrl>>{

	@Override
	public List<ClickUrl> parserJson(String jsonStr) throws JSONException {
		List<ClickUrl> clickUrls = null;
		if (jsonStr ==null) {
			return null;
		}
		JSONObject jsonObject = new JSONObject(jsonStr);
		JSONObject jsonObject2 = (JSONObject) jsonObject.get("tbk_mobile_items_convert_response");
		JSONObject reJsonObject = (JSONObject) jsonObject2.get("tbk_items");
		JSONArray jsonArray = (JSONArray) reJsonObject.get("tbk_item");
		clickUrls = JSON.parseArray(jsonArray.toString(), ClickUrl.class);
		return clickUrls;
	}

}
