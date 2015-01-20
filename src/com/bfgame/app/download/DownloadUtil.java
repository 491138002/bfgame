package com.bfgame.app.download;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Button;

import com.bfgame.app.R;
import com.bfgame.app.net.utils.CheckNetWorkUtil;
import com.bfgame.app.util.ApkInfo;
import com.bfgame.app.util.DialogUtil;
import com.bfgame.app.util.StatisticsUtil;
import com.bfgame.app.util.DialogUtil.OnAlertSelectId;
import com.bfgame.app.util.LogUtil;
import com.bfgame.app.util.preference.PreferencesUtils;
/**
 * 下载工具类
 * @author admin
 *
 */
public class DownloadUtil {

	/**
	 * 下载状态
	 */
	public static final int TASK_STATE_NO_BEGIN = 0;						// 还未开始下载
	public static final int TASK_STATE_PAUSE = 1;							// 确定暂停
	public static final int TASK_STATE_DOWNLOADING = 2;						// 正在下载中
	public static final int TASK_STATE_COMPLETE = 3;						// 完成
	public static final int TASK_STATE_WAITING = 4;							// 等待下载
	public static final int TASK_STATE_FAIL = 5;							// 下载失败
	
	/**
	 * 广播用到标签
	 * 
	 */
	public static final String TASK_TAG_APP_ID = "appId";					//应用ID
	public static final String TASK_TAG_APP_ICON = "appIcon";				//应用图标地址
	public static final String TASK_TAG_APP_NAME = "appName"; 				//应用名称
	public static final String TASK_TAG_APP_DOWNLOAD_URL = "downloadUrl"; 	//应用下载地址
	public static final String TASK_TAG_APP_PACKAGE_NAME = "packageName"; 	//应用包名
	public static final String TASK_TAG_APP_VERSION_NAME = "appVersionName"; 	//应用版本名
	public static final String TASK_TAG_APP_VERSION_CODE = "appVersionCode"; 	//应用版本号
	public static final String TASK_TAG_APP_DOWNLOAD_NUM = "appDownloadNum"; 	//应用下载数描述
	public static final String TASK_TAG_APP_SIZE = "appSize"; 	//应用大小
	public static final String TASK_TAG_DOWNLOAD_STATE = "downloadState";	//下载状态
	
	
	/**
	 *  下载map，用于记录已经下载的应用
	 *  key ID
	 *  value 状态
	 */
	public static final Map<Integer, Integer> downloadMap = new HashMap<Integer, Integer>();
	
	/**
	 * 新增下载广播Action
	 */
	public static final String NEW_TASK_ACTION = "com.storm.smart.bfgame.newtask";
	/**
	 * 下载状态更新广播Action
	 */
	public static final String DOWNLOAD_STATE_UPDATE_ACTION = "com.storm.smart.bfgame.download";
	/**
	 * 下载更新小红点状态广播Action
	 */
	public static final String DOWNLOAD_UPDATE_RED_DOT_ACTION = "com.storm.smart.bfgame.download.dot";
	
	/**
	 * 获取下载状态文本
	 * @param context
	 * @param gameInfo
	 * @param state
	 * @return
	 */
	public static String getDownloadText(Context context, int appId, String packageName, int versionCode, String appName){
		String text = context.getString(R.string.bfgame_dwonload_text_start);
		
		//版本是否相同，如果相同显示打开
		int installState = ApkInfo.checkInstallApps(context, packageName, versionCode);
		if(installState == 2 || installState == 3){
			text = context.getString(R.string.bfgame_dwonload_text_open);
		}else{
			//判断是否有下载文件，如果有下载文件显示安装
			String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/baofeng/download/"+appName+".apk";
			File file = new File(apkPath);
			if(file.exists()){
				text = context.getString(R.string.bfgame_dwonload_text_install);
			}else{
				Integer state = downloadMap.get(appId);
				state = state == null ? -1 : state;
				switch(state){
				case TASK_STATE_NO_BEGIN:
					text = context.getString(R.string.bfgame_dwonload_text_start);
					break;
				case TASK_STATE_PAUSE:
					text = context.getString(R.string.bfgame_dwonload_text);
					break;
				case TASK_STATE_DOWNLOADING:
					text = context.getString(R.string.bfgame_dwonload_text_downloading);
					break;
				case TASK_STATE_WAITING:
					text = context.getString(R.string.bfgame_dwonload_text_downloading);
					break;
				case TASK_STATE_FAIL:
					text = context.getString(R.string.bfgame_dwonload_text_start);
					break;
				default:
					text = context.getString(R.string.bfgame_dwonload_text_start);
					break;
				}
			}
		}
		
		return text;
	}
	/**
	 * 获取下载状态背景
	 * @param context
	 * @param gameInfo
	 * @param state
	 * @return
	 */
	public static String getTextBg(Context context, int appId, String packageName, int versionCode, String appName){
		String color = context.getString(R.string.bfgame_dwonload_text_start);
		int installState = ApkInfo.checkInstallApps(context, packageName, versionCode);
		if(installState == 2 || installState == 3){
			color = context.getString(R.color.green_color);
		}else{
			String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/baofeng/download/"+appName+".apk";
			File file = new File(apkPath);
			if(file.exists()){
				color = context.getString(R.color.green_color);
			}else{
				Integer state = downloadMap.get(appId);
				state = state == null ? -1 : state;
				switch(state){
				case TASK_STATE_NO_BEGIN:
					color = context.getString(R.color.blue_color);
					break;
				case TASK_STATE_PAUSE:
					color = context.getString(R.color.text_gray);
					break;
				case TASK_STATE_DOWNLOADING:
					color = context.getString(R.color.text_orange);
					break;
				case TASK_STATE_WAITING:
					color = context.getString(R.color.blue_color);
					break;
				case TASK_STATE_FAIL:
					color = context.getString(R.color.blue_color);
					break;
				default:
					color = context.getString(R.color.blue_color);
					break;
				}
			}
		}
		
		return color;
	}
	
