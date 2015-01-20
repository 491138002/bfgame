package com.bfgame.app.vo;

import java.util.List;
/**
 * 分类对象
 * @author admin
 *
 */
public class Category extends BaseVO{

	/**
	 * 分类名
	 */
	private String categoryName;
	/**
	 * 分类英文名
	 */
	private String categoryNameEn;
	/**
	 * 分类id
	 */
	private String categoryId;
	/**
	 * 分类点击动作
	 */
	private String action;
	/**
	 * 分类详情uri
	 */
	private String uri;
	/**
	 * 分类图标
	 */
	private String icon;
	/**
	 * 子分类名
	 */
	private String subCateName;
	/**
	 * 子分类英文名
	 */
	private String subCateNameEn;
	/**
	 * 子分类id
	 */
	private String subCateId;
	/**
	 * 子分类点击动作
	 */
	private String subCateAction;
	/**
	 * 子分类游戏详情uri
	 */
	private String subCateUri;
	/**
	 * 子分类图标
	 */
	private String subCateIcon;
	/**
	 * 子分类列表
	 */
	private List<Category> subCates;
	
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getSubCateName() {
		return subCateName;
	}
	public void setSubCateName(String subCateName) {
		this.subCateName = subCateName;
	}
	public String getSubCateId() {
		return subCateId;
	}
	public void setSubCateId(String subCateId) {
		this.subCateId = subCateId;
	}
	public String getSubCateAction() {
		return subCateAction;
	}
	public void setSubCateAction(String subCateAction) {
		this.subCateAction = subCateAction;
	}
	public String getSubCateUri() {
		return subCateUri;
	}
	public void setSubCateUri(String subCateUri) {
		this.subCateUri = subCateUri;
	}
	public String getSubCateIcon() {
		return subCateIcon;
	}
	public void setSubCateIcon(String subCateIcon) {
		this.subCateIcon = subCateIcon;
	}
	public List<Category> getSubCates() {
		return subCates;
	}
	public void setSubCates(List<Category> subCates) {
		this.subCates = subCates;
	}
	public String getCategoryNameEn() {
		return categoryNameEn;
	}
	public void setCategoryNameEn(String categoryNameEn) {
		this.categoryNameEn = categoryNameEn;
	}
	public String getSubCateNameEn() {
		return subCateNameEn;
	}
	public void setSubCateNameEn(String subCateNameEn) {
		this.subCateNameEn = subCateNameEn;
	}
}
