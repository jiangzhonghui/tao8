package com.tao8.app.cache.util;

import android.content.Context;

import com.tao8.app.util.FileManager;

public class FileCache extends AbstractFileCache{

	public FileCache(Context context) {
		super(context);
	
	}
	/**
	 * 用url的hash值作为文件名字
	 */
	@Override
	public String getSavePath(String url) {
		String filename = String.valueOf(url.hashCode());
		return getCacheDir() + filename;
	}

	@Override
	public String getCacheDir() {
		return FileManager.getSaveFilePath();
	}

}
