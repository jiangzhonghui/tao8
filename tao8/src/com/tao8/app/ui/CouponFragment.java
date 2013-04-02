package com.tao8.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.MultiColumnListView.OnLoadMoreListener;
import com.huewu.pla.sample.internal.ImageWrapper;
import com.huewu.pla.sample.internal.ImgResource;
import com.huewu.pla.sample.internal.SimpleViewBuilder;
import com.lurencun.android.adapter.CommonAdapter;
import com.lurencun.android.system.ActivityUtil;
import com.tao8.app.R;
import com.tao8.app.adapter.CouponAdapter;
import com.tao8.app.domain.TaobaokeCouponItem;

public class CouponFragment extends Fragment{

	private LinearLayout linearLayout;
	private MultiColumnListView mWaterfallView;
	private CommonAdapter<ImageWrapper> mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		linearLayout = (LinearLayout) inflater.inflate(R.layout.coupon, null);
		mWaterfallView = (MultiColumnListView) linearLayout.findViewById(R.id.coupon_lv_content_imgs);
		mAdapter = new CommonAdapter<ImageWrapper>(getActivity().getLayoutInflater(), new SimpleViewBuilder());
		mWaterfallView.setAdapter(mAdapter);
		mAdapter.update(ImgResource.genData());
		mWaterfallView.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				mAdapter.add(ImgResource.genData());
				mAdapter.notifyDataSetChanged();
				ActivityUtil.show(getActivity(), "到List底部自动加载更多数据");
				//5秒后完成
				new Handler().postDelayed(new Runnable(){
					@Override
					public void run() {
						mWaterfallView.onLoadMoreComplete();
					}
				}, 5000);
			}
		});
		TextView lableTextView = (TextView) linearLayout.findViewById(R.id.head_tv_lable);
		lableTextView.setText("淘宝折扣");
		List<TaobaokeCouponItem> taokeItems = new ArrayList<TaobaokeCouponItem>();
		
		//imgsListView.setAdapter(new CouponAdapter(getActivity(),taokeItems));
		
		return linearLayout;
	}
}
