package com.emer.egou.app.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoader {

	 private HashMap<String, SoftReference<Drawable>> imageCache;
	  
	     public AsyncImageLoader() {
	    	 imageCache = new HashMap<String, SoftReference<Drawable>>();
	     }
	  
	     public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
	         if (imageCache.containsKey(imageUrl)) {
	             SoftReference<Drawable> softReference = imageCache.get(imageUrl);
	             Drawable drawable = softReference.get();
	             if (drawable != null) {
	                 return drawable;
	             }
	         }
	         final Handler handler = new Handler() {
	             public void handleMessage(Message message) {
	                 imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
	             }
	         };
	         
	         
	         
	         
	         new Thread() {
	             @Override
	             public void run() {
	                 Drawable drawable = loadImageFromUrl(imageUrl);
	                 imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
	                 Message message = handler.obtainMessage(0, drawable);
	                 handler.sendMessage(message);
	             }
	         }.start();
	         
	         /*CanvasImageTaskCall canvasImageTaskCall = new CanvasImageTaskCall();
	         canvasImageTaskCall.execute(imageUrl,imageCache,handler);*/
	         return null;
	     }
	  
		public static Drawable loadImageFromUrl(String url) {
			URL m;
			InputStream i = null;
			try {
				m = new URL(url);
				i = (InputStream) m.getContent();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Drawable d = Drawable.createFromStream(i, "src");
			return d;
		}
	  
	     public interface ImageCallback {
	         public void imageLoaded(Drawable imageDrawable, String imageUrl);
	     }

	     
	     class CanvasImageTaskCall extends AsyncTask<Object, Void, Drawable> /*implements Callback*/{
				private URL url;
				HashMap<String, SoftReference<Drawable>>  imageCache;
				Handler handler;
				String imageUrl;
				/**
				 * 
				 * @param objs uri,HashMap<String, SoftReference<Drawable>>,handler
				 * @return
				 */
			    @SuppressWarnings("unchecked")
				protected Drawable doInBackground(Object... objs) {
			    	Drawable drawable = null ;
			    	imageUrl = (String) objs[0];
			            imageCache = (HashMap<String, SoftReference<Drawable>>) objs[1];
			            handler = (Handler) objs[2];
			            
			            // 根据iconUrl获取图片并渲染，iconUrl的url放在了view的tag中。
			            if (imageUrl != null) {
			                    try {
			                       url = new URL(imageUrl);
			                       HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			                       conn.setConnectTimeout(30000);
			           				conn.setReadTimeout(30000);
			           				conn.setInstanceFollowRedirects(true);
			                       conn.setDoInput(true);
			                       conn.connect();
			                       InputStream stream = conn.getInputStream();
			                       drawable = Drawable.createFromStream(stream, "src");
			                       stream.close();
			                    } catch (Exception e) {
			                            e.printStackTrace();
			                           /* Log.v("img", e.getMessage());
			                            Message msg = new Message();
			                            msg.what = 0;
			                            handleMessage(msg);*/
			                            return null;
			                    }
			            }
			            return drawable;
			    }
			    protected void onPostExecute(Drawable drawable) {
			            if (drawable != null) {
			            	imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
			                 Message message = handler.obtainMessage(0, drawable);
			                 handler.sendMessage(message);
			               /* Message msg = new Message();
			                msg.what = 1;
			                msg.obj = bm;
			                handleMessage(msg);*/
			            }
			    }
			    public boolean handleMessage(Message msg) {
			        return false;
			    }
			    
			}
}
