package com.bfgame.app.activity.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bfgame.app.R;
import com.bfgame.app.activity.GameDetailActivity;
import com.bfgame.app.base.BFBaseAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.vo.GameInfo;
import com.bfgame.app.widget.view.HorizontialListView;

public class GameDetailRecommendAdapter extends BFBaseAdapter{
	
	public GameDetailRecommendAdapter(BaseActivity context, HorizontialListView listView) {
		super(context, null);
	}
	
	@Override
	public int getCount() {
		return dataList.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Holder holder = null;
		if(convertView == null || convertView.getTag() == null){
			convertView = mInflater.inflate(R.layout.bfgame_detail_recommend_item, null);
			
			holder = new Holder();
			holder.item_icon_iv = (ImageView) convertView.findViewById(R.id.item_icon_iv);
			holder.item_name_tv = (TextView) convertView.findViewById(R.id.item_name_tv);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		final GameInfo bean = (GameInfo) dataList.get(position);
		
		mImageFetcher.loadImage(bean.getRecomIcon(), holder.item_icon_iv);
		holder.item_name_tv.setText(bean.getRecomName());
		
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				GameDetailActivity.show(context, bean.getRecomUri());
			}
		});
		
		return convertView;
	}
	
	@Override
	public void loadData() {
	}
	
	class Holder {
		ImageView item_icon_iv;
		TextView item_name_tv;
	}
}
