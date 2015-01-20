package com.bfgame.app.common;

import java.util.HashMap;

/**
 * 全局常量
 * @ClassName Constants
 * @author admin
 *
 */
public class Constants {

	/**
	 * 版本信息
	 */
	public static final String VERSION_NAME = "暴风游戏V";
	/**
	 * 版本
	 */
	public static final String VER ="5.0.20";
	/**
	 * 服务器域名
	 */
//	public static final String BASE_URL = "http://h5.7433.com/";
//	public static final String BASE_URL = "http://bftest.7433.com/";
	 public static final String BASE_URL = "http://api.gcenter.baofeng.com/";
//	 public static final String BASE_URL = "http://101.71.81.80/";

	/**
	 * 是否启用gzip
	 */
	public static boolean isGzip = true;
	/**
	 * 请求线程是否停止
	 */
	public static boolean IS_STOP_REQUEST = false;
	/**
	 * 链接次数
	 */
	public static final int CONNECTION_COUNT = 3;
	/**
	 * 链接失败提示
	 */
	public static final String ERROR_MESSAGE = "请求网络失败，请检查网络设置";
	/**
	 * 加载提示
	 */
	public static String LOADING_CONTENTS = "加载中，请稍候...";
	/**
	 * 加载内容集合
	 */
	public static HashMap<String, String> LOADING_CONTENT_MAP = new HashMap<String, String>();
	/**
	 * 链接超时设置
	 */
	public static final int CONNECTION_SHORT_TIMEOUT = 5000;// 连接超时 5s
	/**
	 * 读取超时设置
	 */
	public static final int READ_SHORT_TIMEOUT = 5000;// 读取超时 5s
	/**
	 * post
	 */
	public static final String HTTP_POST = "POST";
	/**
	 * get
	 */
	public static final String HTTP_GET = "GET";
	
	/**
	 * 下拉刷新
	 */
	public static boolean IS_REFRESH = false;
	/**
	 * 礼包刷新
	 */
	public static boolean NEED_REFRESH_GIFT = true;
	
	/**
	 * 礼包提示
	 */
	public static boolean NEED_TIP_GIFT = true;

	/**
	 * 首页banner url
	 */
	public static final String URL_MAIN_BANNER = BASE_URL + "banner";

	/**
	 * 首页列表url
	 */
	public static final String URL_MAIN_HOME_LIST = BASE_URL + "view";

	/**
	 * 分类列表url
	 */
	public static final String URL_MAIN_CATEGORY_LIST = BASE_URL + "category";
	/**
	 * 排行url
	 */
	public static final String URL_MAIN_TOP_LIST = BASE_URL + "rank";
	/**
	 * 礼包列表url
	 */
	public static final String URL_MAIN_GIFT_LIST = BASE_URL + "gift";

	/**
	 * 单机列表url
	 */
	public static final String URL_MAIN_CONSOLE_LIST = BASE_URL + "single";

	/**
	 * 网游列表url
	 */
	public static final String URL_MAIN_ONLINE_LIST = BASE_URL + "online";

	/**
	 * 领取礼包url
	 */
	public static final String URL_MAIN_GET_GIFT = BASE_URL + "gift";

	/**
	 * 搜索url
	 */
	public static final String URL_SEARCH = BASE_URL + "search";

	/**
	 * 数据统计上传url
	 */
	public static final String URL_UPLOAD_STATISTICS =  BASE_URL + "statistics";

	/**
	 * 广告url
	 */
	public static final String URL_AD = BASE_URL + "ad";

	/**
	 * 获取网游banner  url
	 */
	public static final String URL_ONLINE_BANNER = BASE_URL + "banner";
	/**
	 * 获取单机banner  url
	 */
	public static final String URL_SINGLE_BANNER = BASE_URL + "banner";
	/**
	 * 上传应用信息url
	 */
	public static final String URL_UPLOAD_APP = BASE_URL + "app";
	
}
