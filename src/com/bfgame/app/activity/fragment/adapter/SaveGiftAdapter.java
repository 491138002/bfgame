package com.bfgame.app.activity.fragment.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bfgame.app.R;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.base.FragmentBaseAdapter;
import com.bfgame.app.util.DeviceUtil;
import com.bfgame.app.vo.Gift;

public class SaveGiftAdapter extends FragmentBaseAdapter{
	
	
	public SaveGiftAdapter(BaseActivity context, AbsListView listView,
			BaseFragment ft) {
		super(context, listView, ft);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(CURRENT_TYPE == TYPE_IS_NULL){
			return super.getView(position, convertView, parent);
		}
		
		
		final Holder holder;
		if(convertView == null || convertView.getTag() == null){
			convertView = mInflater.inflate(R.layout.bfgame_fragment_save_gift_item, null);
			
			holder = new Holder();
			holder.item_top_layout = convertView.findViewById(R.id.item_top_layout);
			holder.item_icon_iv = (ImageView) convertView.findViewById(R.id.item_icon_iv);
			holder.item_name_tv = (TextView) convertView.findViewById(R.id.item_name_tv);
			holder.item_gift_des_tv = (TextView) convertView.findViewById(R.id.item_gift_des_tv);
			holder.gift_copy_btn = (Button) convertView.findViewById(R.id.gift_copy_btn);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		final Gift bean = (Gift) dataList.get(position);
		mImageFetcher.loadImage(bean.getIcon(), holder.item_icon_iv);
		holder.item_name_tv.setText(bean.getName());
		holder.item_gift_des_tv.setText(bean.getGiftExplain());
		holder.gift_copy_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				DeviceUtil.copyToClipboard(context, bean.getActivationCode());
				showToast(context.getString(R.string.bfgame_toast_copy));
			}
		});
//		holder.item_top_layout.setOnClickListener(new OnClickListener() {
//			public void onClick(View arg0) {
//				GameDetailActivity.show(context, bean.getUri());
////				showLoadingDialog();
//			}
//		});
		
		return convertView;
	}
	class Holder {
		View item_top_layout;
		ImageView item_icon_iv;
		TextView item_name_tv;
		TextView item_gift_des_tv;
		Button gift_copy_btn;
		LinearLayout item_bottom_layout;
	}
	@Override
	public void loadData() {
		
		
	}
}

