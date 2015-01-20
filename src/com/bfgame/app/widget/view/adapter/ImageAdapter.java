package com.bfgame.app.widget.view.adapter;

import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bfgame.app.R;
import com.bfgame.app.activity.GalleryActivity;
import com.bfgame.app.base.BFBaseAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.util.DeviceUtil;
import com.bfgame.app.vo.GameInfo;
import com.bfgame.app.widget.view.HorizontialListView;
/**
 * 游戏截图轮播的adpter
 * @author admin
 *
 */
public class ImageAdapter extends BFBaseAdapter {
	private int imageWidth = 240;
	private int imageHeight = 0;
	private LayoutParams params;
	private Display display;
	
	public ImageAdapter(BaseActivity context, HorizontialListView gallery, int height) {
		super(context, null);
		imageHeight = height;
		display = context.getWindowManager().getDefaultDisplay();
		imageWidth = (display.getWidth() - DeviceUtil.dip2px(context, 35)) / 2;
		params = new LayoutParams(imageWidth, height);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size() == 0 ? 1: dataList.size();
	}
	
	@SuppressWarnings("deprecation")
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if(dataList.size() == 0){
			convertView = mInflater.inflate(R.layout.bfgame_list_is_null, null);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.height = imageHeight;
			params.width = display.getWidth() - DeviceUtil.dip2px(context, 20);
//			convertView.setLayoutParams(params);
			
			TextView nullText = (TextView) convertView.findViewById(R.id.null_text);
			nullText.setLayoutParams(params);
			nullText.setText(context.getString(R.string.bfgame_no_app_pics, ""));
			return convertView;
		}
		
		GameInfo bean = (GameInfo)dataList.get(position);
		Holder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.bfgame_detail_gallery_item, null);
			
			holder = new Holder();
			holder.item_icon_iv = (ImageView) convertView.findViewById(R.id.item_icon_iv);
			holder.item_icon_iv.setLayoutParams(params);
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}
		
		mImageBigFetcher.loadImage(bean.getPreviewUri(), holder.item_icon_iv);
		
		holder.item_icon_iv.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			public void onClick(View arg0) {
				GalleryActivity.show(context, dataList, position%dataList.size());
			}
		});
		
		return convertView;
	}


	@Override
	public void loadData() {
		
	}
	
	class Holder {
		ImageView item_icon_iv;
	}
}
