package com.bfgame.app.util;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.BaseJson;

import com.bfgame.app.util.preference.Preferences;
import com.bfgame.app.util.preference.PreferencesUtils;
import com.bfgame.app.vo.GameInfo;
import com.bfgame.app.vo.StatisticsVO;
import com.google.gson.reflect.TypeToken;
/**
 * @ClassName: StatisticsUtil
 * @Description:数据统计工具类
 * @author jzy
 *
 */
public class StatisticsUtil {
	
	/**
	 * 添加统计数据
	 * @param vo
	 * @param p
	 */
	public static void addStatistics(StatisticsVO vo, PreferencesUtils p){
		List<StatisticsVO> sList = (List<StatisticsVO>)BaseJson.parser(new TypeToken<List<StatisticsVO>>(){}, p.getString(Preferences.STATISTICS, ""));
		if(sList == null){
			sList = new ArrayList<StatisticsVO>();
		}
		sList.add(vo);
		p.putString(Preferences.STATISTICS, BaseJson.toJson(sList));
	}
	
	/**
	 * 获取统计列表
	 * @param p
	 * @return
	 */
	public static List<StatisticsVO> getStatisticsList(PreferencesUtils p){
		List<StatisticsVO> sList = (List<StatisticsVO>)BaseJson.parser(new TypeToken<List<StatisticsVO>>(){}, p.getString(Preferences.STATISTICS, ""));
		if(sList == null){
			sList = new ArrayList<StatisticsVO>();
		}
		return sList;
	}
	
	public static String getStaticsticsJson(PreferencesUtils p){
		return p.getString(Preferences.STATISTICS, "");
	}
	
	/**
	 * 清除统计数据
	 * @param p
	 * @param size
	 */
	public static void clearStatistics(PreferencesUtils p, int size){
		List<StatisticsVO> sList = (List<StatisticsVO>)BaseJson.parser(new TypeToken<List<StatisticsVO>>(){}, p.getString(Preferences.STATISTICS, ""));
		if(sList != null && sList.size() >= size){
			for (int i = size-1; i >= 0 ; i--) {
				sList.remove(i);
			}
			p.putString(Preferences.STATISTICS, BaseJson.toJson(sList));
		}else{
			p.putString(Preferences.STATISTICS, "");
		}
	}
	
	/**
	 * 显示轮播统计
	 * @param p
	 * @param gameList
	 */
	public static void addShowBannerList(PreferencesUtils p, List<GameInfo> gameList){
		for (int i = 0; gameList != null && i < gameList.size(); i++) {
			StatisticsVO vo = new StatisticsVO();
			vo.setPid(gameList.get(i).getBannerId());
			vo.setType("ban");
			vo.setAct("s");
			vo.setSpid(1);
			
			addStatistics(vo, p);
		}
	}
	/**
	 * 点击轮播统计
	 * @param p
	 * @param gameInfo
	 */
	public static void addClickBanner(PreferencesUtils p, GameInfo gameInfo){
		StatisticsVO vo = new StatisticsVO();
		vo.setPid(gameInfo.getBannerId());
		vo.setType("ban");
		vo.setAct("c");
		vo.setSpid(1);
		addStatistics(vo, p);
	}
	
	/**
	 * 显示广告统计
	 * @param p
	 * @param gameInfo
	 */
	public static void addShowAd(PreferencesUtils p, GameInfo gameInfo){
		StatisticsVO vo = new StatisticsVO();
		vo.setPid(gameInfo.getAdId());
		vo.setType("ad");
		vo.setAct("s");
		vo.setSpid(1);
		addStatistics(vo, p);
	}
	
	/**
	 * 点击广告统计
	 * @param p
	 * @param gameInfo
	 */
	public static void addClickAd(PreferencesUtils p, GameInfo gameInfo){
		StatisticsVO vo = new StatisticsVO();
		vo.setPid(gameInfo.getAdId());
		vo.setType("ad");
		vo.setAct("c");
		vo.setSpid(1);
		addStatistics(vo, p);
	}
	
	/**
	 * 广告下载统计
	 * @param p
	 * @param gameInfo
	 */
	public static void addDownloadAd(PreferencesUtils p, GameInfo gameInfo){
		StatisticsVO vo = new StatisticsVO();
		vo.setPid(gameInfo.getAdId());
		vo.setType("ad");
		vo.setAct("d");
		vo.setSpid(1);
		addStatistics(vo, p);
	}
	
	
	public static void addClickKey(PreferencesUtils p, int keyID){
		StatisticsVO vo = new StatisticsVO();
		vo.setPid(keyID);
		vo.setAct("c");
		vo.setType("key");
		vo.setSpid(1);
		addStatistics(vo, p);
	}
	
	public static void addShowPage(PreferencesUtils p, int pid,int spid){
		StatisticsVO vo = new StatisticsVO();
		vo.setPid(pid);
		vo.setAct("s");
		vo.setSpid(spid);
		vo.setType("page");
		addStatistics(vo, p);
	}
	
	/**
	 *  0=下载开始
		1=下载完成
		2=下载失败-用户主动终止（断网等操作）
		3=下载失败-服务器连接超时
		4=下载失败-下载地址不正确
		9=下载失败-其他原因
		10=安装成功
		11=安装失败-用户主动终止（中断等操作）
		12=安装失败-安装包不正确
		13=安装失败-应用冲突（或被拦截）
		19=安装失败-其他原因
	 */
	public static final int APP_DOWNLOAD_STATE_START = 0;
	public static final int APP_DOWNLOAD_STATE_END = 1;
	public static final int APP_DOWNLOAD_STATE_ERROR_USER = 2;
	public static final int APP_DOWNLOAD_STATE_ERROR_SERVER = 3;
	public static final int APP_DOWNLOAD_STATE_ERROR_URL = 4;
	public static final int APP_DOWNLOAD_STATE_ERROR_OTHER = 9;
	public static final int APP_INSTALL_STATE_SUCCESS = 10;
	public static final int APP_INSTALL_STATE_ERROR_USER = 11;
	public static final int APP_INSTALL_STATE_ERROR_APP = 12;
	public static final int APP_INSTALL_STATE_ERROR_PACKAGE = 13;
	public static final int APP_INSTALL_STATE_ERROR_OTHER = 19;
	
	public static void addDownloadApp(PreferencesUtils p,int spid,int cid, int pid, int state){
		StatisticsVO vo = new StatisticsVO();
		vo.setPid(pid);
		vo.setAct("d");
		vo.setType("app");
		vo.setSpid(spid);
		vo.setCid(cid);
		vo.setState(state);
		addStatistics(vo, p);
	}
	
	public static void addInstallApp(PreferencesUtils p, int spid,int pid,int cid,  int state){
		StatisticsVO vo = new StatisticsVO();
		vo.setPid(pid);
		vo.setAct("o");
		vo.setCid(cid);
		vo.setType("app");
		vo.setSpid(spid);
		vo.setState(state);
		addStatistics(vo, p);
	}
	
	public static void addEnterApp(PreferencesUtils p, int pid,int spid,int cid){
		StatisticsVO vo = new StatisticsVO();
		vo.setPid(pid);
		vo.setAct("e");
		vo.setType("app");
		vo.setCid(cid);
		vo.setSpid(spid);
		addStatistics(vo, p);
	}
}
