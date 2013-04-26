package com.tao8.weather;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.tao8.weather.domain.CityCode;
import com.tao8.weather.domain.CityCodeDao;

import android.test.AndroidTestCase;
import android.widget.MultiAutoCompleteTextView.CommaTokenizer;
import android.widget.Toast;

public class DbTest extends AndroidTestCase{

	public void TestAdd() throws Exception{
	CityCodeDao codeDao = new CityCodeDao(getContext());
	codeDao.addAllCityCode();
	}
	public void Testquery() throws Exception{
		CityCodeDao codeDao = new CityCodeDao(getContext());
		CityCode cityCodeByCityName = codeDao.getCityCodeByCityName("³¯Ñô");
		assertNotNull(cityCodeByCityName);
		assertNotNull(cityCodeByCityName.cityCode);
		System.out.println(cityCodeByCityName.cityCode);
	}
	
	public void TestUrl() throws Exception{
		String urlString ="http://m.weather.com.cn/data/101010400.html";
		/*URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(2000);
		conn.setReadTimeout(2000);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");*/
		HttpGet httpget = new HttpGet(urlString);
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpConnectionParams.setSoTimeout(params, 20000);
		HttpClient httpclient = new DefaultHttpClient(params);
		HttpResponse execute = httpclient.execute(httpget);
		int code = execute.getStatusLine().getStatusCode();
		System.out.println(code);
		if (code==200) {
			InputStream inputStream = execute.getEntity().getContent();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buffer))!=-1) {
				bout.write(buffer,0,len);
			}
			String weatherData = new String(bout.toByteArray());
			System.out.println(weatherData);
		}
	}
}
