package com.tao8.app.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.tao8.app.domain.SearchItem;

public class DBAsyncTask extends
		AsyncTask<String[], Void, List<SearchItem>> {
	private Context context;

	public DBAsyncTask(Context context) {
		this.context = context;
	}

	@Override
	protected List<SearchItem> doInBackground(String[]... params) {
		TaoBaokeCouponDao couponDao = new TaoBaokeCouponDao(context);
		ArrayList<SearchItem> taobaokeCouponItems = couponDao.queryAll();
		if (taobaokeCouponItems != null && taobaokeCouponItems.size() > 0) {
			return taobaokeCouponItems;
		}
		return null;
	}
}
