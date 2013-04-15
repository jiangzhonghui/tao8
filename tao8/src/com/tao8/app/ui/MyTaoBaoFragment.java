package com.tao8.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.waps.AdInfo;
import cn.waps.AppConnect;
import cn.waps.SDKUtils;

import com.tao8.app.R;
import com.tao8.app.TopConfig;
import com.tao8.app.ad.AppDetail;
import com.tao8.app.adapter.AccountDetailAdapter;
import com.tao8.app.cache.util.ImageLoader;
import com.tao8.app.cache.util.ImageLoader.BitmapDisplayer;
import com.tao8.app.cache.util.ImageLoader.ImageLoaderCallBack;
import com.tao8.app.cache.util.ImageLoader.PhotoToLoad;
import com.tao8.app.domain.AccountDetail;
import com.tao8.app.util.CommonUtil;
import com.tao8.app.util.Config;
import com.tao8.app.util.LogUtil;
import com.taobao.top.android.auth.AccessToken;

public class MyTaoBaoFragment extends Fragment implements OnClickListener, OnItemClickListener{

	private ImageView userImageView;
	private TextView userNickTextView;
	private ListView taobaoListView;
	private ImageView moreImageView;
	private TextView goMenuTextView;
	private TextView lableTextView;
	private SharedPreferences preferences;
	private long userId;
	private ImageLoader mImageLoader;
	private AccessToken accessToken;
	private ArrayList<AccountDetail> accountDetails;
	private ListView recommondListView;
	private final Handler mHandler = new Handler();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mytaobao, null);
		findView(view);
		initData();
		setListener();
		setData();
		return view;
	}

	private void setData() {
		moreImageView.setVisibility(View.GONE);
		lableTextView.setText("我的淘宝");
		if (accessToken==null)  {
			userNickTextView.setText("未登录, 点击登陆");
			
		}else {
			String nick = accessToken.getAdditionalInformation().get(AccessToken.KEY_TAOBAO_USER_NICK);
			userNickTextView.setText("欢迎您: "+nick);
		}
		if (userId!=0L) {
			String url = "http://wwc.taobaocdn.com/avatar/get_avatar.do?userId="+userId+"1&width=100&height=100&type=sns";
			Bitmap image = mImageLoader.DisplayImage(url, userImageView, false, new ImageLoaderCallBack() {
				
				@Override
				public void imageLoadedError(PhotoToLoad photoToLoad) {
					LogUtil.e("PhotoToLoad", "load pic error"+ photoToLoad.url);
				}
				
				@Override
				public void imageLoaded(BitmapDisplayer bd) {
					bd.photoToLoad.imageView.setImageBitmap(bd.bitmap);
				}
			});
			if (image == null) {  
				userImageView.setImageResource(R.drawable.ic_launcher);  
			}else{  
				userImageView.setImageBitmap(image);  
			}
		}
		taobaoListView.setAdapter(new AccountDetailAdapter(getActivity(),accountDetails));
		setListViewHeightBasedOnChildren(taobaoListView);
	}

	private void setListener() {
		goMenuTextView.setOnClickListener(this);
		taobaoListView.setOnItemClickListener(this);
		userNickTextView.setOnClickListener(this);
		recommondListView.setOnItemClickListener(this);
	}

	private void initData() {
		
		preferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
		userId = preferences.getLong("userId", 0L);
		accessToken = TopConfig.client.getAccessToken(userId);
		mImageLoader = new ImageLoader(getActivity());
		
		recommondListView.setCacheColorHint(0);
		//设置ListView每个Item间的间隔线的颜色渐变
//		GradientDrawable divider_gradient = new GradientDrawable(Orientation.TOP_BOTTOM, 
//			new int[] {Color.parseColor("#cccccc"), Color.parseColor("#ffffff"), Color.parseColor("#cccccc")}); 
//		recommondListView.setDivider(divider_gradient);
//		recommondListView.setDividerHeight(4);
		// 异步加载自定义广告数据
		new GetDiyAdTask(getActivity(), recommondListView).execute();
		

		///////////检测授权
//		if (accessToken==null) {
//			Toast t = Toast.makeText(getActivity(), "请先授权",Toast.LENGTH_SHORT);
//			TopConfig.client.authorize(getActivity());
//			t.show();
//			return;
//		}else {
//			String nick = accessToken.getAdditionalInformation().get(AccessToken.KEY_TAOBAO_USER_NICK);
//			userNickTextView.setText(nick);
//		}
		initAccountDetails();
	}

	private void findView(View view) {
		userImageView = (ImageView) view.findViewById(R.id.mytaobao_im_user_img);
		userNickTextView = (TextView) view.findViewById(R.id.mytaobao_tv_user_nick);
		taobaoListView = (ListView) view.findViewById(R.id.mytaobao_lv_taobao_detail);
		moreImageView = (ImageView) view.findViewById(R.id.head_iv_more);
		goMenuTextView = (TextView) view.findViewById(R.id.head_tv_go_menu);
		lableTextView = (TextView) view.findViewById(R.id.head_tv_lable);
		recommondListView = (ListView) view.findViewById(R.id.mytaobao_lv_ad_recommond);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_tv_go_menu:
			if (getActivity() instanceof ViewPagerActivity) {
				ViewPagerActivity activity = (ViewPagerActivity) getActivity();
				activity.showMenu();
			}
			break;
		case R.id.mytaobao_tv_user_nick:
			if (accessToken==null) {
				Toast t = Toast.makeText(getActivity(), "请先授权",Toast.LENGTH_SHORT);
				TopConfig.client.authorize(getActivity());
				t.show();
			}
			break;
		default:
			break;
		}
	}
	private List<AccountDetail> initAccountDetails() {
		String sid = accessToken==null?"":accessToken.getValue();
		accountDetails = new ArrayList<AccountDetail>();
		AccountDetail tPaymentDetail = new AccountDetail();
		tPaymentDetail.setAction(BrowserActivity.BROWSERACTIVITY_ACTION);
		tPaymentDetail.setItemName("待付款订单");
		tPaymentDetail.setUri(Config.TAO_ORDER.concat(sid));
		accountDetails.add(tPaymentDetail);
		AccountDetail tCollectionDetail = new AccountDetail();
		tCollectionDetail.setAction(BrowserActivity.BROWSERACTIVITY_ACTION);
		tCollectionDetail.setItemName("淘宝收藏");
		tCollectionDetail.setUri(Config.TAO_MYCOLLECTION.concat(sid));
		accountDetails.add(tCollectionDetail);
		AccountDetail tCarDetail = new AccountDetail();
		tCarDetail.setAction(BrowserActivity.BROWSERACTIVITY_ACTION);
		tCarDetail.setItemName("淘宝购物车");
		tCarDetail.setUri(Config.TAO_CAR.concat(sid));
		accountDetails.add(tCarDetail);
		AccountDetail tLogisticsDetail = new AccountDetail();
		tLogisticsDetail.setAction(BrowserActivity.BROWSERACTIVITY_ACTION);
		tLogisticsDetail.setItemName("查看物流");
		tLogisticsDetail.setUri(Config.TAO_LOGISTICS.concat(sid));
		accountDetails.add(tLogisticsDetail);
		AccountDetail checkUpdate = new AccountDetail();
		checkUpdate.setAction("checkUpdate");
		checkUpdate.setItemName("检查更新");
		checkUpdate.setUri(null);
		accountDetails.add(checkUpdate);
		return accountDetails;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		switch (parent.getId()) {
		case R.id.mytaobao_lv_ad_recommond:
			AppDetail.getInstanct().showAdDetail(getActivity(),(AdInfo)view.getTag());
			break;
		case R.id.mytaobao_lv_taobao_detail:
			if ("checkUpdate".equals(accountDetails.get(position).getAction())) {
				AppConnect.getInstance(getActivity()).checkUpdate(getActivity());
				break;
			}
			if (!CommonUtil.checkNetState(getActivity())) {
				view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
						R.anim.shake));
				Toast.makeText(getActivity(), "网络不给力,检查网络", 0).show();
				
			}else {
				if (accessToken==null) {
					Toast t = Toast.makeText(getActivity(), "请先授权",Toast.LENGTH_SHORT);
					TopConfig.client.authorize(getActivity());
					t.show();
				}else {
					Intent intent = new Intent();
					AccountDetail accountDetail = accountDetails.get(position);
					intent.setAction(accountDetail.getAction());
					intent.putExtra(BrowserActivity.BROWSERACTIVITY_TITLE, ((TextView)view.findViewById(R.id.account_list_item_tv_name)).getText().toString().trim());
					intent.putExtra(BrowserActivity.BROWSERACTIVITY_URI, accountDetail.getUri());
					startActivity(intent);
				}
			}
			break;
		default:
			break;
		}
		
	}
	
