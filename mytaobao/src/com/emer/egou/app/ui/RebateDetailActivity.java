package com.emer.egou.app.ui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.emer.egou.app.BuildConfig;
import com.emer.egou.app.R;
import com.emer.egou.app.domain.RebateOrder;
import com.emer.egou.app.widget.MySearchContentListView;

public class RebateDetailActivity extends BaseActivity{
	private TableLayout rebatesLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rebate_detail);
		findView();
		setListener();
		initData();
		setData();
	}

	private void setData() {
		
	}

	private void addRow(List<RebateOrder> rebateOrders,TableLayout rebatesLayout) {
		for (RebateOrder rebateOrder : rebateOrders) {
			TableRow tableRow = (TableRow) View.inflate(getApplicationContext(), R.layout.rebate_detail_tablerow, null);
			TextView noTextView = (TextView) tableRow.findViewById(R.id.rebate_detail_row_tv_order_no);
			TextView priceTextView = (TextView) tableRow.findViewById(R.id.rebate_detail_row_tv_order_price);
			TextView rebateTextView = (TextView) tableRow.findViewById(R.id.rebate_detail_row_tv_order_rebate);
			TextView timeTextView = (TextView) tableRow.findViewById(R.id.rebate_detail_row_tv_order_time);
			TextView stateTextView = (TextView) tableRow.findViewById(R.id.rebate_detail_row_tv_order_state);
			
			
			
			noTextView.setText("3333333333333");
			priceTextView.setText("13.6元");
			rebateTextView.setText("0.23元");
			timeTextView.setText("2011.11.23");
			stateTextView.setText("正在审核");
			rebatesLayout.addView(tableRow, rebatesLayout.getChildCount()-1);
		}
		
	}

	private void initData() {
		// TODO 获取订单
		
	}

	private void setListener() {
		
		
		
	}

	private void findView() {
		
		
		rebatesLayout = (TableLayout) findViewById(R.id.rebate_detail_tablel_rebates);
		
	}
}
