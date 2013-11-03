package com.tao8.app.api;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tao8.app.AppException;
import com.tao8.app.BuildConfig;
import com.tao8.app.TopConfig;
import com.tao8.app.domain.ApiResponseVo;
import com.tao8.app.parser.ApiErrorParser;
import com.tao8.app.parser.ITopJsonParser;
import com.tao8.app.parser.TopJsonParser;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopTqlListener;

public class GetTopData  {
	/**获取到成功,服务器返回正确的信息*/
	public static final int GET_DATA_CONPLETE_CODE = 1;
	/**获取到信息,服务器返回错误码*/
	public static final int GET_DATA_RESPONSE_EXCEPTION_CODE = 2;
	/**访问服务器错误*/
	public static final int GET_DATA_EXCEPTION_CODE = -1;
	
	private static final String LOG_TAG = "GetTopData";

	public static <T> void getDataFromTop(String tql, TopJsonParser<T> parser,
			Long userId, MyTqlListener listenter) {
		tql(tql, userId, TopConfig.client, parser, listenter);
	}

	public synchronized static <T>  void  tql(String tql, Long userId,
			TopAndroidClient client, final TopJsonParser<T> parser,
			final MyTqlListener listenter) {
		client.tql(tql, userId, new TopTqlListener() {
			@SuppressLint("NewApi")
			@Override
			public void onComplete(String result) {
				if (result!=null&&result.length()>0) {
					handleApiResponse(result, parser,listenter);
				}
			}

			@Override
			public void onException(Exception e) {
				Message obtainMessage = dataHandler.obtainMessage();
				obtainMessage.what = GET_DATA_EXCEPTION_CODE;
				ApiResponseVo vo = new ApiResponseVo();
				vo.listener = listenter;
				vo.resultValue = e;
				vo.typeCode = GET_DATA_EXCEPTION_CODE;
				obtainMessage.obj = vo;
				dataHandler.sendMessage(obtainMessage);
			}
		}, true);
	}

	public static <T> void handleApiResponse(String jsonStr, ITopJsonParser<T> parser,MyTqlListener listenter)
			{
		ApiErrorParser apiErrorParser = new ApiErrorParser();
		ApiError error = apiErrorParser.parserJson(jsonStr);
		if (error != null) {// failed
			if (BuildConfig.DEBUG) {
				Log.e(LOG_TAG, jsonStr);
			}
			Message obtainMessage = dataHandler.obtainMessage();
			obtainMessage.what = GET_DATA_RESPONSE_EXCEPTION_CODE;
			ApiResponseVo vo = new ApiResponseVo();
			vo.listener = listenter;
			vo.resultValue = error;
			vo.typeCode = GET_DATA_RESPONSE_EXCEPTION_CODE;
			obtainMessage.obj = vo;
			dataHandler.sendMessage(obtainMessage);
		} else {
			Message obtainMessage = dataHandler.obtainMessage();
			obtainMessage.what = GET_DATA_CONPLETE_CODE;
			ApiResponseVo vo = new ApiResponseVo();
			vo.listener = listenter;
			try {
				vo.resultValue = parser.parserJson(jsonStr);
			} catch (JSONException e) {
				AppException.json(e);
			}
			vo.typeCode = GET_DATA_CONPLETE_CODE;
			obtainMessage.obj = vo;
			dataHandler.sendMessage(obtainMessage);
		}
	}
	static Handler dataHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			ApiResponseVo vo = (ApiResponseVo) msg.obj;
			switch (what) {
			case GET_DATA_CONPLETE_CODE:
				vo.listener.onComplete(vo.resultValue);
				break;
			case GET_DATA_RESPONSE_EXCEPTION_CODE:
				vo.listener.onResponseException(vo.resultValue);
				break;
			case GET_DATA_EXCEPTION_CODE:
				vo.listener.onException((Exception)vo.resultValue);
				break;
			default:
				break;
			}
		};
	};
}