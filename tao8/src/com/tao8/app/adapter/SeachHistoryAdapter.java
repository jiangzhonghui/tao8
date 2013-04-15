package com.tao8.app.adapter;

import java.util.Iterator;
import java.util.LinkedHashSet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tao8.app.R;


public class SeachHistoryAdapter extends BaseAdapter{
	private LinkedHashSet<String> historySeachLists;
	private Context context;
	public SeachHistoryAdapter(Context context,LinkedHashSet<String> historySeachLists){
		this.context = context;
		if (historySeachLists==null) {
			throw new RuntimeException("historySeachLists can not be null");
		}
		this.historySeachLists = historySeachLists;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return historySeachLists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		int i = historySeachLists.size();
		String history = "";
		for (String s : historySeachLists) {
			if (position==i) {
				history = s;
				break;
			}
			i--;
		}
		return history;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view  = null;
		ViewHolder viewHolder = null;
		if (convertView==null) {
			viewHolder = new ViewHolder();
			view = View.inflate(context, R.layout.seach_history_list_item, null);
			viewHolder.historyTextView = (TextView) view.findViewById(R.id.seach_history_list_item_tv_hoistory_seach);
			view.setTag(viewHolder);
		}else {
			view = convertView;
		}
		viewHolder =  (ViewHolder) view.getTag();
		viewHolder.historyTextView.setText(getItem(position).toString());
		return view;
	}
	static class ViewHolder{
		public TextView historyTextView;
	}
}
