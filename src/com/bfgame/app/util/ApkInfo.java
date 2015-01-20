package com.bfgame.app.util;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;

import com.bfgame.app.vo.AppsInfo;
/**
 * 获取apk相关信息类
 * @author Evil
 *
 */
public class ApkInfo {
	
//	/**
//	 * 用于能管理中的 已安装、我的应用、我的下载
//	 * @param ctx
//	 * @return
//	 */
	public static HashMap<String,HashMap<String,Object>> getInstalledAppInfoForManager(Context ctx){
		HashMap<String,HashMap<String,Object>> map = new HashMap<String,HashMap<String,Object>>();
		List<PackageInfo> packs = ctx.getPackageManager().getInstalledPackages(0);     
		for(PackageInfo p: packs) {
			//只要第三方软件列表
			/*
			 * 剔除内置软件
			 */
		    if((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) !=0)
		    	continue;
		    
		    HashMap<String,Object> av = new HashMap<String,Object>(); 
			av.put("installed", "true");//是否安装
			av.put("appName",  p.applicationInfo.loadLabel(ctx.getPackageManager())==null?"":p.applicationInfo.loadLabel(ctx.getPackageManager()).toString());// 软件名称
			av.put("packagePath", p.packageName==null?"":p.packageName);// 包路径
			av.put("versionName", p.versionName==null?"":p.versionName);// 版本名称
			av.put("versionCode", p.versionCode+"");// 版本号
			av.put("icon", p.applicationInfo.loadIcon(ctx.getPackageManager()));// 软件图标
		    
		    map.put(p.packageName, av);
		}     
		
		packs = null;
		
		return map;
	}
//	
	public static PackageInfo getPkgInfoByPKG(Context ctx,String pkg){
		PackageInfo pinfo = null;
		if(pkg!=null&&!"".equals(pkg)){
			try {
				pinfo = ctx.getPackageManager().getPackageInfo(pkg, 0);
			} catch (NameNotFoundException e) {
				//Do nothing.
			}
		}
		return pinfo;
	}
	/**
	 * 获取第三方软件列表
	 * @param ctx
	 * @return
	 */
	public static HashMap<String,AppsInfo> getInstalledAppInfo(Context ctx){
		return getInstalledAppInfoByCondition(ctx,0);
	}
	/**
	 * 获取系统内置软件列表
	 * @param ctx
	 * @return
	 */
	public static HashMap<String,AppsInfo> getInstalledSysAppInfo(Context ctx){
		return getInstalledAppInfoByCondition(ctx,1);
	}
	/**
	 * 获取系统所有软件列表
	 * @param ctx
	 * @return
	 */
	public static HashMap<String,AppsInfo> getAllAppInfo(Context ctx){
		return getInstalledAppInfoByCondition(ctx,-1);
	}

	/**
	 * 获取已安装apk信息
	 * @param ctx
	 * @param sysOrThird 1:系统  0：第三方
	 * @return
	 */
	private static HashMap<String, AppsInfo> getInstalledAppInfoByCondition(
			Context ctx, int sysOrThird) {
		HashMap<String,AppsInfo> map = new HashMap<String,AppsInfo>();
		//当sysOrThirds==-1时，表示获取所有apkInfo（包括已卸载但未删除用户信息的apk）
		List<PackageInfo> packs = ctx.getPackageManager()
			.getInstalledPackages(sysOrThird==-1?PackageManager.GET_UNINSTALLED_PACKAGES:0 );  
		for(PackageInfo p: packs) {
			if(sysOrThird==1){//只要内置软件列表
				/*
				 * 剔除第三方软件
				 */
			    if((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) ==0){
			    	continue;
			    }
			}else if(sysOrThird==0){//只要第三方软件列表
				/*
				 * 剔除内置软件
				 */
			    if((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) !=0){
			    	continue;
			    }
			}
		    
			AppsInfo av = new AppsInfo(); 
		    av.setAppsName( p.applicationInfo.loadLabel(ctx.getPackageManager()).toString());// 软件名称
		    av.setAppsPackage(p.packageName);// 包路径
		    av.setVersionName(p.versionName);// 版本名称
		    av.setVersionCode(p.versionCode);// 版本号
		    android.content.pm.Signature[] sig1 = p.signatures;
			if(sig1 != null && sig1.length > 0){
				byte[] data = Utils.hexToBytes(sig1[0].toCharsString());
	     		String encodeData = new String(Base64Util.encode(data));
	     		String md5 = Md5Encode.getMD5(encodeData);
	     		av.setAppsSignMD5(md5);
			}
		    
		    map.put(av.getAppsPackage(), av);
		}     
		
		packs = null;
		
		return map;
	}
	
