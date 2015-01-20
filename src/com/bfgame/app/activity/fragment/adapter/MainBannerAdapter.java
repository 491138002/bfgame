package com.bfgame.app.activity.fragment.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.bfgame.app.R;
import com.bfgame.app.activity.GameDetailActivity;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.bitmap.util.ImageCache.ImageCacheParams;
import com.bfgame.app.bitmap.util.ImageFetcher;
import com.bfgame.app.util.StatisticsUtil;
import com.bfgame.app.util.preference.Preferences;
import com.bfgame.app.util.preference.PreferencesUtils;
import com.bfgame.app.vo.GameInfo;
import com.bfgame.app.widget.view.ChildViewPager;
import com.bfgame.app.widget.view.ChildViewPager.OnSingleTouchListener;
import com.bfgame.app.widget.view.PointWidget;
/**
 * 首页轮播adpter
 * @author admin
 *
 */
public class MainBannerAdapter extends PagerAdapter{
	private Context context;
	private List<View> list = new ArrayList<View>();
	private List<GameInfo> dataList;
	protected ImageFetcher mImageFetcher;
	private int mImageThumbSize;
	private static final String IMAGE_CACHE_DIR = "thumbs";
	private int count = Integer.MAX_VALUE;
	private ChildViewPager viewPager;
	private PointWidget pw;
	private ScrollThread mScrollThread;
	private PreferencesUtils p;
	
	private boolean isScroll = true;
	private boolean isLoop = true;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(isScroll)
				viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
		};
	};
	
	private void initImageLoader(Context context) {
		mImageThumbSize = context.getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_big_size);
		mImageFetcher = new ImageFetcher(context, mImageThumbSize);
		mImageFetcher.setLoadingImage(R.drawable.bfgame_default_big_img);

		ImageCacheParams cacheParams = new ImageCacheParams((Activity) context,
				IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
													// app memory
		mImageFetcher.addImageCache(
				((BaseActivity) context).getSupportFragmentManager(),
				cacheParams);
	}

	public MainBannerAdapter(Context mContext, List<GameInfo> mDataList, ChildViewPager mViewPager, PointWidget pw) {
		this.pw = pw;
		this.p = new PreferencesUtils(mContext, Preferences.CONFIG_FILE);
		this.context = mContext;
		initImageLoader(mContext);
		this.viewPager = mViewPager;
		this.dataList = mDataList;
		pw.setPointCount(dataList.size());
		viewPager.setOnPageChangeListener(onPageChangeListener);
		if (dataList != null && dataList.size() > 0) {
			if(dataList.size() < 3){
				while(dataList.size() < 3){
					dataList.addAll(dataList);
				}
			}
			list.clear();
			notifyDataSetChanged();
			for (int i = 0; i < dataList.size(); i++) {
				list.add(((Activity) context).getLayoutInflater().inflate(
						R.layout.bfgame_main_banner_item, null));
			}
			mScrollThread = new ScrollThread();
			mScrollThread.start();
			notifyDataSetChanged();
		}
		
		viewPager.setOnSingleTouchListener(new OnSingleTouchListener() {
			public void onSingleTouch() {
				if(dataList !=  null && dataList.size() > 0 && dataList.size() > viewPager.getCurrentItem()%dataList.size()){
					final GameInfo bean = dataList.get(viewPager.getCurrentItem()%dataList.size());
					if(bean != null){
						StatisticsUtil.addClickBanner(p, bean);
						GameDetailActivity.show(context, bean.getUri());
					}
				}
			}
		});
	}

	
	 public void resetList(List list){
         dataList.clear();
         dataList.addAll(list);
         notifyDataSetChanged();
     }
	 
	 
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		if (dataList != null && dataList.size() > 0) {
			return count;
		}
		return list.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		try {
			if (dataList != null && dataList.size() > 0) {
				position = position % dataList.size();
			}

			if(list.get(position).getParent() != null && ((View)list.get(position).getParent()).equals(container)){
				((ViewPager) container).removeView(list.get(position));
			}
			
			((ViewPager) container).addView(list.get(position));
			if (dataList != null && dataList.size() > 0) {
				final GameInfo bean = dataList.get(position);
				ImageView imgView = (ImageView) list.get(position).findViewById(R.id.item_image);
				mImageFetcher.loadImage(bean.getImageUrl(), imgView);
				imgView.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						StatisticsUtil.addClickBanner(p, bean);
						GameDetailActivity.show(context, bean.getUri());
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list.get(position);
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// if(productInfoList != null && productInfoList.size() > 0)
		// position = position % productInfoList.size();
		// ((ViewPager) container).removeView(list.get(position));
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}
	
	class ScrollThread extends Thread{
		@Override
		public void run() {
			try{
				while(isLoop){
					Thread.sleep(5*1000);
					handler.sendEmptyMessage(0);
					Thread.sleep(5*1000);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void onDestory(){
		isLoop = false;
	}
	
	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			pw.setPoint(arg0%pw.getPointCount());
			if(dataList != null && dataList.size() != 0){
				if (arg0 == dataList.size() - 1) {
					return;
				}
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
}
