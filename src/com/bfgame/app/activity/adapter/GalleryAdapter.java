package com.bfgame.app.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;

import com.bfgame.app.R;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.bitmap.util.ImageCache.ImageCacheParams;
import com.bfgame.app.bitmap.util.ImageFetcher;
import com.bfgame.app.vo.GameInfo;
import com.bfgame.app.widget.view.PointWidget;

public class GalleryAdapter extends PagerAdapter{
	private List<View> list = new ArrayList<View>();
	private List<GameInfo> dataList;
	protected ImageFetcher mImageFetcher;
	private int mImageThumbSize;
	private static final String IMAGE_CACHE_DIR = "thumbs";
//	private int count = Integer.MAX_VALUE;
	private PointWidget pw;
	
	private void initImageLoader(Context context) {
		mImageThumbSize = context.getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_big_size);
		mImageFetcher = new ImageFetcher(context, mImageThumbSize);
		mImageFetcher.setLoadingImage(R.drawable.bfgame_default_big_img);

		ImageCacheParams cacheParams = new ImageCacheParams((Activity) context,
				IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.5f); // Set memory cache to 25% of
													// app memory
		mImageFetcher.addImageCache(
				((BaseActivity) context).getSupportFragmentManager(),
				cacheParams);
	}

	public GalleryAdapter(Context context, List<GameInfo> dataList, ViewPager viewPager, PointWidget pw) {
		this.pw = pw;
		initImageLoader(context);
		this.dataList = dataList;
		pw.setPointCount(dataList.size());
		viewPager.setOnPageChangeListener(onPageChangeListener);
		if (dataList != null && dataList.size() > 0) {
			for (int i = 0; i < dataList.size(); i++) {
				list.add(((Activity) context).getLayoutInflater().inflate(
						R.layout.bfgame_main_banner_item, null));
			}
			notifyDataSetChanged();
		}
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
//		if (dataList != null && dataList.size() > 0) {
//			return count;
//		}
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
				mImageFetcher.loadImage(bean.getPreviewUri(), imgView);
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
	
	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			pw.setPoint(arg0%dataList.size());
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
