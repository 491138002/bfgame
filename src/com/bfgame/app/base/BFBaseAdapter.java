package com.bfgame.app.base;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.bfgame.app.util.DeviceUtil;
import com.bfgame.app.util.preference.Preferences;
import com.bfgame.app.util.preference.PreferencesUtils;
import com.bfgame.app.widget.dialog.CustomLoadingDialog;
public abstract class BFBaseAdapter extends BaseAdapter{
	protected BFBaseAdapter adapter = this;
	protected BaseActivity context;
	protected LayoutInflater mInflater;
	protected AbsListView listView;
	protected final String RELEASE_IMAGE_TAG = "release";
	protected PreferencesUtils preferencesUtils;
	
	/**
	 * 是否正在加载
	 */
	private boolean isLoading = true;
	
	//主线程Handler
	protected Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case UPDATE_ADAPTER:
				//修改数据集的时候， 不要调用这个
				adapter.notifyDataSetChanged();
				//dismissLoadingDialog();
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
	protected int loadRow = 0;	//加载行
	protected int CURRENT_TYPE = TYPE_LOADING;
	public static final int TYPE_LOADING = 1;
	public static final int TYPE_SHOW_DATA = 2;
	public static final int TYPE_IS_NULL = 3;
	
	public BFBaseAdapter(BaseActivity context, AbsListView listView){
		this.context = context;
		if(listView != null){
			this.listView = listView;
			this.listView.setSelector(context.getResources().getDrawable(R.color.background));
			this.listView.setOnScrollListener(new AbsListView.OnScrollListener() {
		            @Override
		            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
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
		            	if(firstVisibleItem + visibleItemCount >= totalItemCount-4){
		            		loadData();
		            	}
		            }
		        });
		}
		
		preferencesUtils = new PreferencesUtils(context, Preferences.CONFIG_FILE);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initLoadingDialog();
		initImageLoader();
		isLoading = true;
	}
	
	private void initImageLoader(){
		mImageThumbSize = context.getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
		mImageBigThumbSize = context.getResources().getDimensionPixelSize(R.dimen.image_thumbnail_big_size);
		
		mImageFetcher = new ImageFetcher(context, mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.bfgame_default_img);
        
        mImageBigFetcher = new ImageFetcher(context, mImageBigThumbSize);
        mImageBigFetcher.setLoadingImage(R.drawable.bfgame_default_big_img);
        
        ImageCacheParams cacheParams = new ImageCacheParams((Activity)context, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.15f); // Set memory cache to 25% of app memory
        mImageFetcher.addImageCache(((BaseActivity)context).getSupportFragmentManager(), cacheParams);
        mImageBigFetcher.addImageCache(((BaseActivity)context).getSupportFragmentManager(), cacheParams);
	}
	
	private void initLoadingDialog(){
		loadingDialog = new  CustomLoadingDialog(context, Constants.LOADING_CONTENTS, true);
	}
	
	public void showLoadingDialog(){
		if(loadingDialog != null && !loadingDialog.isShowing())
			loadingDialog.show();
	}
	
	public void dismissLoadingDialog(){
		if(loadingDialog != null && loadingDialog.isShowing())
			loadingDialog.dismiss();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addFirst(final List list){
		isLoadingData = false;
		isLoading = false;
		dismissLoadingDialog();
		if(list != null){
			dataList.addAll(0, list);
			list.clear();
		}
		notifyDataSetChanged();
	}
	
	@SuppressWarnings("unchecked")
	public void addItem(Object o){
		isLoadingData = false;
		isLoading = false;
		if(o != null){
			dataList.add(o);
			notifyDataSetChanged();
		}
	}
	
	public boolean remove(int i){
		if(dataList != null && dataList.size() > i){
			dataList.remove(i);
		}
		return true;
	}
	
	public boolean remove(Object o){
		if(dataList != null && o != null){
			return dataList.remove(o);
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public void addLast(List list){
		addLast(list, true);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addLast(List list, boolean isClear){
		isLoadingData = false;
		isLoading = false;
		dismissLoadingDialog();
		if(list != null){
			this.dataList.addAll(list);
			if(isClear)
				list.clear();
		}
		notifyDataSetChanged();
	}
	
	public void clear(){
		isLoading = true;
		isLoadingData = false;
		dataList.clear();
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		if(position >= 0 && position < dataList.size())
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
		// TODO Auto-generated method stub
		return dataList.size() + loadRow;
	}
	
	public void releaseCacheImage(){
		for (int i = 0; listView != null && i < listView.getCount(); i++) {
			View view = listView.getChildAt(i);
			if(view != null){
				ImageView imgView = (ImageView)view.findViewWithTag(RELEASE_IMAGE_TAG);
				if(imgView != null)
					imgView.setImageResource(R.drawable.bfgame_loading_background);
			}
		}
	}
	
	public int updateType() {
		if (isLoading) {
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
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (CURRENT_TYPE == TYPE_IS_NULL) {
			convertView = mInflater.inflate(R.layout.bfgame_list_is_null, null);

			AbsListView.LayoutParams params = new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			;
			
			params.height = listView.getHeight() < DeviceUtil.dip2px(context, 200) ? DeviceUtil.dip2px(context, 200) : listView.getHeight();
			params.width = listView.getWidth();
			convertView.setLayoutParams(params);

			TextView nullText = (TextView) convertView
					.findViewById(R.id.null_text);
			nullText.setText(context.getString(R.string.list_is_null_text,
					""));
			convertView.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					
				}
			});
			return convertView;
		}
		return null;
	}
	
	public void onDestroy(){
		dataList.clear();
		dataList = null;
		context = null;
		listView = null;
		mImageFetcher.closeCache();
	}
	
	public void onPause(){
		mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
	}
	
	public void onResume(){
		mImageFetcher.setExitTasksEarly(false);
		notifyDataSetChanged();
	}
	
	public boolean checkTag(int tag){
		if("1".equals(tag)){
			return true;
		}else{
			return false;
		}
	}
	
	public void showToast(String content){
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
	
	public abstract void loadData();
}
