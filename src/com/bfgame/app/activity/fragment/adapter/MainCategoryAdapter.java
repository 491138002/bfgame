package com.bfgame.app.activity.fragment.adapter;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bfgame.app.R;
import com.bfgame.app.activity.CategoryActivity;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.base.FragmentBaseAdapter;
import com.bfgame.app.vo.Category;
/**
 * 分类adapter
 * @author admin
 *
 */
public class MainCategoryAdapter extends FragmentBaseAdapter {

	public MainCategoryAdapter(BaseActivity context, AbsListView listView,
			BaseFragment ft) {
		super(context, listView, ft);
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		
		if (CURRENT_TYPE == TYPE_IS_NULL) {
			return super.getView(position, convertView, parent);
		}

		Holder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = mInflater.inflate(R.layout.bfgame_main_category_item,
					null);
//			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
//					RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//			convertView.setLayoutParams(lp);
			holder = new Holder();
			holder.layout = (RelativeLayout) convertView
					.findViewById(R.id.layout);
			holder.item_icon_iv = (ImageView) holder.layout
					.findViewById(R.id.item_icon_iv);
			holder.item_name_tv = (TextView) holder.layout
					.findViewById(R.id.item_name_tv);
			holder.item_tag_tv = (TextView) holder.layout
					.findViewById(R.id.item_tag_tv);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		final Category bean = (Category) dataList.get(position);
		String str1 = "";
		String str2 = "";
		String str3 = "";
		holder.item_name_tv.setText(bean.getCategoryName());
		holder.item_tag_tv.setText("");
		if (bean.getSubCates() != null && bean.getSubCates().size() > 0) {
			str1 = bean.getSubCates().get(0).getSubCateName();
			if (bean.getSubCates().size() > 1)
				str2 = "|"+bean.getSubCates().get(1).getSubCateName();
			if (bean.getSubCates().size() > 2)
				str3 = "|"+bean.getSubCates().get(2).getSubCateName();
			holder.item_tag_tv.setText(Html.fromHtml(str1 + str2 + str3));
			

		}
		mImageFetcher.loadImage(bean.getIcon(), holder.item_icon_iv);
		holder.layout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Bundle data = new Bundle();
				data.putString("title", bean.getCategoryName());
				data.putString("more", bean.getUri());
				BaseActivity
						.showActivity(context, CategoryActivity.class, data);
			}
		});

		return convertView;
	}

	@Override
	public void loadData() {
	}

	class Holder {
		RelativeLayout layout;
		ImageView item_icon_iv;
		TextView item_name_tv;
		TextView item_tag_tv;
	}
}
