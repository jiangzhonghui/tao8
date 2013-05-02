package com.tao8.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.domob.android.ads.DomobAdView;

import com.tao8.app.AppException;
import com.tao8.app.BuildConfig;
import com.tao8.app.R;
import com.tao8.app.TopConfig;
import com.tao8.app.adapter.RechargeAdapter;
import com.tao8.app.api.GetTopData;
import com.tao8.app.api.MyTqlListener;
import com.tao8.app.domain.SearchItem;
import com.tao8.app.parser.SearchItemParser;
import com.tao8.app.parser.TmallToTaokeItemParser;
import com.tao8.app.util.CommonUtil;
import com.tao8.app.util.LogUtil;
import com.tao8.app.util.TqlHelper;
import com.tao8.app.ws.NumberService;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.auth.AccessToken;

public class RechargeFragment extends Fragment implements OnItemClickListener,
		OnClickListener, OnFocusChangeListener {

	private static final String TAG = "RechargeFragment";
	protected static final int GET_ADDRESS_SUCCESS = 0x00;
	protected static final int GET_ADDRESS_FAILED = 0x01;
	private GridView rechargeItmes;
	private EditText numEditText;
	private Button buyButton;
	private TextView phoneAddressTextView;
	private SharedPreferences sharedPreferences;
	private int pageSize = 10;
	String card = null;
	private String q;
	private ProgressBar pb;
	private String address;
	private TextView priceTextView;
	private Map<String, SearchItem> resultMap;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_ADDRESS_FAILED:
				Toast.makeText(getActivity(), "获取归属地失败！", 0).show();
				phoneAddressTextView.setText("获取归属地失败");
				pb.setVisibility(View.GONE);
				headProgressBar.setVisibility(View.GONE);
				break;
			case GET_ADDRESS_SUCCESS:
				pb.setVisibility(View.GONE);
				headProgressBar.setVisibility(View.GONE);
				String phoneAddress = msg.obj.toString();
				address = phoneAddress.substring(phoneAddress.lastIndexOf(" "),
						phoneAddress.length());
				phoneAddressTextView.setText(address);
				if (BuildConfig.DEBUG) {
					Toast.makeText(getActivity(), address, 1).show();
				}

				break;
			default:
				break;
			}
		};
	};
	private long userId;
	private ImageView moreImageView;
	private ProgressBar headProgressBar;
	private DomobAdView mAdview320x50;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		sharedPreferences = getActivity().getSharedPreferences("config",
				Context.MODE_PRIVATE);
		View view = inflater.inflate(R.layout.recharge_fragment, null);
		rechargeItmes = (GridView) view
				.findViewById(R.id.recharge_gv_money_to_recharge);
		numEditText = (EditText) view.findViewById(R.id.recharge_et_phoneNum);
		numEditText.setOnClickListener(this);
		numEditText.setOnFocusChangeListener(this);
		numEditText.setText(sharedPreferences.getString("phoneNum", ""));
		buyButton = (Button) view.findViewById(R.id.recharge_btm_buy);
		buyButton.setOnClickListener(this);
		phoneAddressTextView = (TextView) view
				.findViewById(R.id.recharge_tv_phone_address);
		pb = (ProgressBar) view.findViewById(R.id.recharge_progressBar);
		headProgressBar = (ProgressBar) view
				.findViewById(R.id.head_progressBar);
		priceTextView = (TextView) view.findViewById(R.id.recharge_tv_price);
		moreImageView = (ImageView) view.findViewById(R.id.head_iv_more);
		TextView goMenuTextView = (TextView) view
				.findViewById(R.id.head_tv_go_menu);
		goMenuTextView.setOnClickListener(this);
		moreImageView.setVisibility(View.GONE);
		// ///////
		RelativeLayout adRelativeLayout = (RelativeLayout) view
				.findViewById(R.id.recharge_rl_ad);

		mAdview320x50 = new DomobAdView(getActivity(), TopConfig.PUBLISHER_ID,
				DomobAdView.INLINE_SIZE_320X50);
		// 将广告View增加到视图中。
		adRelativeLayout.removeAllViews();
		adRelativeLayout.addView(mAdview320x50);

		// /////

		ArrayList<String> moneyItems = new ArrayList<String>();
		moneyItems.add("10");
		moneyItems.add("20");
		moneyItems.add("30");
		moneyItems.add("50");
		moneyItems.add("100");
		moneyItems.add("200");
		rechargeItmes
				.setAdapter(new RechargeAdapter(getActivity(), moneyItems));
		rechargeItmes.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		numEditText.setCursorVisible(false);
		numEditText.setFocusableInTouchMode(false);
		numEditText.clearFocus();
		String num = numEditText.getText().toString();
		if (TextUtils.isEmpty(num.trim())) {
			Toast.makeText(getActivity(), "号码不能为空", 0).show();
			return;
		}
		if (!checkNumber(num)) {
			Toast.makeText(getActivity(), "号码格式不正确！", 0).show();
			return;
		}
		if (!CommonUtil.checkNetState(getActivity())) {
			Toast.makeText(getActivity(), "对不起,您的网络不可用....", 0).show();
			return;
		}
		if (address == null) {
			getAddressFromServer(num.trim());
			Toast.makeText(getActivity(), "稍等,正在获取号码归属地......", 0).show();
			return;
		}
		view.setBackgroundColor(Color.YELLOW);
		ImageView selectedImageView = (ImageView) view
				.findViewById(R.id.recharge_gridview_item_im_selected);
		TextView cardTextView = (TextView) view
				.findViewById(R.id.recharge_gridview_item_tv);
		card = cardTextView.getText().toString();
		selectedImageView.setVisibility(View.VISIBLE);
		int childSize = rechargeItmes.getChildCount();
		for (int i = 0; i < childSize; i++) {
			View child = rechargeItmes.getChildAt(i);
			if (!(child != null && child == view)) {
				child.setBackgroundColor(Color.WHITE);
				ImageView selected = (ImageView) child
						.findViewById(R.id.recharge_gridview_item_im_selected);
				selected.setVisibility(View.GONE);
			}
		}
		// q = 归属地 +card；

		String subaddress = address.substring(0, 3);
		String mobile ="";
		if (address.contains("电信")) {
			mobile = "电信";
		}
		if (address.contains("移动")) {
			mobile = "移动";
		}
		if (address.contains("联通")) {
			mobile = "联通";
		}
		q = subaddress +mobile+ " 充值" + " " + card;
		String sort = "price_asc";
		if (resultMap == null || resultMap.get(q) == null) {
			seachTaobaokeFromKeyWord(q, sort, true, false, 1);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_tv_go_menu:
			if (getActivity() instanceof ViewPagerActivity) {
				ViewPagerActivity viewPagerActivity = (ViewPagerActivity) getActivity();
				viewPagerActivity.showMenu();
			}

			break;
		case R.id.recharge_et_phoneNum:
			numEditText.setCursorVisible(true);
			numEditText.setFocusableInTouchMode(true);
			numEditText.requestFocus();
			break;
		case R.id.recharge_btm_buy:
			String phoneNum = numEditText.getText().toString().trim();
			if (!CommonUtil.checkNetState(getActivity())) {
				v.startAnimation(AnimationUtils.loadAnimation(getActivity(),
						R.anim.shake));
				Toast.makeText(getActivity(), "网络不给力,检查网络", 0).show();
				return;
			}
			if (TextUtils.isEmpty(phoneNum.trim())) {
				Toast.makeText(getActivity(), "号码不能为空", 0).show();
				return;
			}
			if (!checkNumber(phoneNum)) {
				Toast.makeText(getActivity(), "号码位数或者格式不对！！", 0).show();
				return;
			}
			if (TextUtils.isEmpty(card)) {
				Toast.makeText(getActivity(), "请选择充值面额!", 0).show();
				return;
			}
			if (TextUtils.isEmpty(address)) {
				Toast.makeText(getActivity(), "请重新输入号码或重新选择", 0).show();
			}
			Editor edit = sharedPreferences.edit();
			edit.putString("phoneNum", phoneNum);
			edit.commit();
			if (resultMap != null&&resultMap.get(q)!=null) {
				SearchItem searchItem = resultMap.get(q);
				if (searchItem != null) {
					userId = sharedPreferences.getLong("userId", 0l);
					AccessToken accessToken = TopConfig.client
							.getAccessToken(userId);
//					String tql = "";
//					if (accessToken == null || userId == 0l) {
//						Toast t = Toast.makeText(getActivity(), "请先授权",
//								Toast.LENGTH_SHORT);
//						t.show();
//						TopConfig.client.authorize(getActivity());
//						return;
//					}
					Intent intent = new Intent();
					if (searchItem.getClick_url() != null) {
						String uri = CommonUtil.generateTopClickUri(
								searchItem.getClick_url(), getActivity(),
								accessToken);
						LogUtil.i(TAG, uri);
						intent.putExtra(BrowserActivity.BROWSERACTIVITY_URI,
								uri);
						intent.setAction(BrowserActivity.BROWSERACTIVITY_ACTION);
						getActivity().startActivity(intent);
					}
				}
			}else {
				Toast.makeText(getActivity(), "正在获取价格信息, 请稍候...", 0).show();
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			EditText editText = (EditText) v;
			final String num = editText.getText().toString().trim();
			if (!checkNumber(num)) {
				Toast.makeText(getActivity(), "号码格式不正确", 0).show();
			} else {
				getAddressFromServer(num);
			}
		}

	}

	private void getAddressFromServer(final String num) {
		pb.setVisibility(View.VISIBLE);
		headProgressBar.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run() {
				try {
					NumberService service = new NumberService();
					String loacation = service.getLocation(num);
					Message msg = new Message();
					msg.obj = loacation;
					msg.what = GET_ADDRESS_SUCCESS;
					handler.sendMessage(msg);
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = GET_ADDRESS_FAILED;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();
	}

	private boolean checkNumber(String num) {
		String regex = "^1[3-8]+\\d{9}$";
		return num.matches(regex);
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

		long userId = sharedPreferences.getLong("userId", 10000);
		AccessToken accessToken = TopConfig.client.getAccessToken(userId);
		String tql = "";
		/*
		 * if (userId == 0l || accessToken == null) { Toast t =
		 * Toast.makeText(ProductsActivity.this, "请先授权", Toast.LENGTH_SHORT);
		 * t.show(); client.authorize(ProductsActivity.this); return; }
		 */
		/**
		 * select num_iid,title,click_url from
		 * taobao.taobaoke.widget.items.convert where num_iids in (select
		 * item_id from tmall.items.discount.search where q = %e6%89%8b%e6%9c%ba
		 * )
		 */
		List<String> fields = new ArrayList<String>();
		fields.add("item_id");
		Map<String, String> params = new HashMap<String, String>();
		// params.put("q", "%e6%89%8b%e6%9c%ba");
		if (!TextUtils.isEmpty(q)) {
			params.put("q", Uri.encode(q));
		}
		if (!TextUtils.isEmpty(cat)) {
			params.put("cat", cat);
		}
		if (TextUtils.isEmpty(q) & TextUtils.isEmpty(cat)) {
			throw new RuntimeException("关键字q和前台类目不能同时为空！");
		}
		int start = pageNo * pageSize;
		params.put("start", Integer.toString(start));// 最多1000页
		params.put("sort", sort);
		String tmallTql = TqlHelper.generateTMallTql(fields, params);
		tql = TqlHelper.generateTMallConvertToTaoKenestTql(SearchItem.class,
				"num_iids", tmallTql);
		System.out.println(tql);

		GetTopData.getDataFromTop(tql, new TmallToTaokeItemParser(), userId,
				new MyTqlListener() {
					@Override
					public void onResponseException(Object apiError) {

						Toast.makeText(getActivity(),
								((ApiError) apiError).getMsg(), 0).show();
						if (BuildConfig.DEBUG) {
							Log.i(TAG, apiError.toString());
						}
					}

					@Override
					public void onException(Exception e) {
						AppException.network(e).makeToast(getActivity());
					}

					@Override
					public void onComplete(Object result) {
						// TODO Auto-generated method stub
						ArrayList<SearchItem> results = (ArrayList) result;
						if (results != null && results.size() > 0) {
							if (!TextUtils.isEmpty(card)) {
								String money = card.replace(" 元", "");
								Integer mon = Integer.parseInt(money.trim());
								resultMap = new HashMap<String, SearchItem>();
								for (int i = 0; i < results.size(); i++) {
									String reg = "\\d+";
									Pattern pattern = Pattern.compile(reg);
									String resultPrice = results.get(i)
											.getPrice().replace("\\'", "");
									Matcher matcher = pattern
											.matcher(resultPrice);
									if (matcher.find()) {
										resultPrice = matcher.group();
										if ((mon - Float
												.parseFloat(resultPrice)) < 10) {
											LogUtil.i(
													TAG,
													Float.parseFloat(resultPrice)
															+ "  Float.parseFloat(resultPrice)");
											LogUtil.i(
													TAG,
													mon
															- Float.parseFloat(resultPrice)
															+ "  mon-Float.parseFloat(resultPrice)");
											Toast.makeText(getActivity(),
													resultPrice, 1).show();
											resultMap = new HashMap<String, SearchItem>();
											resultMap.put(
													RechargeFragment.this.q,
													results.get(i));
											java.text.DecimalFormat   df=new   java.text.DecimalFormat("#0.00"); 
											float cheap = (mon-Float.parseFloat(resultPrice))/mon;
											df.format(cheap); 
											priceTextView.setText(resultPrice
													+ " 元"+ "  为您节省"+cheap+"%");
											break;
										}
									}
								}
							}
						}
						if (BuildConfig.DEBUG) {
							if (results != null) {
								Log.i(TAG, Integer.toString(results.size()));
							}
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
		sharedPreferences = getActivity().getSharedPreferences("config",
				Context.MODE_PRIVATE);
		long userId = sharedPreferences.getLong("userId", 10000);
		AccessToken accessToken = TopConfig.client.getAccessToken(userId);
		String tql = "";
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
		if (BuildConfig.DEBUG) {
			System.out.println(tql);
		}
		headProgressBar.setVisibility(View.VISIBLE);
		GetTopData.getDataFromTop(tql, new SearchItemParser(), userId,
				new MyTqlListener() {
					@Override
					public void onComplete(Object result) {
						headProgressBar.setVisibility(View.GONE);
						ArrayList<SearchItem> results = (ArrayList) result;
						if (results != null && results.size() > 0) {
							if (!TextUtils.isEmpty(card)) {
								String money = card.replace(" 元", "");
								Integer mon = Integer.parseInt(money.trim());
								resultMap = new HashMap<String, SearchItem>();
								for (int i = 0; i < results.size(); i++) {
									String reg = "\\d+\\.\\d+";
									Pattern pattern = Pattern.compile(reg);
									String resultPrice = results.get(i)
											.getPrice().replace("\\'", "");
									LogUtil.i(TAG, "price"
											+ results.get(i).getPrice());
									Matcher matcher = pattern
											.matcher(resultPrice);
									if (matcher.find()) {
										resultPrice = matcher.group();
										if (mon==30) {
											if ((mon - Float
													.parseFloat(resultPrice)) < 5) {
												LogUtil.i(
														TAG,
														Float.parseFloat(resultPrice)
														+ "  Float.parseFloat(resultPrice)");
												LogUtil.i(
														TAG,
														mon
														- Float.parseFloat(resultPrice)
														+ "  mon-Float.parseFloat(resultPrice)");
												Toast.makeText(getActivity(),
														resultPrice, 1).show();
												resultMap = new HashMap<String, SearchItem>();
												resultMap.put(
														RechargeFragment.this.q,
														results.get(i));
												Float cheap = (mon-Float.parseFloat(resultPrice))/mon;
											     String cheapString = String.format("%.5f",cheap);
												priceTextView.setText(resultPrice
														+ " 元"+ " 为您节省"+cheapString+"%");
												break;
											}
										}else {
											if ((mon - Float
													.parseFloat(resultPrice)) < 10) {
												LogUtil.i(
														TAG,
														Float.parseFloat(resultPrice)
														+ "  Float.parseFloat(resultPrice)");
												LogUtil.i(
														TAG,
														mon
														- Float.parseFloat(resultPrice)
														+ "  mon-Float.parseFloat(resultPrice)");
												Toast.makeText(getActivity(),
														resultPrice, 1).show();
												resultMap = new HashMap<String, SearchItem>();
												resultMap.put(
														RechargeFragment.this.q,
														results.get(i));
												Float cheap = (mon-Float.parseFloat(resultPrice))/mon;
												     String cheapString = String.format("%.5f",cheap);
												priceTextView.setText(resultPrice
														+ " 元"+ " 为您节省"+cheapString+"%");
												break;
											}
											
										}
									}
								}
							}
						}
						if (BuildConfig.DEBUG) {
							if (results != null) {
								Log.i(TAG, Integer.toString(results.size()));
							}
						}
					}

					@Override
					public void onException(Exception e) {
						headProgressBar.setVisibility(View.GONE);
						AppException.network(e).makeToast(getActivity());
					}

					@Override
					public void onResponseException(Object apiError) {
						headProgressBar.setVisibility(View.GONE);
						Toast.makeText(getActivity(),
								((ApiError) apiError).getMsg(), 0).show();
						if (BuildConfig.DEBUG) {
							Log.i(TAG, apiError.toString());
						}
					}
				});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mAdview320x50 = null;
		super.onDestroy();
	}
}