private class GetDiyAdTask extends AsyncTask<Void, Void, Boolean>{
		
		Context context;
		ListView listView;
		List<AdInfo> list;
		
		GetDiyAdTask(Context context, ListView listView){
			this.context = context;
			this.listView = listView;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected Boolean doInBackground(Void... params) {
			
			try {
				while(true){
					if(!new SDKUtils(context).isConnect()){
						mHandler.post(new Runnable(){
							
							@Override
							public void run() {
								Toast.makeText(context, "数据获取失败,请检查网络重新加载", Toast.LENGTH_LONG).show();
								//((Activity)context).finish();
							}
						});	
						
						break;
					}
					list = AppConnect.getInstance(context).getAdInfoList();
					if(list != null){
						
						mHandler.post(new Runnable(){
							
							@Override
							public void run() {
								listView.setAdapter(new MyAdapter(context, list));
								setListViewHeightBasedOnChildren(recommondListView);
							}
						});	
						
						break;
					}
					
					try {
						Thread.sleep(500); 
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
private class MyAdapter extends BaseAdapter{
	Context context;
	List<AdInfo> list;
	public MyAdapter(Context context, List<AdInfo> list){
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		final AdInfo adInfo = list.get(position);
//		
//		View adatperView = null;
//		
//		try {
//			adatperView = AppItemView.getInstance().getAdapterView(context, adInfo, 0, 0);
//				
//			convertView = adatperView;
//			convertView.setTag(adatperView);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//        return adatperView;
		
		
		View view = null;
		if (convertView==null) {
			view = View.inflate(context, R.layout.ad_waps_item, null);
		}else {
			convertView = view;
		}
		final AdInfo adInfo = list.get(position);
		ImageView downLoadImageView = (ImageView) view.findViewById(R.id.ad_im_download);
		downLoadImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppConnect.getInstance(context).downloadAd(adInfo.getAdId());
			}
		});
		ImageView iconImageView = (ImageView) view.findViewById(R.id.ad_im_icon);
		iconImageView.setImageDrawable(new BitmapDrawable(adInfo.getAdIcon())); 
		TextView descriptTextView = (TextView) view.findViewById(R.id.ad_tv_descript);
		descriptTextView.setText(adInfo.getAdText());
		TextView titleTextView = (TextView) view.findViewById(R.id.ad_tv_title);
		titleTextView.setText(adInfo.getAdName());
		TextView sizeTextView = (TextView) view.findViewById(R.id.ad_tv_size);
		sizeTextView.setText(adInfo.getFilesize() + "M");
		view.setTag(adInfo);
		return view;
	}
}
public void setListViewHeightBasedOnChildren(ListView listView) {  
    Adapter listAdapter = listView.getAdapter();   
    if (listAdapter == null) {  
        return;  
    }  

    int totalHeight = 0;  
    for (int i = 0; i < listAdapter.getCount(); i++) {  
        View listItem = listAdapter.getView(i, null, listView);  
        if (listItem!=null) {
        	listItem.getMeasuredHeight();
        	listItem.measure(0, 0);
        	
        	totalHeight += listItem.getMeasuredHeight();  
		}
    }  

    ViewGroup.LayoutParams params = listView.getLayoutParams();  
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
    ((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
    listView.setLayoutParams(params);  
} 
}
