package com.tao8.app.ad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
/**
 * 
 * @author zgb
 *
 */
public class AsyncImageLoader {
	private HashMap<String, SoftReference<Drawable>> imageCache;
	private ExecutorService threadPool = Executors.newFixedThreadPool(2);
	public AsyncImageLoader(){
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}
	
	public Drawable loadDrawable(final String imageUrl,final ImageCallback imageCallback){
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Drawable> softDrawable = imageCache.get(imageUrl);
			Drawable drawable = softDrawable.get();
			if(drawable != null){
				return drawable;
			}
		}
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				imageCallback.imageLoad((Drawable)msg.obj, imageUrl);
			}
		};
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				Drawable drawable = loadImageFormUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		};
		threadPool.execute(task);
		return null;
	}
	
	
	private static Drawable loadImageFormUrl(String imageUrl){
		URL url;
		InputStream is = null;
		Drawable drawable = null;
		try{
			url = new URL(imageUrl);
			is = (InputStream)url.getContent();
			drawable = Drawable.createFromStream(is, "src");
		}catch(Exception e){
			e.printStackTrace();
		}
		return drawable;
	}
	
	
	public interface ImageCallback{
		public void imageLoad(Drawable drawable,String url);
	}
	
	
	private static boolean isSdcard(){
		String mount = Environment.getExternalStorageState();
		if(mount != null && mount.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
	
	public static void savePic(Bitmap bitmap, String packageName,String imageUrl) {
		if (bitmap != null && imageUrl != null && !"".equals(imageUrl)) {
			if (isSdcard()) {
				saveImageToCard(bitmap,packageName,imageUrl);
			} else {
				saveToDataDir(bitmap,packageName,imageUrl);
			}
		}
	}
	
	private static void saveImageToCard(Bitmap bitmap,String packageName,String fileName){
		fileName = fileName.substring(fileName.lastIndexOf("/"));
		String path = Environment.getExternalStorageState()+"/"+packageName+"/"+fileName;
		File file = new File(path);
		try{
			if(!file.exists()){
				 file.getParentFile().mkdirs();
				 file.createNewFile();
			     FileOutputStream fos = new FileOutputStream(file);
			     bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			     fos.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void saveToDataDir(Bitmap bitmap,String packageName,String fileName) {
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		String path = Environment.getDataDirectory()+"/data/data"+packageName+"/"+fileName;
		File file = new File(path + fileName);
		FileOutputStream outputStream;
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
				outputStream = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
				outputStream.close();
			} catch (Exception e) {
				Log.e("", e.toString());
			}
		}
	}
	
	public void recyle(){
		threadPool.shutdownNow();
	}

}
