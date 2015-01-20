package com.bfgame.app.activity.adapter;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bfgame.app.R;
import com.bfgame.app.activity.GameDetailActivity;
import com.bfgame.app.activity.SearchActivity;
import com.bfgame.app.base.BFBaseAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.util.DeviceUtil;
import com.bfgame.app.util.StatisticsUtil;
import com.bfgame.app.vo.SearchKey;

public class SearchKeyAdapter extends BFBaseAdapter{

	LinearLayout.LayoutParams keyTvParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
	
	public SearchKeyAdapter(BaseActivity context, AbsListView listView) {
		super(context, listView);
		
		keyTvParams.setMargins(DeviceUtil.dip2px(context, 5), DeviceUtil.dip2px(context, 5), 0, 0);
		keyTvParams.weight = 1;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		final SearchKey bean = (SearchKey) dataList.get(position);
		
		int count = bean.getFeature().size()%2 > 0 ? bean.getFeature().size()/2+1 : bean.getFeature().size()/2;
		
		//set feature app
		for (int i = 0; i < count; i++) {
			LinearLayout childLayout = (LinearLayout) mInflater.inflate(R.layout.bfgame_search_item, null);
			for (int j = 0; j < childLayout.getChildCount(); j++) {
				if(i*2+j < bean.getFeature().size()){
					childLayout.getChildAt(j).setVisibility(View.VISIBLE);
					ImageView item_icon_iv = (ImageView) childLayout.getChildAt(j).findViewById(R.id.item_icon_iv);
					TextView item_name_tv = (TextView) childLayout.getChildAt(j).findViewById(R.id.item_name_tv);
					TextView item_download_number_tv = (TextView) childLayout.getChildAt(j).findViewById(R.id.item_download_number_tv);
					
					final int curIndex = i*2+j;
					mImageFetcher.loadImage(bean.getFeature().get(curIndex).getIcon(), item_icon_iv);
					item_name_tv.setText(bean.getFeature().get(curIndex).getName());
					item_download_number_tv.setText(bean.getFeature().get(curIndex).getDownloadCount());
					childLayout.getChildAt(j).setOnClickListener(new OnClickListener() {
						public void onClick(View arg0) {
							StatisticsUtil.addClickKey(preferencesUtils, bean.getFeature().get(curIndex).getKeyId());
							GameDetailActivity.show(context, bean.getFeature().get(curIndex).getUri());
						}
					});
				}else{
					childLayout.getChildAt(j).setVisibility(View.INVISIBLE);
				}
			}
			layout.addView(childLayout);
		}
		
		//set key
		LinearLayout childLayout = null;
		for (int i = 0; i < bean.getKeys().size(); i++) {
			final SearchKey childBean = bean.getKeys().get(i);
			if(i % 4 == 0){
				childLayout = new LinearLayout(context);
				childLayout.setPadding(0, 0, DeviceUtil.dip2px(context, 5), 0);
				childLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				layout.addView(childLayout);
			}
			
			TextView item_key_tv = (TextView) mInflater.inflate(R.layout.bfgame_search_key_item, null);
			item_key_tv.setText(childBean.getKeyName());
			item_key_tv.setBackgroundColor(Color.parseColor(childBean.getKeyColor()));
			item_key_tv.setLayoutParams(keyTvParams);
			item_key_tv.setGravity(Gravity.CENTER);
			item_key_tv.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					((SearchActivity)context).getSearchResult(childBean.getKeyName());
				}
			});
			childLayout.addView(item_key_tv);
			
			if(i == bean.getKeys().size() - 1){
				for (int j = 0; bean.getKeys().size() % 4 != 0 && j < 4 - bean.getKeys().size() % 4; j++) {
					item_key_tv = (TextView) mInflater.inflate(R.layout.bfgame_search_key_item, null);
					item_key_tv.setVisibility(View.INVISIBLE);
					item_key_tv.setLayoutParams(keyTvParams);
					item_key_tv.setOnClickListener(new OnClickListener() {
						public void onClick(View arg0) {
							
						}
					});
					childLayout.addView(item_key_tv);
				}
			}
			
		}
		
		return layout;
	}

	@Override
	public void loadData() {
		
	}

}
