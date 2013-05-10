package com.tao8.app.ad;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.emar.escore.sdk.view.DetailSDK;
import com.emar.escore.sdk.widget.DetailInfo;
import com.emar.escore.sdk.widget.WallInfo;
import com.tao8.app.R;
/**
 * 
 * @author zgb
 *
 */
public class CustomDetailActivity extends Activity {
	
	private final static int DETAIL_LOAD_SUCCESS = 0; // 详情加载成功
	private final static int DETAIL_LOAD_ERROR = 1; // 详情加载失败
	private Context context;
	private DetailInfo detailInfo;
	private WallInfo wallInfo;
	private CustomProgressBar bar;
	private FrameLayout mFrameLayout;
	private ImageView detailImage;
	private TextView detailContent;
	private TextView title;
	private TextView version;
	private TextView size;
	private TextView detail_desccontent;
	private TextView desc;
	
	private ImageView detailImage1;
	private ImageView detailImage2;
	
	private Button smallDownload;
	private Button bigDownload;
	private LinearLayout linearLayout;
	
	private LinearLayout smallLayout;
	private LinearLayout bigLayout;
	
	private AsyncImageLoader mImageLoader;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DETAIL_LOAD_SUCCESS:
				mFrameLayout.removeView(bar);
				detailInfo = DetailSDK.getInstance(context).getDetailInfo();
				initUI();
				initData();
				break;
			case DETAIL_LOAD_ERROR:
				Toast.makeText(context,"网络超时或异常?",Toast.LENGTH_SHORT).show();
				finish();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_detail);
		context = this;
		wallInfo = (WallInfo)getIntent().getSerializableExtra("wallInfo");
		mImageLoader = AsyncImageLoader.getInstance();
		bar = new CustomProgressBar(context, null, 0);
		mFrameLayout = (FrameLayout)findViewById(R.id.frameLayout);
		mFrameLayout.addView(bar);
		if(wallInfo != null){
			DetailSDK.getInstance(context).initDetailInfo(context, wallInfo, mHandler);
		}
	}
	
	
	private void initUI(){
		
		detailContent = (TextView)findViewById(R.id.detail_content);
		detailContent.setText("精彩无限,赢得积分");
		linearLayout =  (LinearLayout)findViewById(R.id.bglayout);
		detailImage = (ImageView)findViewById(R.id.detail_icon);
		detailImage1 = (ImageView)findViewById(R.id.detail_image1);
		detailImage2 = (ImageView)findViewById(R.id.detail_image2);		
		title = (TextView)findViewById(R.id.detail_title);		
		version = (TextView)findViewById(R.id.detail_version);		
		size = (TextView)findViewById(R.id.detail_filesize);
		desc = (TextView)findViewById(R.id.detail_description);
		detail_desccontent = (TextView)findViewById(R.id.detail_desccontent);
		smallLayout = (LinearLayout)findViewById(R.id.smallLayout);
		bigLayout = (LinearLayout)findViewById(R.id.bigLayout);
		smallDownload = new Button(this);
		LinearLayout.LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		smallDownload.setLayoutParams(params1);
		bigDownload = new Button(this);
		LinearLayout.LayoutParams params2 = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		bigDownload.setLayoutParams(params2);
	}
	
	
	private void initData(){
		mImageLoader.loadBitmap(detailInfo.detail_icon_Url, new AsyncImageLoader.ImageCallback() {
			
			@Override
			public void imageLoad(Bitmap drawable, String url) {
				// TODO Auto-generated method stub
				detailImage.setImageBitmap(drawable);
			}
		});
		
		List<String> list = detailInfo.detail_picturesUrl;
		if(list != null && list.size() > 0 ){
			for (int i = 0; i < list.size(); i++) {
				if(list.size() >= 1){
					if(i == 0){
						mImageLoader.loadBitmap(list.get(0), new AsyncImageLoader.ImageCallback() {
							
							@Override
							public void imageLoad(Bitmap drawable, String url) {
								// TODO Auto-generated method stub
								detailImage1.setImageBitmap(drawable);
							}
						});
					}else if(i == 1){
							mImageLoader.loadBitmap(list.get(1), new AsyncImageLoader.ImageCallback() {
							
							@Override
							public void imageLoad(Bitmap drawable, String url) {
								// TODO Auto-generated method stub
								detailImage2.setImageBitmap(drawable);
							}
						});
					}
				}
			}
		}
		linearLayout.setBackgroundColor(Color.BLUE);
		title.setText(detailInfo.detail_first);
		version.setText(detailInfo.detail_third);
		size.setText(detailInfo.detail_fourth);
		desc.setText(detailInfo.detail_sixth);
		detail_desccontent.setText(detailInfo.detail_seventh);
		smallLayout.addView(smallDownload);
		bigLayout.addView(bigDownload);
		smallDownload.setText("免费下载");
		bigDownload.setText("立即免费下载");
		smallDownload.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						DetailSDK.getInstance(context).downloadAd();
					}
				});
				
				
		bigDownload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DetailSDK.getInstance(context).downloadAd();
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		DetailSDK.getInstance(this).onDestroy();
	}
}
