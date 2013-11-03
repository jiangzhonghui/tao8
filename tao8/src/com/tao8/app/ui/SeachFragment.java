package com.tao8.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.tao8.app.AppException;
import com.tao8.app.BuildConfig;
import com.tao8.app.R;
import com.tao8.app.TopConfig;
import com.tao8.app.adapter.CouponAdapter;
import com.tao8.app.adapter.SeachContentAdapter;
import com.tao8.app.adapter.SeachHistoryAdapter;
import com.tao8.app.api.GetTopData;
import com.tao8.app.api.MyTqlListener;
import com.tao8.app.domain.SearchItem;
import com.tao8.app.parser.SearchItemParser;
import com.tao8.app.util.CommonUtil;
import com.tao8.app.util.TqlHelper;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.auth.AccessToken;

public class SeachFragment extends Fragment implements OnClickListener,
		OnItemClickListener, OnCheckedChangeListener, OnScrollListener, TextWatcher {

	public static final String REBATE_DETAIL_ACTION = "com.emar.egou.app.rebate_detail";
	private static final String TAG = "SearchActivity";
	private EditText contentEditText;
	private LinkedHashSet<String> seachHistoryStrings;
	private ImageView clearImageView;
	private ListView seachListView;
	private TextView backMenu;
	private Button seachButton;
	private SharedPreferences sharedPreferences;
	private boolean flag;
	private RadioButton classButton;
	private RadioButton historyButton;
	private SeachHistoryAdapter arrayAdapter;
	private ListView historyListView;
	public ViewFlipper view;
	private static LinearLayout tryoutLinearLayout;
	private static RelativeLayout seachRelativeLayout;
	private ImageView moreImageView;
	private ProgressBar pbHead;
	private List<SearchItem> taobaokeCouponItems;
	private TextView goMenuTextView;
	private TextView lableTextView;
	private String keyword;
	private LinearLayout toFreshLayout;
	private LinearLayout toplLayout;
	private ListView imgsListView;
	private int pageSize = 20;
	private String sort = "volume_desc";// 成交量从高到低
	private CouponAdapter couponAdapter;
	private int firstVisibleItem;
	private int visibleItemCount;
	private int totalItemCount;
	private int page_no = 1;
	private RadioButton attentionRadioButton;
	private RadioButton creditRadioButton;
	private RadioButton priceraRadioButton;
	private RadioButton sellsRadioButton;
	private RadioGroup orderGroup;
	private int i;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = (ViewFlipper) inflater.inflate(R.layout.seach_fragment, null);
		findView(view);
		initData();
		setData();
		setListener();
		return view;
	}

	private void setData() {
		moreImageView.setVisibility(View.GONE);
		goMenuTextView.setText("返回");
		orderGroup.setVisibility(View.VISIBLE);
		
		
		int width = CommonUtil.getScreenWidth(getActivity());
		attentionRadioButton.setWidth(width / 4);
		creditRadioButton.setWidth(width / 4);
		priceraRadioButton.setWidth(width / 4);
		sellsRadioButton.setWidth(width / 4);
		// //////////////////
		taobaokeCouponItems = new ArrayList<SearchItem>();
		if (couponAdapter == null) {
			couponAdapter = new CouponAdapter(getActivity(),
					taobaokeCouponItems);
		}
		// 设置ListView每个Item间的间隔线的颜色渐变
		GradientDrawable divider_gradient = new GradientDrawable(
				Orientation.TOP_BOTTOM, new int[] {
						Color.parseColor("#cccccc"),
						Color.parseColor("#ffffff"),
						Color.parseColor("#cccccc") });
		imgsListView.setDivider(divider_gradient);
		imgsListView.setDividerHeight(3);
		imgsListView.setAdapter(couponAdapter);

		// //////////////////
		LinkedHashMap<String, String[]> groupMaps = new LinkedHashMap<String, String[]>();
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
		groupMaps.put("成人", adult.split(","));
		String food = "零食,巧克力,坚果,肉脯,咖啡,茶叶,酒类,特产,牛肉干,减肥,速食品,果冻布丁,果蔬汁,糖果";
		groupMaps.put("食品", food.split(","));
		SeachContentAdapter adapter = new SeachContentAdapter(getActivity(),
				groupMaps) {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.seach_content_imbtn_icon:
					ImageButton imbtn = (ImageButton) v;
					RelativeLayout itemsLayout = (RelativeLayout) ((LinearLayout) v
							.getParent().getParent())
							.findViewById(R.id.seach_content_if_group_detail_items);
					if (itemsLayout.getVisibility() == View.VISIBLE) {
						imbtn.setImageDrawable(getResources().getDrawable(
								R.drawable.ic_seach_list_more));
						itemsLayout.setVisibility(View.GONE);
					} else {
						imbtn.setImageDrawable(getResources().getDrawable(
								R.drawable.ic_seach_list_nomore));
						itemsLayout.setVisibility(View.VISIBLE);
					}
					break;
				case R.id.seach_content_list_item_rl_title:
					// System.out.println(v.getParent());
					RelativeLayout itemsRelativeLayout = (RelativeLayout) ((LinearLayout) v
							.getParent())
							.findViewById(R.id.seach_content_if_group_detail_items);
					ImageButton iconImageView = (ImageButton) ((LinearLayout) v
							.getParent())
							.findViewById(R.id.seach_content_imbtn_icon);
					if (itemsRelativeLayout.getVisibility() == View.VISIBLE) {
						iconImageView.setImageDrawable(getResources()
								.getDrawable(R.drawable.ic_seach_list_more));
						itemsRelativeLayout.setVisibility(View.GONE);
					} else {
						iconImageView.setImageDrawable(getResources()
								.getDrawable(R.drawable.ic_seach_list_nomore));
						itemsRelativeLayout.setVisibility(View.VISIBLE);
					}
					break;
				case R.id.seach_content_item_tv:
					if (BuildConfig.DEBUG) {
						Toast.makeText(getActivity(),
								((TextView) v).getText().toString(), 0).show();
					}
					keyword = ((TextView) v).getText().toString();
					lableTextView.setText(keyword);
					view.showNext();
					sellsRadioButton.setChecked(true);
					sort = "volume_desc";//初始化状态
					seachTaobaokeCouponFromKeyWord(keyword, sort, false, false,
							1);
					break;
				default:
					break;
				}
			}
		};
		seachListView.setAdapter(adapter);
		seachHistoryStrings.remove("");
		arrayAdapter = new SeachHistoryAdapter(getActivity(),
				seachHistoryStrings);
		historyListView.setAdapter(arrayAdapter);
	}

	private void setListener() {
		seachButton.setOnClickListener(this);
		clearImageView.setOnClickListener(this);
		backMenu.setOnClickListener(this);

		classButton.setOnCheckedChangeListener(this);
		historyButton.setOnCheckedChangeListener(this);

		historyListView.setOnItemClickListener(this);
		toFreshLayout.setOnClickListener(this);
		goMenuTextView.setOnClickListener(this);
		imgsListView.setOnItemClickListener(this);
		imgsListView.setOnScrollListener(this);
		contentEditText.addTextChangedListener(this);
		
		/////
		attentionRadioButton.setOnClickListener(this);
		creditRadioButton.setOnClickListener(this);
		priceraRadioButton.setOnClickListener(this);
		sellsRadioButton.setOnClickListener(this);
		
	}

	private void initData() {
		view.setInAnimation(outIn(getActivity()));
		view.setOutAnimation(inOut(getActivity()));
		if (seachHistoryStrings == null) {
			seachHistoryStrings = new LinkedHashSet<String>();
		}
		sharedPreferences = getActivity().getSharedPreferences("config",
				Context.MODE_PRIVATE);
		String stoneString = sharedPreferences
				.getString("autoStringStored", "");
		String[] split = stoneString.trim().split(",");
		for (int i = 0; i < split.length; i++) {
			seachHistoryStrings.add(split[i]);
		}
		for (String string : seachHistoryStrings) {
			if (string.trim().equalsIgnoreCase("")) {
				seachHistoryStrings.remove(string);
			}
		}
	}

	private void findView(View view) {
		backMenu = (TextView) view.findViewById(R.id.seach_head_tv_go_menu);
		seachButton = (Button) view.findViewById(R.id.seach_head_btn_seach);
		seachListView = (ListView) view
				.findViewById(R.id.seach_lv_seach_item_class);
		historyListView = (ListView) view
				.findViewById(R.id.seach_lv_seach_item_history);
		contentEditText = (EditText) view
				.findViewById(R.id.seach_head_actv_content);
		clearImageView = (ImageView) view
				.findViewById(R.id.seach_iv_content_clear);
		classButton = (RadioButton) view.findViewById(R.id.seach_rb_class);
		historyButton = (RadioButton) view.findViewById(R.id.seach_rb_history);
		tryoutLinearLayout = (LinearLayout) view.findViewById(R.id.tryout_ll);
		seachRelativeLayout = (RelativeLayout) view.findViewById(R.id.seach_rl);

		// ////////////////
		moreImageView = (ImageView) view.findViewById(R.id.head_iv_more);
		pbHead = (ProgressBar) view.findViewById(R.id.head_progressBar);
		goMenuTextView = (TextView) view.findViewById(R.id.head_tv_go_menu);
		lableTextView = (TextView) view.findViewById(R.id.head_tv_lable);

		// ///
		toFreshLayout = (LinearLayout) view
				.findViewById(R.id.coupon_ll_to_refresh);
		toplLayout = (LinearLayout) view.findViewById(R.id.coupon_ll_top);
		imgsListView = (ListView) view
				.findViewById(R.id.coupon_lv_content_imgs);
		attentionRadioButton = (RadioButton) view.findViewById(R.id.coupon_rbtn_orderby_attention);
		creditRadioButton = (RadioButton) view.findViewById(R.id.coupon_rbtn_orderby_credit);
		priceraRadioButton = (RadioButton) view.findViewById(R.id.coupon_rbtn_orderby_price);
		sellsRadioButton = (RadioButton) view.findViewById(R.id.coupon_rbtn_orderby_sells);
		orderGroup = (RadioGroup) view.findViewById(R.id.coupon_rg_orderby);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.seach_head_tv_go_menu:
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "account_head_btn_back");
			}
			if (getActivity() instanceof ViewPagerActivity) {
				ViewPagerActivity viewPagerActivity = (ViewPagerActivity) getActivity();
				viewPagerActivity.showMenu();
			}
			// onBackPressed();
			break;
		case R.id.seach_iv_content_clear:
			contentEditText.setText("");
			break;
		case R.id.seach_head_btn_seach:
			String content = contentEditText.getText().toString();
			if (!CommonUtil.checkNetState(getActivity())) {
				v.startAnimation(AnimationUtils.loadAnimation(getActivity(),
						R.anim.shake));
				Toast.makeText(getActivity(), "网络不给力,检查网络", 0).show();
				return;
			}
			if (TextUtils.isEmpty(content)) {
				Toast.makeText(getActivity(), "亲,搜索内容不能为空！", 0).show();
				return;
			}
			keyword = content;
			lableTextView.setText(keyword);
			view.showNext();
			//
			storeStringAndCheck(keyword);
			// 通知搜索历史更新
			if (historyButton.isChecked()) {
				arrayAdapter.notifyDataSetChanged();
			}
			seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, 1);
		case R.id.coupon_ll_to_refresh:
			if (!CommonUtil.checkNetState(getActivity())) {
				toFreshLayout.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
				Toast.makeText(getActivity(), "网络不给力,检查网络", 0).show();
			}else {
				seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, 1);
			}
			break;
			
		case R.id.coupon_rbtn_orderby_attention:
			if (sort.equalsIgnoreCase("commissionRate_desc")) {
				break;
			}
			sort = "commissionRate_desc";//佣金
			page_no = 1;
			seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, page_no);
			break;
		case R.id.coupon_rbtn_orderby_credit:
			if (sort.equalsIgnoreCase("credit_desc")) {
				break;
			}
			page_no = 1;
			sort = "credit_desc";//信用等级从高到低
			seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, page_no);
			break;
		case R.id.coupon_rbtn_orderby_price:
			if (i%2 == 0) {
				sort = "price_asc";//折扣价格从低到高
			}else {
				sort = "price_desc";//折扣价格从高到低
			}
			i++;
			page_no = 1;
			seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, page_no);
			break;
		case R.id.coupon_rbtn_orderby_sells:
			if (sort.equalsIgnoreCase("volume_desc")) {
				break;
			}
			sort = "volume_desc";//成交量
			page_no = 1;
			seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, page_no);
			break;
		case R.id.head_tv_go_menu:
			view.showNext();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @param content
	 * @return 如果返回true说明包含该搜索历史
	 */
	private boolean storeStringAndCheck(String content) {
		if (seachHistoryStrings.contains(content)) {
			return true;
		} else {
			seachHistoryStrings.add(content);
		}
		StringBuilder sb = new StringBuilder();
		int i = 0;

		for (String history : seachHistoryStrings) {
			if (TextUtils.isEmpty(history)) {
				continue;
			}
			if (i == seachHistoryStrings.size() - 1) {
				sb.append(history);
			} else {
				sb.append(history).append(",");
			}
			i++;
		}
		// 去除多余的项目
		int size = seachHistoryStrings.size();
		while (seachHistoryStrings.size() > 15) {
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
				if (historyListView.getVisibility() == View.VISIBLE) {
					historyListView.setVisibility(View.GONE);
				}
				if (seachListView.getVisibility() == View.GONE) {
					seachListView.setVisibility(View.VISIBLE);
				}
			}
			break;
		case R.id.seach_rb_history:
			if (isChecked) {
				if (historyListView.getVisibility() == View.GONE) {
					historyListView.setVisibility(View.VISIBLE);
				}
				if (seachListView.getVisibility() == View.VISIBLE) {
					seachListView.setVisibility(View.GONE);
				}
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.seach_lv_seach_item_history:
			String history = parent.getAdapter().getItem(position).toString();
			contentEditText.setText(history);
			break;
		case R.id.coupon_lv_content_imgs:
			Adapter adapter = parent.getAdapter();
			SearchItem item = (SearchItem) adapter
					.getItem(position);
			if (item != null) {
				Intent intent = new Intent();
				intent.setAction(BrowserActivity.BROWSERACTIVITY_ACTION);
				if (sharedPreferences == null) {
					sharedPreferences = getActivity().getSharedPreferences(
							"config", Context.MODE_PRIVATE);
				}
				long userId = sharedPreferences.getLong("userId", 10000);
				AccessToken accessToken = TopConfig.client
						.getAccessToken(userId);
				String uri = CommonUtil.generateTopClickUri(
						item.getClick_url(), getActivity(), accessToken);
				intent.putExtra(BrowserActivity.BROWSERACTIVITY_NUM_IID, item.num_iid);
				intent.putExtra(BrowserActivity.BROWSERACTIVITY_TITLE,
						item.getTitle());
				getActivity().startActivity(intent);
			}
			break;
		default:
			break;
		}
	}


	// 从下到上
	private static AnimationSet outIn(Context context) {
		// AlphaAnimation alpha = new AlphaAnimation(1, 0);
		System.out.println(CommonUtil.getScreenHeight(context));
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0,
				CommonUtil.getScreenHeight(context), 0);
		translateAnimation.setDuration(1000);
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(translateAnimation);
		// translateAnimation.setFillBefore(true);
		return animationSet;
	}

	// 从下到上
	private static AnimationSet inOut(Context context) {
		// AlphaAnimation alpha = new AlphaAnimation(1, 0);
		System.out.println(CommonUtil.getScreenHeight(context));
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0,
				CommonUtil.getScreenHeight(context));
		translateAnimation.setDuration(1000);
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(translateAnimation);
		// translateAnimation.setFillBefore(true);
		return animationSet;
	}

	/**
	 * 淘客折扣请求的方法
	 * 
	 * @param keyword
	 *            关键字 (如果是全部,则为cid = 0);
	 * @param sort
	 *            default(默认排序), price_desc(折扣价格从高到低), price_asc(折扣价格从低到高),
	 *            credit_desc(信用等级从高到低), credit_asc(信用等级从低到高),
	 *            commissionRate_desc(佣金比率从高到低), commissionRate_asc(佣金比率从低到高),
	 *            volume_desc(成交量成高到低), volume_asc(成交量从低到高)
	 * @param isFromTmall
	 *            是否请求天猫的数据
	 * @param isAll
	 *            是否请求的全部数据
	 * @param page_no
	 *            请求的页数
	 */
	private void seachTaobaokeCouponFromKeyWord(String keyword, String sort,
			boolean isFromTmall, boolean isAll, final int page_no) {
		if (sharedPreferences == null) {
			sharedPreferences = getActivity().getSharedPreferences("config",
					Context.MODE_PRIVATE);
		}
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
		tql = TqlHelper.generateTaoBaoKeTql(SearchItem.class,
				params);
		if (BuildConfig.DEBUG) {
			System.out.println(tql);
		}
		toplLayout.setVisibility(View.VISIBLE);
		pbHead.setVisibility(View.VISIBLE);
		GetTopData.getDataFromTop(tql, new SearchItemParser(), userId,
				new MyTqlListener() {
					@Override
					public void onComplete(Object result) {
						toFreshLayout.setVisibility(View.GONE);
						toplLayout.setVisibility(View.GONE);
						imgsListView.setVisibility(View.VISIBLE);
						pbHead.setVisibility(View.GONE);
						ArrayList<SearchItem> results = (ArrayList) result;
						if (results != null && results.size() > 0) {
							if (BuildConfig.DEBUG) {
								Toast.makeText(getActivity(),
										results.size() + "  总共", 1).show();
							}

							if (page_no == 1) {
								taobaokeCouponItems.clear();
								taobaokeCouponItems.addAll(results);
								imgsListView.setAdapter(new CouponAdapter(getActivity(), results));
							}else {
								imgsListView.setAdapter(couponAdapter);
								taobaokeCouponItems.addAll(results);
								couponAdapter.notifyDataSetChanged();
								if (BuildConfig.DEBUG) {
									Toast.makeText(getActivity(),
											taobaokeCouponItems.size() + "  总共", 1).show();
								}
								imgsListView.setSelection(taobaokeCouponItems.size()-results.size());
							}
							toFreshLayout.setVisibility(View.GONE);
							imgsListView.setVisibility(View.VISIBLE);
						}
						if (BuildConfig.DEBUG) {
							if (results != null) {
								Log.i(TAG, Integer.toString(results.size()));
							}
						}
					}

					@Override
					public void onException(Exception e) {
						toplLayout.setVisibility(View.GONE);
						toFreshLayout.setVisibility(View.VISIBLE);
						imgsListView.setVisibility(View.GONE);
						pbHead.setVisibility(View.GONE);
						AppException.network(e).makeToast(getActivity());
					}

					@Override
					public void onResponseException(Object apiError) {
						toplLayout.setVisibility(View.GONE);
						toFreshLayout.setVisibility(View.VISIBLE);
						imgsListView.setVisibility(View.GONE);
						pbHead.setVisibility(View.GONE);
						Toast.makeText(getActivity(),
								((ApiError) apiError).getMsg(), 0).show();
						if (BuildConfig.DEBUG) {
							Log.i(TAG, apiError.toString());
						}
					}
				});
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if ((totalItemCount - firstVisibleItem) == visibleItemCount
				&& scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
			 
			page_no  = (taobaokeCouponItems.size() / pageSize) + 1;

			seachTaobaokeCouponFromKeyWord(keyword, sort, false, false,
					page_no);
			if (BuildConfig.DEBUG) {
				Toast.makeText(getActivity(), "滑倒底部了", 1).show();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
		this.visibleItemCount = visibleItemCount;
		this.totalItemCount = totalItemCount;
	}
	
	@Override
	public void onDestroy() {
		if (taobaokeCouponItems!=null) {
			taobaokeCouponItems.clear();
		}
		taobaokeCouponItems = null;
		super.onDestroy();
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (!TextUtils.isEmpty(contentEditText.getText().toString())) {
			clearImageView.setVisibility(View.VISIBLE);
		}else {
			clearImageView.setVisibility(View.INVISIBLE);
		}
		
	}
}