	/**
	 * 点击下载按钮
	 * @param context
	 * @param gameInfo
	 */
	public static void downloadClick(final Context context, final PreferencesUtils p, final int id,final int spid,final int cid, final String name, 
			final String downloadUrl, final String packageName, final String icon, final int versionCode, final String versionName,final String downloadNum, final String size, final Button button){
//		StatisticsUtil.addDownloadApp(p, id, StatisticsUtil.APP_DOWNLOAD_STATE_END);
		
		LogUtil.d("点击下载按钮", "==================");
		LogUtil.d(TASK_TAG_APP_ID, String.valueOf(id));
		LogUtil.d(TASK_TAG_APP_ICON, icon);
		LogUtil.d(TASK_TAG_APP_NAME, name);
		LogUtil.d(TASK_TAG_APP_DOWNLOAD_URL, downloadUrl);
		LogUtil.d(TASK_TAG_APP_PACKAGE_NAME, packageName);
		LogUtil.d(TASK_TAG_APP_SIZE, size);
		
		Integer state = downloadMap.get(id);
		int installState = ApkInfo.checkInstallApps(context, packageName, versionCode);
		
		String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/baofeng/download/"+name+".apk";
		File file = new File(apkPath);
		if(!file.exists())
			state = TASK_STATE_FAIL;
	
		if(installState == 2 || installState == 3){
			ApkInfo.openApp(context, packageName);
			StatisticsUtil.addEnterApp(p, id, spid, cid);
		}else if((state != null && state == TASK_STATE_COMPLETE) || file.exists()){
			StatisticsUtil.addInstallApp(p, id,spid,cid, StatisticsUtil.APP_INSTALL_STATE_SUCCESS);
			ApkInfo.installApk(context, apkPath);
		}else{
			if(CheckNetWorkUtil.getNetType(context) != 1){
				DialogUtil.show3GDwonlaodialog(context, null, CheckNetWorkUtil.getNetType(context), new OnAlertSelectId() {
					public void onClick(int whichButton, Object o) {
						
					}
				});
			}else{
				if(downloadMap == null || downloadMap.get(id) == null || TASK_STATE_DOWNLOADING != downloadMap.get(id)){
					StatisticsUtil.addDownloadApp(p, id,spid,cid, StatisticsUtil.APP_DOWNLOAD_STATE_START);
					Intent intent = new Intent();
//					intent.setAction(NEW_TASK_ACTION);
					intent.setAction("downloading");
					intent.putExtra(TASK_TAG_APP_ID, id);
					intent.putExtra(TASK_TAG_APP_ICON, icon);
					intent.putExtra(TASK_TAG_APP_NAME, name);
					intent.putExtra(TASK_TAG_APP_DOWNLOAD_URL, downloadUrl);
					intent.putExtra(TASK_TAG_APP_PACKAGE_NAME, packageName);
					intent.putExtra(TASK_TAG_APP_VERSION_NAME, versionName);
					intent.putExtra(TASK_TAG_APP_VERSION_CODE, versionCode);
					intent.putExtra(TASK_TAG_APP_DOWNLOAD_NUM, downloadNum);
					intent.putExtra(TASK_TAG_APP_SIZE, size);
					context.sendBroadcast(intent);
					
				}else{
					LogUtil.d("========", "正在下载");
				}
			}
		}
	}
}
