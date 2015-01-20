package com.bfgame.app.net.utils;

import java.util.List;

public abstract class BaseList<T> {
	List<T> list = null;

	public BaseList(List<T> pList) {

		this.list = pList;
	}

	public abstract String getObjectType();

	public List<T> getList() {
		return this.list;
	};

	public void setList(List<T> pList) {

		this.list = pList;
	};

	public int getCount() {
		return list.size();
	}
}
