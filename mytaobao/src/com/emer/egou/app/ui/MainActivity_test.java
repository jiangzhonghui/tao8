package com.emer.egou.app.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.emer.egou.app.BuildConfig;
import com.emer.egou.app.R;
import com.emer.egou.app.TopConfig;
import com.emer.egou.app.api.GetTopData;
import com.emer.egou.app.api.MyTqlListener;
import com.emer.egou.app.domain.SearchItem;
import com.emer.egou.app.parser.SearchItemParser;
import com.emer.egou.app.parser.TmallToTaokeItemParser;
import com.emer.egou.app.util.Md5Util;
import com.emer.egou.app.util.TqlHelper;
import com.taobao.top.android.Installation;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.TopParameters;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopApiListener;
import com.taobao.top.android.auth.AccessToken;
import com.taobao.top.android.auth.AuthActivity;
import com.taobao.top.android.auth.AuthError;
import com.taobao.top.android.auth.AuthException;
import com.taobao.top.android.auth.AuthorizeListener;

public class MainActivity_test extends AuthActivity {
	protected static final String TAG = "ActivityActivity";
	private TopAndroidClient client = TopConfig.client;
	protected Long userId;
	private String clickString;
	private  ArrayList resultList;
	private TextView topResult;
	private AccessToken accessToken;
	private WebView webview;
	private SharedPreferences sharedPreferences;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity);
		
		String deviceId = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		Log.e(TAG, Secure.ANDROID_ID+"  "+deviceId);
		
		Button apibtn = (Button) this.findViewById(R.id.button2);

		apibtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("button2");
				Intent intent  =  new Intent();
				intent.setClass(MainActivity_test.this, MainActivity.class);
				startActivity(intent);
				
				
