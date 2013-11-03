package com.tao8.app.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tao8.app.AppException;
import com.tao8.app.BuildConfig;
import com.tao8.app.R;
import com.tao8.app.TopConfig;
import com.tao8.app.adapter.TryoutAdapter;
import com.tao8.app.api.GetTopData;
import com.tao8.app.api.MyTqlListener;
import com.tao8.app.db.dao.TaoBaokeCouponDao;
import com.tao8.app.domain.SearchItem;
import com.tao8.app.parser.SearchItemParser;
import com.tao8.app.util.CommonUtil;
import com.tao8.app.util.LogUtil;
import com.tao8.app.util.TqlHelper;
import com.tao8.app.widget.PullToRefreshListView;
import com.tao8.app.widget.PullToRefreshListView.OnRefreshListener;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.auth.AccessToken;

public class TryoutFragment extends Fragment implements OnClickListener,
		OnRefreshListener, OnItemClickListener {

	private static final int CACHE_TIME = 60 * 1000 * 60*24;
	protected static final String TAG = "TryoutFragment";
	private PullToRefreshListView imgsListView;
	private ImageView moreImageView;
	private TextView menuTextView;
	private RelativeLayout rl;
	private TextView lableTextView;
	private LinearLayout toFreshLayout;
	private String keyword = "手机付邮";
	// private long getDataTime=0l;
	private ArrayList<SearchItem> taobaokeCouponItems;
	private String sort = "volume_desc";// 成交量从高到低
	private TryoutAdapter tryoutAdapter;
	private SharedPreferences sharedPreferences;
	private int pageSize = 100;
	public PopupWindow popupWindow;
	private LinearLayout topLayout;
	private long getDataTime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.tryout_fragment, null);
		findView(linearLayout);
		if (BuildConfig.DEBUG) {
			Toast.makeText(getActivity(), "onCreateView", 0).show();
		}
		initData();
		setData();
		setListener();
		return linearLayout;
	}

	private void initData() {
		if (sharedPreferences == null) {
			sharedPreferences = getActivity().getSharedPreferences("config",
					Context.MODE_PRIVATE);
		}
		getDataTime = sharedPreferences.getLong("getDataTime", 0l);
	}

	@Override
	public void onDestroyView() {
		if (BuildConfig.DEBUG) {
			Toast.makeText(getActivity(), "onDestroyView", 0).show();
		}
		super.onDestroyView();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// setData();
		super.onCreate(savedInstanceState);
	}

	private void setListener() {
		menuTextView.setOnClickListener(this);
		imgsListView.setOnRefreshListener(this);
		moreImageView.setOnClickListener(this);
		imgsListView.setOnItemClickListener(this);
		toFreshLayout.setOnClickListener(this);
	}

	private void setData() {

		moreImageView.setImageResource(R.drawable.icon_notice);

		taobaokeCouponItems = new ArrayList<SearchItem>();
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
		lableTextView.setText("付费试用");
		// if (taobaokeCouponItems != null && taobaokeCouponItems.size() > 0) {
		// return;
		// }
		if ((new Date().getTime() - getDataTime) > CACHE_TIME) {
			seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, 1);
		} else {
			getDataFromBb();
		}
	}

	private void getDataFromBb() {
		new CouponBDTask().execute();
	}

	private void findView(View view) {
		imgsListView = (PullToRefreshListView) view
				.findViewById(R.id.tryout_plv_content_imgs);
		moreImageView = (ImageView) view.findViewById(R.id.head_iv_more);
		rl = (RelativeLayout) view.findViewById(R.id.head_rl);
		menuTextView = (TextView) view.findViewById(R.id.head_tv_go_menu);
		lableTextView = (TextView) view.findViewById(R.id.head_tv_lable);
		toFreshLayout = (LinearLayout) view
				.findViewById(R.id.tryout_ll_to_refresh);
		topLayout = (LinearLayout) view.findViewById(R.id.tryout_ll_top);
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
		tql = TqlHelper.generateTaoBaoKeTql(SearchItem.class,
				params);
		if (BuildConfig.DEBUG) {
			System.out.println(tql);
		}
		topLayout.setVisibility(View.VISIBLE);
		GetTopData.getDataFromTop(tql, new SearchItemParser(), userId,
				new MyTqlListener() {
					@Override
					public void onComplete(Object result) {
						toFreshLayout.setVisibility(View.GONE);
						topLayout.setVisibility(View.GONE);
						final ArrayList<SearchItem> results = (ArrayList) result;
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
							imgsListView.onRefreshComplete();
							toFreshLayout.setVisibility(View.GONE);
							imgsListView.setVisibility(View.VISIBLE);
						}
						if (BuildConfig.DEBUG) {
							if (results != null) {
								Log.i(TAG, Integer.toString(results.size()));
							}
						}

						new Thread() {
							public void run() {
								TaoBaokeCouponDao dao = new TaoBaokeCouponDao(
										getActivity());
								if (results == null) {
									return;
								}
								dao.delAll();
								for (SearchItem taobaokeCouponItem : results) {
									dao.insert(taobaokeCouponItem);
									System.out.println("添加成功");
								}
								Editor edit = sharedPreferences.edit();
								edit.putLong("getDataTime",
										new Date().getTime());
								edit.commit();
								int count = dao.queryCount();
								if (BuildConfig.DEBUG) {
									LogUtil.e(TAG, "总共数量    " + count);
								}
							};
						}.start();
					}

					@Override
					public void onException(Exception e) {
						toFreshLayout.setVisibility(View.VISIBLE);
						imgsListView.setVisibility(View.GONE);
						topLayout.setVisibility(View.GONE);
						imgsListView.onRefreshComplete();
						AppException.network(e).makeToast(getActivity());
					}

					@Override
					public void onResponseException(Object apiError) {
						toFreshLayout.setVisibility(View.VISIBLE);
						imgsListView.setVisibility(View.GONE);
						topLayout.setVisibility(View.GONE);
						imgsListView.onRefreshComplete();
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
			popupWindow = new PopupWindow(getActivity());
			View view = View.inflate(getActivity(),
					R.layout.tryout_popu_content, null);
			ImageView closeImageView = (ImageView) view
					.findViewById(R.id.try_popu_iv_close);
			// //////////
			// 互动广告调用方式
			RelativeLayout container = (RelativeLayout) view
					.findViewById(R.id.tryout_popu_ll_ad_container);
			
			// 将广告View增加到视图中。
			// ///////////////////
			closeImageView.setOnClickListener(this);
			popupWindow.setContentView(view);
			popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
			popupWindow.setOutsideTouchable(true);
			popupWindow
					.setHeight(CommonUtil.getScreenHeight(getActivity()) / 2);
			popupWindow.setWidth(getActivity().getWindowManager()
					.getDefaultDisplay().getWidth() - 18);
			// popupWindow.showAsDropDown(rl,
			// CommonUtil.getScreenWidth(getActivity())/2,
			// CommonUtil.getScreenHeight(getActivity())/2);
			popupWindow.showAtLocation(rl, Gravity.CENTER, 0, 0);
			break;
		case R.id.head_tv_go_menu:
			if (getActivity() instanceof ViewPagerActivity) {
				ViewPagerActivity viewPagerActivity = (ViewPagerActivity) getActivity();
				viewPagerActivity.showMenu();
			}
			break;
		case R.id.try_popu_iv_close:
			popupWindow.dismiss();
			break;
		case R.id.tryout_ll_to_refresh:
			if (!CommonUtil.checkNetState(getActivity())) {
				toFreshLayout.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), R.anim.shake));
				Toast.makeText(getActivity(), "网络不给力,检查网络", 0).show();
			} else {
				seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, 1);
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onRefresh() {
		if ((new Date().getTime() - getDataTime) < CACHE_TIME) {
			Toast.makeText(getActivity(), "当前已经是最新数据,请稍后重试", 0).show();
			// seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, 1);
			tryoutAdapter.notifyDataSetChanged();
			imgsListView.onRefreshComplete();
		} else {
			seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, 1);
		}
	}

	private final class CouponBDTask extends
			AsyncTask<Void, Void, ArrayList<SearchItem>> {
		@Override
		protected void onPreExecute() {
			topLayout.setVisibility(View.VISIBLE);
			toFreshLayout.setVisibility(View.GONE);
			imgsListView.setVisibility(View.GONE);

			super.onPreExecute();
		}

		@Override
		protected ArrayList<SearchItem> doInBackground(Void... params) {
			TaoBaokeCouponDao couponDao = new TaoBaokeCouponDao(getActivity());
			ArrayList<SearchItem> taobaokeCouponItems = couponDao
					.queryAll();
			if (taobaokeCouponItems != null && taobaokeCouponItems.size() > 0) {
				return taobaokeCouponItems;
			}
			return null;

		}

		@Override
		protected void onPostExecute(ArrayList<SearchItem> result) {
			imgsListView.onRefreshComplete();
			if (result != null) {
				if (BuildConfig.DEBUG) {
					Toast.makeText(getActivity(), "从数据库取数据", 0).show();
				}
				toFreshLayout.setVisibility(View.GONE);
				topLayout.setVisibility(View.GONE);
				imgsListView.setVisibility(View.VISIBLE);
				taobaokeCouponItems = result;
				if (imgsListView.getAdapter() != null) {
					tryoutAdapter.notifyDataSetChanged();
				} else {
					imgsListView.setAdapter(new TryoutAdapter(getActivity(),
							taobaokeCouponItems));
				}
			} else {
				toFreshLayout.setVisibility(View.VISIBLE);
				topLayout.setVisibility(View.GONE);
				imgsListView.setVisibility(View.GONE);
				if (CommonUtil.checkNetState(getActivity())) {
					seachTaobaokeCouponFromKeyWord(keyword, sort, false, false,
							1);
				}
			}
			imgsListView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		if (BuildConfig.DEBUG) {
			Toast.makeText(getActivity(), "onSaveInstanceState", 0).show();
		}
		super.onSaveInstanceState(outState);
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
			long userId = sharedPreferences.getLong("userId", 0L);
			AccessToken accessToken = TopConfig.client.getAccessToken(userId);
			String uri = CommonUtil.generateTopClickUri(item.getClick_url(),
					getActivity(), accessToken);
			intent.putExtra(BrowserActivity.BROWSERACTIVITY_NUM_IID, item.num_iid);
			intent.putExtra(BrowserActivity.BROWSERACTIVITY_TITLE,
					item.getTitle());
			getActivity().startActivity(intent);
		}

	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
