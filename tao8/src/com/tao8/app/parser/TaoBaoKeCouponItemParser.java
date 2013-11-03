//package com.tao8.app.parser;
//
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.util.Log;
//
//import com.alibaba.fastjson.JSON;
//import com.tao8.app.AppException;
//import com.tao8.app.BuildConfig;
//import com.tao8.app.domain.SearchItem;
//import com.tao8.app.domain.TaobaokeCouponItem;
//
//public class TaoBaoKeCouponItemParser extends TopJsonParser<List<TaobaokeCouponItem>>{
//
//	@Override
//	public List<TaobaokeCouponItem> parserJson(String jsonStr)
//			throws JSONException {
//		List<TaobaokeCouponItem> parseArray = null;
//		try {
//			if (jsonStr==null) {
//				return null;
//			}
//			//{"error_response":{"code":41,"msg":"Invalid arguments:cid"}}
//			
//			JSONObject jsonObject = new JSONObject(jsonStr);
//			JSONObject jsonObject2 = (JSONObject) jsonObject.get("taobaoke_items_coupon_get_response");
//			JSONObject reJsonObject = (JSONObject) jsonObject2.get("taobaoke_items");
//			JSONArray jsonArray = (JSONArray) reJsonObject.get("taobaoke_item");
//			parseArray = JSON.parseArray(jsonArray.toString(),TaobaokeCouponItem.class);
//		} catch (JSONException e) {
//			AppException.json(e);
//			if (BuildConfig.DEBUG) {
//				Log.e(LOG_TAG, e.getLocalizedMessage(),e);
//			}
//		}
//		return parseArray;
//	}
//
//}
