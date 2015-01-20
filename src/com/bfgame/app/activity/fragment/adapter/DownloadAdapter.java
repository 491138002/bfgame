package com.bfgame.app.activity.fragment.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bfgame.app.R;
import com.bfgame.app.activity.fragment.OnlineGiftFragment;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.base.FragmentBaseAdapter;
import com.bfgame.app.util.NumberUtils;
import com.bfgame.app.util.StringUtil;
import com.bfgame.app.vo.Gift;
/**
 * 网游礼包adapter
 * @author admin
 *
 */
public class DownloadAdapter extends FragmentBaseAdapter {


	public DownloadAdapter(BaseActivity context, AbsListView listView,
			BaseFragment ft) {
		super(context, listView, ft);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (CURRENT_TYPE == TYPE_IS_NULL) {
			return super.getView(position, convertView, parent);
		}

		final Holder holder;
		if (convertView == null || convertView.getTag() == null) {
			convertView = mInflater.inflate(R.layout.bfgame_fragment_gift_item,
					null);

			holder = new Holder();
			holder.item_top_layout = convertView
					.findViewById(R.id.item_top_layout);
			holder.pbBar = (ProgressBar) convertView
					.findViewById(R.id.MyProgressBar);
			holder.item_icon_iv = (ImageView) convertView
					.findViewById(R.id.item_icon_iv);
			holder.item_name_tv = (TextView) convertView
					.findViewById(R.id.item_name_tv);
			holder.item_size_tv = (TextView) convertView
					.findViewById(R.id.item_size_tv);
			holder.item_des = (TextView) convertView
					.findViewById(R.id.item_gift_des_tv);
			holder.item_get_btn = (Button) convertView
					.findViewById(R.id.gift_get_btn);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		final Gift bean = (Gift) dataList.get(position);
		mImageFetcher.loadImage(bean.getIcon(), holder.item_icon_iv);
		holder.item_name_tv.setText(bean.getName());
		
		String str = NumberUtils.getResult(bean.getGiftRemain(),
				bean.getGiftCount());
		if (StringUtil.isEmpty(str)) {
			str = "0";
		}

		holder.item_size_tv.setText("剩余" + str + "%");
		holder.pbBar.setProgress(Integer.parseInt(str));
		holder.item_des.setText(bean.getGiftExplain());
		holder.item_get_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				holder.item_get_btn
						.setText(R.string.bfgame_gift_receive_loading);
			}
		});
		return convertView;
	}
	
	
	
	
	class Holder {
		View item_top_layout;
		ImageView item_icon_iv;
		TextView item_name_tv;
		TextView item_size_tv;
		TextView item_des;
		ProgressBar pbBar;
		Button item_get_btn;
		LinearLayout item_bottom_layout;
	}

	@Override
	public void loadData() {
		if(!isLoadingData && dataList.size() > 0){
			isLoadingData = true;
			((OnlineGiftFragment)ft).nextPage();
		}
	}

}
