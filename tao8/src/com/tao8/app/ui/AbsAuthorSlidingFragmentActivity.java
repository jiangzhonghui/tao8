package com.tao8.app.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tao8.app.AppManager;
import com.tao8.app.BuildConfig;
import com.tao8.app.R;
import com.tao8.app.TopConfig;
import com.taobao.top.android.TOPUtils;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.auth.AccessToken;
import com.taobao.top.android.auth.AuthError;
import com.taobao.top.android.auth.AuthException;
import com.taobao.top.android.auth.AuthorizeListener;

public abstract class AbsAuthorSlidingFragmentActivity extends SlidingFragmentActivity{
	private int mTitleRes;
	protected BehindMenuFragment mFrag;
	protected static final String TAG = "AbsAuthorSlidingFragmentActivity";
/*	public AbsAuthorSlidingFragmentActivity(int titleRes) {
		mTitleRes = titleRes;
	}*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// 添加Activity到堆栈
				AppManager.getAppManager().addActivity(this);
		///////////////////////////////
		Intent intent = getIntent();
		Uri uri = intent.getData();

		final TopAndroidClient client = getTopAndroidClient();
		Uri u = Uri.parse(client.getRedirectURI());
		if (uri != null && uri.getScheme().equals(u.getScheme())
				&& uri.getHost().equals(u.getHost())
				&& uri.getPort() == u.getPort()
				&& uri.getPath().equals(u.getPath())) {

			String errorStr = uri.getQueryParameter("error");
			AuthorizeListener listener = getAuthorizeListener();
			if (errorStr == null) {// 授权成功
			// String ret = url.substring(url.indexOf("#") + 1);
				if (BuildConfig.DEBUG) {
					android.util.Log.e("认证..", uri+"");
				}
				String ret = uri.getFragment();
				String[] kv = ret.split("&");
				Bundle values = new Bundle();
				for (String each : kv) {
					String[] ss = each.split("=");
					if (ss != null && ss.length == 2) {
						values.putString(ss[0], ss[1]);
					}
				}
				final AccessToken token = TOPUtils.convertToAccessToken(values);
				// Android3.0后ui主线程中同步访问网络会有限制。
				// 使用ExecutorService.invokeAll()阻塞主线程的方式起一个线程再去调用api
				Callable<Date> task = new Callable<Date>() {
					@Override
					public Date call() throws Exception {
						Date date = client.getTime();
						return date;
					}
				};
				List<Callable<Date>> tasks = new ArrayList<Callable<Date>>();
				tasks.add(task);
				ExecutorService es = Executors.newSingleThreadExecutor();
				try {
					List<Future<Date>> results = es.invokeAll(tasks);
					Future<Date> future = results.get(0);
					token.setStartDate(future.get());
					
					client.addAccessToken(token);
				} catch (Exception e) {
					listener.onAuthException(new AuthException(e));
				}				
				listener.onComplete(token);
			} else {// 授权失败
				String errorDes = uri.getQueryParameter("error_description");
				AuthError error = new AuthError();
				error.setError(errorStr);
				error.setErrorDescription(errorDes);
				listener.onError(error);
			}
		}
		///////////////////////////////////////
		
		
		
		
		setTitle(mTitleRes);

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		mFrag = new BehindMenuFragment();
		t.replace(R.id.menu_frame, mFrag);
		t.commit();

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
	
	/**
	 * 待子类实现。需要返回{@code TopAndroidClient}对象，用来接收授权成功后的access token
	 * @return
	 */
	protected abstract TopAndroidClient getTopAndroidClient();
	/**
	 * 待子类实现。需要返回一个处理授权结果的监听器
	 * @see AuthorizeListener
	 * @return
	 */
	protected abstract AuthorizeListener getAuthorizeListener();
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

}
