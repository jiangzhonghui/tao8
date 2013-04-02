package com.emer.egou.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emer.egou.app.AppException;
import com.emer.egou.app.BuildConfig;
import com.emer.egou.app.R;
import com.emer.egou.app.TopConfig;
import com.emer.egou.app.adapter.ProductsAdapter;
import com.emer.egou.app.api.GetTopData;
import com.emer.egou.app.api.MyTqlListener;
import com.emer.egou.app.cache.util.ImageLoader;
import com.emer.egou.app.domain.SearchItem;
import com.emer.egou.app.parser.SearchItemParser;
import com.emer.egou.app.parser.TmallToTaokeItemParser;
import com.emer.egou.app.util.CommonUtil;
import com.emer.egou.app.util.TopStatParmUtil;
import com.emer.egou.app.util.TqlHelper;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.auth.AccessToken;

public class ProductsActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener, OnCheckedChangeListener {
	private static final String TAG = "ProductsActivity";
	private ListView productsListView;
	private List<SearchItem> resultList;
	private TopAndroidClient client = TopConfig.client;
	private SharedPreferences sharedPreferences;
	private int pageSize = 10;
	private ProductsAdapter productsAdapter;
	private List<RadioButton> radioButtons;
	private String keyword;
	private boolean isAll;
	private boolean isTmall = false;
	private ImageView seachButton;
	private ProgressDialog pd;
	private Button nextButton;
	private LinearLayout orderByLayout;
	private boolean isTmallCompetitive;
	private ImageView headBackImageView;
	private RelativeLayout headLayout;
	private TextView lableTextView;
	private RadioGroup lablesRadioGroup;
	//
	private CheckBox volumeCheckBox;
	private boolean isVolumePressed;
	private boolean isPricePressed;
	private CheckBox priceCheckBox;
	
