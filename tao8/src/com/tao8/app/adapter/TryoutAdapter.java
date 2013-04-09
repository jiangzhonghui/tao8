package com.tao8.app.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.Html;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tao8.app.R;
import com.tao8.app.cache.util.ImageLoader;
import com.tao8.app.cache.util.ImageLoader.BitmapDisplayer;
import com.tao8.app.cache.util.ImageLoader.ImageLoaderCallBack;
import com.tao8.app.cache.util.ImageLoader.PhotoToLoad;
import com.tao8.app.domain.TaobaokeCouponItem;
import com.tao8.app.util.AsyncImageLoader;

public class TryoutAdapter extends BaseAdapter {

	private Context context;
	private List<TaobaokeCouponItem> searchItems;
	private AsyncImageLoader asyncImageLoader;
	private ImageLoader mImageLoader;

	public TryoutAdapter(Context context, List<TaobaokeCouponItem> searchItems) {
		this.context = context;
		this.searchItems = searchItems;
		asyncImageLoader = new AsyncImageLoader();
		mImageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return searchItems == null ? 0 : searchItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return searchItems == null ? null : searchItems.get(position);
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
		if (convertView == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.products_list_all_item, null);
			ImageView proImageView = (ImageView) view
					.findViewById(R.id.products_item_iv_pic);
			TextView hasSellTextView = (TextView) view
					.findViewById(R.id.products_item_tv_has_sell);
			TextView originalPriceTextView = (TextView) view
					.findViewById(R.id.products_item_tv_original_price);
			TextView proNameTextView = (TextView) view
					.findViewById(R.id.products_item_tv_name);
			TextView pricetTextView = (TextView) view
					.findViewById(R.id.products_item_tv_price);
			TextView couponTextView = (TextView) view
					.findViewById(R.id.products_item_tv_rebate);
			holder.hasSellTextView = hasSellTextView;
			;
			holder.couponTextView = couponTextView;
			holder.priceTextView = pricetTextView;
			holder.proNameTextView = proNameTextView;
			holder.proPicImageView = proImageView;
			holder.originalPriceTextView = originalPriceTextView;
			view.setTag(holder);
		} else {
			view = convertView;
		}
		holder = (ViewHolder) view.getTag();

		TaobaokeCouponItem item = (TaobaokeCouponItem) getItem(position);
		System.out.println(item);
		System.out.println(position);
		holder.proNameTextView.setText(Html.fromHtml(item.getTitle()));
		holder.priceTextView.setText("￥" + item.getCoupon_price());
		TextPaint paint = holder.originalPriceTextView.getPaint();
		paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		holder.originalPriceTextView.setText("￥" + item.getPrice());

		holder.couponTextView.setText(String.format("%.1f",
				(Float.parseFloat(item.getCoupon_rate()) / 1000))
				+ "折");// 折扣
		holder.hasSellTextView.setText("最近售出:" + item.getVolume() + "件");
		holder.proPicImageView.setTag(item.getPic_url() + "_80x80.jpg");
		Bitmap cachedImage = mImageLoader.DisplayImage(item.getPic_url()
				+ "_80x80.jpg", holder.proPicImageView, false,
				new ImageLoaderCallBack() {

					@Override
					public void imageLoadedError(PhotoToLoad photoToLoad) {
						System.out.println("PhotoToLoad     " + photoToLoad);
					}

					@Override
					public void imageLoaded(BitmapDisplayer bd) {
						System.out.println("BitmapDisplayer   " + bd);
						if (ImageLoader.imageViewReused(bd.photoToLoad)) {
							return;
						}
						bd.photoToLoad.imageView.setImageBitmap(bd.bitmap);
					}
				});
		if (cachedImage == null) {
			holder.proPicImageView.setImageResource(R.drawable.ic_launcher);
		} else {
			holder.proPicImageView.setImageBitmap(cachedImage);
		}

		/*
		 * Drawable cachedImage =
		 * asyncImageLoader.loadDrawable(item.getPic_url()+"_80x80.jpg", new
		 * ImageCallback() {
		 * 
		 * @Override public void imageLoaded(Drawable imageDrawable, String
		 * imageUrl) { // TODO Auto-generated method stub ImageView
		 * imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl); if
		 * (imageViewByTag != null) {
		 * imageViewByTag.setImageDrawable(imageDrawable); } } });
		 * 
		 * if (cachedImage == null) {
		 * holder.proPicImageView.setImageResource(R.drawable.ic_launcher);
		 * }else{ holder.proPicImageView.setImageDrawable(cachedImage); }
		 */

		return view;
	}

	static final class ViewHolder {
		public TextView originalPriceTextView;
		public TextView priceTextView;
		public TextView titleTextView;
		public TextView couponTextView;
		public TextView hasSellTextView;
		public TextView proNameTextView;
		public ImageView proPicImageView;
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

}
