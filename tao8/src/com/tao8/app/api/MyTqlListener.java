package com.tao8.app.api;

import android.os.Parcelable;


/**
 * 获取淘宝数据成功或者失败的回调接口(子线程调取的)
 * @author Administrator
 *
 */
public interface MyTqlListener {
	/**结果有可能是两种 1.获取了正确的结果  2.服务端出错返回的结果*/
	public void onComplete(Object result);
	/** 出现网络问题等未知异常时会回调此方法*/
	public void onException(Exception e);
	/**服务器出现错误或者参数等出错,服务器给返回的错误信息,回调的方法*/
	public void onResponseException(Object apiError);
}
