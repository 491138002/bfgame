package com.bfgame.app.vo;

import java.util.List;
/**
 * 游戏对象
 * @author admin
 *
 */
public class GameInfo extends BaseVO{
	/**
	 * 视图id
	 */
	private String viewId;
	
	
	/**
	 * 版本名称
	 */
	private String versionName;
	/**
	 * 游戏大小
	 */
	private String size;
	/**
	 * 游戏总下载量
	 */
	private String downloadCount;
	/**
	 * 游戏id
	 */
	private int id;
	/**
	 * 游戏名称
	 */
	private String name;
	
	/**
	 * 当前游戏截图列表
	 */
	private List<GameInfo> previewList;
	/**
	 * 当前视图点击动作
	 */
	private String previewAction;
	/**
	 * 应用截图下载地址
	 */
	private String previewUri;
	/**
	 * 游戏简介
	 */
	private String description;
	/**
	 * 推荐游戏列表
	 */
	private List<GameInfo> recommendList;
	/**
	 * 推荐游戏图标
	 */
	private String recomIcon;
	/**
	 * 推荐游戏名称
	 */
	private String recomName;
	/**
	 * 推荐游戏uri
	 */
	private String recomUri;
	/**
	 * 游戏攻略
	 */
	private String raiders;
	/**
	 * 礼包url
	 */
	private String packs;
	/**
	 * 礼包
	 */
	private Gift gift;
	/**
	 * 是否有礼包
	 */
	private int gifts;
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
	 * 图片uri
	 */
	private String imageUrl;
	
	/**
	 * 一句话描述
	 */
	private String smallDes;
	/**
	 * 精品单机游戏列表
	 */
	private List<GameInfo> single;
	/**
	 * 热门推荐游戏列表
	 */
	private List<GameInfo> recommend;
	/**
	 * 精品网络游戏列表
	 */
	private List<GameInfo> online;
	/**
	 * 游戏下载uri
	 */
	private String downloadUri;
	/**
	 * 点击动作
	 */
	private String action;
	/**
	 * 游戏图标
	 */
	private String icon;
	/**
	 * 版本code
	 */
	private int versionCode;
	/**
	 * 应用包名
	 */
	private String packages;
	/**
	 * 1为手法，0为新服
	 */
	private int isFeature;
	/**
	 * 1为新服，0为非新服
	 */
	private int isNew;
	
	/**
	 * 1为热门，0为非热门
	 */
	private int isHot;
	/**
	 * 按照分类获取游戏列表的URI
	 */
	private String uri;
	/**
	 * 分类名称
	 */
	private String categoryName;
	/**
	 * app包名
	 */
	private String packageName;
	/**
	 * 轮播图id
	 */
	private int bannerId;
	/**
	 * 游戏id
	 */
	private int appId;
	/**
	 * 首页广告uri
	 */
	private String adUri;
	/**
	 * 首页广告id
	 */
	private int adId;
	/**
	 * 游戏名称
	 */
	private String appName;
	/**
	 * 游戏类型
	 */
	private int typeGame;
	
	
	/**
	 * 分类id
	 */
	private int categoryId;
	
	/**
	 * 总下载量
	 */
	private String downloadNum;
	/**
	 * 月下载量
	 */
	private int monthDownNum;
	/**
	 * 下载uri
	 */
	private String downloadUrl;
	
	
	public String getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(String downloadCount) {
		this.downloadCount = downloadCount;
	}
	public List<GameInfo> getPreviewList() {
		return previewList;
	}
	public void setPreviewList(List<GameInfo> previewList) {
		this.previewList = previewList;
	}
	public String getPreviewAction() {
		return previewAction;
	}
	public void setPreviewAction(String previewAction) {
		this.previewAction = previewAction;
	}
	public String getPreviewUri() {
		return previewUri;
	}
	public void setPreviewUri(String previewUri) {
		this.previewUri = previewUri;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<GameInfo> getRecommendList() {
		return recommendList;
	}
	public void setRecommendList(List<GameInfo> recommendList) {
		this.recommendList = recommendList;
	}
	public String getRecomIcon() {
		return recomIcon;
	}
	public void setRecomIcon(String recomIcon) {
		this.recomIcon = recomIcon;
	}
	public String getRecomName() {
		return recomName;
	}
	public void setRecomName(String recomName) {
		this.recomName = recomName;
	}
	public String getRecomUri() {
		return recomUri;
	}
	public void setRecomUri(String recomUri) {
		this.recomUri = recomUri;
	}
	public String getRaiders() {
		return raiders;
	}
	public void setRaiders(String raiders) {
		this.raiders = raiders;
	}
	public String getPacks() {
		return packs;
	}
	public void setPacks(String packs) {
		this.packs = packs;
	}
	public Gift getGift() {
		return gift;
	}
	public void setGift(Gift gift) {
		this.gift = gift;
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
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getSmallDes() {
		return smallDes;
	}
	public void setSmallDes(String smallDes) {
		this.smallDes = smallDes;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public int getBannerId() {
		return bannerId;
	}
	public void setBannerId(int bannerId) {
		this.bannerId = bannerId;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAdUri() {
		return adUri;
	}
	public void setAdUri(String adUri) {
		this.adUri = adUri;
	}
	public int getAdId() {
		return adId;
	}
	public void setAdId(int adId) {
		this.adId = adId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public int getTypeGame() {
		return typeGame;
	}
	public void setTypeGame(int typeGame) {
		this.typeGame = typeGame;
	}
	public int getIsFeature() {
		return isFeature;
	}
	public void setIsFeature(int isFeature) {
		this.isFeature = isFeature;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getDownloadNum() {
		return downloadNum;
	}
	public void setDownloadNum(String downloadNum) {
		this.downloadNum = downloadNum;
	}
	public int getMonthDownNum() {
		return monthDownNum;
	}
	public void setMonthDownNum(int monthDownNum) {
		this.monthDownNum = monthDownNum;
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
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public int getIsNew() {
		return isNew;
	}
	public void setIsNew(int isNew) {
		this.isNew = isNew;
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
	public String getViewId() {
		return viewId;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	
	public List<GameInfo> getSingle() {
		return single;
	}
	public void setSingle(List<GameInfo> single) {
		this.single = single;
	}
	public List<GameInfo> getRecommend() {
		return recommend;
	}
	public void setRecommend(List<GameInfo> recommend) {
		this.recommend = recommend;
	}
	public List<GameInfo> getOnline() {
		return online;
	}
	public void setOnline(List<GameInfo> online) {
		this.online = online;
	}
	public String getDownloadUri() {
		return downloadUri;
	}
	public void setDownloadUri(String downloadUri) {
		this.downloadUri = downloadUri;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public int getIsHot() {
		return isHot;
	}
	public void setIsHot(int isHot) {
		this.isHot = isHot;
	}
	public String getPackages() {
		return packages;
	}
	public void setPackages(String packages) {
		this.packages = packages;
	}
	public int getGifts() {
		return gifts;
	}
	public void setGifts(int gifts) {
		this.gifts = gifts;
	}
}
