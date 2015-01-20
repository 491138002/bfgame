package com.bfgame.app.db;


/**
 * 缓存对象
 * @author admin
 *
 */
public class Cache {
	/**
	 * 缓存id
	 */
	private int id;  
	/**
	 * 缓存key
	 */
	private String key;
	/**
	 * 缓存创建时间
	 */
	private long create_time;
	/**
	 * 缓存内容
	 */
	private String content;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
