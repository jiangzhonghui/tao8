package com.tao8.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.slidingmenu.lib.SlidingMenu;
import com.tao8.app.AppException;
import com.tao8.app.BuildConfig;
import com.tao8.app.R;
import com.tao8.app.TopConfig;
import com.tao8.app.adapter.CouponAdapter;
import com.tao8.app.api.GetTopData;
import com.tao8.app.api.MyTqlListener;
import com.tao8.app.domain.ClickUrl;
import com.tao8.app.domain.SearchItem;
import com.tao8.app.parser.ClickUrlParser;
import com.tao8.app.parser.SearchItemParser;
import com.tao8.app.util.CommonUtil;
import com.tao8.app.util.Config;
import com.tao8.app.util.TqlHelper;
import com.taobao.top.android.TopParameters;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopApiListener;
import com.taobao.top.android.auth.AccessToken;

public class BrowserActivity extends BaseFragmentActivity implements
		OnClickListener {
	public static final String BROWSERACTIVITY_TITLE = "title";
	public static final String BROWSERACTIVITY_ACTION = "com.tao8.app";
	public static final String BROWSERACTIVITY_NUM_IID = "num_iid";
	private WebView webview;
	private String num_iid;
	private ImageButton backButton;
	private TextView lableTextView;
	// private ProgressBar pb;
	private ProgressBar pbIndicate;
	private ImageView freshenButton;
	private ImageView nextButton;
	private ImageView previewButton;
	private ImageView closeButton;
	private SharedPreferences sharedPreferences;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// requestWindowFeature(Window.FEATURE_PROGRESS);// 让进度条显示在标题栏上
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		////////////////防止左滑出菜单
		getSlidingMenu().setTouchModeAbove(
				SlidingMenu.TOUCHMODE_MARGIN);
		setContentView(R.layout.browser);
		///////////////
		// 找到view
		lableTextView = (TextView) findViewById(R.id.head_tv_lable);
		// //////
		freshenButton = (ImageView) findViewById(R.id.browser_im_freshen);
		nextButton = (ImageView) findViewById(R.id.browser_im_next);
		previewButton = (ImageView) findViewById(R.id.browser_im_preview);
		closeButton = (ImageView) findViewById(R.id.browser_im_close);
		nextButton.setEnabled(false);
		previewButton.setEnabled(false);
		nextButton.setOnClickListener(this);
		freshenButton.setOnClickListener(this);
		previewButton.setOnClickListener(this);
		closeButton.setOnClickListener(this);
		// ////
		pbIndicate = (ProgressBar) findViewById(R.id.brower_pb);
		pbIndicate.setMax(100);
		TextView goMenuTextView = (TextView) findViewById(R.id.head_tv_go_menu);
		goMenuTextView.setVisibility(View.GONE);

		ImageView moreImageView = (ImageView) findViewById(R.id.head_iv_more);
		moreImageView.setVisibility(View.GONE);

		webview = (WebView) findViewById(R.id.webview);

		String title = getIntent().getStringExtra(BROWSERACTIVITY_TITLE);
		if (title != null) {
			lableTextView.setText(Html.fromHtml(title));
		}
		String action = getIntent().getAction();
		num_iid = getIntent().getStringExtra(BROWSERACTIVITY_NUM_IID);
		if (action.equalsIgnoreCase(BROWSERACTIVITY_ACTION)) {
			WebSettings webSettings = webview.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setUseWideViewPort(true);
			webview.setWebChromeClient(new WebChromeClient() {
				public void onProgressChanged(WebView view, int progress) {
					// System.out.println(System.currentTimeMillis());
					// // BrowserActivity.this.setProgress(progress * 1000);
					// System.out.println("view.getUrl() " + view.getUrl());
					if (BuildConfig.DEBUG) {
						System.out.println(progress);
					}
					pbIndicate.setProgress(progress);
				}
			});

			webview.setWebViewClient(new WebViewClient() {
				@Override
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					Toast.makeText(BrowserActivity.this,
							"Oh no! " + description, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onPageStarted(WebView view, String url,
						Bitmap favicon) {
					pbIndicate.setVisibility(View.VISIBLE);
					pbIndicate.setProgress(0);
					super.onPageStarted(view, url, favicon);
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					pbIndicate.setProgress(100);
					pbIndicate.setVisibility(View.GONE);
					if (webview.canGoBack()) {
						previewButton.setEnabled(true);
					} else {
						previewButton.setEnabled(false);
					}
					if (webview.canGoForward()) {
						nextButton.setEnabled(true);
					} else {
						nextButton.setEnabled(false);
					}
					super.onPageFinished(view, url);
				}

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					if (BuildConfig.DEBUG) {
						System.out.println("url-->" + url);
					}
					// Toast.makeText(getApplicationContext(), url+"",
					// 0).show();
					if (url.startsWith("com.tao8.app://authorize#")&& url.contains("mobile_token")) {
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.addCategory("android.intent.category.BROWSABLE");
						intent.setData(Uri.parse(url));
						BrowserActivity.this.startActivity(intent);
						BrowserActivity.this.finish();
						return true;
					} else if (url.contains("http://")) {
						view.loadUrl(url);
						return true;
					}else if (url.contains("itaobao://")) {
						Log.e(TAG, "itaobao= "+url);
//						url.replace("itaobao", "http");
//						view.loadUrl(url);
						return true;
					}
					return false;
				}
			});
			if (CommonUtil.checkNetState(getApplicationContext())) {
				if (num_iid.startsWith("http")) {
					webview.loadUrl(num_iid);
				}else {
					getClickUrlWithId(num_iid); 
				}
				
			} else {
				Toast.makeText(getApplicationContext(), "网络连接异常，请检查网络", 0)
						.show();
				onBackPressed();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_tv_go_menu:
			onBackPressed();
			break;
		case R.id.browser_im_freshen:
			webview.reload();
			break;
		case R.id.browser_im_next:
			if (webview.canGoForward()) {
				webview.goForward();
				if (webview.canGoBack()) {
					v.setEnabled(true);
				} else {
					v.setEnabled(false);
				}
			}
			break;
		case R.id.browser_im_preview:
			if (webview.canGoBack()) {
				webview.goBack();
				if (webview.canGoForward()) {
					v.setEnabled(true);
				} else {
					v.setEnabled(false);
				}
			}
			break;
		case R.id.browser_im_close:
			// 获得控制键盘的类的对象  
//			this.onKeyDown(KeyEvent.KEYCODE_BACK, null);
			onBackPressed();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getRepeatCount() == 2) {
				onBackPressed();
			}
			if (webview.canGoBack()) {
				webview.goBack();
				return true;
			} else {
				onBackPressed();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		webview.destroy();
		webview = null;
		super.onDestroy();
	}
	
	
	/**
	 * 通过id转换成click_url
	 * @param num_iid
	 */
	private void getClickUrlWithId(String num_iid) {
//		if (sharedPreferences == null) {
//			sharedPreferences = this.getSharedPreferences("config",
//					Context.MODE_PRIVATE);
//		}
//		long userId = sharedPreferences.getLong("userId", 10000);
//		final AccessToken accessToken = TopConfig.client.getAccessToken(userId);
//		String tql = "select click_url form taobao.tbk.mobile.items.convert where num_iid = "+num_iid;
//		if (BuildConfig.DEBUG) {
//			System.out.println(tql);
//		}
//		GetTopData.getDataFromTop(tql, new SearchItemParser(), userId,
//				new MyTqlListener() {
//					@Override
//					public void onComplete(Object result) {
//						
//						final ArrayList<ClickUrl> results = (ArrayList) result;
//						if (results != null && results.size() > 0) {
//							if (BuildConfig.DEBUG && results != null) {
//								Toast.makeText(BrowserActivity.this,
//										results.size() + "  总共", 1).show();
//							}
//							ClickUrl clickUrl = results.get(0);
//							String uri = CommonUtil.generateTopClickUri(clickUrl.click_url,
//									BrowserActivity.this, accessToken);
//						}
//						if (BuildConfig.DEBUG) {
//							if (results != null) {
//								Log.i(TAG, Integer.toString(results.size()));
//							}
//						}
//					}
//
//					@Override
//					public void onException(Exception e) {
//						AppException.network(e).makeToast(BrowserActivity.this);
//					}
//
//					@Override
//					public void onResponseException(Object apiError) {
//						Toast.makeText(BrowserActivity.this,
//								((ApiError) apiError).getMsg(), 0).show();
//						if (BuildConfig.DEBUG) {
//							Log.i(TAG, apiError.toString());
//						}
//					}
//				});
		
		TopParameters params=new TopParameters();
		params.setMethod(Config.TAOBAOKE_CLICK_CONVERT);//api方法
		params.addParam("num_iids", num_iid);//业务参数
		params.addFields("click_url");
		if (sharedPreferences == null) {
		sharedPreferences = this.getSharedPreferences("config",Context.MODE_PRIVATE);
		}
		long userId = sharedPreferences.getLong("userId", 10000);
		final AccessToken accessToken = TopConfig.client.getAccessToken(userId);
		client.api(params, userId, new TopApiListener() {
			
			@Override
			public void onException(final Exception e) {
				BrowserActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						AppException.network(e).makeToast(BrowserActivity.this);
					}
				});
			}
			@Override
			public void onError(ApiError error) {
				Toast.makeText(BrowserActivity.this,
						((ApiError) error).getMsg(), 0).show();
			}
			@Override
			public void onComplete(JSONObject json) {
				ClickUrlParser parser = new ClickUrlParser();
				try {
					final List<ClickUrl> results = parser.parserJson(json.toString());
					if (results!=null&&results.size()>0) {
						if (BuildConfig.DEBUG && results != null) {
							BrowserActivity.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(BrowserActivity.this,
											results.size() + "  总共", 1).show();
								}
							});
						}
						ClickUrl clickUrl = results.get(0);
						String url = CommonUtil.generateTopClickUri(clickUrl.click_url,
								BrowserActivity.this, accessToken);
						webview.loadUrl(url);
					}
				} catch (JSONException e) {
					Log.e(TAG,e.getLocalizedMessage(),	e);
				
				}
			}
		}, true);
	}
}
