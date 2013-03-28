package com.tao8.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.tao8.app.R;
import com.tao8.app.adapter.CouponAdapter;
import com.tao8.app.domain.TaobaokeCouponItem;
import com.tao8.app.domain.TaokeItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CouponFragment extends Fragment{

	private LinearLayout linearLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		linearLayout = (LinearLayout) inflater.inflate(R.layout.coupon, null);
		ListView imgsListView = (ListView) linearLayout.findViewById(R.id.coupon_lv_content_imgs);
		TextView lableTextView = (TextView) linearLayout.findViewById(R.id.head_tv_lable);
		lableTextView.setText("淘宝折扣");
		List<TaobaokeCouponItem> taokeItems = new ArrayList<TaobaokeCouponItem>();
		imgsListView.setAdapter(new CouponAdapter(getActivity(),taokeItems));
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
