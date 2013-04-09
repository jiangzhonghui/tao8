package com.tao8.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.tao8.app.domain.TaobaokeCouponItem;
import com.tao8.app.parser.TaoBaoKeCouponItemParser;
import com.tao8.app.util.CommonUtil;
import com.tao8.app.util.TqlHelper;
import com.tao8.app.widget.PullToRefreshListView;
import com.tao8.app.widget.PullToRefreshListView.OnRefreshListener;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.auth.AccessToken;

public class TryoutFragment extends Fragment implements OnClickListener,
		OnRefreshListener {

	protected static final String TAG = "TryoutFragment";
	private PullToRefreshListView imgsListView;
	private ImageView moreImageView;
	private TextView menuTextView;
	private RelativeLayout rl;
	private TextView lableTextView;
	private LinearLayout toFreshLayout;
	private String keyword = "手机付邮";
	private ArrayList<TaobaokeCouponItem> taobaokeCouponItems;
	private String sort = "volume_desc";// 成交量从高到低
	private TryoutAdapter tryoutAdapter;
	private SharedPreferences sharedPreferences;
	private int pageSize = 100;
	private PopupWindow popupWindow;
	private LinearLayout topLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.tryout_fragment, null);
		findView(linearLayout);
		Toast.makeText(getActivity(), "onCreateView", 0).show();
		setData();
		setListener();
		return linearLayout;
	}
	@Override
	public void onDestroyView() {
		//Toast.makeText(getActivity(), "onDestroyView", 0).show();
		super.onDestroyView();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//setData();
		super.onCreate(savedInstanceState);
	}
	
	private void setListener() {
		menuTextView.setOnClickListener(this);
		imgsListView.setOnRefreshListener(this);
		moreImageView.setOnClickListener(this);
	}

	private void setData() {
		
		moreImageView.setImageResource(R.drawable.icon_notice);
		topLayout.setVisibility(View.VISIBLE);
		taobaokeCouponItems = new ArrayList<TaobaokeCouponItem>();
		if (tryoutAdapter == null) {
			tryoutAdapter = new TryoutAdapter(getActivity(),taobaokeCouponItems);
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
		if (taobaokeCouponItems!=null&&taobaokeCouponItems.size()>0) {
			return;
		}
		seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, 1);
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
						ArrayList<TaobaokeCouponItem> results = (ArrayList) result;
						if (BuildConfig.DEBUG) {
							Toast.makeText(getActivity(),
									results.size() + "  总共", 1).show();
						}
						if (results != null && results.size() > 0) {
							if (page_no == 1) {
								taobaokeCouponItems.clear();
							}
							taobaokeCouponItems.addAll(results);
							if (imgsListView.getAdapter() == null||imgsListView.getAdapter().getCount()<=0) {
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
		case R.id.head_iv_more:
			popupWindow = new PopupWindow(getActivity());
			View view = View.inflate(getActivity(),
					R.layout.tryout_popu_content, null);
			ImageView closeImageView = (ImageView) view
					.findViewById(R.id.try_popu_iv_close);
			closeImageView.setOnClickListener(this);
			popupWindow.setContentView(view);
			popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
			popupWindow.setOutsideTouchable(true);
			popupWindow
					.setHeight(CommonUtil.getScreenHeight(getActivity()) / 2);
			popupWindow.setWidth(getActivity().getWindowManager()
					.getDefaultDisplay().getWidth() / 2 + 40);
			// popupWindow.showAsDropDown(rl);
			// popupWindow.showAsDropDown(rl);
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
		default:
			break;
		}

	}

	@Override
	public void onRefresh() {
		seachTaobaokeCouponFromKeyWord(keyword, sort, false, false, 1);
	}

}
