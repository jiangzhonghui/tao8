package com.tao8.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tao8.app.BuildConfig;
import com.tao8.app.R;

public class BehindMenuFragment extends Fragment implements OnClickListener{
	
	
	private ImageView loginImageView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.behind_menu, null);
		RelativeLayout czzxRelativeLayout = (RelativeLayout) v.findViewById(R.id.behind_menu_rl_czzx);
		RelativeLayout lmllRelativeLayout = (RelativeLayout) v.findViewById(R.id.behind_menu_rl_lmll);
		RelativeLayout tttjRelativeLayout = (RelativeLayout) v.findViewById(R.id.behind_menu_rl_tttj);
		lmllRelativeLayout.setOnClickListener(this);
		czzxRelativeLayout.setOnClickListener(this);
		tttjRelativeLayout.setOnClickListener(this);
		loginImageView = (ImageView) v.findViewById(R.id.behind_menu_iv_login);
		loginImageView.setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.behind_menu_iv_login:
			if (getActivity() == null)
				return;
			if (getActivity() instanceof ViewPagerActivity) {
				ViewPagerActivity fca = (ViewPagerActivity) getActivity();
				fca.client.authorize(fca);
			} 
			break;
		case R.id.behind_menu_rl_czzx:
			if (BuildConfig.DEBUG) {
				Toast.makeText(getActivity(),"充值中心", 0).show();
			}
			switchFragment(new RechargeFragment());
			break;
		case R.id.behind_menu_rl_lmll:
			if (BuildConfig.DEBUG) {
				Toast.makeText(getActivity(),"类目浏览", 0).show();
			}
//			Intent intent = new Intent(getActivity(), PullToRefreshActivity.class);
//			getActivity().startActivity(intent);
			break;
		case R.id.behind_menu_rl_tttj:
			if (BuildConfig.DEBUG) {
				Toast.makeText(getActivity(),"天天特价", 0).show();
			}
			switchFragment(new CouponEveryDayFragment());
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
		} /*else if (getActivity() instanceof ResponsiveUIActivity) {
			ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
			ra.switchContent(fragment);
		}*/
	}
	
	

}
