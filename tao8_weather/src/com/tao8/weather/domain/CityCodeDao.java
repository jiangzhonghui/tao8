package com.tao8.weather.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;


public class CityCodeDao {
	private  DbHelper dbHelper;
	private Context context;
	public CityCodeDao(Context context) {
		dbHelper = new DbHelper(context);
		this.context = context;
	}
	public void addAllCityCode() throws IOException{
		this.delAll();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		InputStream in =context.getAssets().open("code.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = "";
		while ((line = reader.readLine())!=null) {
			if (!TextUtils.isEmpty(line)) {
				String[] split = line.split("=");
				ContentValues contentValues = new ContentValues();
				if (split.length==2) {
					contentValues.put("cityName", split[1]);
					contentValues.put("cityCode", split[0]);
					db.insert(DbHelper.CITY_CODE_TABLE, null, contentValues);
				}
			}
		}
		in.close();
		db.close();
	}
	public CityCode getCityCodeByCityName(String cityName){
		CityCode cityCode = null;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		//Cursor cursor = db.query(DbHelper.CITY_CODE_TABLE, new String[]{"cityName","cityCode"}, "cityName like '%?%'", new String[]{cityName}, null, null, null);
		Cursor cursor = db.query(true, DbHelper.CITY_CODE_TABLE, new String[]{"cityName","cityCode"}, "cityName like ?", new String[]{"%"+cityName+"%"}, null, null, null, null);
		if (cursor!=null) {
			if (cursor.moveToFirst()) {
				cityCode = new CityCode();
				cityCode.cityCode = cursor.getString(cursor.getColumnIndex("cityCode"));
				cityCode.cityName = cursor.getString(cursor.getColumnIndex("cityName"));
			}
		}
		cursor.close();
		db.close();
		return cityCode;
		
	}
	
	public boolean delAll() {
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL("delete from " + DbHelper.CITY_CODE_TABLE);
		} catch (Exception e) {
			return false;
		} finally {
			db.close();
		}
		return true;
	}
}
