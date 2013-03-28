package com.tao8.app.adapter;

import java.util.List;

import com.tao8.app.R;

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

public class RechargeAdapter extends BaseAdapter{
	List<String> moneyItems;
	private FragmentActivity context;
	public RechargeAdapter(FragmentActivity context,List<String> moneyItems){
		this.moneyItems = moneyItems;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return moneyItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return moneyItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout view = (RelativeLayout) View.inflate(context, R.layout.recharge_gridview_item, null);
		int width = context.getWindowManager().getDefaultDisplay().getWidth()/3-30;
		new RelativeLayout.LayoutParams(width, 80);
		TextView moneyTextView = (TextView) view.findViewById(R.id.recharge_gridview_item_tv);
		moneyTextView.setText(moneyItems.get(position)+" 元");
		ImageView selectedImageView = (ImageView) view.findViewById(R.id.recharge_gridview_item_im_selected);
		selectedImageView.setVisibility(View.GONE);
		return view;
	}
	
}