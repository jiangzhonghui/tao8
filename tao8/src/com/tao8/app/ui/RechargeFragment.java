package com.tao8.app.ui;

import java.util.ArrayList;
import java.util.regex.Pattern;

import com.tao8.app.R;
import com.tao8.app.adapter.RechargeAdapter;
import com.tao8.app.ws.NumberService;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class RechargeFragment extends Fragment implements OnItemClickListener, OnClickListener, OnFocusChangeListener{

	private GridView rechargeItmes;
	private EditText numEditText;
	private Button buyButton;
	private TextView phoneAddressTextView;

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
		rechargeItmes = (GridView) view.findViewById(R.id.recharge_gv_money_to_recharge);
		numEditText = (EditText) view.findViewById(R.id.recharge_et_phoneNum);
		numEditText.setOnClickListener(this);
		numEditText.setOnFocusChangeListener(this);
		buyButton = (Button) view.findViewById(R.id.recharge_btm_buy);
		phoneAddressTextView = (TextView) view.findViewById(R.id.recharge_tv_phone_address);
		ArrayList<String> moneyItems = new ArrayList<String>();
		moneyItems.add("10");
		moneyItems.add("20");
		moneyItems.add("30");
		moneyItems.add("50");
		moneyItems.add("100");
		moneyItems.add("200");
		rechargeItmes.setAdapter(new RechargeAdapter(getActivity(),moneyItems));
		rechargeItmes.setOnItemClickListener(this);
		return view;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		numEditText.setCursorVisible(false);
		numEditText.setFocusableInTouchMode(false);
		numEditText.clearFocus();
		view.setBackgroundColor(Color.YELLOW);
		ImageView selectedImageView = (ImageView) view.findViewById(R.id.recharge_gridview_item_im_selected);
		selectedImageView.setVisibility(View.VISIBLE);
		int childSize = rechargeItmes.getChildCount();
		for (int i = 0; i < childSize; i++) {
			View child = rechargeItmes.getChildAt(i);
			if (!(child!=null&&child==view)) {
				child.setBackgroundColor(Color.WHITE);
				ImageView selected = (ImageView) child.findViewById(R.id.recharge_gridview_item_im_selected);
				selected.setVisibility(View.GONE);
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.recharge_et_phoneNum:
			numEditText.setCursorVisible(true);
			numEditText.setFocusableInTouchMode(true);
			numEditText.requestFocus();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			EditText editText = (EditText) v;
			String num = editText.getText().toString().trim();
			if (!checkNumber(num)) {
				Toast.makeText(getActivity(), "号码格式不正确", 0).show();
			}else {
				 try {  
			            NumberService service = new NumberService();  
			            String loacation = service.getLocation(num);  
			            phoneAddressTextView.setText(loacation);  
			        } catch (Exception e) {  
			            e.printStackTrace();  
			            Toast.makeText(getActivity(), "查无此号", 1).show();  
			        }  
			}
		}
		
	}

	private boolean checkNumber(String num) {
		String regex = "^1[3-8]+\\d{9}$";
		return num.matches(regex);
	}
}
