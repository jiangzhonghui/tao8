package com.tao8.app.ad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	private static final AsyncImageLoader asyncImageLoader  = new AsyncImageLoader();;
	private static Map<String, SoftReference<Bitmap>> imageCache ;
	private static final ExecutorService threadPool = Executors.newFixedThreadPool(2);
	private  AsyncImageLoader(){
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}
	public static AsyncImageLoader getInstance(){
		return asyncImageLoader;
	}
	
	public Bitmap loadBitmap(final String imageUrl,final ImageCallback imageCallback){
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Bitmap> softBitmap = imageCache.get(imageUrl);
			Bitmap Bitmap = softBitmap.get();
			if(Bitmap != null){
				return Bitmap;
			}
		}
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				imageCallback.imageLoad((Bitmap)msg.obj, imageUrl);
			}
		};
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				Bitmap Bitmap = loadImageFormUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Bitmap>(Bitmap));
				Message message = handler.obtainMessage(0, Bitmap);
				handler.sendMessage(message);
			}
		};
		threadPool.execute(task);
		return null;
	}
	
	
	private static Bitmap loadImageFormUrl(String imageUrl){
		URL url;
		InputStream is = null;
		Bitmap bitmap = null;
		try{
			url = new URL(imageUrl);
			is = (InputStream)url.getContent();
			bitmap = BitmapFactory.decodeStream(is);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			url = null;
		}
		return bitmap;
	}
	
	
	public interface ImageCallback{
		public void imageLoad(Bitmap Bitmap,String url);
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
