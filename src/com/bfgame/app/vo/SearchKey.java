package com.bfgame.app.vo;

import java.util.List;


/**
 * 搜索关键字
 * @author admin
 *
 */
public class SearchKey extends BaseVO{

	/**
	 * 推荐关键字列表
	 */
	private List<SearchKey> feature;
	/**
	 * 应用包名
	 */
	private String packageName;
	/**
	 * 点击动作
	 */
	private String action;
	/**
	 * 应用图标
	 */
	private String icon;
	/**
	 * 应用名称
	 */
	private String name;
	/**
	 * 版本code
	 */
	private String versionCode;
	/**
	 * 版本名称
	 */
	private String versionName;
	/**
	 * 1为有礼包，0为没有礼包
	 */
	private int gifts;
	/**
	 * 1为新服，0为非新服
	 */
	private int isNew;
	/**
	 * App文件大小
	 */
	private String size;
	/**
	 * 分类名称
	 */
	private String categoryName;
	/**
	 * 下载量
	 */
	private String downloadCount;
	
	/**
	 * 游戏详情uri
	 */
	private String uri;
	/**
	 * 搜索关键字列表
	 */
	private List<SearchKey> keys;
	/**
	 * 关键词id
	 */
	private int keyId;
	/**
	 * 关键词名称
	 */
	private String keyName;
	/**
	 * 关键词颜色
	 */
	private String keyColor;
	/**
	 * 简短简介
	 */
	private String smallDes;
	
	public List<SearchKey> getFeature() {
		return feature;
	}
	public void setFeature(List<SearchKey> feature) {
		this.feature = feature;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public int getGifts() {
		return gifts;
	}
	public void setGifts(int gifts) {
		this.gifts = gifts;
	}
	public int getIsNew() {
		return isNew;
	}
	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(String downloadCount) {
		this.downloadCount = downloadCount;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public List<SearchKey> getKeys() {
		return keys;
	}
	public void setKeys(List<SearchKey> keys) {
		this.keys = keys;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getKeyColor() {
		return keyColor;
	}
	public void setKeyColor(String keyColor) {
		this.keyColor = keyColor;
	}
	public String getSmallDes() {
		return smallDes;
	}
	public void setSmallDes(String smallDes) {
		this.smallDes = smallDes;
	}
	public int getKeyId() {
		return keyId;
	}
	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}
}
