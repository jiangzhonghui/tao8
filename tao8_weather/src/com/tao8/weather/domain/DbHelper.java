package com.tao8.weather.domain;

import java.lang.reflect.Field;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
	private static final int version = 1;
	private static final String DB_NAME = "city_code.db";
	public static final String CITY_CODE_TABLE = "city_code";
	private String dictionary_table_create;
	public DbHelper(Context context) {
		super(context, DB_NAME, null, version);
		dictionary_table_create = generateTableSql(CityCode.class);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(dictionary_table_create);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + CITY_CODE_TABLE);
		db.execSQL(dictionary_table_create);
	}
	
	private String generateTableSql(Class clazz ) {
		StringBuilder sb = new StringBuilder();
		sb.append("create table IF NOT EXISTS ");
		sb.append(CITY_CODE_TABLE);
			sb.append("(_id integer primary key NOT NULL, ");
		Field[] fields = clazz.getFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equalsIgnoreCase("serialVersionUID")) {
				continue;
			}
			sb.append(fields[i].getName());
			sb.append(" varchar(30),");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
 		sb.append(");");
		return sb.toString();
	}

}
