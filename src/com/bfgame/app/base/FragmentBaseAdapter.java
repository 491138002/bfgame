package com.bfgame.app.base;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bfgame.app.R;
import com.bfgame.app.bitmap.util.ImageCache.ImageCacheParams;
import com.bfgame.app.bitmap.util.ImageFetcher;
import com.bfgame.app.common.Constants;
import com.bfgame.app.util.preference.Preferences;
import com.bfgame.app.util.preference.PreferencesUtils;
import com.bfgame.app.widget.dialog.CustomLoadingDialog;

public abstract class FragmentBaseAdapter extends BaseAdapter {
	protected FragmentBaseAdapter adapter = this;
	protected BaseActivity context;
	protected BaseFragment ft;
	protected LayoutInflater mInflater;
	protected AbsListView listView;
	protected final String RELEASE_IMAGE_TAG = "release";
	protected PreferencesUtils preferencesUtils;

	// 主线程Handler
	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_ADAPTER:
				// 修改数据集的时候， 不要调用这个
				adapter.notifyDataSetChanged();
				dismissLoadingDialog();
				break;
			}
		};
	};

	protected ImageFetcher mImageFetcher;
	private int mImageThumbSize;

	protected ImageFetcher mImageBigFetcher;
	private int mImageBigThumbSize;

	/**
	 * 刷新adapter
	 */
	public static final int UPDATE_ADAPTER = 1;

	/**
	 * 加载提示框
	 */
	public static CustomLoadingDialog loadingDialog;

	/**
	 * 数据集
	 */
	@SuppressWarnings("rawtypes")
	protected List dataList = new ArrayList();

	/**
	 * 图片缓存路径
	 */
	private static final String IMAGE_CACHE_DIR = "thumbs";

	/**
	 * 是否正在加载数据
	 */
	protected boolean isLoadingData = false;
	protected int loadRow = 0; // 加载行
	protected int CURRENT_TYPE = TYPE_LOADING;
	protected static final int TYPE_LOADING = 1;
	protected static final int TYPE_SHOW_DATA = 2;
	protected static final int TYPE_IS_NULL = 3;

	public FragmentBaseAdapter(BaseActivity context, AbsListView listView,
			BaseFragment ft) {
		dataList = new ArrayList();
		this.ft = ft;
		this.context = context;
		this.listView = listView;
		this.listView.setSelector(context.getResources().getDrawable(
				R.color.background));
		this.listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView,
					int scrollState) {
				// Pause fetcher to ensure smoother scrolling when flinging
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					mImageFetcher.setPauseWork(true);
					mImageBigFetcher.setPauseWork(true);
				} else {
					mImageFetcher.setPauseWork(false);
					mImageBigFetcher.setPauseWork(false);
				}
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount >= totalItemCount - 4) {
					loadData();
				}
			}
		});

		preferencesUtils = new PreferencesUtils(context,
				Preferences.CONFIG_FILE);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initLoadingDialog();
		initImageLoader();
	}

	private void initImageLoader() {
		mImageThumbSize = context.getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_size);
		mImageBigThumbSize = context.getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_big_size);

		mImageFetcher = new ImageFetcher(context, mImageThumbSize);
		mImageFetcher.setLoadingImage(R.drawable.bfgame_default_img);

		mImageBigFetcher = new ImageFetcher(context, mImageBigThumbSize);
		mImageBigFetcher.setLoadingImage(R.drawable.bfgame_default_big_img);

		ImageCacheParams cacheParams = new ImageCacheParams((Activity) context,
				IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.15f); // Set memory cache to 25% of
													// app memory
		mImageFetcher.addImageCache(
				((BaseActivity) context).getSupportFragmentManager(),
				cacheParams);
		mImageBigFetcher.addImageCache(
				((BaseActivity) context).getSupportFragmentManager(),
				cacheParams);
	}

	private void initLoadingDialog() {
		loadingDialog = new CustomLoadingDialog(context,
				Constants.LOADING_CONTENTS, true);
	}

	/**
	 * 显示加载对话框
	 */
	public void showLoadingDialog() {
		if (loadingDialog != null && !loadingDialog.isShowing())
			loadingDialog.show();
	}

	/**
	 * 隐藏加载对话框
	 */
	public void dismissLoadingDialog() {
		if (loadingDialog != null && loadingDialog.isShowing())
			loadingDialog.dismiss();
	}

	/**
	 * 向列表开始添加数据
	 * @param list
	 */
	public void addFirst(final List list) {
		isLoadingData = false;
		dismissLoadingDialog();
		if (list != null) {
			dataList.addAll(0, list);
			list.clear();
		}
		notifyDataSetChanged();
	}

	/**
	 * 重置列表数据
	 * @param list
	 */
	  public void resetList(List list){
          dataList.clear();
          dataList.addAll(list);
          notifyDataSetChanged();
      }
	  
	  
	  /**
	   * 添加对象
	   * @param o
	   */
	public void addItem(Object o) {
		isLoadingData = false;
		if (o != null) {
			dataList.add(o);
			notifyDataSetChanged();
		}
	}

	/**
	 * 移除列表某个位置的数据
	 * @param i
	 * @return
	 */
	public boolean remove(int i) {
		if (dataList != null && dataList.size() > i) {
			dataList.remove(i);
		}
		return true;
	}

	/**
	 * 移除某个对象是否成功
	 * @param o
	 * @return
	 */
	public boolean remove(Object o) {
		if (dataList != null && o != null) {
			return dataList.remove(o);
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public void addLast(List list) {
		addLast(list, true);
	}

	/**
	 * 在列表底添加数据
	 * @param list
	 * @param isClear
	 */
	public void addLast(List list, boolean isClear) {
		isLoadingData = false;
		dismissLoadingDialog();
		if (list != null) {
			this.dataList.addAll(list);
			if (isClear)
				list.clear();
		}
		notifyDataSetChanged();
	}

	/**
	 * 清除list
	 */
	public void clear() {
		isLoadingData = false;
		dataList.clear();
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		if (position >= 0 && position < dataList.size())
			return dataList.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getCount() {
		if (dataList == null)
			dataList = new ArrayList();
		// TODO Auto-generated method stub
		return dataList.size() + loadRow;
	}

	/**
	 * 释放图像缓存
	 */
	public void releaseCacheImage() {
		for (int i = 0; i < listView.getCount(); i++) {
			View view = listView.getChildAt(i);
			if (view != null) {
				ImageView imgView = (ImageView) view
						.findViewWithTag(RELEASE_IMAGE_TAG);
				if (imgView != null)
					imgView.setImageResource(R.drawable.bfgame_loading_background);
			}
		}
	}

	/**
	 * 更新加载类型
	 * @return
	 */
	public int updateType() {
		if (isLoadingData) {
			CURRENT_TYPE = TYPE_LOADING;
			loadRow = 0;
		} else if (dataList.size() == 0) {
			CURRENT_TYPE = TYPE_IS_NULL;
			loadRow = 1;
		} else {
			CURRENT_TYPE = TYPE_SHOW_DATA;
			loadRow = 0;
		}
		return CURRENT_TYPE;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		updateType();
		super.notifyDataSetChanged();
	}

	public void onDestroy() {
		dataList.clear();
		dataList = null;
		context = null;
		listView = null;
		mImageFetcher.closeCache();
	}

	public void onPause() {
		mImageFetcher.setPauseWork(false);
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}

	public void onResume() {
		mImageFetcher.setExitTasksEarly(false);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (CURRENT_TYPE == TYPE_IS_NULL) {
			convertView = mInflater.inflate(R.layout.bfgame_list_is_null, null);

			AbsListView.LayoutParams params = new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			;
			params.height = listView.getHeight();
			convertView.setLayoutParams(params);

			TextView nullText = (TextView) convertView
					.findViewById(R.id.null_text);
			if (context!=null) {
				nullText.setText(context.getString(R.string.list_is_null_text, ""));
			}
			return convertView;
		}
		return convertView;
	}

	public boolean checkTag(int tag) {
		if (tag==1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 显示土司
	 * @param content
	 */
	public void showToast(String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

	public int getSize() {
		return dataList.size();
	}

	/**
	 * 加载数据
	 */
	public abstract void loadData();
}
