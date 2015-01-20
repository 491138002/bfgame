package com.bfgame.app.vo;


/**
 * 应用详情对象
 * @author admin
 *
 */
public class AppsInfo extends AppsInfoBase{
	/**
	 * 应用名称
	 */
	private String appsName;
	/**
	 * 应用包名
	 */
	private String appsPackage;
	/**
	 * 应用版本名称
	 */
	private String versionName;
	/**
	 * 版本码
	 */
	private int versionCode;
	/**
	 * 
	 */
	private String appsSignMD5;
	
	public String getAppsName() {
		return appsName;
	}
	public void setAppsName(String appsName) {
		this.appsName = appsName;
	}
	public String getAppsPackage() {
		return appsPackage;
	}
	public void setAppsPackage(String appsPackage) {
		this.appsPackage = appsPackage;
		super.setPackageName(appsPackage);
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
		super.setVersionCode(versionCode);
	}
	public String getAppsSignMD5() {
		return appsSignMD5;
	}
	public void setAppsSignMD5(String appsSignMD5) {
		this.appsSignMD5 = appsSignMD5;
	}
}
