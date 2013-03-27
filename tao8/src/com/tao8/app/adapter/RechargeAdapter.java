package com.tao8.app.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
		System.out.println(moneyItems.get(position));
		int width = context.getWindowManager().getDefaultDisplay().getWidth()/3-30;
		TextView tv = new TextView(context);
		tv.setWidth(width);
		tv.setHeight(80);
		tv.setGravity(Gravity.CENTER);
		tv.setBackgroundColor(Color.GRAY);
		tv.setTextColor(Color.BLACK);
		tv.setText(moneyItems.get(position)+"元");
		return tv;
	}
	
}