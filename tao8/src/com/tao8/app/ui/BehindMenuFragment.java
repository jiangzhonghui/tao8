package com.tao8.app.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.waps.AppConnect;

import com.emar.escore.banner.BannerSDK;
import com.emar.escore.recommendwall.RecommendSDK;
import com.emar.escore.sdk.view.bannerView;
import com.tao8.app.BuildConfig;
import com.tao8.app.R;
import com.tao8.app.util.CommonUtil;

public class BehindMenuFragment extends Fragment implements OnClickListener {

	private ImageView loginImageView;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.behind_menu, null);
		RelativeLayout czzxRelativeLayout = (RelativeLayout) v
				.findViewById(R.id.behind_menu_rl_czzx);
		RelativeLayout lmllRelativeLayout = (RelativeLayout) v
				.findViewById(R.id.behind_menu_rl_lmll);
		RelativeLayout tttjRelativeLayout = (RelativeLayout) v
				.findViewById(R.id.behind_menu_rl_tttj);
		RelativeLayout rjtjRelativeLayout = (RelativeLayout) v
				.findViewById(R.id.behind_menu_rl_rjtj);
		RelativeLayout sjfyRelativeLayout = (RelativeLayout) v
				.findViewById(R.id.behind_menu_rl_sjfy);
		TextView aboutTextView = (TextView) v
				.findViewById(R.id.behind_menu_tv_about);
		TextView feedbackTextView = (TextView) v
				.findViewById(R.id.behind_menu_tv_feedback);
		RelativeLayout wdtbRelativeLayout = (RelativeLayout) v
				.findViewById(R.id.behind_menu_rl_wdtb);
		/////////////////////////////
			RelativeLayout containerRelativeLayout = (RelativeLayout) v.findViewById(R.id.behind_menu_rl_container);
			// 互动广告调用方式
//			DomobAdView mAdview320x50 = new DomobAdView(getActivity(), TopConfig.PUBLISHER_ID, DomobAdView.INLINE_SIZE_320X50);
			//将广告View增加到视图中。
//			containerRelativeLayout.addView(mAdview320x50);
		/////////////////////////////
			
			//Banner广告---------------------------------------------------------------------------------------------------------------------------------			
			// 显示推广条
			
			bannerView bannerView = BannerSDK.getInstance(getActivity()).getBanner();
			containerRelativeLayout.addView(bannerView);
			// 推广条轮转
			BannerSDK.getInstance(getActivity()).showBanner(bannerView);
	//Banner广告---------------------------------------------------------------------------------------------------------------------------------	
		lmllRelativeLayout.setOnClickListener(this);
		czzxRelativeLayout.setOnClickListener(this);
		tttjRelativeLayout.setOnClickListener(this);
		rjtjRelativeLayout.setOnClickListener(this);
		aboutTextView.setOnClickListener(this);
		feedbackTextView.setOnClickListener(this);
		wdtbRelativeLayout.setOnClickListener(this);
		loginImageView = (ImageView) v.findViewById(R.id.behind_menu_iv_login);
		loginImageView.setOnClickListener(this);
		sjfyRelativeLayout.setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.behind_menu_iv_login:
			if (CommonUtil.checkNetState(getActivity())) {
				if (getActivity() instanceof ViewPagerActivity) {
					ViewPagerActivity fca = (ViewPagerActivity) getActivity();
					fca.client.authorize(fca);
				}
			}else {
				v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
				Toast.makeText(getActivity(), "网络不给力,检查网络", 0).show();
			}
			break;
		case R.id.behind_menu_rl_czzx:
			if (BuildConfig.DEBUG) {
				Toast.makeText(getActivity(), "充值中心", 0).show();
			}
			switchFragment(new RechargeFragment());
			break;
		case R.id.behind_menu_rl_lmll:
			if (BuildConfig.DEBUG) {
				Toast.makeText(getActivity(), "类目浏览", 0).show();
			}
			// Intent intent = new Intent(getActivity(),
			// PullToRefreshActivity.class);
			// getActivity().startActivity(intent);
			switchFragment(new SeachFragment());
			break;
		case R.id.behind_menu_rl_tttj:
			if (BuildConfig.DEBUG) {
				Toast.makeText(getActivity(), "天天特价", 0).show();
			}
			switchFragment(new CouponEveryDayFragment());
			break;
		case R.id.behind_menu_rl_rjtj:
			RecommendSDK.getInstance(getActivity()).showAdlist();
			break;
		case R.id.behind_menu_tv_about:
			Intent intent = new Intent(getActivity(), AboutActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.behind_menu_tv_feedback:
			// 用户反馈
			AppConnect.getInstance(getActivity()).showFeedback();
			break;
		case R.id.behind_menu_rl_wdtb:
			if (BuildConfig.DEBUG) {
				Toast.makeText(getActivity(), "我的淘宝", 0).show();
			}
			switchFragment(new MyTaoBaoFragment());
			break;
		case R.id.behind_menu_rl_sjfy:
			if (BuildConfig.DEBUG) {
				Toast.makeText(getActivity(), "付邮试用", 0).show();
			}
			switchFragment(new TryoutFragment());
			break;
		default:
			break;
		}
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof ViewPagerActivity) {
			ViewPagerActivity fca = (ViewPagerActivity) getActivity();
			fca.switchContent(fragment);
		} /*
		 * else if (getActivity() instanceof ResponsiveUIActivity) {
		 * ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
		 * ra.switchContent(fragment); }
		 */
	}
	
}
