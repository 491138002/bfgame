package com.bfgame.app.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Display;

import com.bfgame.app.common.Constants;

/**
* @ClassName: SystemInfoUtils
* @Description: TODO(获取系统信息的工具类)
* @author sailor
* @date 2012-10-21 上午10:41:20
* 
*/
public class SystemInfoUtils {

	/**
	 * @Title :getLoadingContentByCode
	 * @Description :通过code取加载内容
	 * @params @param loadingCode
	 * @params @return
	 * @return String
	 * 
	 */
	public static String getLoadingContentByCode(String loadingCode){
		String loadingContent = Constants.LOADING_CONTENTS;
		if(Constants.LOADING_CONTENT_MAP.containsKey(loadingCode)){
			loadingContent = Constants.LOADING_CONTENT_MAP.get(loadingCode);
		}
		return loadingContent;
	}
	/**
	* 获取当前的网络状态 -1：没有网络
	* 1：WIFI网络2：wap网络3：net网络
	* @param context
	* @return
	*/
	public static String getNetWorkType(Context context) {
		String netType = "unknown";
		try{
			ConnectivityManager connMgr = (ConnectivityManager) context
			.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if(networkInfo!=null){
				int nType = networkInfo.getType();
				if (nType == ConnectivityManager.TYPE_MOBILE) {
					netType =  networkInfo.getExtraInfo().toLowerCase();
				} else if (nType == ConnectivityManager.TYPE_WIFI) {
					netType =  "wifi";
				}
			}
		}catch (Exception e) {
		}
		return netType;
	}
	

	/**
	 * @Title :getScreenSize
	 * @Description :获取屏幕尺寸
	 * @params @param context
	 * @params @return 
	 * @return String  高_宽
	 * 
	 */
	public static String getScreenSize(Context context){
		String defaultSize = "800_480";
		try{
			Display d = ((Activity)context).getWindowManager().getDefaultDisplay();
//			DisplayMetrics dm = new DisplayMetrics();
//			d.getMetrics(dm);
			defaultSize = d.getHeight()+"_"+d.getWidth();

		}catch (Exception e) {
			// TODO: handle exception
		}
		return defaultSize;
	}
}
