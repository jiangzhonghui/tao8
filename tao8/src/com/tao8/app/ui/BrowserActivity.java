package com.tao8.app.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tao8.app.R;
import com.tao8.app.util.CommonUtil;

public class BrowserActivity extends BaseFragmentActivity implements OnClickListener {
	public static final String BROWSERACTIVITY_ACTION = "com.tao8.app";
	public static final String BROWSERACTIVITY_URI = "uri";
	private WebView webview;
	private String uri;
	private ProgressDialog pd;
	private ImageButton backButton;
	private TextView lableTextView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//requestWindowFeature(Window.FEATURE_PROGRESS);// 让进度条显示在标题栏上
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browser);
		//找到view
		lableTextView = (TextView) findViewById(R.id.head_tv_lable);
		webview = (WebView)findViewById(R.id.webview);
		
		String title = getIntent().getStringExtra("title");
		if (title!=null) {
			lableTextView.setText(Html.fromHtml(title));
		}
		if (pd==null) {
			pd = new ProgressDialog(BrowserActivity.this);
			pd.setMessage("数据加载中......");
		}
		String action = getIntent().getAction();
		uri = getIntent().getStringExtra(BROWSERACTIVITY_URI);
		if (action.equalsIgnoreCase(BROWSERACTIVITY_ACTION)) {
			//Toast.makeText(this, uri, 0).show();
			WebSettings webSettings = webview.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webview.setWebChromeClient(new WebChromeClient() {
				public void onProgressChanged(WebView view, int progress) {
					System.out.println(System.currentTimeMillis());
					//BrowserActivity.this.setProgress(progress * 1000);
					System.out.println("view.getUrl() " + view.getUrl());
					System.out.println(progress);
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
					if (!pd.isShowing()) {
						pd.show();
					}
					super.onPageStarted(view, url, favicon);
				}
				@Override
				public void onPageFinished(WebView view, String url) {
					if (pd!=null&&pd.isShowing()) {
						pd.dismiss();
					}
					super.onPageFinished(view, url);
				}
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					System.out.println("url " + url);
					//Toast.makeText(getApplicationContext(), url+"", 0).show();
					if (url.startsWith("com.tao8.app://authorize#")&&url.contains("mobile_token")) {
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.addCategory("android.intent.category.BROWSABLE");
						intent.setData(Uri.parse(url));
						BrowserActivity.this.startActivity(intent);
						BrowserActivity.this.finish();
						return true;
					}else {
						view.loadUrl(url);
					}
					return false;
				}
			});
			if (CommonUtil.checkNetState(getApplicationContext())) {
				webview.loadUrl(uri);
			}else {
				Toast.makeText(getApplicationContext(), "网络连接异常，请检查网络", 0).show();
			}
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			if (webview.canGoBack()) {
				webview.goBack();
				return true;
			}else {
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
