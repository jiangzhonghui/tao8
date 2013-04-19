package com.tao8.app.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.tao8.app.AppException;
import com.tao8.app.BuildConfig;
import com.tao8.app.R;
import com.tao8.app.TopConfig;
import com.tao8.app.adapter.TryoutAdapter;
import com.tao8.app.api.GetTopData;
import com.tao8.app.api.MyTqlListener;
import com.tao8.app.db.dao.TaoBaokeCouponDao;
import com.tao8.app.domain.TaobaokeCouponItem;
import com.tao8.app.parser.TaoBaoKeCouponItemParser;
import com.tao8.app.util.CommonUtil;
import com.tao8.app.util.LogUtil;
import com.tao8.app.util.TqlHelper;
import com.tao8.app.widget.PullToRefreshListView;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.auth.AccessToken;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.StaticLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GoldenFragment extends Fragment implements OnClickListener,
		OnItemClickListener, OnScrollListener {
	private Context mContext;
	private ImageView goldImageView;
	private ListView imgsListView;
	private ImageView moreImageView;
	private TextView menuTextView;
	private RelativeLayout rl;
	private TextView lableTextView;
	private LinearLayout toFreshLayout;
	private LinearLayout topLayout;
	private long getDataTime;
	private ArrayList<TaobaokeCouponItem> taobaokeCouponItems;
	private TryoutAdapter tryoutAdapter;
	private String keyword = "淘金币";
	private String sort = "";//"volume_desc";
	private SharedPreferences sharedPreferences;
	private int pageSize = 20;
	private int page_no = 1;
	private int firstVisibleItem;
	private int visibleItemCount;
	private int totalItemCount;
	private static final int CACHE_TIME = 60 * 1000 * 60 * 24;
	protected static final String TAG = "GoldenFragment";
	private static final String COIN_URL  = "http://i.m.taobao.com/coin/take_coin.htm";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.golden_fragment, null);
		findView(view);
		initData();
		setData();
		setListener();
		return view;
	}

	private void setListener() {
		menuTextView.setOnClickListener(this);
		moreImageView.setOnClickListener(this);
		imgsListView.setOnItemClickListener(this);
		toFreshLayout.setOnClickListener(this);
		imgsListView.setOnScrollListener(this);
	}

	private void setData() {
		moreImageView.setImageResource(R.drawable.ic_taojb);
		lableTextView.setText("淘金币");
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
		// if (taobaokeCouponItems != null && taobaokeCouponItems.size() > 0) {
		// return;
		// }
		seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, 1);
		// if ((new Date().getTime() - getDataTime) > CACHE_TIME) {
		// } else {
		// getDataFromBb();
		// }
	}

	private void initData() {
		if (sharedPreferences == null) {
			sharedPreferences = getActivity().getSharedPreferences("config",
					Context.MODE_PRIVATE);
		}
		getDataTime = sharedPreferences.getLong("getDataTime", 0l);

	}

	private void findView(View view) {
		imgsListView = (ListView) view
				.findViewById(R.id.golden_lv_content_imgs);
		moreImageView = (ImageView) view.findViewById(R.id.head_iv_more);
		rl = (RelativeLayout) view.findViewById(R.id.head_rl);
		menuTextView = (TextView) view.findViewById(R.id.head_tv_go_menu);
		lableTextView = (TextView) view.findViewById(R.id.head_tv_lable);
		toFreshLayout = (LinearLayout) view
				.findViewById(R.id.golden_ll_to_refresh);
		topLayout = (LinearLayout) view.findViewById(R.id.golden_ll_top);
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
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
		if (BuildConfig.DEBUG) {
			System.out.println(tql);
		}
		topLayout.setVisibility(View.VISIBLE);
		GetTopData.getDataFromTop(tql, new TaoBaoKeCouponItemParser(), userId,
				new MyTqlListener() {
					@Override
					public void onComplete(Object result) {
						toFreshLayout.setVisibility(View.GONE);
						topLayout.setVisibility(View.GONE);
						final ArrayList<TaobaokeCouponItem> results = (ArrayList) result;
						if (BuildConfig.DEBUG && result != null) {
							Toast.makeText(getActivity(),
									results.size() + "  总共", 1).show();
						}
						if (results != null && results.size() > 0) {

							if (page_no == 1) {
								taobaokeCouponItems.clear();
							}
							taobaokeCouponItems.addAll(results);
							if (imgsListView.getAdapter() == null) {
								imgsListView.setAdapter(tryoutAdapter);
							}
							tryoutAdapter.notifyDataSetChanged();
							toFreshLayout.setVisibility(View.GONE);
							imgsListView.setVisibility(View.VISIBLE);
						}
						if (BuildConfig.DEBUG) {
							if (results != null) {
								Log.i(TAG, Integer.toString(results.size()));
							}
						}

//						new Thread() {
//							public void run() {
//								TaoBaokeCouponDao dao = new TaoBaokeCouponDao(
//										getActivity());
//								if (results == null) {
//									return;
//								}
//								dao.delAll();
//								for (TaobaokeCouponItem taobaokeCouponItem : results) {
//									dao.insert(taobaokeCouponItem);
//									System.out.println("添加成功");
//								}
//								Editor edit = sharedPreferences.edit();
//								edit.putLong("getDataTime",
//										new Date().getTime());
//								edit.commit();
//								int count = dao.queryCount();
//								if (BuildConfig.DEBUG) {
//									LogUtil.e(TAG, "总共数量    " + count);
//								}
//							};
//						}.start();
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
			AccessToken accessToken = TopConfig.client.getAccessToken(userId);
			String uri = CommonUtil.generateTopClickUri(item.getClick_url(),
					getActivity(), accessToken);
			intent.putExtra(BrowserActivity.BROWSERACTIVITY_URI, uri);
			intent.putExtra(BrowserActivity.BROWSERACTIVITY_TITLE,
					item.getTitle());
			getActivity().startActivity(intent);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_iv_more:
				Intent intent = new Intent();
				intent.setAction(BrowserActivity.BROWSERACTIVITY_ACTION);
				intent.putExtra(BrowserActivity.BROWSERACTIVITY_URI, COIN_URL);
				intent.putExtra(BrowserActivity.BROWSERACTIVITY_TITLE,
						"领取淘金币");
				getActivity().startActivity(intent);
			
			break;
		case R.id.head_tv_go_menu:
			if (mContext!=null&&mContext instanceof ViewPagerActivity) {
				ViewPagerActivity viewPagerActivity = (ViewPagerActivity) mContext;
				viewPagerActivity.showMenu();
			}
			break;
		case R.id.golden_ll_to_refresh:
			if (!CommonUtil.checkNetState(getActivity())) {
				toFreshLayout.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), R.anim.shake));
				Toast.makeText(getActivity(), "网络不给力,检查网络", 0).show();
			}else {
				seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, page_no);
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
}
