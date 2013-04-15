package com.tao8.app.adapter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.tao8.app.R;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CouponEveryGridViewAdapter extends BaseAdapter{
	LinkedHashMap<String,String> catorys;
	private FragmentActivity context;
	public CouponEveryGridViewAdapter(FragmentActivity context,LinkedHashMap<String,String> catorys){
		this.catorys = catorys;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return catorys.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return getItem(catorys, position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout view = (RelativeLayout) View.inflate(context, R.layout.recharge_gridview_item, null);
		TextView moneyTextView = (TextView) view.findViewById(R.id.recharge_gridview_item_tv);
		moneyTextView.setText(getItem(position).toString());
		ImageView selectedImageView = (ImageView) view.findViewById(R.id.recharge_gridview_item_im_selected);
		selectedImageView.setVisibility(View.GONE);
		return view;
	}
	
	private String getItem(Map<String, String> map,int position){
		int i = 0;
		Set<Entry<String, String>> entrySet = map.entrySet();
		String result = null;
		for (Entry<String, String> entry : entrySet) {
			result = entry.getKey();
			if (i==position) {
				break;
			}
			i++;
		}
		return result;
		
	}
}