package com.tao8.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tao8.app.R;
import com.tao8.app.domain.AccountDetail;

public class AccountDetailAdapter extends BaseAdapter{
	
	private Context context;
	private List<AccountDetail> accountDetails;
	public AccountDetailAdapter(Context context,List<AccountDetail> accountDetails){
		this.context = context;
		this.accountDetails = accountDetails;
	}
	@Override
	public int getCount() {
		return accountDetails.size();
	}

	@Override
	public Object getItem(int position) {
		return accountDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView==null) {
			view = View.inflate(context, R.layout.account_list_item, null);
			convertView = view;
			view.setPadding(10, 15, 15, 10);
		}else {
			view = convertView;
		}
		TextView itemNameTextView = (TextView) view.findViewById(R.id.account_list_item_tv_name);
		AccountDetail detail = (AccountDetail) getItem(position);
		itemNameTextView.setText(detail.getItemName());
		return view;
	}
	
}