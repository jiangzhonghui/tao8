package com.emer.egou.app.ui;

import java.util.Date;

import com.emer.egou.app.AppManager;
import com.emer.egou.app.BuildConfig;
import com.emer.egou.app.R;
import com.emer.egou.app.TopConfig;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.auth.AccessToken;
import com.taobao.top.android.auth.AuthActivity;
import com.taobao.top.android.auth.AuthError;
import com.taobao.top.android.auth.AuthException;
import com.taobao.top.android.auth.AuthorizeListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class BaseActivity extends AuthActivity {
	protected static final String TAG = "BaseActivity";
	protected long userId;
	public TopAndroidClient client = TopConfig.client;
	protected AccessToken accessToken;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
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
			private SharedPreferences sharedPreferences;
			
			@Override
			public void onComplete(AccessToken accessToken) {
				BaseActivity.this.accessToken = accessToken;
				Log.d(TAG, "callback");
				Toast.makeText(BaseActivity.this, "accessToken", 0).show();
				
				String id = accessToken.getAdditionalInformation().get(
						AccessToken.KEY_SUB_TAOBAO_USER_ID);
				if (id == null) {
					id = accessToken.getAdditionalInformation().get(
							AccessToken.KEY_TAOBAO_USER_ID);
					System.out.println("topResult id " + id);
				}
				
				TopConfig.userId = BaseActivity.this.userId = Long.parseLong(id);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "onOptionsItemSelected");
			}
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_exit:
			AppManager.getAppManager().AppExit(this);
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
