package com.bfgame.app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bfgame.app.util.StringUtil;

/**
 * 缓存数据库处理类
 * @author jzy
 * 
 */
public class CacheDB extends HandlerDB {

	/**
	 * 获取缓存
	 * 
	 * @param cx
	 * @param pid
	 * @param type
	 * @return
	 */
	public List<Cache> getCache(Context cx, String key) {
		List<Cache> cahceList = new ArrayList<Cache>();
		try {
			getDBInstance(cx).beginTransaction();
			String sql = "select * from " + CACHE_TABLE_NAME + " where 1=1";
			if(StringUtil.isNotEmpty(key)){
				sql += " and key='" + key + "'";
			}
			Cursor c = db.rawQuery(sql, null);
			// 处理信息
			if (c != null) {
				while (c.moveToNext()) {

					Cache cache = new Cache();
					cache.setId(c.getInt(0));
					cache.setKey(c.getString(1));
					cache.setCreate_time(c.getLong(2));
					cache.setContent(c.getString(3));
					cahceList.add(cache);
				}
			}
			c.close();
			db.endTransaction();
			SQLiteDatabase.releaseMemory();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cahceList;
	}
	
	/**
	 * 添加缓存
	 * @param cx
	 * @param key
	 * @param content
	 */
	public boolean addCache(Context cx, String key, String content, long time) {
		boolean result = false;
		try {
			getDBInstance(cx);
			db.beginTransaction();
			ContentValues cv = new ContentValues();
			cv.put("key", key);
			cv.put("content", content);
			cv.put("create_time", time);
			db.insert(CACHE_TABLE_NAME, null, cv);
			
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 删除缓存
	 * @param cx
	 * @param pid
	 * @return
	 */
	public boolean delCache(Context cx, String key) {
		boolean result = false;
		try {
			
			String sql = "DELETE FROM " + CACHE_TABLE_NAME + " WHERE 1=1";
			if(StringUtil.isNotEmpty(key)){
				sql += " and key='" + key + "'";
			}
			getDBInstance(cx);
			db.beginTransaction();
			db.execSQL(sql);
			db.setTransactionSuccessful();
			db.endTransaction();
			SQLiteDatabase.releaseMemory();
			db.close();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean clearCache(Context cx){
		try{
			String sql = "DELETE FROM " + CACHE_TABLE_NAME;
			getDBInstance(cx);
			db.beginTransaction();
			db.execSQL(sql);
			db.setTransactionSuccessful();
			db.endTransaction();
			SQLiteDatabase.releaseMemory();
			db.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void onDestroy(){
	}
}
