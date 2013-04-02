package com.tao8.app.domain;

import com.tao8.app.api.MyTqlListener;

public class ApiResponseVo {
	public  MyTqlListener listener;
	/**标识信息*/
	public  int typeCode;
	/**处理的结果*/
	public  Object resultValue;
}
