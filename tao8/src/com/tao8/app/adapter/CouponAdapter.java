package com.tao8.app.adapter;

import java.util.List;

import com.tao8.app.domain.TaobaokeCouponItem;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CouponAdapter extends BaseAdapter {
	private Context context;
	private List<TaobaokeCouponItem> taokeItems;
	public CouponAdapter(FragmentActivity activity,List<TaobaokeCouponItem> taokeItems) {
		this.context = activity;
		this.taokeItems =  taokeItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return taokeItems==null?0:taokeItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return taokeItems==null?null:taokeItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		return null;
	}

}