/*				TopParameters params = new TopParameters();
				
				 * CountDownLatch CyclicBarrier;
				 z

				params.setMethod("taobao.user.buyer.get");
				params.addFields("nick", "sex", "buyer_credit");
				if (userId == null) {
					Toast t = Toast.makeText(MainActivity.this, "请先授权",
							Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				client.api(params, userId, new TopApiListener() {

					@Override
					public void onComplete(JSONObject json) {
						Log.e(TAG, json.toString());
						Toast t = Toast.makeText(MainActivity.this,
								json.toString(), Toast.LENGTH_SHORT);
						t.show();
						setTopResultText(json.toString());
					}

					@Override
					public void onError(ApiError error) {
						Log.e(TAG, error.toString());
						Toast t = Toast.makeText(MainActivity.this,
								error.toString(), Toast.LENGTH_SHORT);
						t.show();
						setTopResultText(error.toString());
					}

					@Override
					public void onException(Exception e) {
						setTopResultText(e.getMessage());

					}
				}, false);*/
			}
		});
		TextView tv = (TextView) findViewById(R.id.tv);
		topResult = (TextView) findViewById(R.id.textView1);
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				client.authorize(MainActivity_test.this);

			}
		});
		
		Button button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("resultList", resultList);
				intent.putExtra("data", bundle);
				intent.setClass(MainActivity_test.this, ProductsActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		Button apiButton = (Button) findViewById(R.id.api);

		apiButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					Log.e(TAG, Md5Util.EncoderPwdByMd5("123"));
					Log.i(TAG, Installation.id(MainActivity_test.this));
				
				TopParameters params = new TopParameters();
				params.setMethod("taobao.favorite.search");
				// params.addFields("nick","sex","buyer_credit");
				params.addParam("collect_type", "SHOP");
				params.addParam("page_no", "2");

				if (userId == null) {
					Toast t = Toast.makeText(MainActivity_test.this, "请先授权",
							Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				client.api(params, userId, new TopApiListener() {
					@Override
					public void onException(Exception e) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), e.getMessage(), 0).show();
					}

					@Override
					public void onError(ApiError error) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), error.toString(), 0).show();
					}

					@Override
					public void onComplete(JSONObject json) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "json  " + json, 0).show();
						Log.e(TAG, "json  " + json);
					}
				}, false);
			}
		});

		Button tql = (Button) this.findViewById(R.id.button3);
		tql.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
				userId = sharedPreferences.getLong("userId", 0l);
				client.getAccessToken(userId);
				
				String tql = "";
				if (userId == null||userId==0l) {
					Toast t = Toast.makeText(MainActivity_test.this, "请先授权",
							Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				
				/*Map<String, String> params = new HashMap<String, String>();
				params.put("keyword", "简佰格 春季新款女包 大牌气质 欧美英伦复古包 单肩手提斜挎包包");
				params.put("page_size", "10");
				tql = TqlHelper.generateTaoBaoKeTql(SearchItem.class, params);
				System.out.println(tql);
				GetTopData.getDataFromTop(tql, new SearchItemParser(), userId, new MyTqlListener(){
					@Override
					public void onComplete(Object result) {
						// TODO Auto-generated method stub
						if (BuildConfig.DEBUG) {
							resultList = (ArrayList) result;
							Log.i(TAG, Integer.toString(resultList.size()));
						}
					}
					@Override
					public void onException(Exception e) {
						// TODO Auto-generated method stub
						if (BuildConfig.DEBUG) {
							Log.i(TAG, e.getMessage());
						}
					}

					@Override
					public void onResponseException(Object apiError) {
						// TODO Auto-generated method stub
						if (BuildConfig.DEBUG) {
							Log.i(TAG, apiError.toString());
						}
					}
				});*/
				
				/**
				 * select num_iid,title,click_url from taobao.taobaoke.widget.items.convert
				 * where num_iids in (select item_id from tmall.items.discount.search where q = %e6%89%8b%e6%9c%ba ) 
				 */
				List<String> fields = new ArrayList<String>();
				fields.add("item_id");
				Map<String, String> params = new HashMap<String, String>();
				params.put("q", "%e6%89%8b%e6%9c%ba");
				params.put("page_size", "10");
				String tmallTql = TqlHelper.generateTMallTql(fields, params);
				tql = TqlHelper.generateTMallConvertToTaoKenestTql(SearchItem.class, "num_iids", tmallTql);
				System.out.println(tql);
				GetTopData.getDataFromTop(tql, new TmallToTaokeItemParser(), userId, new MyTqlListener() {
					
					@Override 
					public void onResponseException(Object apiError) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onException(Exception e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onComplete(Object result) {
						// TODO Auto-generated method stub
						resultList = (ArrayList) result;
						if (result!=null&&resultList.size()>0) {
							
						}else {
							
						}
						if (BuildConfig.DEBUG) {
							Log.i(TAG, Integer.toString(resultList.size()));
						}
					}
				});
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				

				// String
				// ql="select num,price,type,stuff_status,title from item where num_iid=1500009020262";
			/*	try {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(new FileInputStream(new File(
									Environment.getExternalStorageDirectory(),
									"tql.ql"))));
					tql = bufferedReader.readLine();
					System.out.println(tql);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// String
				// ql="select num_iid,title,nick,pic_url,price,click_url from taobaoke.items where nick = 优plus and cid=0";
				// String ql="select detail_url from item where nick =优plus";
				// String
				// ql="select num_iid,titile from item where num_iid in (select num_iid from items where nick = 优plus)";
				client.tql(tql, userId, new TopTqlListener() {
					@Override
					public void onComplete(String result) {
						try {
							JSONObject jsonObject = new JSONObject(result);
							
							JSONObject jsonObject2 = (JSONObject) jsonObject
									.get("taobaoke_items_coupon_get_response");//taobaoke_items_get_response
							JSONObject reJsonObject = (JSONObject) jsonObject2
									.get("taobaoke_items");
							JSONArray jsonArray = (JSONArray) reJsonObject
									.get("taobaoke_item");
							List<SearchItem> parseArray = JSON.parseArray(jsonArray.toString(),SearchItem.class);
							for (SearchItem searchResult : parseArray) {
								System.out.println("clickString------"
										+ searchResult.getClick_url());
								clickString = searchResult.getClick_url();
							}
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						File file = new File(Environment
								.getExternalStorageDirectory(), "json.txt");
						try {
							OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
									new FileOutputStream(file));
							outputStreamWriter.write(result, 0, result.length());
							outputStreamWriter.flush();
							outputStreamWriter.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {

						}
						setTopResultText(result);

					}

					@Override
					public void onException(Exception e) {
						// TODO Auto-generated method stub

					}
				}, false);*/
			}
		});

		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setTopResultTextVisable();
				System.out.println(System.currentTimeMillis());
				// Uri uri =
				// Uri.parse("http://s.click.taobao.com/t?e=zGU34CA7K%2BPkqB07S4%2FK0CITy7klxn%2F7bvn0ay1PWAmnFSfWsIaUy2NJzN3hhLYWgGJefMnzfe3x5H8M0LJ7FLgoO9T0Hq7GBG%2FuhKEi9rNLBVvv7rZBZdxDA9wDXnVOvM%2F1g39%2B%2BQiALq4WRbTxdeUKUOAMvLCynFCsVTeY0Xw00N9uMzuY%2Bm23zigeta8%3D&spm=2014.21362041.2.0");
				/*
				 * Uri uri = Uri.parse(
				 * "http://s.click.taobao.com/t?e=zGU34CA7K%2BPkqB07S4%2FK0CITy7klxn%2F7bvn0ay1PV4E1%2BSvqtX4kWh1Ni86zRm5mYDgCwhtgVaWzmhjywjGWBnIJLXJ416lh%2Fu4EgIeAG%2BbEVve6qnxca%2FTNz67fXqYcfiVZd0BslEhJXn%2FbiM5pPPq%2BE3ECotNUWDvMGh8CVrHAq8A%2FtTDrB%2FDhxHCONg%3D%3D&spm=2014.21362041.2.0"
				 * ); Intent it = new Intent(Intent.ACTION_VIEW, uri); try {
				 * ComponentName name = new ComponentName("com.android.browser",
				 * "com.android.browser.BrowserActivity"); // 判断系统自带浏览器是否安装
				 * ActivityActivity
				 * .this.getPackageManager().getActivityInfo(name,
				 * PackageManager.GET_INTENT_FILTERS); it.setComponent(name); }
				 * catch (Exception e) {
				 * 
				 * 
				 * Log.e(TAG, e.getMessage(), e); }
				 * ActivityActivity.this.startActivity(it);
				 */

				// setTopResultTextVisable();
				sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
				userId = sharedPreferences.getLong("userId", 0l);
				//accessToken = client.getAccessToken(userId);
				
				String tql = "";
				if (userId == null||userId==0l) {
					Toast t = Toast.makeText(MainActivity_test.this, "请先授权",
							Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				/*Intent it = new Intent();
				it.setAction(BrowserActivity.BROWSER_ACTION);
				String uri = clickString
						+ "&unid=0OVOaV2GWly3111"
						+ "&ttid="
						+ getTTID()
						+ "&sid="
						+ accessToken.getAdditionalInformation().get(
								AccessToken.KEY_MOBILE_TOKEN);
				it.putExtra("uri", uri);
				Toast.makeText(MainActivity.this, accessToken.getValue(), 1).show();
				MainActivity.this.startActivity(it);
				*/
				
				webview = (WebView) MainActivity_test.this
						.findViewById(R.id.web);
				WebSettings webSettings = webview.getSettings();
				webSettings.setJavaScriptEnabled(true);
				CookieSyncManager.createInstance(MainActivity_test.this);  
			    CookieManager cookieManager = CookieManager.getInstance();  
			    cookieManager.setAcceptCookie(true); 
				webview.setWebChromeClient(new WebChromeClient() {
					public void onProgressChanged(WebView view, int progress) {
						System.out.println(System.currentTimeMillis());
						// Activities and WebViews measure progress with
						// different scales.
						// The progress meter will automatically disappear when
						// we reach 100%
						// activity.setProgress(progress * 1000);
						System.out.println("view.getUrl() " + view.getUrl());
						System.out.println(progress);

					}

				});
				webview.setWebViewClient(new WebViewClient() {
					public void onReceivedError(WebView view, int errorCode,
							String description, String failingUrl) {
						Toast.makeText(MainActivity_test.this,
								"Oh no! " + description, Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public boolean shouldOverrideUrlLoading(WebView view,
							String url) {
						System.out.println(System.currentTimeMillis());
						System.out.println("url " + url);
						if (url.startsWith("http://a.m.tmall.com/i")) {
							 // Direct use of Pattern:
							 Pattern p = Pattern.compile("http://a.m.tmall.com/i(\\d+)");
							 Matcher m = p.matcher(url);
							 while (m.find()) { // Find each match in turn; String can't do this.
							     String name = m.group(1); // Access a submatch group; String can't do this.
							     Toast.makeText(getApplicationContext(), name, 1).show();
							 }
						}else if (url.startsWith("http://a.m.taobao.com/i")) {
							// Direct use of Pattern:
							 Pattern p = Pattern.compile("http://a.m.taobao.com/i(\\d+)");
							 Matcher m = p.matcher(url);
							 while (m.find()) { // Find each match in turn; String can't do this.
							     String name = m.group(1); // Access a submatch group; String can't do this.
							     Toast.makeText(getApplicationContext(), name, 1).show();
							 }
						} else {
							view.loadUrl(url);
						}
						return true;
					}
				});
				String topUri = "http://s.click.taobao.com/t?e=zGU0g6e1d7xnyW5i7tVTF34AiQ6j288BlhA7tjRixKJucBJpXX%2B8hOyAfFfHEl%2FRpMov3g6bOdr9XRaoaxGA%2FGwLOrVLEKNayWt%2B3Nafi2EvFGDelXDDVxJhk3C%2BLtuz60ZPzr8NU9jOMD5vxE2w8%2F8ZlkqpAlnFW7LqS1CRQ3AcdArQm9SETl%2FWqxSH&spm=2014.21362041.1.0";
				String myCollection = "http://fav.m.taobao.com/my_collect_list.htm?sid=";
				String order = "http://m.taobao.com/trade/bought_item_lists.htm?sid=";
				String car = "http://cart.m.taobao.com/my_cart.htm?sid=";
				String tm = "http://tm.m.taobao.com/order_list.htm?statusId=5&sid=";//加上statusId=5的时候就引导到物流信息
				//String car = "http://d.m.taobao.com/my_bag.htm?sid=";
				//String tm = "http://tm.m.taobao.com/order_list.htm?sid=";//到了最近三个月的订单列表
				String collectionUri = tm
						+ accessToken.getAdditionalInformation().get(
								AccessToken.KEY_MOBILE_TOKEN);
				// &unid=0OVOaV2GWly3111&ttid=400000_12450255@taofen8_android_2.2.1111&sid=a34740c950396ea73bcbb733125c4fde111
				System.out.println("AccessToken.KEY_MOBILE_TOKEN "
						+ accessToken.getAdditionalInformation().get(
								AccessToken.KEY_MOBILE_TOKEN));
				String uri = clickString
						+ "&unid=0OVOaV2GWly3111"
						+ "&ttid="
						+ getTTID()
						+ "&sid="
						+ accessToken==null?"":accessToken.getAdditionalInformation().get(
								AccessToken.KEY_MOBILE_TOKEN);
				System.out.println(uri);
				webview.loadUrl(uri);

				// webview.loadUrl("http://s.click.taobao.com/t?e=zGU0g6e1d7xnyW5i7tVTF34AiQ6j288BlhA7tjRixKJp2xwDgutI%2FETp4pLgNoPxf14m%2BN2Xk1XPPnaikGssKKi2CF3VVeBRsBMPuuO4gSWrMqJbdtt7bQekKqRJV8yd5zNgi1cBcCgO%2Bb6zzdOhDaNP03z9l6cz7vfKNV8%2FPOLdm7nRdrbgvtsS9tHX&spm=2014.21362041.2.0&sid=a34740c950396ea73bcbb733125c4fde111&unid=0OVOaV2GWly3111&ttid=400000_12450255@taofen8_android_2.2.1111");
				// webview.loadUrl("http://s.click.taobao.com/t?e=zGU0g6e1d7xnyW5i7tVTF34AiQ6j288BlhA7tjRixKJucBJpXX%2B8hOyAfFfHEl%2FRpMov3g6bOdr9XRaoaxGA%2FGwLOrVLEKNayWt%2B3Nafi2EvFGDelXDDVxJhk3C%2BLtuz60ZPzr8NU9jOMD5vxE2w8%2F8ZlkqpAlnFW7LqS1CRQ3AcdArQm9SETl%2FWqxSH&spm=2014.21362041.1.0&unid=0OVOaV2GWly3111&ttid=400000_12450255@taofen8_android_2.2.1111&sid=a34740c950396ea73bcbb733125c4fde111");
				// webview.loadUrl("http://s.click.taobao.com/");//t?e=zGU34CA7K%2BPkqB07S4%2FK0CITy7klxn%2F7bvn0ay1PWAmnFSfWsIaUy2NJzN3hhLYWgGJefMnzfe3x5H8M0LJ7FLgoO9T0Hq7GBG%2FuhKEi9rNLBVvv7rZBZdxDA9wDXnVOvM%2F1g39%2B%2BQiALq4WRbTxdeUKUOAMvLCynFCsVTeY0Xw00N9uMzuY%2Bm23zigeta8%3D&spm=2014.21362041.2.0");
				// webview.loadUrl("http://s.click.taobao.com/t? e=zGU0g6e1d7xnyW5i7tVTF34AiQ6j288BlhA7tjRixKXb1xYXYCt5mkA0H5A%2Fa1sftom9ob7C2F5zlMzMOZCL%2Fh01QePA34dQVFNSpc3c39hJqWYOwOAZbzBFd6iumQAXleexN3EpQwLmDI0XrXOxu1%2Ftu1yEAOuajxDBro5U%2BFbP9iSOpX2yoy1kcBN7&unid=0OVOaV2GWly3&spm=2014.21219519.1.0&ttid=400000_12450255@taofen8_android_2.2.1&sid=a34740c950396ea73bcbb733125c4fde");

				// webview.loadUrl(url, extraHeaders)
			}
		});

	}
