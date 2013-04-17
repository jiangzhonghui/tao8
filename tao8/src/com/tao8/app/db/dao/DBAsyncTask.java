package com.tao8.app.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.tao8.app.domain.TaobaokeCouponItem;

import android.content.Context;
import android.os.AsyncTask;

public class DBAsyncTask extends
		AsyncTask<String[], Void, List<TaobaokeCouponItem>> {
	private Context context;

	public DBAsyncTask(Context context) {
		this.context = context;
	}

	@Override
	protected List<TaobaokeCouponItem> doInBackground(String[]... params) {
		TaoBaokeCouponDao couponDao = new TaoBaokeCouponDao(context);
		ArrayList<TaobaokeCouponItem> taobaokeCouponItems = couponDao.queryAll();
		if (taobaokeCouponItems != null && taobaokeCouponItems.size() > 0) {
			return taobaokeCouponItems;
		}
		return null;
	}
}
