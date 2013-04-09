package com.tao8.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
	private String keyword = "天天特价";
	private ArrayList<TaobaokeCouponItem> taobaokeCouponItems;
	private String sort = "volume_desc";// 成交量从高到低
	private TryoutAdapter tryoutAdapter;
	private SharedPreferences sharedPreferences;
	private int pageSize = 40;
	private LinearLayout topLayout;
	private LinearLayout catgoryLayout;// 点击展开分类
	private GridView catoryGridView;
	private String cat;
	private int page_no;
	private int firstVisibleItem;
	private int visibleItemCount;
	private int totalItemCount;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.coupon_everyday_fragment, null);
		findView(linearLayout);
		Toast.makeText(getActivity(), "onCreateView", 0).show();
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
		seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, 1);
		List<String> catorys = new ArrayList<String>();
		catorys.add("限时精选");
		catorys.add("时尚女装");
		catorys.add("舒适内衣");
		catorys.add("包包配饰");
		catorys.add("男鞋女鞋");
		catorys.add("品质男装");
		catorys.add("母婴儿童");
		catorys.add("日用百货");
		catorys.add("美食特产");
		catorys.add("数码家电");
		catorys.add("美容护肤");
		catorys.add("车品户外");
		catoryGridView.setAdapter(new CouponEveryGridViewAdapter(getActivity(),
				catorys));

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
		tql = TqlHelper.generateTaoBaoKeCouponTql(TaobaokeCouponItem.class,
				params);
		System.out.println(tql);
		GetTopData.getDataFromTop(tql, new TaoBaoKeCouponItemParser(), userId,
				new MyTqlListener() {
					@Override
					public void onComplete(Object result) {
						toFreshLayout.setVisibility(View.GONE);
						topLayout.setVisibility(View.GONE);
						final ArrayList<TaobaokeCouponItem> results = (ArrayList) result;
						if (BuildConfig.DEBUG&&results!=null) {
							Toast.makeText(getActivity(),
									results.size() + "  总共", 1).show();
						}
						if (results != null && results.size() > 0) {
							if (page_no == 1) {
								taobaokeCouponItems.clear();
							}
							taobaokeCouponItems.addAll(results);
							if (imgsListView.getAdapter() == null
									|| imgsListView.getAdapter().getCount() <= 0) {
								imgsListView.setAdapter(tryoutAdapter);
							}
							tryoutAdapter.notifyDataSetChanged();
							toFreshLayout.setVisibility(View.GONE);
							imgsListView.setVisibility(View.VISIBLE);
							
							
						}
						new Thread(){
							public void run() {
								TaoBaokeCouponDao dao = new TaoBaokeCouponDao(getActivity());
								if (results==null) {
									return;
								}
								for (TaobaokeCouponItem taobaokeCouponItem : results) {
									dao.insert(taobaokeCouponItem);
									System.out.println("添加成功"); 
								}
								int count = dao.queryCount();
								if (BuildConfig.DEBUG) {
									LogUtil.e(TAG, "总共数量    "+count);
								}
							};
						}.start();
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
						AppException.network(e).makeToast(getActivity());
					}

					@Override
					public void onResponseException(Object apiError) {
						toFreshLayout.setVisibility(View.VISIBLE);
						imgsListView.setVisibility(View.GONE);
						topLayout.setVisibility(View.GONE);
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
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

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
			cat =""; 
		}
		lableTextView.setText(cat);
		catoryGridView.setVisibility(View.GONE);
		moreImageView.setImageResource(R.drawable.icon_arrow_close);
		seachTaobaokeCouponFromKeyWord(keyword + cat, sort, false, false, 1);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if ((totalItemCount - firstVisibleItem) == visibleItemCount
				&& scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
			page_no = (taobaokeCouponItems.size() / pageSize) + 1;

			seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, page_no);

			Toast.makeText(getActivity(), "滑倒底部了", 1).show();
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
