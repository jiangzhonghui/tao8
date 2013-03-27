package com.emer.egou.app.ui;

import com.emar.escore.recommendwall.RecommendSDK;
import com.emar.escore.sdk.YjfSDK;
import com.emar.escore.sdk.widget.UpdateScordNotifier;
import com.emer.egou.app.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends BaseActivity implements OnClickListener, UpdateScordNotifier{

	private ImageView accountImageView;
	private ImageView seachImageView;
	private ImageView taobaoImageView;
	private ImageView tmallImageView;
	private RelativeLayout headLayout;
	private ImageButton goRebateImageButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
		setListener();
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		  YjfSDK.getInstance(this, this).initInstance("","","","");
		if (getSharedPreferences("config", Context.MODE_PRIVATE).getLong("userId", 0L)==0L) {
			goRebateImageButton.setVisibility(View.VISIBLE);
			headLayout.setBackgroundResource(R.drawable.bg_head_normal_mode);
		}else {
			goRebateImageButton.setVisibility(View.GONE);
			headLayout.setBackgroundResource(R.drawable.bg_head_rebate_mode);
		}
		
	}

	private void setListener() {
		goRebateImageButton.setOnClickListener(this);
		accountImageView.setOnClickListener(this);
		seachImageView.setOnClickListener(this);
		taobaoImageView.setOnClickListener(this);
		tmallImageView.setOnClickListener(this);
		
	}

	private void findView() {
		headLayout = (RelativeLayout) findViewById(R.id.main_head_rl);
		accountImageView = (ImageView) findViewById(R.id.main_iv_account);
		seachImageView = (ImageView) findViewById(R.id.main_iv_seach);
		taobaoImageView = (ImageView) findViewById(R.id.main_iv_taobao);
		tmallImageView = (ImageView) findViewById(R.id.main_iv_tmall);
		goRebateImageButton = (ImageButton) findViewById(R.id.main_ibtn_go_rebate);
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
		case R.id.main_ibtn_go_rebate:
			intent.setClass(getApplicationContext(), MainActivity_test.class);
			startActivity(intent);
			break;
		case R.id.main_iv_account:
			intent.setClass(getApplicationContext(), AccountActivity.class);
			startActivity(intent);
			break;
		case R.id.main_iv_seach:
			intent.setClass(getApplicationContext(), SeachActivity.class);
			startActivity(intent);
			break;
		case R.id.main_iv_taobao:
			intent.setClass(getApplicationContext(), ProductsActivity.class);
			intent.putExtra("isTmallCompetitive", false);
			startActivity(intent);
			
			break;
		case R.id.main_iv_tmall:
			RecommendSDK.getInstance(MainActivity.this).showAdlist();
			
			
			/*intent.setClass(getApplicationContext(), ProductsActivity.class);
			intent.putExtra("isTmallCompetitive", true);
			startActivity(intent);*/
			break;

		default:
			break;
		}
		
	}

	@Override
	public void updateScoreFailed(int arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateScoreSuccess(int arg0, int arg1, int arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}
}
