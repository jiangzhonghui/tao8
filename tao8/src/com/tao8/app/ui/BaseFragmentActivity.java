package com.tao8.app.ui;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

import com.tao8.app.TopConfig;
import com.tao8.app.util.LogUtil;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.auth.AccessToken;
import com.taobao.top.android.auth.AuthError;
import com.taobao.top.android.auth.AuthException;
import com.taobao.top.android.auth.AuthorizeListener;

public class BaseFragmentActivity extends AbsAuthorSlidingFragmentActivity {

	private int mTitleRes;
	protected BehindMenuFragment mFrag;
	protected static final String TAG = "BaseActivity";
	protected long userId;
	public TopAndroidClient client = TopConfig.client;
	protected AccessToken accessToken;
	/*public BaseFragmentActivity(int titleRes) {
		super(titleRes);
		mTitleRes = titleRes;
	}*/

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
				BaseFragmentActivity.this.accessToken = accessToken;
				Log.d(TAG, "callback");
				Toast.makeText(BaseFragmentActivity.this, "accessToken", 0).show();
				
				String id = accessToken.getAdditionalInformation().get(
						AccessToken.KEY_SUB_TAOBAO_USER_ID);
				if (id == null) {
					id = accessToken.getAdditionalInformation().get(
							AccessToken.KEY_TAOBAO_USER_ID);
					System.out.println("topResult id " + id);
				}
				
				TopConfig.userId = BaseFragmentActivity.this.userId = Long.parseLong(id);
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
				LogUtil.e(TAG, e.getErrorDescription());
			}

			@Override
			public void onAuthException(AuthException e) {
				LogUtil.e(TAG, e.getMessage());
			}
		};
	}
}
