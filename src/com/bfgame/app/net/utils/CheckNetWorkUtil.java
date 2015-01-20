package com.bfgame.app.net.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.bfgame.app.util.DialogUtil;

/**
 * 检查网络状态
 * 
 * @author jzy
 * 
 */
public class CheckNetWorkUtil {

	public static boolean checkNetWork(Context context) {
		if(context != null){
			// 判断网络是否可用，如果不可用，给出提示
			boolean isAvailable = netWorkIsAvailable(context);
			if (!isAvailable) {// 如果不可用
				DialogUtil.showSettingWIFIDialog(context);
				return false;
			}
			return true;
		}else{
			return true;
		}
	}

	public static boolean netWorkIsAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null) {
			if (activeNetInfo.isAvailable()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static boolean gpsWorkIsAvailable(Context context) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * 获取当前网络状态
	 * 
	 * @return NetworkInfo
	 */
	public static NetworkInfo getCurrentNetStatus(Context ctx) {
		ConnectivityManager manager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return manager.getActiveNetworkInfo();
	}

	/**
	 * 返回当前网络状态
	 * 
	 * @param ctx
	 *            0无网络连接 1有WIFI连接 2有3G连接 3有2G链接 4有4G链接
	 * @return
	 */
	public static int getNetType(Context ctx) {
		NetworkInfo nki = getCurrentNetStatus(ctx);
		if (nki == null)
			return 0;
		if ("WIFI".equalsIgnoreCase(nki.getTypeName())) {
			return 1;
		} else {
			switch(nki.getSubtype()){
			case 13:
				//4G网络
				return 4;
			case TelephonyManager.NETWORK_TYPE_GPRS:
				//2G
				return 3;
			case TelephonyManager.NETWORK_TYPE_EDGE:
				//2G
				return 3;
			case TelephonyManager.NETWORK_TYPE_CDMA:
				//2G
				return 3;
			default:
				//3G
				return 2;
			}
		}
	}
}