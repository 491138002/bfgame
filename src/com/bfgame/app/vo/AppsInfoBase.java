package com.bfgame.app.vo;


/**
 * 基类应用详情对象
 * @author admin
 *
 */
public class AppsInfoBase {
	/**
	 * 应用包名
	 */
	private String packageName;
	/**
	 * 版本code
	 */
	private int versionCode;
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
}
