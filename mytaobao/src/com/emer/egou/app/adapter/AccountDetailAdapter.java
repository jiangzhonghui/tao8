package com.emer.egou.app.adapter;

import java.util.List;

import com.emer.egou.app.R;
import com.emer.egou.app.domain.AccountDetail;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class AccountDetailAdapter extends BaseAdapter{
	
	private Context context;
	private List<AccountDetail> accountDetails;
	public AccountDetailAdapter(Context context,List<AccountDetail> accountDetails){
		this.context = context;
		this.accountDetails = accountDetails;
	}
	@Override
	public int getCount() {
		return accountDetails==null?0:accountDetails.size();
	}

	@Override
	public Object getItem(int position) {
		return accountDetails==null?null:accountDetails.get(position);
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
		}else {
			view = convertView;
		}
		TextView itemNameTextView = (TextView) view.findViewById(R.id.account_list_item_tv_name);
		AccountDetail detail = (AccountDetail) getItem(position);
		System.out.println(detail);
		itemNameTextView.setText(detail.getItemName());
		return view;
	}
	
}