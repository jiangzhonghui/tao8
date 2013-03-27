package com.emer.egou.app.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.TimeUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emer.egou.app.R;
import com.emer.egou.app.TopConfig;
import com.emer.egou.app.adapter.AccountDetailAdapter;
import com.emer.egou.app.domain.AccountDetail;
import com.emer.egou.app.util.Config;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.auth.AccessToken;

public class AccountActivity extends Activity implements OnItemClickListener {
	
	private TextView nameTextView;
	private TextView thisMounthRestoreTextView;
	private TextView totalRestoreTextView;
	private ListView detaiListView;
	private List<AccountDetail> accountDetails;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	private SharedPreferences sharedPreferences;
	private Long userId;
	private TopAndroidClient client = TopConfig.client;
	private AccessToken accessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.account);
		super.onCreate(savedInstanceState);
		findView();
		initData();
		setListener();
		setData();
	}

	private void setData() {
		detaiListView.setAdapter(new AccountDetailAdapter(this,accountDetails));
		
	}

	private void setListener() {
		detaiListView.setOnItemClickListener(this);
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onRestart() {
		if (accessToken==null) {
			finish();
			
		}
		super.onRestart();
	}
	private void initData() {
		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		userId = sharedPreferences.getLong("userId", 10000);
		accessToken = client.getAccessToken(userId);
		if (userId == null||accessToken==null) {
			Toast t = Toast.makeText(AccountActivity.this, "请先授权",Toast.LENGTH_SHORT);
			client.authorize(AccountActivity.this);
			t.show();
			return;
		}else {
			String nick = accessToken.getAdditionalInformation().get(AccessToken.KEY_TAOBAO_USER_NICK);
			nameTextView.setText(nick);
		}
		final ProgressDialog pd = ProgressDialog.show(AccountActivity.this, null, "正在努力加载中.....");
		HandlerThread ht = new HandlerThread("handler"){
			@Override
			public void run() {
				SystemClock.sleep(1000);
				pd.cancel();
				super.run();
			}
		};
		ht.start();
		accountDetails = initAccountDetails();
		System.out.println(accountDetails);
	}

	private List<AccountDetail> initAccountDetails() {
		String sid = accessToken==null?"":accessToken.getValue();
		accountDetails = new ArrayList<AccountDetail>();
		AccountDetail rebateDetail = new AccountDetail();
		rebateDetail.setAction(SeachActivity.REBATE_DETAIL_ACTION);
		rebateDetail.setItemName("返利订单");
		rebateDetail.setUri(null);
		accountDetails.add(rebateDetail);
		AccountDetail tPaymentDetail = new AccountDetail();
		tPaymentDetail.setAction(BrowserActivity.BROWSERACTIVITY_ACTION);
		tPaymentDetail.setItemName("待付款订单");
		tPaymentDetail.setUri(Config.TAO_ORDER.concat(sid));
		accountDetails.add(tPaymentDetail);
		AccountDetail tCollectionDetail = new AccountDetail();
		tCollectionDetail.setAction(BrowserActivity.BROWSERACTIVITY_ACTION);
		tCollectionDetail.setItemName("淘宝收藏");
		tCollectionDetail.setUri(Config.TAO_MYCOLLECTION.concat(sid));
		accountDetails.add(tCollectionDetail);
		AccountDetail tCarDetail = new AccountDetail();
		tCarDetail.setAction(BrowserActivity.BROWSERACTIVITY_ACTION);
		tCarDetail.setItemName("淘宝购物车");
		tCarDetail.setUri(Config.TAO_CAR.concat(sid));
		accountDetails.add(tCarDetail);
		AccountDetail tLogisticsDetail = new AccountDetail();
		tLogisticsDetail.setAction(BrowserActivity.BROWSERACTIVITY_ACTION);
		tLogisticsDetail.setItemName("查看物流");
		tLogisticsDetail.setUri(Config.TAO_LOGISTICS.concat(sid));
		accountDetails.add(tLogisticsDetail);
		return accountDetails;
	}

	private void findView() {
		nameTextView = (TextView) findViewById(R.id.account_tv_name);
		thisMounthRestoreTextView = (TextView) findViewById(R.id.account_tv_this_month_restore);
		totalRestoreTextView = (TextView) findViewById(R.id.account_tv_total_restore);
		detaiListView = (ListView) findViewById(R.id.account_lv_account_detail);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		AccountDetail accountDetail = accountDetails.get(position);
		intent.setAction(accountDetail.getAction());
		intent.putExtra(BrowserActivity.BROWSERACTIVITY_URI, accountDetail.getUri());
		startActivity(intent);
		
	}

}
