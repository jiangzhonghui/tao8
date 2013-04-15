package com.tao8.app.db.dao;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tao8.app.BuildConfig;
import com.tao8.app.db.Tao8DBHelper;
import com.tao8.app.domain.TaobaokeCouponItem;
import com.tao8.app.util.LogUtil;

public class TaoBaokeCouponDao {

	private Tao8DBHelper helper;

	public TaoBaokeCouponDao(Context context) {
		helper = new Tao8DBHelper(context);
	}

	public void insert(TaobaokeCouponItem taobaokeCouponItem) {
		SQLiteDatabase db = null;
		try {
			// 准备数据
			if (taobaokeCouponItem != null
					&& hasItem(taobaokeCouponItem.getNum_iid())) {
				if (BuildConfig.DEBUG) {
					System.out.println("有   return了   ");
				}
				return;
			}
			ContentValues values = new ContentValues();
			Field[] fields = taobaokeCouponItem.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				if (field.getName().equalsIgnoreCase("serialVersionUID")) {
					continue;
				}
				Object object = field.get(taobaokeCouponItem);
				values.put(field.getName(),
						object == null ? null : object.toString());
				// 通过ContentValue中的数据拼接sql语句,执行插入操作,id 为表中的一个列名
			}
			db = helper.getWritableDatabase();
			db.insert(Tao8DBHelper.DICTIONARY_TABLE_NAME, null, values);
		} catch (IllegalArgumentException e) {
			if (BuildConfig.DEBUG) {
				LogUtil.e("IllegalArgumentException",
						taobaokeCouponItem.getNum_iid());
			}
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			if (BuildConfig.DEBUG) {
				LogUtil.e("IllegalAccessException",
						taobaokeCouponItem.getNum_iid());
			}
			throw new RuntimeException(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}

	public void delete(String num_iid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		// 执行删除操作, 在person表中删除id为指定值的记录
		try {
			db.delete(Tao8DBHelper.DICTIONARY_TABLE_NAME, "num_iid=?",
					new String[] { num_iid });
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}

	public boolean hasItem(String num_iid) {
		/*
		 * SQLiteDatabase db = null; try { db = helper.getWritableDatabase();
		 * String sql =
		 * "if exists(select num_iid from "+helper.DICTIONARY_TABLE_NAME
		 * +" where num_iid = "+num_iid+")"; db.execSQL(sql); db.qu
		 * db.query(helper.DICTIONARY_TABLE_NAME, new String[]{"num_iid"},
		 * "num_iid=?", new String[]{num_iid}, null, null, null, null);
		 * }finally{
		 * 
		 * }
		 */
		return query(num_iid) != null;
	}

	public void update(TaobaokeCouponItem taobaokeCouponItem) {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			// 要更新的数据
			ContentValues values = new ContentValues();
			Field[] fields = taobaokeCouponItem.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				values.put(field.getName(), field.get(taobaokeCouponItem)
						.toString());
				// 更新person表中id为指定值的记录
			}
			db.update("person", values, "num_iid=?", new String[] { String
					.valueOf(taobaokeCouponItem.getNum_iid()) });
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}

	public TaobaokeCouponItem query(String num_iid) {
		SQLiteDatabase db = null;
		TaobaokeCouponItem taobaokeCouponItem = null;
		Cursor c = null;
		try {
			db = helper.getWritableDatabase();
			// 执行查询: 不去重复, 表是xx, 查询aa和bb两列, Where条件是"id=?", 占位符是id, 不分组,
			// 没有having, 不排序, 没有分页
			Field[] declaredFields = TaobaokeCouponItem.class
					.getDeclaredFields();
			String[] strings = new String[declaredFields.length - 1];
			int j = 0;
			for (int i = 0; i < declaredFields.length; i++) {
				if ("serialVersionUID".equalsIgnoreCase(declaredFields[i]
						.getName())) {
					continue;
				}
				strings[j] = declaredFields[i].getName();
				j++;
			}
			c = db.query(false, Tao8DBHelper.DICTIONARY_TABLE_NAME, strings,
					"num_iid = ?", new String[] { num_iid }, null, null, null,
					null);

			// 判断Cursor是否有下一条记录
			if (c.moveToNext()) {
				// 从Cursor中获取数据, 创建Person对象
				taobaokeCouponItem = new TaobaokeCouponItem();
				String[] columnNames = c.getColumnNames();
				for (String string : columnNames) {
					if (string.equalsIgnoreCase("serialVersionUID")) {
						continue;
					}
					Field declaredField = taobaokeCouponItem.getClass()
							.getDeclaredField(string);
					if (declaredField != null) {
						declaredField.setAccessible(true);
						declaredField.set(taobaokeCouponItem,
								c.getString(c.getColumnIndex(string)));
					}
				}
			}
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} finally {
			if (c != null) {
				c.close();
			}
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
		return taobaokeCouponItem;
	}

	public int queryCount() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = null;
		int count;
		try {
			// 查询记录条数
			c = db.query(false, Tao8DBHelper.DICTIONARY_TABLE_NAME,
					new String[] { "COUNT(*)" }, null, null, null, null, null,
					null);
			c.moveToNext();
			count = c.getInt(0);
		} finally {
			if (c != null) {
				c.close();
			}
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
		return count;
	}

	public ArrayList<TaobaokeCouponItem> queryAll() {
		SQLiteDatabase db = null;
		Cursor c = null;
		ArrayList<TaobaokeCouponItem> taobaokeCouponItems = null;
		try {
			db = helper.getReadableDatabase();
			Field[] declaredFields = TaobaokeCouponItem.class
					.getDeclaredFields();
			String[] strings = new String[declaredFields.length - 1];
			int j = 0;
			for (int i = 0; i < declaredFields.length; i++) {
				if ("serialVersionUID".equalsIgnoreCase(declaredFields[i]
						.getName())) {
					continue;
				}
				strings[j] = declaredFields[i].getName();
				j++;
			}
			for (String string : strings) {
				System.out.println(string);
			}
			// 翻页查询
			c = db.query(false, helper.DICTIONARY_TABLE_NAME, strings, null,
					null, null, null, null, null);
			taobaokeCouponItems = new ArrayList<TaobaokeCouponItem>();
			while (c.moveToNext()) {
				TaobaokeCouponItem taobaokeCouponItem = new TaobaokeCouponItem();
				String[] columnNames = c.getColumnNames();
				for (String string : columnNames) {
					if (string.equalsIgnoreCase("serialVersionUID")) {
						continue;
					}

					Field declaredField = taobaokeCouponItem.getClass()
							.getDeclaredField(string);
					if (declaredField != null) {
						declaredField.setAccessible(true);
						declaredField.set(taobaokeCouponItem,
								c.getString(c.getColumnIndex(string)));
					}
				}
				taobaokeCouponItems.add(taobaokeCouponItem);
			}
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} finally {
			if (c != null) {
				c.close();
			}
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
		return taobaokeCouponItems;
	}

	public ArrayList<TaobaokeCouponItem> queryPage(int pageNum, int capacity) {
		// 开始索引
		String start = String.valueOf((pageNum - 1) * capacity);
		// 查询的个数
		String length = String.valueOf(capacity);
		SQLiteDatabase db = null;
		Cursor c = null;
		ArrayList<TaobaokeCouponItem> taobaokeCouponItems = null;
		try {
			db = helper.getReadableDatabase();
			Field[] declaredFields = TaobaokeCouponItem.class
					.getDeclaredFields();
			String[] strings = new String[declaredFields.length - 1];
			int j = 0;
			for (int i = 0; i < declaredFields.length; i++) {
				if ("serialVersionUID".equalsIgnoreCase(declaredFields[i]
						.getName())) {
					continue;
				}
				strings[j] = declaredFields[i].getName();
				j++;
			}
			// 翻页查询
			c = db.query(false, helper.DICTIONARY_TABLE_NAME, strings, null,
					null, null, null, null, start + "," + length);

			taobaokeCouponItems = new ArrayList<TaobaokeCouponItem>();
			while (c.moveToNext()) {
				TaobaokeCouponItem taobaokeCouponItem = new TaobaokeCouponItem();
				String[] columnNames = c.getColumnNames();
				for (String string : columnNames) {
					if (string.equalsIgnoreCase("serialVersionUID")) {
						continue;
					}

					Field declaredField = taobaokeCouponItem.getClass()
							.getDeclaredField(string);
					if (declaredField != null) {
						declaredField.setAccessible(true);
						declaredField.set(taobaokeCouponItem,
								c.getString(c.getColumnIndex(string)));
					}
				}
				taobaokeCouponItems.add(taobaokeCouponItem);
			}
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} finally {
			if (c != null) {
				c.close();
			}
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
		return taobaokeCouponItems;
	}

	// You have to have an INTEGER PRIMARY KEY AUTOINCREMENT somewhere in
	// your schema or else the SQLITE_SEQUENCE table does not exist.
	public boolean delAll() {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			db.execSQL("delete from " + Tao8DBHelper.DICTIONARY_TABLE_NAME);
			// db.execSQL("select * from sqlite_sequence");
			// db.execSQL("update sqlite_sequence set seq=0 where name='"+
			// Tao8DBHelper.DICTIONARY_TABLE_NAME+"' ;");
		} catch (Exception e) {
			return false;
		} finally {
			db.close();
		}
		return true;
	}
}
