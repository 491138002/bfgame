package com.bfgame.app.activity.fragment.adapter;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bfgame.app.R;
import com.bfgame.app.activity.GameDetailActivity;
import com.bfgame.app.activity.fragment.MainSingleFragment;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.base.FragmentBaseAdapter;
import com.bfgame.app.download.DownloadUtil;
import com.bfgame.app.util.AnimationUtil;
import com.bfgame.app.vo.GameInfo;

public class MainSingleAdapter extends FragmentBaseAdapter{
	
	public MainSingleAdapter(BaseActivity context, AbsListView listView, BaseFragment ft) {
		super(context, listView, ft);
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
			holder.item_hot_iv = convertView.findViewById(R.id.item_hot_iv);
			holder.item_name_tv = (TextView) convertView.findViewById(R.id.item_name_tv);
			holder.item_info_tv=(TextView) convertView.findViewById(R.id.item_info_tv);
			
			holder.item_download_btn = (Button) convertView.findViewById(R.id.item_download_btn);
			holder.item_desc_tv = (TextView) convertView.findViewById(R.id.item_desc_tv);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		final GameInfo bean = (GameInfo) dataList.get(position);
		
		holder.item_name_tv.setText(bean.getName());
		String str="月下载量："+bean.getMonthDownNum()+"|大小："+bean.getSize();
		holder.item_info_tv.setText(str);
		holder.item_desc_tv.setText(bean.getSmallDes());
		
		mImageFetcher.loadImage(bean.getIcon(), holder.item_icon_iv);
		
		holder.item_download_btn.setText(DownloadUtil.getDownloadText(context, bean.getId(), bean.getPackageName(), bean.getVersionCode(), bean.getName()));
		holder.item_download_btn.setBackgroundColor(Color.parseColor(DownloadUtil.getTextBg(context, bean.getId(), bean.getPackageName(), bean.getVersionCode(), bean.getName())));
		holder.item_download_btn.setOnClickListener(new OnClickListener() {
			public void onClick(final View arg0) {
				if (holder.item_download_btn.getText().equals("下载")) {

					AnimationUtil.startDownLoadAnim(context,
							holder.item_icon_iv, null);
					new Handler().postDelayed(new Runnable() {

						public void run() {

							DownloadUtil.downloadClick(context,
									preferencesUtils, bean.getId(),-6,bean.getCategoryId(),
									bean.getName(), bean.getDownloadUrl(),
									bean.getPackageName(), bean.getIcon(),
									bean.getVersionCode(),
									bean.getVersionName(),
									bean.getDownloadNum(), bean.getSize(),
									(Button) arg0);
						}

					}, 500);
				}

				DownloadUtil.downloadClick(context, preferencesUtils,
						bean.getId(),-6,bean.getCategoryId(), bean.getName(), bean.getDownloadUrl(),
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
			((MainSingleFragment)ft).nextPage();
		}
	}
	
	class Holder {
		ImageView item_icon_iv;
		View item_hot_iv;
		TextView item_name_tv;
		TextView item_info_tv;
		Button item_download_btn;
		RelativeLayout item_bottom_layout;
		TextView item_desc_tv;
	}
}