	//
	private String sort;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products);
		findView();
		handIntent();
		/*getData();
		setData();*/
		setListener();
	}

	private void handIntent() {
		if (resultList == null) {
			resultList = new ArrayList<SearchItem>();
		}
		Intent intent = getIntent();
		isTmallCompetitive = intent.getBooleanExtra("isTmallCompetitive", false);
	
		keyword = intent.getStringExtra("keyword");
		boolean isFromSeach = intent.getBooleanExtra("isFromSeach", false);
		//从搜索的地方跳过来的
		if (isFromSeach&&keyword!=null) {
			volumeCheckBox.setWidth(getWindowManager().getDefaultDisplay().getWidth() / 2);
			priceCheckBox.setWidth(getWindowManager().getDefaultDisplay().getWidth() / 2);
			orderByLayout.setVisibility(View.VISIBLE);
			lablesRadioGroup.setVisibility(View.GONE);
			lableTextView.setText(keyword);
			//默认价格从低到高
			priceCheckBox.setBackgroundResource(R.drawable.bg_lable_pressed);
			volumeCheckBox.setBackgroundResource(R.drawable.bg_lable_mormal);
			sort = "commissionNum_desc";
			seachTaobaokeFromKeyWord(keyword, sort, isTmall, isAll, 1);
		}else {
			orderByLayout.setVisibility(View.GONE);
			lablesRadioGroup.setVisibility(View.VISIBLE);
			if (isTmallCompetitive) {
				lableTextView.setText("天猫特卖");
			}else {
				lableTextView.setText("淘宝人气");
			}
			getData();
		}
		setData();
		
	}

	private void getData() {
		resultList.clear();
		keyword = "0";
		isAll = true;
		isTmall = isTmallCompetitive;
		if (CommonUtil.checkNetState(getApplicationContext())) {
			seachTaobaokeFromKeyWord(keyword, "commissionNum_desc", isTmall, isAll, 1);
		} else {
			Toast.makeText(getApplicationContext(), "网络异常请检查网络", 0).show();
		}
		
	}

	private void setListener() {
		// TODO Auto-generated method stub
		productsListView.setOnItemClickListener(this);
		for (RadioButton radioButton : radioButtons) {
			radioButton.setOnClickListener(this);
		}
		seachButton.setOnClickListener(this);
		headBackImageView.setOnClickListener(this);
		volumeCheckBox.setOnCheckedChangeListener(this);
		volumeCheckBox.setOnClickListener(this);
		priceCheckBox.setOnCheckedChangeListener(this);
		priceCheckBox.setOnClickListener(this);
	}

	private void setData() {
		if (productsAdapter == null) {
			productsAdapter = new ProductsAdapter(getApplicationContext(),
					resultList, productsListView);
		}
		if (resultList != null && resultList.size() > 0) {
			if (productsListView.getAdapter() == null) {
				nextButton = new Button(this);
				nextButton.setText("加载下一页");
				nextButton.setGravity(Gravity.CENTER);
				nextButton.setId(123456);
				productsListView.addFooterView(nextButton, null, true);
				productsListView.setAdapter(productsAdapter);
				nextButton.setOnClickListener(this);
			}
		} else {
			// TODO: 没有数据
		}
	
	}

	private void findView() {
		radioButtons = new ArrayList<RadioButton>();
		productsListView = (ListView) findViewById(R.id.products_lv_all);

		RadioButton aRadioButton = (RadioButton) findViewById(R.id.products_lable_rbtn_0);
		RadioButton radioButton1 = (RadioButton) findViewById(R.id.products_lable_rbtn_1);
		RadioButton radioButton2 = (RadioButton) findViewById(R.id.products_lable_rbtn_2);
		RadioButton radioButton3 = (RadioButton) findViewById(R.id.products_lable_rbtn_3);
		RadioButton radioButton4 = (RadioButton) findViewById(R.id.products_lable_rbtn_4);
		RadioButton radioButton5 = (RadioButton) findViewById(R.id.products_lable_rbtn_5);
		RadioButton radioButton6 = (RadioButton) findViewById(R.id.products_lable_rbtn_6);
		RadioButton radioButton7 = (RadioButton) findViewById(R.id.products_lable_rbtn_7);
		RadioButton radioButton8 = (RadioButton) findViewById(R.id.products_lable_rbtn_8);
		RadioButton radioButton9 = (RadioButton) findViewById(R.id.products_lable_rbtn_9);
		radioButtons.add(aRadioButton);
		radioButtons.add(radioButton1);
		radioButtons.add(radioButton2);
		radioButtons.add(radioButton3);
		radioButtons.add(radioButton4);
		radioButtons.add(radioButton5);
		radioButtons.add(radioButton6);
		radioButtons.add(radioButton7);
		radioButtons.add(radioButton8);
		radioButtons.add(radioButton9);
		seachButton = (ImageView) findViewById(R.id.products_head_img_search);
		orderByLayout = (LinearLayout) findViewById(R.id.products_seach_details_order_ll_orderby);
		headBackImageView = (ImageView) findViewById(R.id.products_head_img_back);
		headLayout = (RelativeLayout) findViewById(R.id.products_head_rl_lable);
		lableTextView = (TextView) findViewById(R.id.products_head_tv_lable);
		lablesRadioGroup = (RadioGroup) findViewById(R.id.products_rg_lables);
		//排序
		volumeCheckBox = (CheckBox) findViewById(R.id.products_seach_details_cb_orderby_sales_volume);
		priceCheckBox = (CheckBox) findViewById(R.id.products_seach_details_cb_orderby_price);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		Long userId = sharedPreferences.getLong("userId", 10000);
		AccessToken accessToken = client.getAccessToken(userId);
		if (userId == 0l || accessToken == null) {
			Toast t = Toast.makeText(ProductsActivity.this, "请先授权",Toast.LENGTH_SHORT);
			t.show();
			client.authorize(ProductsActivity.this);
			return;
		}
		SearchItem searchItem = resultList.get(position);
		String url = searchItem.getClick_url();
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		sb.append("&unid=0OVOaV2GWly3111");
		sb.append("&ttid=");
		sb.append(TopStatParmUtil.getTTID(ProductsActivity.this));
		sb.append("&sid=");
		sb.append(accessToken == null ? TopStatParmUtil
				.getDefaultSid(ProductsActivity.this) : accessToken
				.getAdditionalInformation().get(AccessToken.KEY_MOBILE_TOKEN));
		Intent it = new Intent();
		it.setAction(BrowserActivity.BROWSERACTIVITY_ACTION);
		it.putExtra(BrowserActivity.BROWSERACTIVITY_URI, sb.toString());
		it.putExtra("title", searchItem.getTitle());
		startActivity(it);
	}

	@Override
	public void onClick(View v) {
		if (v instanceof RadioButton) {
			keyword = ((RadioButton) v).getText().toString();
		}
		switch (v.getId()) {
		case 123456:
			int page_no = (resultList.size() / pageSize) + 1;
			if (isTmallCompetitive) {
				getFromTmallByKeyWords(keyword, "s", null, page_no);
			}else {
				seachTaobaokeFromKeyWord(keyword, "commissionNum_desc", isTmall, isAll,
						page_no);
			}
			break;
		case R.id.products_lable_rbtn_0:
			resultList.clear();
			keyword = "0";
			isAll = true;
			seachTaobaokeFromKeyWord(keyword, "commissionNum_desc", isTmall, isAll, 1);
			break;
		case R.id.products_head_img_back:
			onBackPressed();
			break;
		case R.id.products_seach_details_cb_orderby_price:
			priceCheckBox.setBackgroundResource(R.drawable.bg_lable_pressed);
			volumeCheckBox.setBackgroundResource(R.drawable.bg_lable_mormal);
			//priceCheckBox.toggle();
			if (!isPricePressed) {
				priceCheckBox.toggle();
				isPricePressed=true;
				isVolumePressed = false;
			}
			seachTaobaokeFromKeyWord(keyword, sort, false, false, 1);
			
			break;
		case R.id.products_seach_details_cb_orderby_sales_volume:
			priceCheckBox.setBackgroundResource(R.drawable.bg_lable_mormal);
			volumeCheckBox.setBackgroundResource(R.drawable.bg_lable_pressed);
			if (!isVolumePressed) {
				volumeCheckBox.toggle();
				isVolumePressed=true;
				isPricePressed = false;
			}
			seachTaobaokeFromKeyWord(keyword, sort, false, false, 1);
			break;
		default:
			if (v instanceof RadioButton) {
				if (isTmallCompetitive) {
					System.out.println("点击");
					getFromTmallByKeyWords(keyword, "s", null, 1);
				} else {
					isAll = false;
					seachTaobaokeFromKeyWord(keyword, "commissionNum_desc", isTmall, isAll, 1);
				}
			}
			break;
		}
	}

	/**
	 * 
	 * @param q
	 *            表示搜索的关键字，例如搜索query=nike。 当输入关键字为中文时，将对他进行URLEncode的UTF-8格式编码，如
	 *            耐克，那么q=%E8%80%90%E5%85%8B。
	 * @param sort
	 *            排序类型。类型包括： s: 人气排序 p: 价格从低到高; pd: 价格从高到低; d: 月销量从高到低; td:
	 *            总销量从高到低; pt: 按发布时间排序.
	 * @param cat
	 *            前台类目id，目前其他接口无法获取，
	 *            只能自己去寻找。建议使用关键字获取数据。支持多选过滤，cat=catid1,catid2。
	 * @param start
	 *            可以用该字段来实现分页功能。表示查询起始位置，
	 *            默认从第0条开始，start=10,表示从第10条数据开始查询，start不得大于1000。
	 */
	private void getFromTmallByKeyWords(String q, String sort, String cat,
			final int pageNo) {
		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		long userId = sharedPreferences.getLong("userId", 10000);
		AccessToken accessToken = client.getAccessToken(userId);
		String tql = "";
		/*if (userId == 0l || accessToken == null) {
			Toast t = Toast.makeText(ProductsActivity.this, "请先授权",
					Toast.LENGTH_SHORT);
			t.show();
			client.authorize(ProductsActivity.this);
			return;
		}*/
		/**
		 * select num_iid,title,click_url from
		 * taobao.taobaoke.widget.items.convert where num_iids in (select
		 * item_id from tmall.items.discount.search where q = %e6%89%8b%e6%9c%ba
		 * )
		 */ 
		List<String> fields = new ArrayList<String>();
		fields.add("item_id");
		Map<String, String> params = new HashMap<String, String>();
		//params.put("q", "%e6%89%8b%e6%9c%ba");
		if (!TextUtils.isEmpty(q)) {
			params.put("q", Uri.encode(q));
		}
		if (!TextUtils.isEmpty(cat)) {
			params.put("cat", cat);
		}
		if (TextUtils.isEmpty(q)&&TextUtils.isEmpty(cat)) {
			throw new RuntimeException("关键字q和前台类目不能同时为空！");
		}
		int start = pageNo * pageSize;
		params.put("start", Integer.toString(start));// 最多1000页
		params.put("sort", sort);
		String tmallTql = TqlHelper.generateTMallTql(fields, params);
		tql = TqlHelper.generateTMallConvertToTaoKenestTql(SearchItem.class,
				"num_iids", tmallTql);
		System.out.println(tql);
		if (pd==null) {
			pd = new ProgressDialog(ProductsActivity.this);
			pd.setMessage("数据努力加载中......");
			//pd = ProgressDialog.show(ProductsActivity.this, null, "数据加载中......");
		}
		if (!pd.isShowing()) {
			pd.show();
		}
		if (nextButton != null) {
			nextButton.setText("数据加载中......");
		}
		GetTopData.getDataFromTop(tql, new TmallToTaokeItemParser(),userId,
				new MyTqlListener() {
					@Override
					public void onResponseException(Object apiError) {
						if (pd.isShowing()) {
							pd.cancel();
						}
						Toast.makeText(getApplicationContext(),
								((ApiError) apiError).getMsg(), 0).show();
						if (BuildConfig.DEBUG) {
							Log.i(TAG, apiError.toString());
						}

					}

					@Override
					public void onException(Exception e) {
						if (pd.isShowing()) {
							pd.cancel();
						}
						AppException.network(e).makeToast(getApplicationContext());
						if (nextButton!=null) {
							nextButton.setText("加载下一页");
						}
					}

					@Override
					public void onComplete(Object result) {
						// TODO Auto-generated method stub
						ArrayList<SearchItem> results = (ArrayList) result;
						if (results != null && results.size() > 0) {
							if (pd.isShowing()) {
								pd.cancel();
							}
							if (pageNo == 1) {
								resultList.clear();
							}
							resultList.addAll(results);
							setData();
							// Toast.makeText(getApplicationContext(), "数据过来了",
							// 0).show();
							productsAdapter.notifyDataSetChanged();
							int page_no = (resultList.size() / pageSize) + 1;
							if (nextButton != null) {
								if (page_no*pageSize == 11) {
									nextButton.setText("已经是最后一页了");
								} else {
									nextButton.setText("加载下一页");
								}
							}
						} else {
							// TODO:

						}
						if (BuildConfig.DEBUG) {
							Log.i(TAG, Integer.toString(resultList.size()));
						}
					}
				});

	}

	/**
	 * 淘客api请求的方法
	 * 
	 * @param keyword
	 *            关键字 (如果是全部,则为cid = 0);
	 * @param sort
	 *            排序规则 sort String 可选 price_desc 默认排序:default price_desc(价格从高到低)
	 *            price_asc(价格从低到高) credit_desc(信用等级从高到低)
	 *            commissionRate_desc(佣金比率从高到低) commissionRate_asc(佣金比率从低到高)
	 *            commissionNum_desc(成交量成高到低) commissionNum_asc(成交量从低到高)
	 *            commissionVolume_desc(总支出佣金从高到低)
	 *            commissionVolume_asc(总支出佣金从低到高) delistTime_desc(商品下架时间从高到低)
	 *            delistTime_asc(商品下架时间从低到高)
	 * @param isFromTmall
	 *            是否请求天猫的数据
	 * @param isAll
	 *            是否请求的全部数据
	 * @param page_no
	 *            请求的页数
	 */
	private void seachTaobaokeFromKeyWord(String keyword, String sort,
			boolean isFromTmall, boolean isAll, final int page_no) {
		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		long userId = sharedPreferences.getLong("userId", 10000);
		AccessToken accessToken = client.getAccessToken(userId);
		String tql = "";
		/*if (userId == 0l || accessToken == null) {
			Toast t = Toast.makeText(ProductsActivity.this, "请先授权",
					Toast.LENGTH_SHORT);
			t.show();
			client.authorize(ProductsActivity.this);
			return;
		}*/
		if (page_no == 11) {
			Toast.makeText(getApplicationContext(), "已经是最后一页了", 0).show();
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		if (isAll) {
			params.put("cid", keyword);
		} else {
			params.put("keyword", keyword);
		}
		params.put("page_size", Integer.toString(pageSize));// 最大40个,
		params.put("page_no", Integer.toString(page_no));// 最多10页
		params.put("mall_item", isFromTmall + "");
		params.put("sort", sort);
		tql = TqlHelper.generateTaoBaoKeTql(SearchItem.class, params);
		System.out.println(tql);
		if (pd==null) {
			pd = new ProgressDialog(ProductsActivity.this);
			pd.setMessage("数据努力加载中......");
			//pd = ProgressDialog.show(ProductsActivity.this, null, "数据加载中......");
		}
		if (!pd.isShowing()) {
			pd.show();
		}
		if (nextButton != null) {
			nextButton.setText("数据加载中......");
		}
		GetTopData.getDataFromTop(tql, new SearchItemParser(), userId,
				new MyTqlListener() {
					@Override
					public void onComplete(Object result) {
						ArrayList<SearchItem> results = (ArrayList) result;
						if (BuildConfig.DEBUG) {
							if (results != null) {
								Log.i(TAG, Integer.toString(results.size()));
							}
						}
						if (results != null && results.size() > 0) {
							if (pd.isShowing()) {
								pd.cancel();
							}
							if (page_no == 1) {
								resultList.clear();
							}
							resultList.addAll(results);
							setData();
							// Toast.makeText(getApplicationContext(), "数据过来了",
							// 0).show();
							productsAdapter.notifyDataSetChanged();
							int page_no = (resultList.size() / pageSize) + 1;
							if (nextButton != null) {
								if (page_no == 11) {
									nextButton.setText("已经是最后一页了");
								} else {
									nextButton.setText("加载下一页");
								}
							}
						} else {
							// TODO:

						}
					}

					@Override
					public void onException(Exception e) {
						if (pd.isShowing()) {
							pd.cancel();
						}
						AppException.network(e).makeToast(getApplicationContext());
						if (nextButton!=null) {
							nextButton.setText("加载下一页");
						}
					}

					@Override
					public void onResponseException(Object apiError) {
						if (pd.isShowing()) {
							pd.cancel();
						}
						Toast.makeText(getApplicationContext(),
								((ApiError) apiError).getMsg(), 0).show();
						if (BuildConfig.DEBUG) {
							Log.i(TAG, apiError.toString());
						}
					}
				});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (productsAdapter != null) {
			ImageLoader imageLoader = productsAdapter.getImageLoader();
			if (imageLoader != null) {
				imageLoader.clearCache();
			}
		}
		super.onDestroy();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		/*
		 price_desc 默认排序:default price_desc(价格从高到低) price_asc(价格从低到高) 
		 credit_desc(信用等级从高到低) commissionRate_desc(佣金比率从高到低) 
		 commissionRate_asc(佣金比率从低到高)
		  commissionNum_desc(成交量成高到低) commissionNum_asc(成交量从低到高) 
		  commissionVolume_desc(总支出佣金从高到低) commissionVolume_asc(总支出佣金从低到高) 
		  delistTime_desc(商品下架时间从高到低) delistTime_asc(商品下架时间从低到高)

		 */
		case R.id.products_seach_details_cb_orderby_price:
			if (isChecked) {
				buttonView.setText("价格从高到低");
				sort = "price_desc";
			}else {
				buttonView.setText("价格从低到高");
				sort = "price_asc";
			}
			break;
		case R.id.products_seach_details_cb_orderby_sales_volume:
			if (isChecked) {
				buttonView.setText("销量从低到高");
				sort = "commissionNum_asc";
			}else {
				buttonView.setText("销量从高到低");
				sort = "commissionNum_desc";
			}
			break;

		default:
			break;
		}
		if (isChecked) {
			
		}else {
			
		}
		
	}
}
