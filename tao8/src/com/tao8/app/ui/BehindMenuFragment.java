package com.tao8.app.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tao8.app.R;

public class BehindMenuFragment extends Fragment implements OnClickListener{
	
	
	private ImageView loginImageView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.behind_menu, null);
		RelativeLayout czzxRelativeLayout = (RelativeLayout) v.findViewById(R.id.behind_menu_rl_czzx);
		czzxRelativeLayout.setOnClickListener(this);
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
			Toast.makeText(getActivity(),"充值", 0).show();
			switchFragment(new RechargeFragment());
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
