package com.tao8.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tao8.app.AppException;
import com.tao8.app.BuildConfig;
import com.tao8.app.R;
import com.tao8.app.TopConfig;
import com.tao8.app.adapter.CouponEveryGridViewAdapter;
import com.tao8.app.adapter.TryoutAdapter;
import com.tao8.app.api.GetTopData;
import com.tao8.app.api.MyTqlListener;
import com.tao8.app.db.Tao8DBHelper;
import com.tao8.app.db.dao.TaoBaokeCouponDao;
import com.tao8.app.domain.TaobaokeCouponItem;
import com.tao8.app.parser.TaoBaoKeCouponItemParser;
import com.tao8.app.util.CommonUtil;
import com.tao8.app.util.LogUtil;
import com.tao8.app.util.TqlHelper;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.auth.AccessToken;

public class CouponEveryDayFragment extends Fragment implements
		OnClickListener, OnItemClickListener, OnScrollListener {

	protected static final String TAG = "CouponEveryDayFragment";
	private ListView imgsListView;
	private ImageView moreImageView;
	private TextView menuTextView;
	private RelativeLayout rl;
	private TextView lableTextView;
	private LinearLayout toFreshLayout;
	private static final String BASEKEYWORD_STRING = "天天特价";
	private String keyword = "天天特价";
	private ArrayList<TaobaokeCouponItem> taobaokeCouponItems;
	private String sort = "volume_desc";// 成交量从高到低
	private TryoutAdapter tryoutAdapter;
	private SharedPreferences sharedPreferences;
	private int pageSize = 40;
	private LinearLayout topLayout;
	private LinearLayout catgoryLayout;// 点击展开分类
	private GridView catoryGridView;
	private int page_no;
	private int firstVisibleItem;
	private int visibleItemCount;
	private int totalItemCount;
	private PopupWindow popupWindow;
	private String cat;// 类目名称
	private int radioId = 1;
	private LinkedHashMap<String, String> catoryMap;
	private RadioGroup radioGroup;
	private LinearLayout lableLayout;
	private int i;
	private ProgressBar pb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.coupon_everyday_fragment, null);
		findView(linearLayout);
		if (BuildConfig.DEBUG) {
			Toast.makeText(getActivity(), "onCreateView", 0).show();
		}
		setData();
		setListener();
		return linearLayout;
	}

	@Override
	public void onDestroyView() {
		// Toast.makeText(getActivity(), "onDestroyView", 0).show();
		super.onDestroyView();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// setData();
		super.onCreate(savedInstanceState);
	}

	private void setListener() {
		menuTextView.setOnClickListener(this);
		// moreImageView.setOnClickListener(this);
		catgoryLayout.setOnClickListener(this);
		catoryGridView.setOnItemClickListener(this);
		imgsListView.setOnScrollListener(this);
		lableLayout.setOnClickListener(this);
		imgsListView.setOnItemClickListener(this);
		toFreshLayout.setOnClickListener(this);
	}

	private void setData() {
		topLayout.setVisibility(View.VISIBLE);
		taobaokeCouponItems = new ArrayList<TaobaokeCouponItem>();
		if (tryoutAdapter == null) {
			tryoutAdapter = new TryoutAdapter(getActivity(),
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
		lableTextView.setText("特价精选");
		page_no = 1;
		seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, page_no,
				null);
		// List<String> catorys = new ArrayList<String>();
		catoryMap = new LinkedHashMap<String, String>();
		// catorys.add("限时精选");
		catoryMap.put("限时精选", "");
		// catorys.add("时尚女装");
		catoryMap.put("时尚女装", "连衣裙,T恤,衬衫,牛仔裤,半身裙,蕾丝衫,针织衫,卫衣,中老年装,大码女装");
		// catorys.add("舒适内衣");
		catoryMap.put("舒适内衣", "文胸,内裤,家居服,保暖套装,打底袜");
		// catorys.add("包包配饰");
		catoryMap.put("包包配饰", "时尚眼镜,手表,包包,钱包,卡套,旅行箱,腰带,帽子,流行饰品,项链,手链");
		// catorys.add("男鞋女鞋");
		catoryMap.put("男鞋女鞋", "气质女鞋,流行男鞋,跑鞋,女皮鞋,男皮鞋");
		// catorys.add("品质男装");
		catoryMap.put("品质男装", "T恤,Polo衫,衬衫,针织衫,牛仔裤,休闲裤,薄夹克");
		// catorys.add("母婴儿童");
		catoryMap.put("母婴儿童", "玩具,儿童裤子,婴童内衣,儿童针织衫,儿童薄外套,儿童套装,童鞋,婴儿用品,孕妇用品");
		// catorys.add("日用百货");
		catoryMap.put("日用百货", "居家创意,个人护理,保健,按摩器材,床品");
		// catorys.add("美食特产");
		catoryMap.put("美食特产", "零食/坚果/特产,果干,坚果,肉脯,糖果零食,南北干货,滋补品,茶叶,生鲜蔬果");
		// catorys.add("数码家电");
		catoryMap.put("数码家电", "电脑周边,3C数码配件,苹果专用配件,影音电器,厨房电器,生活电器,存储器材,文具");
		// catorys.add("美容护肤");
		catoryMap.put("美容护肤", "美容护肤,美体,精油,春季彩妆必备,美发护发,假发");
		// catorys.add("车品户外");
		catoryMap.put("车品户外", "汽车配件,运动用品,运动服,休闲服装,户外用品");
		catoryGridView.setAdapter(new CouponEveryGridViewAdapter(getActivity(),
				catoryMap));

	}

	private void findView(View view) {
		imgsListView = (ListView) view
				.findViewById(R.id.coupon_everyday_lv_content_imgs);
		catgoryLayout = (LinearLayout) view
				.findViewById(R.id.coupon_everyday_ll_catgory);
		moreImageView = (ImageView) view
				.findViewById(R.id.coupon_everyday_head_iv_more);
		rl = (RelativeLayout) view.findViewById(R.id.head_rl);
		menuTextView = (TextView) view.findViewById(R.id.head_tv_go_menu);
		lableTextView = (TextView) view
				.findViewById(R.id.coupon_everyday_tv_lable);
		toFreshLayout = (LinearLayout) view
				.findViewById(R.id.coupon_everyday_ll_to_refresh);
		topLayout = (LinearLayout) view
				.findViewById(R.id.coupon_everyday_ll_top);
		catoryGridView = (GridView) view
				.findViewById(R.id.coupon_everyday_gv_catory);
		lableLayout = (LinearLayout) view
				.findViewById(R.id.coupon_everyday_ll_lable);
		pb = (ProgressBar) view.findViewById(R.id.coupon_every_pb_head);

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
			boolean isFromTmall, boolean isAll, final int page_no, String cid) {
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
		if (cid != null) {
			params.put("cid", cid);
		}
		params.put("page_size", Integer.toString(pageSize));// 最大40个,
		params.put("page_no", Integer.toString(page_no));// 最多10页
		params.put("mall_item", isFromTmall + "");
		params.put("sort", sort);
		tql = TqlHelper.generateTaoBaoKeCouponTql(TaobaokeCouponItem.class,
				params);
		if (BuildConfig.DEBUG) {
			System.out.println(tql);
		}
		topLayout.setVisibility(View.VISIBLE);
		pb.setVisibility(View.VISIBLE);
		GetTopData.getDataFromTop(tql, new TaoBaoKeCouponItemParser(), userId,
				new MyTqlListener() {
					@Override
					public void onComplete(Object result) {
						toFreshLayout.setVisibility(View.GONE);
						topLayout.setVisibility(View.GONE);
						pb.setVisibility(View.GONE);
						final ArrayList<TaobaokeCouponItem> results = (ArrayList) result;
						if (BuildConfig.DEBUG && results != null) {
							Toast.makeText(getActivity(),
									results.size() + "  总共", 1).show();
						}
						if (results != null && results.size() > 0) {
							if (page_no == 1) {
								taobaokeCouponItems.clear();
							}
							if (imgsListView.getAdapter() == null
									|| imgsListView.getAdapter().getCount() <= 0) {
								imgsListView.setAdapter(tryoutAdapter);
							}
							taobaokeCouponItems.addAll(results);
							tryoutAdapter.notifyDataSetChanged();
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
						toFreshLayout.setVisibility(View.VISIBLE);
						imgsListView.setVisibility(View.GONE);
						topLayout.setVisibility(View.GONE);
						pb.setVisibility(View.GONE);
						AppException.network(e).makeToast(getActivity());
					}

					@Override
					public void onResponseException(Object apiError) {
						toFreshLayout.setVisibility(View.VISIBLE);
						imgsListView.setVisibility(View.GONE);
						topLayout.setVisibility(View.GONE);
						pb.setVisibility(View.GONE);
						Toast.makeText(getActivity(),
								((ApiError) apiError).getMsg(), 0).show();
						if (BuildConfig.DEBUG) {
							Log.i(TAG, apiError.toString());
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.coupon_everyday_ll_catgory:
			if (catoryGridView.getVisibility() == View.GONE) {
				catoryGridView.setVisibility(View.VISIBLE);
				moreImageView.setImageResource(R.drawable.icon_arrow_open);
			} else {
				catoryGridView.setVisibility(View.GONE);
				moreImageView.setImageResource(R.drawable.icon_arrow_close);
			}
			break;
		case R.id.head_tv_go_menu:
			if (getActivity() instanceof ViewPagerActivity) {
				ViewPagerActivity viewPagerActivity = (ViewPagerActivity) getActivity();
				viewPagerActivity.showMenu();
			}
			break;
		case R.id.coupon_everyday_ll_lable:
			if (popupWindow != null && popupWindow.getContentView() != null) {
				System.out.println(i % 2);
				if (i % 2 == 1 && !popupWindow.isShowing()) {
					popupWindow.showAsDropDown(rl);
				}
				i++;
			}

			break;
		case R.id.coupon_everyday_ll_to_refresh:
			if (!CommonUtil.checkNetState(getActivity())) {
				toFreshLayout.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), R.anim.shake));
				Toast.makeText(getActivity(), "网络不给力,检查网络", 0).show();
			} else {
				if ("0".equalsIgnoreCase(keyword)) {
					seachTaobaokeCouponFromKeyWord(keyword, sort, false, true,
							page_no, null);
				} else {
					seachTaobaokeCouponFromKeyWord(keyword, sort, false, false,
							page_no, null);
				}
			}

			break;
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 18:
		case 19:
		case 20:
			if (v instanceof RadioButton) {
				RadioButton radioButton = (RadioButton) v;
				keyword = BASEKEYWORD_STRING + " "
						+ radioButton.getText().toString().trim();

				if (BuildConfig.DEBUG) {
					Toast.makeText(getActivity(), keyword, 1).show();
				}
				page_no = 1;
				seachTaobaokeCouponFromKeyWord(keyword, sort, false, false,
						page_no, null);
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.coupon_everyday_gv_catory:

			for (int i = 0; i < parent.getCount(); i++) {
				View view2 = parent.getChildAt(i);
				((TextView) view2.findViewById(R.id.recharge_gridview_item_tv))
						.setTextColor(Color.BLACK);
			}
			TextView catTextView = (TextView) view
					.findViewById(R.id.recharge_gridview_item_tv);
			// #FF3030
			catTextView.setTextColor(Color.rgb(0xFF, 0x30, 0x30));
			cat = catTextView.getText().toString().trim();
			if ("限时精选".equals(cat)) {
				cat = "";
			} else {
				lableTextView.setText(cat);
			}
			// keyword = BASEKEYWORD_STRING +" "+ cat;
			moreImageView.setImageResource(R.drawable.icon_arrow_close);
			String cats = catoryMap.get(cat);
			if (popupWindow == null) {
				popupWindow = new PopupWindow(getActivity());
			}
			// View popuView = View.inflate(getActivity(), R.layout.popup_lable,
			// null);
			HorizontalScrollView horizontalScrollView = new HorizontalScrollView(
					getActivity());
			radioGroup = new RadioGroup(getActivity());
			horizontalScrollView.setHorizontalScrollBarEnabled(false);
			RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			radioGroup.setLayoutParams(layoutParams);
			radioGroup.setOrientation(0);
			radioGroup.setBackgroundResource(R.drawable.title_bg);
			radioGroup.setGravity(Gravity.CENTER_VERTICAL);
			if (!TextUtils.isEmpty(cats)) {
				String[] split = cats.split(",");
				keyword = BASEKEYWORD_STRING + " " + split[0] == null ? ""
						: split[0];
				for (String string : split) {
					if (!TextUtils.isEmpty(string)) {
						RadioButton radioButton = new RadioButton(getActivity());
						LayoutParams layoutParams2 = new RadioGroup.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						radioButton.setLayoutParams(layoutParams2);
						// layoutParams2.leftMargin = 10;
						layoutParams2.rightMargin = 10;
						layoutParams2.weight = 1;
						radioButton
								.setBackgroundResource(R.drawable.coupon_everyday_tab);
						radioButton.setId(radioId++);
						radioButton.setButtonDrawable(new ColorDrawable(
								Color.TRANSPARENT));
						radioButton.setText(string);
						radioButton.setClickable(true);
						radioButton.setOnClickListener(this);
						radioGroup.addView(radioButton);
					}
				}

				horizontalScrollView.addView(radioGroup);
				popupWindow.setContentView(horizontalScrollView);
				popupWindow.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));
				popupWindow.setOutsideTouchable(true);
				popupWindow.setHeight(80);
				popupWindow.setWidth(getActivity().getWindowManager()
						.getDefaultDisplay().getWidth());

				RadioButton selectedButton = (RadioButton) radioGroup
						.getChildAt(0);
				selectedButton.setChecked(true);
				keyword = BASEKEYWORD_STRING + " "
						+ selectedButton.getText().toString().trim();
				catoryGridView.setVisibility(View.GONE);
				popupWindow.showAsDropDown(rl);
			}
			catoryGridView.setVisibility(View.GONE);
			seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, 1, null);
			break;
		case R.id.coupon_everyday_lv_content_imgs:
			Adapter adapter = parent.getAdapter();
			TaobaokeCouponItem item = (TaobaokeCouponItem) adapter
					.getItem(position);
			if (item != null) {
				Intent intent = new Intent();
				intent.setAction(BrowserActivity.BROWSERACTIVITY_ACTION);
				if (sharedPreferences == null) {
					sharedPreferences = getActivity().getSharedPreferences(
							"config", Context.MODE_PRIVATE);
				}
				long userId = sharedPreferences.getLong("userId", 0L);
				AccessToken accessToken = TopConfig.client
						.getAccessToken(userId);
				String uri = CommonUtil.generateTopClickUri(
						item.getClick_url(), getActivity(), accessToken);
				intent.putExtra(BrowserActivity.BROWSERACTIVITY_URI, uri);
				intent.putExtra(BrowserActivity.BROWSERACTIVITY_TITLE,
						item.getTitle());
				getActivity().startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if ((totalItemCount - firstVisibleItem) == visibleItemCount
				&& scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
			page_no = (taobaokeCouponItems.size() / pageSize) + 1;

			seachTaobaokeCouponFromKeyWord(keyword, sort, false, false,
					page_no, null);
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

}
