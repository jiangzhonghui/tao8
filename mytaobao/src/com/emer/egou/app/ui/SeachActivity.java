package com.emer.egou.app.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emar.escore.banner.BannerSDK;
import com.emar.escore.recommendwall.RecommendSDK;
import com.emar.escore.sdk.view.bannerView;
import com.emer.egou.app.BuildConfig;
import com.emer.egou.app.R;
import com.emer.egou.app.adapter.SeachContentAdapter;
import com.emer.egou.app.adapter.SeachHistoryAdapter;

public class SeachActivity extends BaseActivity implements OnClickListener/*, OnFocusChangeListener*/, OnItemClickListener , OnCheckedChangeListener{
	public static final String REBATE_DETAIL_ACTION = "com.emar.egou.app.rebate_detail";
	private static final String TAG = "SearchActivity";
	private EditText contentEditText;
	private  LinkedHashSet<String> seachHistoryStrings;
	private ImageView clearImageView;
	private ListView seachListView;
	private ImageButton backImButton;
	private Button seachButton;
	private SharedPreferences sharedPreferences;
	private boolean flag;
	private PopupWindow mPopupWindow = null;
	private RadioButton classButton;
	private RadioButton historyButton;
	private SeachHistoryAdapter arrayAdapter;
	private ListView historyListView;
	//private ArrayAdapter<String> adapter;  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seach);
		findView();
		initData();
		setData();
		setListener();
	} 

	private void setData() {
		LinkedHashMap<String,  String[]> groupMaps  =  new LinkedHashMap<String,  String[]>();
		List<String> itemStrings = new ArrayList<String>();
		String address = "连衣裙,短裙,毛衣,针织衫,衬衫,牛仔裤,文胸,打底裤,森系,开衫,大码,外套,皮草,男士";
		groupMaps.put("服饰", address.split(","));
		String makeover = "卸妆,清洁,化妆水,眼霜,防晒,美白 面膜,隔离霜,BB霜,粉底,蜜粉,唇彩,指甲油,香水";
		groupMaps.put("美妆", makeover.split(","));
		String baby_mom = "奶粉,鱼肝油,牛初乳,钙铁锌,DHA,早教书,防辐射,孕装,婴儿装,纸尿裤,玩具,推车,奶瓶,消毒锅";
		groupMaps.put("母婴", baby_mom.split(","));
		String digit = "三星,尼康,索尼,单反,U盘 鼠标,苹果,HTC,加湿器,笔记本,路由器,手机,htc,小米,四核,八核";
		groupMaps.put("数码", digit.split(","));
		String outdoors = "运动鞋,篮球鞋,跑步鞋,板鞋,骑行,耐克,阿迪达斯,李宁,卫衣,运动服,冲锋衣,双肩包,滑雪服";
		groupMaps.put("运动户外", outdoors.split(","));
		String adult = "计生用品,男用器具,女用器具,情趣内衣,情趣用品,避孕套,情趣";
		groupMaps.put("成人",adult.split(","));
		String food = "零食,巧克力,坚果,肉脯,咖啡,茶叶,酒类,特产,牛肉干,减肥,速食品,果冻布丁,果蔬汁,糖果";
		groupMaps.put("食品", food.split(","));
		SeachContentAdapter adapter = new SeachContentAdapter(getApplicationContext(), groupMaps){
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.seach_content_imbtn_icon:
					ImageButton imbtn = (ImageButton) v;
					RelativeLayout itemsLayout = (RelativeLayout) ((LinearLayout)v.getParent().getParent()).findViewById(R.id.seach_content_if_group_detail_items);
					if (itemsLayout.getVisibility()==View.VISIBLE) {
						imbtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_seach_list_more));
						itemsLayout.setVisibility(View.GONE);
					}else {
						imbtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_seach_list_nomore));
						itemsLayout.setVisibility(View.VISIBLE);
					}
					break;
				case R.id.seach_content_list_item_rl_title:
					//System.out.println(v.getParent());
					RelativeLayout itemsRelativeLayout = (RelativeLayout) ((LinearLayout)v.getParent()).findViewById(R.id.seach_content_if_group_detail_items);
					ImageButton iconImageView = (ImageButton)((LinearLayout)v.getParent()).findViewById(R.id.seach_content_imbtn_icon);
					if (itemsRelativeLayout.getVisibility()==View.VISIBLE) {
						iconImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_seach_list_more));
						itemsRelativeLayout.setVisibility(View.GONE);
					}else {
						iconImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_seach_list_nomore));
						itemsRelativeLayout.setVisibility(View.VISIBLE);
					}
					break;
				case R.id.seach_content_item_tv:
					/*if (BuildConfig.DEBUG) {
						Toast.makeText(getApplicationContext(), ((TextView)v).getText().toString(), 0).show();
					}*/
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), ProductsActivity.class);
					intent.putExtra("keyword", ((TextView)v).getText().toString());
					intent.putExtra("isFromSeach", true);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		};
		seachListView.setAdapter(adapter);
		arrayAdapter = new SeachHistoryAdapter(SeachActivity.this, seachHistoryStrings);
		//arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.seach_content_item_textview, autoStrings);
		historyListView.setAdapter(arrayAdapter);
	}
	private void setListener() {
		// TODO Auto-generated method stub
		seachButton.setOnClickListener(this);
		clearImageView.setOnClickListener(this);
		backImButton.setOnClickListener(this);
		//contentEditText.setOnFocusChangeListener(this);
		//seachListView.setOnItemClickListener(this);
		
		classButton.setOnCheckedChangeListener(this);
		historyButton.setOnCheckedChangeListener(this);
		
		historyListView.setOnItemClickListener(this);
	}

	private void initData() {
		if (seachHistoryStrings==null) {
			seachHistoryStrings = new LinkedHashSet<String>();
		}
		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		String stoneString = sharedPreferences.getString("autoStringStored", "");
		String[] split = stoneString.trim().split(",");
				for (int i = 0; i < split.length; i++) {
					seachHistoryStrings.add(split[i]);
				}
	}

	private void findView() {
		backImButton = (ImageButton) findViewById(R.id.seach_head_imbtn_back);
		seachButton = (Button) findViewById(R.id.seach_head_btn_seach);
		seachListView = (ListView) findViewById(R.id.seach_lv_seach_item_class);
		historyListView = (ListView) findViewById(R.id.seach_lv_seach_item_history);
		contentEditText = (EditText) findViewById(R.id.seach_head_actv_content);
		clearImageView = (ImageView) findViewById(R.id.seach_iv_content_clear);
		classButton = (RadioButton) findViewById(R.id.seach_rb_class);
		historyButton = (RadioButton) findViewById(R.id.seach_rb_history);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.seach_head_imbtn_back:
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "account_head_btn_back");
			}
			
			//onBackPressed();
			break;
		case R.id.seach_iv_content_clear:
			contentEditText.setText("");
			break;
		case R.id.seach_head_btn_seach:
			
			
			
			RecommendSDK.getInstance(SeachActivity.this).showAdlist();
			/*String content = contentEditText.getText().toString();
			if (TextUtils.isEmpty(content)) {
				Toast.makeText(getApplicationContext(), "亲,搜索内容不能为空！", 0).show();
				return;
			}
			//
			storeStringAndCheck(content);
			//通知搜索历史更新
			if (historyButton.isChecked()) {
				arrayAdapter.notifyDataSetChanged();
			}
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ProductsActivity.class);
			intent.putExtra("keyword", content);
			intent.putExtra("isFromSeach", true);
			startActivity(intent);*/
			break;
		default:
			break;
		}
		// TODO 将字符串切割,存到share里面,每次更新,去除老的
	}
	/**
	 * 
	 * @param content
	 * @return 如果返回true说明包含该搜索历史
	 */
	private boolean storeStringAndCheck(String content) {
		if (seachHistoryStrings.contains(content)) {
			return true;
		}else {
			seachHistoryStrings.add(content);
		}
		StringBuilder sb = new StringBuilder();
		int i = 0;
	
		for (String history : seachHistoryStrings) {
			sb.append(history).append(",");
		}
		//去除多余的项目
		int size = seachHistoryStrings.size();
		while (seachHistoryStrings.size()>15) {
			seachHistoryStrings.remove(--size);
		}
		Editor edit = sharedPreferences.edit();
		edit.putString("autoStringStored", sb.toString());
		edit.commit();
		return false;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.seach_rb_class:
			if (isChecked) {
				if (historyListView.getVisibility()==View.VISIBLE) {
					historyListView.setVisibility(View.GONE);
				}
				if (seachListView.getVisibility()==View.GONE) {
					seachListView.setVisibility(View.VISIBLE);
				}
			}
			break;
		case R.id.seach_rb_history:
			if (isChecked) {
				if (historyListView.getVisibility()==View.GONE) {
					historyListView.setVisibility(View.VISIBLE);
				}
				if (seachListView.getVisibility()==View.VISIBLE) {
					seachListView.setVisibility(View.GONE);
				}
			}
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.seach_lv_seach_item_history:
			String history =parent.getAdapter().getItem(position).toString();
			contentEditText.setText(history);
			break;
		default:
			break;
		}
		
	}

	/*@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
		if (hasFocus) {
			View view  = View.inflate(getApplicationContext(), R.layout.seach_popu_seach_history, null);
			ListView listView = (ListView) view.findViewById(R.id.seach_popu_lv_history);
			int width = contentEditText.getWidth();
			if (mPopupWindow==null) {
				mPopupWindow = new PopupWindow(view,width,width);
			}
			mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.review_bg_blue)); 
			listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.seach_content_item_textview, autoStrings));
			mPopupWindow.showAsDropDown(contentEditText);
		}else {
			if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
		}
		
	}*/

/*	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		RelativeLayout itemsLayout = (RelativeLayout) view.findViewById(R.id.seach_content_if_group_detail_items);
		ImageView iconImageView = (ImageView) view.findViewById(R.id.seach_content_iv_icon);
		if (itemsLayout.getVisibility()==View.VISIBLE) {
			iconImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_seach_list_more));
			itemsLayout.setVisibility(View.GONE);
		}else {
			iconImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_seach_list_nomore));
			itemsLayout.setVisibility(View.VISIBLE);
		}
	}
	*/
	
}
