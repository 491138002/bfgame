package com.bfgame.app.activity.adapter;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bfgame.app.R;
import com.bfgame.app.activity.GameDetailActivity;
import com.bfgame.app.activity.SearchActivity;
import com.bfgame.app.base.BFBaseAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.download.DownloadUtil;
import com.bfgame.app.util.AnimationUtil;
import com.bfgame.app.vo.GameInfo;

public class SearchAdapter extends BFBaseAdapter{
	
	public SearchAdapter(BaseActivity context, AbsListView listView) {
		super(context, listView);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(CURRENT_TYPE == TYPE_IS_NULL){
			return super.getView(position, convertView, parent);
		}
		
		final Holder holder;
		if(convertView == null || convertView.getTag() == null){
			convertView = mInflater.inflate(R.layout.bfgame_main_item, null);
			
			holder = new Holder();
			holder.item_icon_iv = (ImageView) convertView.findViewById(R.id.item_icon_iv);
//			holder.item_hot_iv = convertView.findViewById(R.id.item_hot_iv);
			holder.item_info=(TextView) convertView.findViewById(R.id.item_info_tv);
			holder.item_name_tv = (TextView) convertView.findViewById(R.id.item_name_tv);
//			holder.item_download_number_tv = (TextView) convertView.findViewById(R.id.item_download_number_tv);
//			holder.item_size_tv = (TextView) convertView.findViewById(R.id.item_size_tv);
//			holder.item_version_tv = (TextView) convertView.findViewById(R.id.item_version_tv);
			holder.item_download_btn = (Button) convertView.findViewById(R.id.item_download_btn);
//			holder.item_tag_iv_1 = convertView.findViewById(R.id.item_tag_iv_1);
//			holder.item_tag_iv_2 = convertView.findViewById(R.id.item_tag_iv_2);
			holder.item_desc_tv = (TextView) convertView.findViewById(R.id.item_desc_tv);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		final GameInfo bean = (GameInfo) dataList.get(position);
		holder.item_name_tv.setText(bean.getName());
//		holder.item_download_number_tv.setText(bean.getDownloadCount());
//		holder.item_size_tv.setText(bean.getSize());
//		holder.item_version_tv.setText(bean.getVersionName());
		holder.item_info.setText(bean.getDownloadNum()+"  "+bean.getSize()+"  "+bean.getVersionName());
		holder.item_desc_tv.setText(bean.getSmallDes());
		
//		if(checkTag(bean.getIsHot())){
//			holder.item_hot_iv.setVisibility(View.INVISIBLE);
//		}else{
//			holder.item_hot_iv.setVisibility(View.GONE);
//		}
//		
//		if(checkTag(bean.getIsFeature())){
//			holder.item_tag_iv_1.setVisibility(View.VISIBLE);
//		}else{
//			holder.item_tag_iv_1.setVisibility(View.GONE);
//		}
//		
//		if(checkTag(bean.getIsNew())){
//			holder.item_tag_iv_2.setVisibility(View.VISIBLE);
//		}else{
//			holder.item_tag_iv_2.setVisibility(View.GONE);
//		}
		
		mImageFetcher.loadImage(bean.getIcon(), holder.item_icon_iv);
		
		holder.item_download_btn.setText(DownloadUtil.getDownloadText(context, bean.getId(), bean.getPackageName(), bean.getVersionCode(), bean.getName()));
		holder.item_download_btn.setBackgroundColor(Color.parseColor(DownloadUtil.getTextBg(context, bean.getId(), bean.getPackageName(), bean.getVersionCode(), bean.getName())));
		holder.item_download_btn.setOnClickListener(new OnClickListener() {
			public void onClick(final View arg0) {
				DownloadUtil.downloadClick(context, preferencesUtils,
						bean.getId(),-7,bean.getCategoryId(), bean.getName(), bean.getDownloadUri(),
						bean.getPackageName(), bean.getIcon(),
						bean.getVersionCode(), bean.getVersionName(),
						bean.getDownloadNum(), bean.getSize(), (Button) arg0);

			}
		});
		
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				GameDetailActivity.show(context, bean.getUri());
			}
		});
		
		return convertView;
	}
	
	@Override
	public void loadData() {
		if(!isLoadingData && dataList.size() > 0){
			isLoadingData = true;
			((SearchActivity)context).nextPage();
		}
	}
	
	class Holder {
		ImageView item_icon_iv;
//		View item_hot_iv;
		TextView item_name_tv;
//		TextView item_download_number_tv;
		
		TextView item_info;
//		TextView item_size_tv;
//		TextView item_version_tv;
		Button item_download_btn;
//		RelativeLayout item_bottom_layout;
//		View item_tag_iv_1;
//		View item_tag_iv_2;
		TextView item_desc_tv;
	}
}
