package com.tao8.app.ui;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.tao8.app.AppException;
import com.tao8.app.BuildConfig;
import com.tao8.app.R;
import com.tao8.app.TopConfig;
import com.tao8.app.adapter.CouponAdapter;
import com.tao8.app.api.GetTopData;
import com.tao8.app.api.MyTqlListener;
import com.tao8.app.domain.TaobaokeCouponItem;
import com.tao8.app.parser.TaoBaoKeCouponItemParser;
import com.tao8.app.util.CommonUtil;
import com.tao8.app.util.LogUtil;
import com.tao8.app.util.TqlHelper;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.auth.AccessToken;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class CatoryFragment extends Fragment implements OnClickListener, OnScrollListener, OnItemClickListener {
	protected static final String TAG = "CatoryFragment";
	Context mContext;
	private WebView mWebView;
	private int pageSize = 20;
	String keyword = "";
	private boolean isFromTmall;
	private String sort = "volume_desc";
	private SharedPreferences sharedPreferences;
	private ArrayList<TaobaokeCouponItem> taobaokeCouponItems;
	private CouponAdapter couponAdapter;
	private ListView imgsListView;
	private LinearLayout toFreshLayout;
	private LinearLayout toplLayout;
	public  ViewFlipper mView;
	private TextView goMeenuTextView;
	private RadioButton attentionButton;// 关注
	private RadioButton creditButton;// 信誉
	private RadioButton priceButton;// 价格
	private RadioButton sellsButton;// 销量
	private int page_no = 1;
	private int firstVisibleItem;
	private int visibleItemCount;
	private int totalItemCount;
	private TextView lableTextView;
	private ImageView moreImageView;
	private int i;
	private ImageView catoryMoreImageView;
	private TextView goTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = (ViewFlipper) inflater.inflate(R.layout.catory_fragment, null);
		findView(mView);
		initData();
		setData();
		setListener();
		return mView;
	}

	private void setListener() {
		goMeenuTextView.setOnClickListener(this);
		imgsListView.setOnScrollListener(this);
		imgsListView.setOnItemClickListener(this);
		attentionButton.setOnClickListener(this);
		creditButton.setOnClickListener(this);
		priceButton.setOnClickListener(this);
		sellsButton.setOnClickListener(this);
		goTextView.setOnClickListener(this);

	}

	private void setData() {
		moreImageView.setImageResource(R.drawable.icon_catory_filter);
		catoryMoreImageView.setVisibility(View.GONE);
		goMeenuTextView.setText("返回");
		
		
		int width = CommonUtil.getScreenWidth(getActivity());
		attentionButton.setWidth(width / 4);
		creditButton.setWidth(width / 4);
		priceButton.setWidth(width / 4);
		sellsButton.setWidth(width / 4);

		// ////
		taobaokeCouponItems = new ArrayList<TaobaokeCouponItem>();
		if (couponAdapter == null) {
			couponAdapter = new CouponAdapter(getActivity(),taobaokeCouponItems);
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
		// /////////////
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// System.out.println(System.currentTimeMillis());
				// // BrowserActivity.this.setProgress(progress * 1000);
				// System.out.println("view.getUrl() " + view.getUrl());
				if (BuildConfig.DEBUG) {
					System.out.println(progress);
				}
			}
		});

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(mContext, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {

				super.onPageFinished(view, url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("url " + url);
				// http://s.m.taobao.com/search.htm?sid=7d8f54d743a58a1f4b8a4cfe84fade37&q=%E9%92%88%E7%BB%87%E8%A1%AB&spm=41.139785.167725.3&catmap=16
				try {
					URL connUrl = new URL(url);
					String query = connUrl.getQuery();
					String[] splits = query.split("&");
					for (int i = 0; i < splits.length; i++) {
						String[] split = splits[i].split("=");
						if (split[0] != null && split[0].equalsIgnoreCase("q")) {
							keyword = URLDecoder.decode(split[1], "UTF-8");
							page_no = 1;
							seachTaobaokeCouponFromKeyWord(keyword, sort,
									isFromTmall, false, page_no);
							mView.showNext();
							sort = "volume_desc";
							sellsButton.setChecked(true);
							lableTextView.setText(keyword);
							Toast.makeText(mContext, keyword, 1).show();
							break;
						}
					}

					if (BuildConfig.DEBUG) {
						System.out.println("query" + query);
					}
				} catch (MalformedURLException e) {
					LogUtil.e(TAG, e.getLocalizedMessage(), e);
				} catch (UnsupportedEncodingException e) {
					LogUtil.e(TAG, e.getLocalizedMessage(), e);
				}
				// view.loadUrl(url);
				return true;
			}
		});
		mWebView.loadUrl("file:///android_asset/catgory.html");
	}

	private void initData() {
		if (sharedPreferences == null) {
			sharedPreferences = getActivity().getSharedPreferences("config",
					Context.MODE_PRIVATE);
		}

	}

	private void findView(View view) {
		mWebView = (WebView) view.findViewById(R.id.catory_wv_webview);
		imgsListView = (ListView) view
				.findViewById(R.id.catory_lv_content_imgs);
		toFreshLayout = (LinearLayout) view
				.findViewById(R.id.catory_ll_to_refresh);
		toplLayout = (LinearLayout) view.findViewById(R.id.catory_ll_top);
		///
		attentionButton = (RadioButton) view
				.findViewById(R.id.catory_rbtn_orderby_attention);
		creditButton = (RadioButton) view
				.findViewById(R.id.catory_rbtn_orderby_credit);
		priceButton = (RadioButton) view
				.findViewById(R.id.catory_rbtn_orderby_price);
		sellsButton = (RadioButton) view
				.findViewById(R.id.catory_rbtn_orderby_sells);
		//////////
		lableTextView = (TextView) view.findViewById(R.id.head_tv_lable);
		goMeenuTextView = (TextView) view.findViewById(R.id.head_tv_go_menu);
		moreImageView = (ImageView) view.findViewById(R.id.head_iv_more);
		
		catoryMoreImageView = (ImageView) view.findViewById(R.id.catory_head_iv_more);
		goTextView = (TextView) view.findViewById(R.id.catory_head_tv_go_menu);
		
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
		toplLayout.setVisibility(View.VISIBLE);
		GetTopData.getDataFromTop(tql, new TaoBaoKeCouponItemParser(), userId,
				new MyTqlListener() {
					@Override
					public void onComplete(Object result) {
						toFreshLayout.setVisibility(View.GONE);
						toplLayout.setVisibility(View.GONE);
						imgsListView.setVisibility(View.VISIBLE);
						ArrayList<TaobaokeCouponItem> results = (ArrayList) result;

						if (results != null && results.size() > 0) {

							if (page_no == 1) {
								taobaokeCouponItems.clear();
								taobaokeCouponItems.addAll(results);
								imgsListView.setAdapter(new CouponAdapter(getActivity(), results));
							} else {
								imgsListView.setAdapter(couponAdapter);
								taobaokeCouponItems.addAll(results);
								couponAdapter.notifyDataSetChanged();
								imgsListView.setSelection(taobaokeCouponItems
										.size() - results.size());
							}
							toFreshLayout.setVisibility(View.GONE);
							imgsListView.setVisibility(View.VISIBLE);
						}
						if (BuildConfig.DEBUG) {
							if (results != null) {
								Log.i(TAG, Integer.toString(results.size()));
							}
						}
						if (BuildConfig.DEBUG) {
							if (results != null) {
								Toast.makeText(getActivity(),
										results.size() + "  总共", 1).show();
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
		case R.id.head_tv_go_menu:
			mView.showNext();
			break;
		case R.id.head_iv_more:
			
			
			break;
		case R.id.catory_rbtn_orderby_attention:
			if (sort.equalsIgnoreCase("commissionRate_desc")) {
				break;
			}
			sort = "commissionRate_desc";//佣金
			page_no = 1;
			seachTaobaokeCouponFromKeyWord(keyword, sort, isFromTmall, false, page_no);
			break;
		case R.id.catory_rbtn_orderby_credit:
			if (sort.equalsIgnoreCase("credit_desc")) {
				break;
			}
			page_no = 1;
			sort = "credit_desc";//信用等级从高到低
			seachTaobaokeCouponFromKeyWord(keyword, sort, isFromTmall, false, page_no);
			break;
		case R.id.catory_rbtn_orderby_price:
			if (i%2 == 0) {
				sort = "price_asc";//折扣价格从低到高
			}else {
				sort = "price_desc";//折扣价格从高到低
			}
			i++;
			page_no = 1;
			seachTaobaokeCouponFromKeyWord(keyword, sort, isFromTmall, false, page_no);
			break;
		case R.id.catory_rbtn_orderby_sells:
			if (sort.equalsIgnoreCase("volume_desc")) {
				break;
			}
			sort = "volume_desc";//成交量
			page_no = 1;
			seachTaobaokeCouponFromKeyWord(keyword, sort, isFromTmall, false, page_no);
			break;
		case R.id.catory_head_tv_go_menu:
			if (mContext instanceof ViewPagerActivity) {
				ViewPagerActivity viewPagerActivity = (ViewPagerActivity) mContext;
				viewPagerActivity.showMenu();
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
			long userId = sharedPreferences.getLong("userId", 10000);
			AccessToken accessToken = TopConfig.client.getAccessToken(userId);
			String uri = CommonUtil.generateTopClickUri(item.getClick_url(),
					getActivity(), accessToken);
			intent.putExtra(BrowserActivity.BROWSERACTIVITY_URI, uri);
			intent.putExtra(BrowserActivity.BROWSERACTIVITY_TITLE,
					item.getTitle());
			getActivity().startActivity(intent);
		}

	}
}