	public static BitmapDrawable getDownloadAppsIcon(Context ctx,String appPath){
		File apkFile = new File(appPath);
		if (!apkFile.exists() || !appPath.toLowerCase().endsWith(".apk")) {
			return null;
		}
//		AppInfoData appInfoData;
		String PATH_PackageParser = "android.content.pm.PackageParser";
		String PATH_AssetManager = "android.content.res.AssetManager";
		try {
			// 反射得到pkgParserCls对象并实例化,有参数
			Class<?> pkgParserCls = Class.forName(PATH_PackageParser);
			Class<?>[] typeArgs = { String.class };
			Constructor<?> pkgParserCt = pkgParserCls.getConstructor(typeArgs);
			Object[] valueArgs = { appPath };
			Object pkgParser = pkgParserCt.newInstance(valueArgs);

			// 从pkgParserCls类得到parsePackage方法
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();// 这个是与显示有关的, 这边使用默认
			typeArgs = new Class<?>[] { File.class, String.class,
					DisplayMetrics.class, int.class };
			Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(
					"parsePackage", typeArgs);

			valueArgs = new Object[] { new File(appPath), appPath, metrics, 0 };

			// 执行pkgParser_parsePackageMtd方法并返回
			Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
					valueArgs);

			// 从返回的对象得到名为"applicationInfo"的字段对象
			if (pkgParserPkg == null) {
				return null;
			}
			Field appInfoFld = pkgParserPkg.getClass().getDeclaredField(
					"applicationInfo");

			// 从对象"pkgParserPkg"得到字段"appInfoFld"的值
			if (appInfoFld.get(pkgParserPkg) == null) {
				return null;
			}
			ApplicationInfo info = (ApplicationInfo) appInfoFld
					.get(pkgParserPkg);

			// 反射得到assetMagCls对象并实例化,无参
			Class<?> assetMagCls = Class.forName(PATH_AssetManager);
			Object assetMag = assetMagCls.newInstance();
			// 从assetMagCls类得到addAssetPath方法
			typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod(
					"addAssetPath", typeArgs);
			valueArgs = new Object[1];
			valueArgs[0] = appPath;
			// 执行assetMag_addAssetPathMtd方法
			assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);

			// 得到Resources对象并实例化,有参数
			Resources res = ctx.getResources();
			typeArgs = new Class[3];
			typeArgs[0] = assetMag.getClass();
			typeArgs[1] = res.getDisplayMetrics().getClass();
			typeArgs[2] = res.getConfiguration().getClass();
			Constructor<Resources> resCt = Resources.class
					.getConstructor(typeArgs);
			valueArgs = new Object[3];
			valueArgs[0] = assetMag;
			valueArgs[1] = res.getDisplayMetrics();
			valueArgs[2] = res.getConfiguration();
			res = (Resources) resCt.newInstance(valueArgs);