/*	public static void synCookies(Context context, String url) {  
	    CookieSyncManager.createInstance(context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.setAcceptCookie(true);  
	    cookieManager.removeSessionCookie();//移除  
	    cookieManager.setCookie(url, cookies);//cookies是在HttpClient中获得的cookie  
	    CookieSyncManager.getInstance().sync();  
	}*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			if (webview!=null) {
				webview.goBack();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private void setTopResultText(final String ret) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				topResult.setText(ret);
			}
		});
	}

	public String getTTID() {
		// ttid=400000_12450255@taofen8_android_2.2.1111
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		String ttid = "";
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			String version = packInfo.versionName;
			ttid = "400000" + "_" + TopConfig.APPKEY + "@淘8" + "_" + "android"
					+ "_" + packInfo.versionName;
			return ttid;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ttid;

	}

	private void setTopResultTextVisable() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				topResult.setVisibility(View.GONE);
			}
		});
	}

	@Override
	protected TopAndroidClient getTopAndroidClient() {
		// TODO Auto-generated method stub
		return client;
	}

	@Override
	protected AuthorizeListener getAuthorizeListener() {
		// TODO Auto-generated method stub
		return new AuthorizeListener() {
			private String nick;
			
			@Override
			public void onComplete(AccessToken accessToken) {
				MainActivity_test.this.accessToken = accessToken;
				Log.d(TAG, "callback");
				Toast.makeText(MainActivity_test.this, "accessToken", 0).show();
				
				String id = accessToken.getAdditionalInformation().get(
						AccessToken.KEY_SUB_TAOBAO_USER_ID);
				if (id == null) {
					id = accessToken.getAdditionalInformation().get(
							AccessToken.KEY_TAOBAO_USER_ID);
					System.out.println("topResult id " + id);
				}
				
				TopConfig.userId = MainActivity_test.this.userId = Long.parseLong(id);
				sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
				Editor edit = sharedPreferences.edit();
				edit.putLong("userId", userId);
				edit.commit();
				
				nick = accessToken.getAdditionalInformation().get(
						AccessToken.KEY_SUB_TAOBAO_USER_NICK);
				if (nick == null) {
					nick = accessToken.getAdditionalInformation().get(
							AccessToken.KEY_TAOBAO_USER_NICK);
				}
				String r2_expires = accessToken.getAdditionalInformation().get(
						AccessToken.KEY_R2_EXPIRES_IN);
				Date start = accessToken.getStartDate();
				Date end = new Date(start.getTime()
						+ Long.parseLong(r2_expires) * 1000L);
			
			}

			@Override
			public void onError(AuthError e) {
				Log.e(TAG, e.getErrorDescription());

			}

			@Override
			public void onAuthException(AuthException e) {
				Log.e(TAG, e.getMessage());

			}
		};

	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "onOptionsItemSelected");
			}
			Intent intent = new Intent(MainActivity_test.this, AccountActivity.class);
			startActivity(intent);
			
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
