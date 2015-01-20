package com.bfgame.app.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bfgame.app.R;
import com.bfgame.app.activity.GameDetailActivity;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.bitmap.util.ImageFetcher;
import com.bfgame.app.download.DownloadUtil;
import com.bfgame.app.util.preference.Preferences;
import com.bfgame.app.util.preference.PreferencesUtils;
import com.bfgame.app.vo.GameInfo;

public class DialogUtil {
	private static Dialog dialog;
	private static GameInfo gameInfo;

	public interface OnAlertSelectId {
		void onClick(int whichButton, Object o);
	}

	/**
	 * 广告dialog
	 * 
	 * @param context
	 * @param view
	 * @return
	 */
	public static Dialog showAdDialog(final Activity context, final GameInfo gameInfo, ImageFetcher mImageFetcher, OnCancelListener cancelListener, final PreferencesUtils p) {
		DialogUtil.gameInfo = gameInfo;
		int bannerDefaultWidth = 458;
		int bannerDefaultHeight = 189;
		WindowManager window = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		window.getDefaultDisplay().getMetrics(dm);
		float bannerHeight = (float)dm.widthPixels / bannerDefaultWidth * bannerDefaultHeight;
		
		StatisticsUtil.addShowAd(p, gameInfo);
		View view = context.getLayoutInflater().inflate(R.layout.bfgame_home_ad_layout, null);
		view.setMinimumWidth(10000);
		
		ImageView ad_banner_iv = (ImageView) view.findViewById(R.id.ad_banner_iv);
		View item_layout = view.findViewById(R.id.item_layout);
		ImageView item_icon_iv = (ImageView) view.findViewById(R.id.item_icon_iv);
		TextView item_name_tv = (TextView) view.findViewById(R.id.item_name_tv);
		TextView item_download_number_tv = (TextView) view.findViewById(R.id.item_download_number_tv);
		TextView item_size_tv = (TextView) view.findViewById(R.id.item_size_tv);
		TextView item_version_tv = (TextView) view.findViewById(R.id.item_version_tv);
		final Button item_download_btn = (Button) view.findViewById(R.id.item_download_btn);
		ImageView item_close_iv = (ImageView) view.findViewById(R.id.item_close_iv);
		
		LinearLayout.LayoutParams bannerParams = (LinearLayout.LayoutParams)ad_banner_iv.getLayoutParams();
		bannerParams.height = (int)bannerHeight;//DeviceUtil.dip2px(bannerHeight, dm.scaledDensity);
		ad_banner_iv.setLayoutParams(bannerParams);
		
		dialog = new Dialog(context, R.style.bfgame_custom_dialog);
		dialog.setContentView(view);
		
		mImageFetcher.loadImage(gameInfo.getAdUri(), ad_banner_iv);
		mImageFetcher.loadImage(gameInfo.getIcon(), item_icon_iv);
		item_name_tv.setText(gameInfo.getName());
		item_download_number_tv.setText(gameInfo.getDownloadCount());
		item_size_tv.setText(gameInfo.getSize());
		item_version_tv.setText(gameInfo.getVersionName());

		item_download_btn.setText(DownloadUtil.getDownloadText(context, gameInfo.getId(), gameInfo.getPackageName(), gameInfo.getVersionCode(), gameInfo.getName()));
		item_download_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				StatisticsUtil.addClickAd(p, gameInfo);
				DownloadUtil.downloadClick(context, p, gameInfo.getId(),1,gameInfo.getCategoryId(), gameInfo.getName(), gameInfo.getDownloadUrl(), gameInfo.getPackageName(), gameInfo.getIcon(), gameInfo.getVersionCode(),gameInfo.getVersionName(),gameInfo.getDownloadNum(), gameInfo.getSize(), (Button)arg0);
			}
		});
		
		ad_banner_iv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				StatisticsUtil.addClickAd(p, gameInfo);
				GameDetailActivity.show(context, gameInfo.getUri());
			}
		});
		
		item_layout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				StatisticsUtil.addClickAd(p, gameInfo);
				GameDetailActivity.show(context, gameInfo.getUri());
			}
		});
		
		item_close_iv.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				dialog.cancel();
			}
		});
		
		dialog.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface arg0) {
				DialogUtil.gameInfo = null;
			}
		});

		dialog.setCanceledOnTouchOutside(false);
		if (cancelListener != null) {
			dialog.setOnCancelListener(cancelListener);
		}
		
		p.putString(Preferences.AD_SHOW_TIME, DateUtils.getDate());
		
		return dialog;
	}
	
	/**
	 * 无网络Dilaog
	 * @param context
	 */
	public static void showSettingWIFIDialog(final Context context) {
		if(dialog != null && dialog.isShowing()){
			return;
		}
		final Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.bfgame_not_network_title);
		builder.setMessage(R.string.bfgame_not_network_content);
		dialog = builder.setPositiveButton(R.string.bfgame_btn_submit, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
				context.startActivity(intent);
			}
		}).setNeutralButton(R.string.bfgame_btn_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if(context instanceof BaseActivity && !"MainActivity".equals(context.getClass().getSimpleName())){
					((BaseActivity)context).goBack();
				}
				dialog.cancel();
			}
		}).create();
		dialog.show();
		
	}
	
	/**
	 * 3g网络情况下下载提示
	 * @param context
	 */
	public static void show3GDwonlaodialog(final Context context, final Object bean, int type, final OnAlertSelectId onSwitchListener) {
		if(dialog != null && dialog.isShowing()){
			return;
		}
		
		String typeName = "";
		switch(type){
		case 0:
			typeName = "无";
			break;
		case 2:
			typeName = "3G";
			break;
		case 3:
			typeName = "2G";
			break;
		case 4:
			typeName = "4G";
			break;
		}
		
		final Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.bfgame_not_3g_network_title);
		builder.setMessage(context.getString(R.string.bfgame_not_3g_network_content, typeName));
		dialog = builder.setPositiveButton(R.string.bfgame_btn_submit, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		}).create();
		dialog.show();
	}
	
	public static void setAdDownloadState(Context context){
		if(dialog != null && dialog.isShowing() && gameInfo != null){
			Button item_download_btn = (Button) dialog.findViewById(R.id.item_download_btn);
			item_download_btn.setText(DownloadUtil.getDownloadText(context, gameInfo.getId(), gameInfo.getPackageName(), gameInfo.getVersionCode(), gameInfo.getName()));
			item_download_btn.invalidate();
		}
	}
}
