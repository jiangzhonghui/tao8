package com.tao8.app.ws;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class NumberService {

	public String getLocation(String number) throws Exception {
		// 读取本地准备好的文件, 用输入的号码替换原来的占位符
		
				InputStream in = NumberService.class.getClassLoader().getResourceAsStream("send.xml");
				byte[] data = StreamUtil.load(in);
				String content = new String(data);
				content = content.replace("$number", number);
				
				// 创建连接对象, 设置请求头, 按照Webservice服务端提供的要求来设置
				URL url = new URL("http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestProperty("Host", "webservice.webxml.com.cn");
				conn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
				conn.setRequestProperty("Content-Length", content.getBytes().length + "");
				conn.setRequestMethod("POST");
				
				// 输出数据
				conn.setDoOutput(true);
				conn.getOutputStream().write(content.getBytes());
			
				
//				// 获取服务端传回的数据, 解析XML, 得到结果
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(conn.getInputStream(), "UTF-8");
				
				for (int type = parser.getEventType();type!=XmlPullParser.END_DOCUMENT;type=parser.next()) 
				  if(type==XmlPullParser.START_TAG&&parser.getName().equals("getMobileCodeInfoResult")){
					  return parser.nextText();
				  }
				return "没有找到此号码";
	}

}
