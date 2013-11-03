package com.tao8.app.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tao8.app.BuildConfig;
import com.tao8.app.R;

public class AboutActivity extends BaseFragmentActivity implements OnClickListener{

//	private BaiduSocialShare socialShare;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		socialShare = BaiduSocialShare.getInstance(this,
//				"unp1eqkHx4yUuI0nWcNNVbom");
		setContentView(R.layout.about);
		TextView goMenuTextView = (TextView) findViewById(R.id.head_tv_go_menu);
		ImageView moreImageView = (ImageView) findViewById(R.id.head_iv_more);
		TextView updatetTextView = (TextView) findViewById(R.id.about_tv_updata);
		TextView shareTextView = (TextView) findViewById(R.id.about_tv_share);
		shareTextView.setOnClickListener(this);
		updatetTextView.setOnClickListener(this);
		TextView weiboTextView = (TextView) findViewById(R.id.about_tv_weibo);
		weiboTextView.setOnClickListener(this);
		moreImageView.setVisibility(View.GONE);
		goMenuTextView.setText("返回");
		goMenuTextView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_tv_go_menu:
			onBackPressed();
			break;
		case R.id.about_tv_updata:
			if (BuildConfig.DEBUG) {
				Toast.makeText(this, "升级", 0).show();
			}
			//手动检查新版本
			break;
		case R.id.about_tv_weibo:
			//用户反馈
			break;
//		case R.id.about_tv_share:
//			ShareContent content = new ShareContent();
//			content.setContent("淘吧这个软件还不错,不仅方便便宜给手机充值,更有每天付邮试用,天天特价精选等超多的好用的功能,给大家极力推荐一下.");
//			content.setUrl("http://www.nodebeginner.org/index-zh-cn.html");
//			content.setImageUrl("http://img01.taobaocdn.com/top/i1/T1hqWcXCdbXXaCwpjX.png");
//			socialShare.showShareMenu(AboutActivity.this, content,
//					"theme_style", new ShareListener() {
//						@Override
//						public void onAuthComplete(Bundle values) {
//							// TODO Auto-generated method stub
//						}
//
//						@Override
//						public void onApiComplete(String responses) {
//
//						}
//
//						@Override
//						public void onError(BaiduShareException e) {
//							// TODO Auto-generated method stub
//							Utility.showAlert(AboutActivity.this, "错误提示",
//									e.toString());
//						}
//
//					});
//			break;
		default:
			break;
		}
		
	}
}
