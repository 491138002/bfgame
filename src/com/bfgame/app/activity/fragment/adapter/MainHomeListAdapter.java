package com.bfgame.app.activity.fragment.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bfgame.app.R;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.vo.GameInfo;
import com.bfgame.app.widget.view.MyGridView;

public class MainHomeListAdapter extends BaseAdapter {
	private String str[] = { "热门推荐", "精品单机", "精品网游" };

	@SuppressWarnings("rawtypes")
	private Map<Integer, List> mListMap; // 0 ,1, 2
	private BaseActivity mContext;
	@SuppressWarnings("unused")
	private AbsListView mListView;
	protected List dataList = new ArrayList();
	private BaseAdapter adapter;
	TextView tv_main;
	MyGridView gv_home;

	@SuppressWarnings("rawtypes")
	@SuppressLint("UseSparseArrays") public MainHomeListAdapter(BaseActivity context, AbsListView listView) {
		mListMap = new HashMap<Integer, List>();
		this.mListView = listView;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return str.length;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		view = View.inflate(mContext, R.layout.bfgame_main_home_item, null);

		gv_home = (MyGridView) view.findViewById(R.id.gv_home);
		tv_main = (TextView) view.findViewById(R.id.tv_main);
		tv_main.setText(str[position]);

		if (gv_home != null) {
			List arrayListForEveryGridView = mListMap.get(position);
			if (arrayListForEveryGridView != null) {
				switch (position) {
				case 0:
					adapter = new MainHomeGridAdapter(mContext, gv_home,
							null, arrayListForEveryGridView);
					break;
				case 1:
					adapter = new MainHomeGridAdapter(mContext, gv_home,
							null, arrayListForEveryGridView);
					break;
				case 2:
					adapter = new MainHomeGridAdapter(mContext, gv_home,
							null, arrayListForEveryGridView);
					break;
				}
				gv_home.setAdapter(adapter);
			}
		}
		return view;
	}

	@SuppressWarnings("rawtypes")
	public void updateMap(int cat, List dataList) {
		List list = mListMap.get(cat);
		if (list != null) {
			mListMap.remove(cat);
		}
		mListMap.put(cat, dataList);
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return mListMap.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getSize() {
		return mListMap.isEmpty() ? 0 : 1;
	}

	public void resetList(List list) {
		dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
		
	}

	public void onDestroy() {
		dataList.clear();
		dataList = null;
		mContext = null;
		mListView = null;
	}
}
