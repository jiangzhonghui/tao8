package com.tao8.app.cache.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.tao8.app.AppException;
import com.tao8.app.BuildConfig;

public class ImageLoader {
	public static final int  RETRY_TIME = 3;
	protected static final int LOAD_SUCCEED = 0x01;
	protected static final int LOAD_FAILED = 0x02;
	private static final String TAG = "ImageLoader";
	private MemoryCache memoryCache = new MemoryCache();
	private AbstractFileCache fileCache;
	private static Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	// 线程池
	private ExecutorService executorService;

	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
		memoryCache.setLimit(1024*1024*10L);
		int num = Runtime.getRuntime().availableProcessors();
		executorService = Executors.newFixedThreadPool(num*5);
		//executorService = Executors.newFixedThreadPool(5);
	}

	// 最主要的方法
	public Bitmap DisplayImage(String url, ImageView imageView, boolean isLoadOnlyFromCache,ImageLoaderCallBack callBack) {
		imageViews.put(imageView, url);
		// 先从内存缓存中查找

		Bitmap bitmap = memoryCache.get(url);
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "内存中    这个图片是"+bitmap);
		}
	/*	if (bitmap != null){
			//imageView.setImageBitmap(bitmap);
			}
		else */if (!isLoadOnlyFromCache&&bitmap==null){
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView,callBack);
		}
		return bitmap;
	}

	private void queuePhoto(String url, ImageView imageView,ImageLoaderCallBack callBack) {
		PhotoToLoad p = new PhotoToLoad(url, imageView,callBack);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		
		File f = fileCache.getFile(url);
		
		// 先从文件缓存中查找是否有
		Bitmap b = null;
		if (f != null && f.exists()){
			b = decodeFile(f);
		}
		if (b != null){
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "有这个图片");
			}
			return b;
		}
		int time = 0;
		do {		
		// 最后从指定的url中下载图片
		try {
			time++;
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			bitmap = decodeFile(f);
//			bitmap = BitmapFactory.decodeStream(is);
			os.close();
			is.close();
			return bitmap;
		} catch (Exception ex) {
			time++;
			if(time < RETRY_TIME) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {} 
				continue;
			}
			AppException.http(ex);
			Log.e("", "getBitmap catch Exception...\nmessage = " + ex.getMessage());
			return null;
		 }
		} while (time<=RETRY_TIME);
		return b;
	}

	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 100;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			// decode with inSampleSize(如果大于100*100,则缩小一倍)
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	public  class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public ImageLoaderCallBack callBack;
		public PhotoToLoad(String u, ImageView i,ImageLoaderCallBack callBack) {
			url = u;
			imageView = i;
			this.callBack = callBack;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			/*if (imageViewReused(photoToLoad))
				return;*/
			Bitmap bmp = getBitmap(photoToLoad.url);
			if (bmp==null) {
				Message message = handler.obtainMessage();
				message.what = LOAD_FAILED;
				message.obj = photoToLoad;
				handler.sendMessage(message);
			}else {
				memoryCache.put(photoToLoad.url, bmp);
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				Message message = handler.obtainMessage();
				message.what = LOAD_SUCCEED;
				message.obj = bd;
				handler.sendMessage(message);
			}
			/*
			if (imageViewReused(photoToLoad))
				return;
			
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// 更新的操作放在UI线程中
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);*/
		}
	}

	/**
	 *  防止图片错位
	 * 
	 * @param photoToLoad
	 * @return
	 */
	public static boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// 用于在UI线程中更新界面
	public class BitmapDisplayer /*implements Runnable */{
		public Bitmap bitmap;
		public PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}
	/*	public void run() {
			System.out.println("run");
			if (imageViewReused(photoToLoad)){
				return;
			}
			if (bitmap != null){
				photoToLoad.imageView.setImageBitmap(bitmap);
			System.out.println("photoToLoad.imageView.setImageBitmap(bitmap)");
			}
	
		}*/
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
		if (!BuildConfig.DEBUG) {
		}
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			Log.e("", "CopyStream catch Exception...");
		}
	}
	public interface ImageLoaderCallBack{
	         void imageLoaded(BitmapDisplayer bd);
	         void imageLoadedError(PhotoToLoad photoToLoad);
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			switch (what) {
			case LOAD_SUCCEED:
				BitmapDisplayer bd = (BitmapDisplayer) msg.obj;
				bd.photoToLoad.callBack.imageLoaded(bd);
				break;
			case LOAD_FAILED:
				PhotoToLoad pl = (PhotoToLoad) msg.obj;
				pl.callBack.imageLoadedError(pl);
				break;

			default:
				break;
			}
			
		};
	};
}