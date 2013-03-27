package com.emer.egou.app.adapter;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.emer.egou.app.R;
import com.emer.egou.app.util.CommonUtil;

public abstract class SeachContentAdapter extends BaseAdapter implements OnClickListener {
	private Context context;
	private LinkedHashMap<String, String[]> groupMaps;

	public SeachContentAdapter(Context context,
			LinkedHashMap<String,  String[]> groupMaps) {
		this.context = context;
		if (groupMaps == null) {
			throw new RuntimeException("groupMap can not be null");
		}
		this.groupMaps = groupMaps;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return groupMaps.keySet().size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Set<Entry<String,  String[]>> entrySet = groupMaps.entrySet();
		for (int i = 0; i < entrySet.size(); i++) {
			entrySet.iterator();
		}
		Iterator<Entry<String,  String[]>> iterator = entrySet.iterator();
		int i = 0;
		Entry<String,  String[]> next = null;
		while (iterator.hasNext()) {
			if (i == position) {
				next = iterator.next();
				break;
			} else {
				iterator.next();
			}
			i++;
		}
		return next;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = View
					.inflate(context, R.layout.seach_content_list_item, null);
		} else {
			view = convertView;
		}
		// ImageView iconImageView = (ImageView)
		// view.findViewById(R.id.seach_content_iv_icon);
		RelativeLayout lableLayout = (RelativeLayout) view.findViewById(R.id.seach_content_list_item_rl_title);
		lableLayout.setOnClickListener(this);
		ImageButton icon = (ImageButton) view.findViewById(R.id.seach_content_imbtn_icon);
		icon.setOnClickListener(this);
		RelativeLayout itemsLayout = (RelativeLayout) view
				.findViewById(R.id.seach_content_if_group_detail_items);
		TextView groupNameTextView = (TextView) view
				.findViewById(R.id.seach_content_tv_group_name);
		Entry<String, String[]> itemsEntry = (Entry<String,  String[]>) getItem(position);
		groupNameTextView.setText(itemsEntry.getKey());
		addTextViews(itemsEntry.getValue(), itemsLayout);
		return view;
	}
	private void addTextViews(String[] value, RelativeLayout itemsLayout) {
		//int id = 100000;
		for (String string : value) {
			TextView textView = (TextView) View.inflate(context, R.layout.seach_content_item_textview, null);
			//textView.setId(id);
			textView.setText(string);
			textView.setOnClickListener(this);
			//LayoutParams rlLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			//rlLayoutParams.leftMargin = CommonUtil.dip2px(context, 3f);
			//textView.setTextSize(CommonUtil.dip2px(context, 12f));
			//textView.setTextColor(Color.parseColor("#000000"));
			//textView.setBackgroundResource(R.drawable.seach_content_text_bg);
			//textView.setBackground(context.getResources().getDrawable(R.drawable.seach_content_text_bg));
			//textView.setTextColor(R.drawable.text_color);
			itemsLayout.addView(textView);
		}
	}
}
