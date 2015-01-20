package com.bfgame.app.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 礼包对象
 * @author admin
 *
 */
public class Gift extends BaseVO{

	/**
	 * 礼包id
	 */
	private String giftId;
	/**
	 * 包名
	 */
	private String packageName;
	/**
	 * 点击动作
	 */
	private String action;
	/**
	 * 礼包图标
	 */
	private String icon;
	/**
	 * 是否已领取
	 */
	public boolean isGet=false;
	/**
	 * 礼包名称
	 */
	private String name;
	/**
	 * 礼包版本code
	 */
	private int versionCode;
	/**
	 * 礼包版本名称
	 */
	private String versionName;
	/**
	 * 1为有礼包，0为没有礼包
	 */
	private int gifts;
	/**
	 * 1为新服，0为新服
	 */
	private int isNew;
	/**
	 * 游戏大小
	 */
	private String size;
	/**
	 * 分类名称
	 */
	private String categoryName;
	/**
	 * 下载数量
	 */
	private String downloadCount;
	/**
	 * 下载uri
	 */
	private String downloadUri;
	/**
	 * 当前页
	 */
	private int currentPage;
	/**
	 * 总页数
	 */
	private int totalPage;
	/**
	 * 游戏详情uri
	 */
	private String uri;
	/**
	 * 礼包列表
	 */
	private List<Gift> giftList;
	/**
	 * 礼包开始时间
	 */
	private String giftStartTime;
	/**
	 * 礼包结束时间
	 */
	private String giftEndTime;
	/**
	 * 礼包详情
	 */
	private String giftInfo;
	/**
	 * 礼包状态
	 */
	private int giftState;
	/**
	 * 礼包数量
	 */
	private int giftCount;
	/**
	 * 礼包剩余数量
	 */
	private int giftRemain;
	
	/**
	 * 礼包简短描述
	 */
	private String smallDes;
	/**
	 * 礼包描述
	 */
	private String giftExplain;
	/**
	 * 礼包激活码
	 */
	private String activationCode;

	public void setGameInfo(GameInfo gameInfo){
		setPackageName(gameInfo.getPackageName());
		setAction(gameInfo.getAction());
		setIcon(gameInfo.getIcon());
		setName(gameInfo.getName());
		setVersionCode(gameInfo.getVersionCode());
		setVersionName(gameInfo.getVersionName());
		setGifts(gameInfo.getIsFeature());
		setIsNew(gameInfo.getIsNew());
		setSize(gameInfo.getSize());
		setCategoryName(gameInfo.getCategoryName());
		setDownloadCount(gameInfo.getDownloadCount());
		setUri(gameInfo.getUri());
		List<Gift> list = new ArrayList<Gift>();
		list.add(gameInfo.getGift());
		setGiftList(list);
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
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
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
	public void setIsNew(int i) {
		this.isNew = i;
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
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public List<Gift> getGiftList() {
		return giftList;
	}
	public void setGiftList(List<Gift> giftList) {
		this.giftList = giftList;
	}
	public String getGiftStartTime() {
		return giftStartTime;
	}
	public void setGiftStartTime(String giftStartTime) {
		this.giftStartTime = giftStartTime;
	}
	public String getGiftEndTime() {
		return giftEndTime;
	}
	public void setGiftEndTime(String giftEndTime) {
		this.giftEndTime = giftEndTime;
	}
	public String getGiftInfo() {
		return giftInfo;
	}
	public void setGiftInfo(String giftInfo) {
		this.giftInfo = giftInfo;
	}
	public int getGiftState() {
		return giftState;
	}
	public void setGiftState(int giftState) {
		this.giftState = giftState;
	}
	public int getGiftCount() {
		return giftCount;
	}
	public void setGiftCount(int giftCount) {
		this.giftCount = giftCount;
	}
	public String getSmallDes() {
		return smallDes;
	}
	public void setSmallDes(String smallDes) {
		this.smallDes = smallDes;
	}
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}


	public String getDownloadUri() {
		return downloadUri;
	}

	public void setDownloadUri(String downloadUri) {
		this.downloadUri = downloadUri;
	}

	public String getGiftExplain() {
		return giftExplain;
	}

	public void setGiftExplain(String giftExplain) {
		this.giftExplain = giftExplain;
	}


	public int getGiftRemain() {
		return giftRemain;
	}

	public void setGiftRemain(int giftRemain) {
		this.giftRemain = giftRemain;
	}

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}
}
