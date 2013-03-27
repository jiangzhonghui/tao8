package com.emer.egou.app.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.emer.egou.app.R;
import com.emer.egou.app.cache.util.ImageLoader;
import com.emer.egou.app.cache.util.ImageLoader.BitmapDisplayer;
import com.emer.egou.app.cache.util.ImageLoader.ImageLoaderCallBack;
import com.emer.egou.app.cache.util.ImageLoader.PhotoToLoad;
import com.emer.egou.app.domain.SearchItem;
import com.emer.egou.app.util.AsyncImageLoader;

public class ProductsAdapter extends BaseAdapter {
	private Context context;
	private List<SearchItem> searchItems;
	ListView listView;
	private AsyncImageLoader asyncImageLoader;
	private ImageLoader mImageLoader;  
	public ProductsAdapter(Context context,List<SearchItem> searchItems,ListView listView){
		this.context = context;
		this.searchItems = searchItems;
		this.listView = listView;
		asyncImageLoader = new AsyncImageLoader();
		mImageLoader = new ImageLoader(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return searchItems==null?0:searchItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return searchItems==null?null:searchItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder holder;
		if (convertView==null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.products_list_all_item, null);
			ImageView proImageView = (ImageView) view.findViewById(R.id.products_item_iv_pic);
			TextView hasSellTextView = (TextView) view.findViewById(R.id.products_item_tv_has_sell);
			TextView proNameTextView = (TextView) view.findViewById(R.id.products_item_tv_name);
			TextView pricetTextView = (TextView) view.findViewById(R.id.products_item_tv_price);
			TextView rebaTextView = (TextView) view.findViewById(R.id.products_item_tv_rebate);
			holder.hasSellTextView = hasSellTextView;;
			holder.rebateTextView = rebaTextView;
			holder.priceTextView = pricetTextView;
			holder.proNameTextView = proNameTextView;
			holder.proPicImageView = proImageView;
			view.setTag(holder);
		}else {
			view = convertView;
		}
		holder = (ViewHolder) view.getTag();
		
		SearchItem item = (SearchItem) getItem(position);
		System.out.println(item);
		System.out.println(position);
		holder.proNameTextView.setText(Html.fromHtml(item.getTitle()));
		holder.priceTextView.setText("￥"+item.getPrice());
		holder.rebateTextView.setText("返:"+item.getCommission()+"元");//佣金
		holder.hasSellTextView.setText("最近售出:"+item.getVolume()+"件");
		holder.proPicImageView.setTag(item.getPic_url()+"_80x80.jpg");
		Bitmap cachedImage = mImageLoader.DisplayImage(item.getPic_url()+"_80x80.jpg", holder.proPicImageView, false,new ImageLoaderCallBack() {
			
			@Override
			public void imageLoadedError(PhotoToLoad photoToLoad) {
				System.out.println("PhotoToLoad     "+photoToLoad);
			}
			@Override
			public void imageLoaded(BitmapDisplayer bd) {
				System.out.println("BitmapDisplayer   "+bd);
				if (ImageLoader.imageViewReused(bd.photoToLoad)){
					return;
				}
				bd.photoToLoad.imageView.setImageBitmap(bd.bitmap);
			}
		});
		if (cachedImage == null) {  
			holder.proPicImageView.setImageResource(R.drawable.ic_launcher);  
        }else{  
        	holder.proPicImageView.setImageBitmap(cachedImage);  
        }
		
		
	/*	Drawable cachedImage = asyncImageLoader.loadDrawable(item.getPic_url()+"_80x80.jpg", new ImageCallback() {
			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				// TODO Auto-generated method stub
				 ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);  
                 if (imageViewByTag != null) {  
                     imageViewByTag.setImageDrawable(imageDrawable);  
                 } 
			}
		});
		
		if (cachedImage == null) {  
			holder.proPicImageView.setImageResource(R.drawable.ic_launcher);  
        }else{  
        	holder.proPicImageView.setImageDrawable(cachedImage);  
        } */ 
		
		return view;
	}
	static final class ViewHolder{
		public TextView priceTextView;
		public TextView titleTextView;
		public TextView rebateTextView;
		public TextView hasSellTextView;
		public TextView proNameTextView;
		public ImageView proPicImageView;
	}
	

	public ImageLoader getImageLoader(){
		return mImageLoader;
	}
	
}
