package com.tao8.app.ui;

import java.util.ArrayList;

import com.tao8.app.R;
import com.tao8.app.adapter.RechargeAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class RechargeFragment extends Fragment{

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.recharge, null);
		GridView rechargeItmes = (GridView) view.findViewById(R.id.recharge_gv_money_to_recharge);
		ArrayList<String> moneyItems = new ArrayList<String>();
		moneyItems.add("10");
		moneyItems.add("20");
		moneyItems.add("30");
		moneyItems.add("50");
		moneyItems.add("100");
		moneyItems.add("200");
		rechargeItmes.setAdapter(new RechargeAdapter(getActivity(),moneyItems));
		return view;
	}
}
