package com.tao8.app.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import cn.waps.AppConnect;

import com.slidingmenu.lib.SlidingMenu;
import com.tao8.app.R;
import com.tao8.app.TopConfig;
import com.tao8.app.ad.QuitPopAd;
import com.tao8.app.util.LogUtil;


public class ViewPagerActivity extends BaseFragmentActivity {

	
	public static  ViewPager vp;
	
	/*public ViewPagerActivity() {
		//super(R.string.viewpager);
	}*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/////////////////////////////
		// 初始化统计器，并通过代码设置WAPS_ID, WAPS_PID
		AppConnect.getInstance(TopConfig.WAPS_ID, "WAPS", this);
		////////////////////////////
		try {
			if (vp==null) {
				vp = new ViewPager(this);
			}
			vp.setAdapter(new ColorPagerAdapter(getSupportFragmentManager()));
			vp.setId("VP".hashCode());
			setContentView(vp);
		} catch (Exception e) {
			LogUtil.e("ViewPagerActivity", e.getLocalizedMessage(), e);
		
		}
		
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) { }

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					getSlidingMenu().setMode(SlidingMenu.LEFT);
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
			}

		});
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		Intent intent = getIntent();
		if (intent!=null) {
			Uri data = intent.getData();
			if (data!=null&&data.getScheme().contains("com.tao8.app")) {
				switchContent(new MyTaoBaoFragment());
				return;
			}
		}
		vp.setCurrentItem(0);
		
	}

	public void switchContent(Fragment fragment){
		ColorPagerAdapter adapter = (ColorPagerAdapter) vp.getAdapter();
		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			Fragment mFragment = adapter.getItem(i);
			if (mFragment.getClass().getName().equalsIgnoreCase(fragment.getClass().getName())) {
				vp.setCurrentItem(i, true);
				break;
			}
		}
		
//		int count  = vp.getChildCount();
//		for (int i = 0; i < count; i++) {
//			View child = vp.getChildAt(i);
//			if (fragment.getClass().getName().equals(child.getClass().getName())) {
//				vp.setCurrentItem(i, true);
//			}
//		}
		getSlidingMenu().showContent();
	}
	
	public class ColorPagerAdapter extends FragmentPagerAdapter {
		
		private ArrayList<Fragment> mFragments;
		
		public ColorPagerAdapter(FragmentManager fm) {
			super(fm);
			mFragments = new ArrayList<Fragment>();
		/*	for (int color : COLORS)
				mFragments.add(new ColorFragment(color));
		}*/
		mFragments.add(new TryoutFragment());
		mFragments.add(new RechargeFragment());
		mFragments.add(new CouponEveryDayFragment());
		mFragments.add(new CouponFragment());
		mFragments.add(new SeachFragment());
		mFragments.add(new MyTaoBaoFragment());
		}
		@Override
		public int getCount() {
			return mFragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			int currentItem = vp.getCurrentItem();
			ColorPagerAdapter adapter = (ColorPagerAdapter) vp.getAdapter();
			Fragment item = adapter.getItem(currentItem);
			if (item.getClass().getName().equalsIgnoreCase(SeachFragment.class.getName())) {
				SeachFragment seachFragment = (SeachFragment) item;
				View currentView = seachFragment.view.getCurrentView();
				if (currentView.getId()==R.id.coupon_ll_fragment) {
					seachFragment.view.showNext();
					return true;
				}else {
					QuitPopAd.getInstance().show(this);
				}
			}else {
				QuitPopAd.getInstance().show(this);
			}
		}
		return true;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppConnect.getInstance(TopConfig.WAPS_ID, "WAPS", this).finalize();
	}
}
