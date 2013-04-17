package com.tao8.app.db;

import java.lang.reflect.Field;

import com.tao8.app.domain.TaobaokeCouponItem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tao8DBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;
	private static final String DB_NAME = "dictionary.db";
	public static final String DICTIONARY_TABLE_NAME = "dictionary";
	private static String DICTIONARY_TABLE_CREATE;/*
												 * "CREATE TABLE " +
												 * DICTIONARY_TABLE_NAME +
												 * " (_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
												 * + KEY_DEFINITION + " TEXT;
												 */

	public Tao8DBHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		DICTIONARY_TABLE_CREATE = generateTableSql(TaobaokeCouponItem.class);
	}
	
	private String generateTableSql(Class<TaobaokeCouponItem> clazz ) {
		StringBuilder sb = new StringBuilder();
		sb.append("create table IF NOT EXISTS ");
		sb.append(DICTIONARY_TABLE_NAME);
			sb.append("(num_iid integer primary key NOT NULL, ");
		Field[] fields = clazz.getFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equalsIgnoreCase("serialVersionUID")) {
				continue;
			}
			if (fields[i].getName().equalsIgnoreCase("num_iid")) {
				continue;
			}
			sb.append(fields[i].getName());
			sb.append(" varchar(400),");
			
		}
		sb.append("createTime timestamp default (datetime('now', 'localtime'))");
		sb.append(");");
		return sb.toString();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DICTIONARY_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + DICTIONARY_TABLE_NAME);
		db.execSQL(DICTIONARY_TABLE_CREATE);
	}

}
