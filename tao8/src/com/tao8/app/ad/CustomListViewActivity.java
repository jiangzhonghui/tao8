package com.tao8.app.ad;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emar.escore.scorewall.ScoreAdListSDK;
import com.emar.escore.sdk.b.g;
import com.emar.escore.sdk.b.h;
import com.emar.escore.sdk.view.DetailSDK;
import com.emar.escore.sdk.widget.WallInfo;
import com.tao8.app.R;
/**
 * 
 * @author zgb
 *
 */
public class CustomListViewActivity extends Activity{

	private ListView listView;
	private CustomAdapter mAdapter;
	private int pageNumber = 1;
	private int pageSize =10;
	private List<WallInfo> mList = new LinkedList<WallInfo>();
	private static final int ADLIST_LOAD_SUCCESS = 4;
	private static final int ADLIST_LOAD_ERROR = 5;
	private static final int ADLIST_LOAD_EMPTY = 6;
	private String message_loaderr;
	private Context context;
	private int visibleLast;
	private FrameLayout mFrameLayout;
	private CustomProgressBar bar;
	boolean isTurnPage = false;
	private int count;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case ADLIST_LOAD_SUCCESS:
				mFrameLayout.removeView(bar);
				mList = ScoreAdListSDK.getInstance(context).getAdList();
				if(!isTurnPage){
					setAdapter();					
				}else{
					loadData();
				}
				setScrollListener();
				break;
			case ADLIST_LOAD_ERROR:
				mFrameLayout.removeView(bar);
				message_loaderr = msg.getData().getString("message");
				if (message_loaderr != null
						&& message_loaderr.trim() != "") {
					Toast.makeText(context, message_loaderr,
							Toast.LENGTH_LONG).show();
				}
				break;
			case ADLIST_LOAD_EMPTY:
				mFrameLayout.removeView(bar);
				message_loaderr = msg.getData().getString(
						"message");
				if (message_loaderr != null
						&& message_loaderr.trim() != "") {
					Toast.makeText(context, message_loaderr,
							Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_list);
		context = this;
		listView = (ListView)findViewById(R.id.listView);
		// 设置ListView每个Item间的间隔线的颜色渐变
				GradientDrawable divider_gradient = new GradientDrawable(
						Orientation.TOP_BOTTOM, new int[] {
								Color.parseColor("#90EE90"),
								Color.parseColor("#ffffff"),
								Color.parseColor("#90EE90") });
				listView.setDivider(divider_gradient);
				listView.setDividerHeight(3);
		mFrameLayout = (FrameLayout)findViewById(R.id.frameLayout);
		bar = new CustomProgressBar(this, null,0);
		mFrameLayout.addView(bar);
		ScoreAdListSDK.getInstance(this).getAdInitInfo(pageSize,pageNumber,mHandler);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				WallInfo info = mList.get(position);
				DetailSDK.getInstance(context).gotoDetail(context, CustomDetailActivity.class, info);
				h.a(context, 0, info.id, info.page_type);
			}
			
		});
	}
	
	
	
	private void setAdapter(){
		mAdapter = new CustomAdapter(this, mList,listView);
		listView.setAdapter(mAdapter);
	}
	
	private void loadData(){
		mAdapter.notifyDataSetChanged();
	}
	
	private void setScrollListener(){
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			int lastIndex = 0;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				lastIndex = mAdapter.getCount() - 1;
//Log.e("123", "lastIndex = "+lastIndex);
				count = ScoreAdListSDK.getInstance(context).getPageCount();
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLast == lastIndex) {
//Log.e("123", "count = "+count);
					if(pageNumber < count){
						pageNumber++;
						doUpdate();
					}else{
						
						while(true){
							if(mFrameLayout.getChildCount() > 1){
							  mFrameLayout.removeViewAt(mFrameLayout.getChildCount() - 1);
							}else{
								break;
							}
						}
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				visibleLast = firstVisibleItem + visibleItemCount - 1;
//Log.e("123", "visibleLast = "+visibleLast);
			}
		});
	}
	
	
	private void doUpdate(){
		isTurnPage = true;
		bar = new CustomProgressBar(this, null,1);
		mFrameLayout.addView(bar);
		ScoreAdListSDK.getInstance(this).getAdInitInfo(pageSize, pageNumber, mHandler);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mList.clear();
		ScoreAdListSDK.getInstance(this).onDestroy();		
	}
	
	static class CustomAdapter extends BaseAdapter{
		static class ViewHolder{
			public ImageView imageView;
			public TextView titleView;
			public TextView scoreView;
			public TextView button;
			public TextView descView;
		}
		private List<WallInfo> mLists;
		private LayoutInflater inflater;
		private Context context;
		private AsyncImageLoader imageLoader;
		private ListView listView;
		
		public CustomAdapter(Context context,List<WallInfo> list,ListView listView){
			this.context = context;
			this.mLists = list;
			this.listView = listView;
			inflater = LayoutInflater.from(context);
			imageLoader = AsyncImageLoader.getInstance();
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mLists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final WallInfo info = mLists.get(position);
			final ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item, null);
				holder.imageView = (ImageView)convertView.findViewById(R.id.iconView);
				holder.titleView = (TextView)convertView.findViewById(R.id.title);
				holder.scoreView = (TextView)convertView.findViewById(R.id.score);
				holder.descView = (TextView)convertView.findViewById(R.id.desc);
				holder.button = (TextView)convertView.findViewById(R.id.download);
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			
//			holder.imageView.setImageBitmap(bm);
			final String imageUrl = info.generalInfo.wall_icon_Url;
			ImageView imageView = holder.imageView;
			imageView.setTag(imageUrl);
			if(imageUrl != null && !imageUrl.equals("")){
				Bitmap drawable = imageLoader.loadBitmap(imageUrl, new AsyncImageLoader.ImageCallback() {
					
					@Override
					public void imageLoad(Bitmap drawable, String url) {
						ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
						 if (imageViewByTag != null) {  
		                        imageViewByTag.setImageBitmap(drawable);  
		                 }  
					}
				});
				
				if(drawable != null){
					imageView.setImageBitmap(drawable);
				}
			}
			holder.titleView.setText(info.title);
			holder.scoreView.setText(info.generalInfo.wall_right);
			holder.descView.setText(info.generalInfo.wall_desc);
			if(info.state == 3){
				holder.button.setText("打开");
			}else{
				holder.button.setText("下载");
			}
			
			holder.button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ScoreAdListSDK.getInstance(context).downloadAd(info);
					new Thread(){
						@Override
						public void run() {
							SystemClock.sleep(500);
							h.a(context, 0, info.id, info.page_type);
							g g1 = new g();
							SystemClock.sleep(500);
							g1.a(context, info);
							super.run();
						}
					}.start();
				}
			});
			
			return convertView;
		}
		
	}

}
