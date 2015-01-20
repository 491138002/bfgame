package com.bfgame.app.util;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Gallery;

public class ViewUtil {

	public static void alignGalleryToLeft(View parentView, Gallery gallery, int itemWidth, int spacing) {
		int galleryWidth = parentView.getWidth();// 得到Parent控件的宽度
		// 在这边我们必须先从资源尺寸中得到子控件的宽度跟间距，因为:
		// 1. 在运行时，我们无法得到间距(因为Gallery这个类，没有这样的权限)
		// 2.有可能在运行得宽度的时候，item资源还没有准备好。
		int offset = 0;
		if (galleryWidth <= itemWidth) {
			offset = galleryWidth / 2 - itemWidth / 2 - spacing;
		} else {
			offset = galleryWidth - itemWidth - 2 * spacing;
		}

		// 现在就可以根据更新的布局参数设置做对其了。
		MarginLayoutParams mlp = (MarginLayoutParams) gallery.getLayoutParams();
		mlp.setMargins(-offset, mlp.topMargin, mlp.rightMargin,
				mlp.bottomMargin);
	}

}