			// 读取apk文件的信息
//			appInfoData = new AppInfoData();
//			if (info != null) {
//				if (info.icon != 0) {// 图片存在，则读取相关信息
//					Drawable icon = res.getDrawable(info.icon);// 图标
//					appInfoData.setAppicon(icon);
//				}
//				if (info.labelRes != 0) {
//					String neme = (String) res.getText(info.labelRes);// 名字
//					appInfoData.setAppname(neme);
//				} else {
//					String apkName = apkFile.getName();
//					appInfoData.setAppname(apkName.substring(0,
//							apkName.lastIndexOf(".")));
//				}
//				String pkgName = info.packageName;// 包名
//				appInfoData.setApppackage(pkgName);
//			} else {
//				return null;
//			}
//			PackageManager pm = ctx.getPackageManager();
//			PackageInfo packageInfo = pm.getPackageArchiveInfo(appPath,
//					PackageManager.GET_ACTIVITIES);
//			if (packageInfo != null) {
//				appInfoData.setAppversion(packageInfo.versionName);// 版本号
//				appInfoData.setAppversionCode(packageInfo.versionCode + "");// 版本码
//			}
			if (info.icon != 0) {// 图片存在，则读取相关信息
				BitmapDrawable icon = (BitmapDrawable)res.getDrawable(info.icon);// 图标
				return icon;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public static String getDownloadAppName(Context ctx, String appPath) {
		File apkFile = new File(appPath);
		if (!apkFile.exists() || !appPath.toLowerCase().endsWith(".apk")) {
			return null;
		}

		String PATH_PackageParser = "android.content.pm.PackageParser";
		String PATH_AssetManager = "android.content.res.AssetManager";
		try {
			// 反射得到pkgParserCls对象并实例化,有参数
			Class<?> pkgParserCls = Class.forName(PATH_PackageParser);
			Class<?>[] typeArgs = { String.class };
			Constructor<?> pkgParserCt = pkgParserCls.getConstructor(typeArgs);
			Object[] valueArgs = { appPath };
			Object pkgParser = pkgParserCt.newInstance(valueArgs);

			// 从pkgParserCls类得到parsePackage方法
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();// 这个是与显示有关的, 这边使用默认
			typeArgs = new Class<?>[] { File.class, String.class,
					DisplayMetrics.class, int.class };
			Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(
					"parsePackage", typeArgs);

			valueArgs = new Object[] { new File(appPath), appPath, metrics, 0 };

			// 执行pkgParser_parsePackageMtd方法并返回
			Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
					valueArgs);

			// 从返回的对象得到名为"applicationInfo"的字段对象
			if (pkgParserPkg == null) {
				return null;
			}
			Field appInfoFld = pkgParserPkg.getClass().getDeclaredField(
					"applicationInfo");

			// 从对象"pkgParserPkg"得到字段"appInfoFld"的值
			if (appInfoFld.get(pkgParserPkg) == null) {
				return null;
			}
			ApplicationInfo info = (ApplicationInfo) appInfoFld
					.get(pkgParserPkg);

			// 反射得到assetMagCls对象并实例化,无参
			Class<?> assetMagCls = Class.forName(PATH_AssetManager);
			Object assetMag = assetMagCls.newInstance();
			// 从assetMagCls类得到addAssetPath方法
			typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod(
					"addAssetPath", typeArgs);
			valueArgs = new Object[1];
			valueArgs[0] = appPath;
			// 执行assetMag_addAssetPathMtd方法
			assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);

			// 得到Resources对象并实例化,有参数
			Resources res = ctx.getResources();
			typeArgs = new Class[3];
			typeArgs[0] = assetMag.getClass();
			typeArgs[1] = res.getDisplayMetrics().getClass();
			typeArgs[2] = res.getConfiguration().getClass();
			Constructor<Resources> resCt = Resources.class
					.getConstructor(typeArgs);
			valueArgs = new Object[3];
			valueArgs[0] = assetMag;
			valueArgs[1] = res.getDisplayMetrics();
			valueArgs[2] = res.getConfiguration();
			res = (Resources) resCt.newInstance(valueArgs);

			if (info.labelRes != 0) {
				String name = (String) res.getText(info.labelRes);// 名字
				return name;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	/**
	 * 卸载APK
	 * 
	 * @param f
	 */
	public static void unInstallApk(Context ctx,String pkg) {
		// 调用卸载软件
		try {
			Uri uri = Uri.fromParts("package", pkg, null);
			Intent it = new Intent(Intent.ACTION_DELETE, uri);
			//ctx.startActivity(it);
			((android.app.Activity) ctx).startActivityForResult(it, 1);//这样会自动关闭卸载窗口！：）
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void installApk(Context context, String filePath) {
		if(StringUtil.isNotEmpty(filePath)){
			Uri fileUri = Uri.fromFile(new File(filePath));
			Intent it = new Intent(Intent.ACTION_VIEW);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.setDataAndType(fileUri, "application/vnd.android.package-archive");
			context.startActivity(it);
		}
	}
	
	/**
	 * 判断已安装app与将要安装的app的签名是否一致
	 * @param ctx
	 * @param filePath
	 * @param pkg
	 * @return
	 */
	public static boolean isSameSignature(Context ctx,String filePath,String pkg){
		try {
			PackageManager pm = ctx.getPackageManager();
			String installedApkSignature = pm.getPackageInfo(pkg, PackageManager.GET_SIGNATURES).signatures[0].toCharsString();
			PackageInfo piFile = pm.getPackageArchiveInfo(filePath, PackageManager.GET_SIGNATURES);
			Signature[] s = piFile.signatures;
			String fileApkSignature = s[0].toCharsString();
			
			if(installedApkSignature.equals(fileApkSignature))
				return true;
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 判断下载完成的包是否已经存在，如果存在判断签名是否一样
	 * @param context
	 * @param filePath
	 * @return 签名不一样返回true ，其他false
	 */
	public static boolean checkSignature(Context context, String filePath){
		try{
			File file = new File(filePath);
			if(file.exists() || !filePath.toLowerCase().endsWith(".apk")){
				PackageManager pm = context.getPackageManager();
				PackageInfo packageInfo = pm.getPackageArchiveInfo(filePath,
						PackageManager.GET_SIGNATURES);
				if (packageInfo != null) {
					PackageInfo installedPackage = context.getPackageManager().getPackageInfo(
							packageInfo.packageName, PackageManager.GET_SIGNATURES);
					if(installedPackage != null){
						android.content.pm.Signature[] sig1 = installedPackage.signatures;
						android.content.pm.Signature[] sig2 = packageInfo.signatures;
						if(sig1 != null && sig1.length > 0 && sig2 != null && sig2.length > 0){
							if(!sig1[0].equals(sig2[0])){
								return true;
							}else{
								return false;
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 判断下载完成的包是否已经存在，如果存在判断签名是否一样
	 * @param context
	 * @param filePath
	 * @return 签名不一样返回true ，其他false
	 */
	public static boolean checkSignature(Context context, AppsInfo vo){
		try{
			PackageInfo installedPackage = context.getPackageManager().getPackageInfo(
					vo.getAppsPackage(), PackageManager.GET_SIGNATURES);
			if(installedPackage != null){
				android.content.pm.Signature[] sig1 = installedPackage.signatures;
				if(sig1 != null && sig1.length > 0){
					byte[] data = Utils.hexToBytes(sig1[0].toCharsString());
		     		String encodeData = new String(Base64Util.encode(data));
		     		String md5 = Md5Encode.getMD5(encodeData);
					if(!md5.equals(vo.getAppsSignMD5())){
						return true;
					}else{
						return false;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 检测软件是否已经安装
	 * @param context
	 * @param pkgName
	 * @param versionCode
	 * @return 0没有安装 1大于当前包版本 2版本相同 3小于当前包版本
	 */
	public static int checkInstallApps(Context context, String pkgName, int versionCode){
		try{
			PackageInfo installedPackage = context.getPackageManager().getPackageInfo(
					pkgName, PackageManager.GET_ACTIVITIES);
			if(installedPackage != null){
				if(installedPackage.versionCode == versionCode){
					return 2;
				}else if(installedPackage.versionCode > versionCode){
					return 3;
				}else{
					return 1;
				}
			}
		}catch(Exception e){
			return 0;
		}
		return 0;
	}
	
	/**
	 * 打开应用方法
	 * @param packageName
	 */
	public static void openApp(Context context, String packageName) {
		try{
			Intent intent = new Intent();
			intent =context.getPackageManager().getLaunchIntentForPackage(packageName);
			context.startActivity(intent);
		}catch(Exception e){
//			e.printStackTrace();
		}
	}
}
