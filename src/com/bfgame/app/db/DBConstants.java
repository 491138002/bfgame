/**
 * 
 */
package com.bfgame.app.db;

/**
 * 数据库常量
 * @author jzy
 * @version 2012-11-2 上午11:59:36
 */

public class DBConstants {
	public DBConstants() {
	}

	// //数据库文件目标存放路径为系统默认位置
	protected static final String DB_NAME = "BFGame.db";
	protected static final String ASSET_NAME = "BFGame.zip";

	protected static final int DBVERSION = 2;		//数据库版本
	
	protected static final String CACHE_TABLE_NAME = "cache";
	protected static final String CREATE_CACHE_TABLE_SQL = ""
			+ "CREATE TABLE " + CACHE_TABLE_NAME + " ("
			+ "id INTEGER PRIMARY KEY NOT NULL, " + "key INTEGER, " + "create_time TEXT, "
			+ "content TEXT" + ")";
}
