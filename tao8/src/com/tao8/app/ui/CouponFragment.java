package com.tao8.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tao8.app.AppException;
import com.tao8.app.BuildConfig;
import com.tao8.app.R;
import com.tao8.app.TopConfig;
import com.tao8.app.adapter.CouponAdapter;
import com.tao8.app.api.GetTopData;
import com.tao8.app.api.MyTqlListener;
import com.tao8.app.domain.SearchItem;
import com.tao8.app.parser.SearchItemParser;
import com.tao8.app.util.CommonUtil;
import com.tao8.app.util.TqlHelper;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.auth.AccessToken;

public class CouponFragment extends Fragment implements OnClickListener,
		OnScrollListener, OnItemClickListener {

	protected static final String TAG = "CouponFragment";
	private LinearLayout linearLayout;
	private SharedPreferences sharedPreferences;
	private int pageSize = 20;
	private TextView menuTextView;
	private ImageView moreImageView;
	private PopupWindow popupWindow;
	private RelativeLayout rl;
	private int page_no = 1;
	private String sort = "volume_desc";// 成交量从高到低
	private ListView imgsListView;
	private List<SearchItem> taobaokeCouponItems;
	private CouponAdapter couponAdapter;
	private TextView lableTextView;
	private String keyword = "0";
	// private ProgressDialog progressDialog;
	private LinearLayout toFreshLayout;// 中间加载失败的显示
	private LinearLayout toplLayout;// 最上面的加载中指示的
	private int scrollState;
	private int firstVisibleItem;
	private int visibleItemCount;
	private int totalItemCount;
	private int i = 0;// popu显示的控制

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		linearLayout = (LinearLayout) inflater.inflate(
				R.layout.coupon_fragment, null);
		findView(linearLayout);
		setLintener();
		setData();
		return linearLayout;
	}

	@Override
	public void onStart() {

		if (BuildConfig.DEBUG) {
			System.out.println("onStart");
		}
		super.onStart();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// setData();
		super.onCreate(savedInstanceState);
	}

	private void setData() {
		// progressDialog = new
		// ProgressDialog(getActivity(),android.R.style.Theme_Black_NoTitleBar);
		// progressDialog.setMessage("数据正在疯狂加载中,请稍后.......");
		if (sharedPreferences == null) {
			sharedPreferences = getActivity().getSharedPreferences("config",
					Context.MODE_PRIVATE);
		}
		popupWindow = createPopuWindow();
		keyword = "0";
		seachTaobaokeCouponFromKeyWord(keyword, sort, false, true, 1);
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
		lableTextView.setText("淘宝折扣");
	}

	private void setLintener() {
		menuTextView.setOnClickListener(this);
		moreImageView.setOnClickListener(this);
		imgsListView.setOnScrollListener(this);
		toFreshLayout.setOnClickListener(this);
		imgsListView.setOnItemClickListener(this);

	}

	private void findView(View view) {
		imgsListView = (ListView) view
				.findViewById(R.id.coupon_lv_content_imgs);
		moreImageView = (ImageView) view.findViewById(R.id.head_iv_more);
		rl = (RelativeLayout) view.findViewById(R.id.head_rl);
		menuTextView = (TextView) view.findViewById(R.id.head_tv_go_menu);
		lableTextView = (TextView) view.findViewById(R.id.head_tv_lable);
		toFreshLayout = (LinearLayout) view
				.findViewById(R.id.coupon_ll_to_refresh);
		toplLayout = (LinearLayout) view.findViewById(R.id.coupon_ll_top);

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

		long userId = sharedPreferences.getLong("userId", 10000);
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
		GetTopData.getDataFromTop(tql, new SearchItemParser(), userId,
				new MyTqlListener() {
					@Override
					public void onComplete(Object result) {
						toFreshLayout.setVisibility(View.GONE);
						toplLayout.setVisibility(View.GONE);
						imgsListView.setVisibility(View.VISIBLE);
						ArrayList<SearchItem> results = (ArrayList) result;
					
						if (results != null && results.size() > 0) {
							if (BuildConfig.DEBUG) {
								Toast.makeText(getActivity(),
										results.size() + "  总共", 1).show();
							}
							if (page_no == 1) {
								taobaokeCouponItems.clear();
								taobaokeCouponItems.addAll(results);
								imgsListView.setAdapter(new CouponAdapter(
										getActivity(), results));
							} else {
								imgsListView.setAdapter(couponAdapter);
								taobaokeCouponItems.addAll(results);
								couponAdapter.notifyDataSetChanged();
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
						AppException.network(e).makeToast(getActivity());
					}

					@Override
					public void onResponseException(Object apiError) {
						toplLayout.setVisibility(View.GONE);
						toFreshLayout.setVisibility(View.VISIBLE);
						imgsListView.setVisibility(View.GONE);
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
		case R.id.head_iv_more:
			if (!popupWindow.isShowing()) {
				popupWindow.showAsDropDown(rl);
			}
			break;
		case R.id.head_tv_go_menu:
			if (getActivity() instanceof ViewPagerActivity) {
				ViewPagerActivity viewPagerActivity = (ViewPagerActivity) getActivity();
				viewPagerActivity.showMenu();
			}
			break;
		case R.id.products_lable_rbtn_0:
		case R.id.products_lable_rbtn_1:
		case R.id.products_lable_rbtn_2:
		case R.id.products_lable_rbtn_3:
		case R.id.products_lable_rbtn_4:
		case R.id.products_lable_rbtn_5:
		case R.id.products_lable_rbtn_6:
		case R.id.products_lable_rbtn_7:
		case R.id.products_lable_rbtn_8:
		case R.id.products_lable_rbtn_9:
			popupWindow.dismiss();
			RadioButton radioButton = (RadioButton) v;
			keyword = radioButton.getText().toString().trim();
			page_no = 1;
			if ("全部".equalsIgnoreCase(keyword)) {
				keyword = "0";
				seachTaobaokeCouponFromKeyWord(keyword, sort, false, true,
						page_no);
			} else {
				seachTaobaokeCouponFromKeyWord(keyword, sort, false, false,
						page_no);
			}
			if (BuildConfig.DEBUG) {
				Toast.makeText(getActivity(), keyword, 0).show();
			}
			break;
		case R.id.coupon_ll_to_refresh:
			if (!CommonUtil.checkNetState(getActivity())) {
				toFreshLayout.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), R.anim.shake));
				Toast.makeText(getActivity(), "网络不给力,检查网络", 0).show();
			} else {
				if ("0".equalsIgnoreCase(keyword)) {
					seachTaobaokeCouponFromKeyWord(keyword, sort, false, true,
							1);
				} else {
					seachTaobaokeCouponFromKeyWord(keyword, sort, false, false,
							1);
				}
			}
			break;
		default:
			break;
		}

	}

	private PopupWindow createPopuWindow() {
		popupWindow = new PopupWindow(getActivity());
		View view = View.inflate(getActivity(), R.layout.popup_lable, null);
		RadioButton radioButton_0 = (RadioButton) view
				.findViewById(R.id.products_lable_rbtn_0);
		RadioButton radioButton_1 = (RadioButton) view
				.findViewById(R.id.products_lable_rbtn_1);
		RadioButton radioButton_2 = (RadioButton) view
				.findViewById(R.id.products_lable_rbtn_2);
		RadioButton radioButton_3 = (RadioButton) view
				.findViewById(R.id.products_lable_rbtn_3);
		RadioButton radioButton_4 = (RadioButton) view
				.findViewById(R.id.products_lable_rbtn_4);
		RadioButton radioButton_5 = (RadioButton) view
				.findViewById(R.id.products_lable_rbtn_5);
		RadioButton radioButton_6 = (RadioButton) view
				.findViewById(R.id.products_lable_rbtn_6);
		RadioButton radioButton_7 = (RadioButton) view
				.findViewById(R.id.products_lable_rbtn_7);
		RadioButton radioButton_8 = (RadioButton) view
				.findViewById(R.id.products_lable_rbtn_8);
		RadioButton radioButton_9 = (RadioButton) view
				.findViewById(R.id.products_lable_rbtn_9);
		radioButton_0.setOnClickListener(this);
		radioButton_1.setOnClickListener(this);
		radioButton_2.setOnClickListener(this);
		radioButton_3.setOnClickListener(this);
		radioButton_4.setOnClickListener(this);
		radioButton_5.setOnClickListener(this);
		radioButton_6.setOnClickListener(this);
		radioButton_7.setOnClickListener(this);
		radioButton_8.setOnClickListener(this);
		radioButton_9.setOnClickListener(this);
		popupWindow.setContentView(view);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setOutsideTouchable(true);
		popupWindow.setHeight(80);
		popupWindow.setWidth(getActivity().getWindowManager()
				.getDefaultDisplay().getWidth());
		return popupWindow;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if ((totalItemCount - firstVisibleItem) == visibleItemCount
				&& scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
			page_no = (taobaokeCouponItems.size() / pageSize) + 1;
			if (keyword.equalsIgnoreCase("0")) {
				seachTaobaokeCouponFromKeyWord(keyword, sort, false, true,
						page_no);
			} else {
				seachTaobaokeCouponFromKeyWord(keyword, sort, false, false,
						page_no);
			}
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
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
			AccessToken accessToken = TopConfig.client.getAccessToken(userId);
			intent.putExtra(BrowserActivity.BROWSERACTIVITY_NUM_IID, item.num_iid);
			intent.putExtra(BrowserActivity.BROWSERACTIVITY_TITLE,
					item.getTitle());
			getActivity().startActivity(intent);
		}

	}

	@Override
	public void onDestroy() {
		imgsListView = null;
		if (taobaokeCouponItems!=null) {
			taobaokeCouponItems.clear();
		}
		taobaokeCouponItems = null;
		super.onDestroy();
	}
}
