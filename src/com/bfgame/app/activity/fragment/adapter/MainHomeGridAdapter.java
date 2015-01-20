package com.bfgame.app.activity.fragment.adapter;

import java.util.List;

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
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.base.FragmentBaseAdapter;
import com.bfgame.app.download.DownloadUtil;
import com.bfgame.app.util.AnimationUtil;
import com.bfgame.app.vo.GameInfo;

public class MainHomeGridAdapter extends FragmentBaseAdapter {

	public MainHomeGridAdapter(BaseActivity context, AbsListView listView,
			BaseFragment ft, @SuppressWarnings("rawtypes") List dataList) {
		super(context, listView, ft);
		this.dataList = dataList;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

//		if (CURRENT_TYPE == TYPE_IS_NULL) {
//			return super.getView(position, convertView, parent);
//		}

		final Holder holder;
		if (convertView == null || convertView.getTag() == null) {
			convertView = mInflater.inflate(
					R.layout.bfgame_main_home_grid_item, null);

			holder = new Holder();
			holder.item_icon_iv = (ImageView) convertView
					.findViewById(R.id.item_icon_iv);
			holder.item_hot_iv = (ImageView) convertView
					.findViewById(R.id.item_hot_iv);
			holder.item_name_tv = (TextView) convertView
					.findViewById(R.id.item_name_tv);
			holder.item_size_tv = (TextView) convertView.findViewById(R.id.item_size_tv);
			holder.item_category_tv = (TextView) convertView.findViewById(R.id.item_category_tv);
			holder.item_download_btn = (Button) convertView
					.findViewById(R.id.item_download_btn);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		final GameInfo bean = (GameInfo) dataList.get(position);

		holder.item_name_tv.setText(bean.getName());
		holder.item_size_tv.setText(bean.getSize());
		holder.item_category_tv.setText(bean.getCategoryName());
		if (checkTag(bean.getIsNew())) {
			holder.item_hot_iv.setVisibility(View.VISIBLE);
			holder.item_hot_iv.setBackgroundResource(R.drawable.xinyou);
		} else if (checkTag(bean.getIsHot())) {
			holder.item_hot_iv.setVisibility(View.VISIBLE);
			holder.item_hot_iv.setBackgroundResource(R.drawable.jinping);
		} else if (checkTag(bean.getIsFeature())) {
			holder.item_hot_iv.setVisibility(View.VISIBLE);
			holder.item_hot_iv.setBackgroundResource(R.drawable.shoufa);
		}else {
			holder.item_hot_iv.setVisibility(View.INVISIBLE);
		}
		mImageFetcher.loadImage(bean.getIcon(), holder.item_icon_iv);
		holder.item_download_btn.setText(DownloadUtil.getDownloadText(context,
				bean.getAppId(), bean.getPackageName(), bean.getVersionCode(),
				bean.getName()));

		holder.item_download_btn.setBackgroundColor(Color
				.parseColor(DownloadUtil.getTextBg(context, bean.getAppId(),
						bean.getPackageName(), bean.getVersionCode(),
						bean.getName())));
		holder.item_download_btn.setOnClickListener(new OnClickListener() {
			public void onClick(final View arg0) {
				if (holder.item_download_btn.getText().equals("下载")) {

					AnimationUtil.startDownLoadAnim(context,
							holder.item_icon_iv, null);
					new Handler().postDelayed(new Runnable() {

						public void run() {

							DownloadUtil.downloadClick(context,
									preferencesUtils, bean.getAppId(),-1,bean.getCategoryId(),
									bean.getName(), bean.getDownloadUri(),
									bean.getPackageName(), bean.getIcon(),
									bean.getVersionCode(),
									bean.getVersionName(),
									bean.getDownloadNum(), bean.getSize(),
									(Button) arg0);
						}

					}, 500);
				}

				DownloadUtil.downloadClick(context, preferencesUtils,
						bean.getAppId(),-1,bean.getCategoryId(), bean.getName(), bean.getDownloadUri(),
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

	class Holder {
		ImageView item_icon_iv;
		ImageView item_hot_iv;
		TextView item_name_tv;
		TextView item_category_tv;
		TextView item_size_tv;
		Button item_download_btn;
	}

	@Override
	public void loadData() {

	}
}
