package com.bfgame.app.vo;

import com.bfgame.app.common.Constants;



/**
 * 统计对象
 * @author apple
 *
 */
public class StatisticsVO {

	/**
	 * 应用id，轮播id,视图id,广告id
	 */
	private int pid;
	/**
	 * 应用类型
	 */
	private String type;
	/**
	 * 点击动作   1下载 2打开
	 */
	private String act;
	/**
	 * count数量
	 */
	private int c=1;
	/**
	 * 应用当前页面
	 */
	private int spid;
	
	/**
	 * 游戏分类id
	 */
	private int cid;
	
	
	/**
	 * 创建时间
	 */
	private long ctime = System.currentTimeMillis();
	
	/**
	 * 状态
	 */
	private int state;
	/**
	 * 版本号
	 */
	private String ver=Constants.VER;
	
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getSpid() {
		return spid;
	}
	public void setSpid(int spid) {
		this.spid = spid;
	}
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	@Override
	public String toString() {
		return "StatisticsVO [pid=" + pid + ", type=" + type + ", act=" + act
				+ ", c=" + c + ", ctime=" + ctime + ", ver=" + ver + "]";
	}
}
