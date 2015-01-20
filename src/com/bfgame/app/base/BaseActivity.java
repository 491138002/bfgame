package com.bfgame.app.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.bfgame.app.R;
import com.bfgame.app.activity.DownloadActivity;
import com.bfgame.app.activity.GameGiftActivity;
import com.bfgame.app.activity.SearchActivity;
import com.bfgame.app.bitmap.util.ImageCache.ImageCacheParams;
import com.bfgame.app.bitmap.util.ImageFetcher;
import com.bfgame.app.common.Constants;
import com.bfgame.app.download.DownloadUtil;
import com.bfgame.app.net.AsyncHttpGet;
import com.bfgame.app.net.AsyncHttpPost;
import com.bfgame.app.net.BaseRequest;
import com.bfgame.app.net.DefaultThreadPool;
import com.bfgame.app.net.ThreadCallBack;
import com.bfgame.app.net.utils.CheckNetWorkUtil;
import com.bfgame.app.net.utils.RequestParameter;
import com.bfgame.app.util.DeviceUtil;
import com.bfgame.app.util.LogUtil;
import com.bfgame.app.util.StringUtil;
import com.bfgame.app.util.preference.Preferences;
import com.bfgame.app.util.preference.PreferencesUtils;
import com.bfgame.app.widget.dialog.CustomLoadingDialog;

/**
 * 
 * @author jzy
 * 
 */
public abstract class BaseActivity extends FragmentActivity implements
		ThreadCallBack, View.OnClickListener {
	private static final long serialVersionUID = 529729636490390130L;

	/**
	 * 上下文
	 */
	protected BaseActivity context = this;

	/**
	 * 页面加载view
	 */
	protected View loadView;
	/**
	 * 底部上拉加载view
	 */
	protected View footView;
	/**
	 * 底部暴风logo view
	 */
	protected View bottomlogo;
	
	/**
	 * 当前activity所持有的所有请求
	 */
	List<BaseRequest> requestList = null;

	/**
	 * 主线程Handler
	 */
	protected Handler handler = new Handler();

	/**
	 * 图片下载
	 */
	protected ImageFetcher mImageFetcher;
	protected ImageFetcher mMaxImageFetcher;
	private int mImageThumbSize;
	private int mMaxImageThumbSize;

	/**
	 * 图片缓存路径
	 */
	private final String IMAGE_CACHE_DIR = "thumbs";

	/**
	 * 加载提示框
	 */
	 public CustomLoadingDialog loadingDialog;

	/**
	 * Activity 前缀
	 */
	public static final String FIRST_ACTION = "com.bfgame.app.";

	/**
	 * 配置文件信息
	 */
	public PreferencesUtils preferencesUtils;

	/**
	 * 访问成功tag
	 */
	public final int SUCCESS = 1;

	/**
	 * 下载状态更新广播接收器
	 */
	private DownloadStateUpdateReceiver downloadStateUpdateReceiver;

	
	protected static boolean isVisible = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestList = new ArrayList<BaseRequest>();
		super.onCreate(savedInstanceState);

		preferencesUtils = new PreferencesUtils(context,
				Preferences.CONFIG_FILE);
		downloadStateUpdateReceiver = new DownloadStateUpdateReceiver();

		initLayout();
		init();
		initImageLoader();
		initView();
		initListener();
		initValue();
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadUtil.DOWNLOAD_STATE_UPDATE_ACTION);
		filter.addAction(DownloadUtil.DOWNLOAD_UPDATE_RED_DOT_ACTION);
		registerReceiver(downloadStateUpdateReceiver, filter);
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	
	public String getVerName(){
		return DeviceUtil.getVersionName(context);
	}
	@Override
	public void onResume() {
		super.onResume();
		View v = findViewById(R.id.title_new_iv);
		if (v != null) {
			v.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
		}
		View view = findViewById(R.id.title_gift_new_iv);
		if (view != null)
			view.setVisibility(Constants.NEED_TIP_GIFT ? View.VISIBLE
					: View.INVISIBLE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		/**
		 * 在activity销毁的时候同时设置停止请求，停止线程请求回调
		 */
		// cancelRequest();
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		unregisterReceiver(downloadStateUpdateReceiver);
		/**
		 * 在activity销毁的时候同时设置停止请求，停止线程请求回调
		 */
		cancelRequest();
		super.onDestroy();
	}

	public void cancelRequest() {
		if (requestList != null && requestList.size() > 0) {
			for (BaseRequest request : requestList) {
				if (request.getRequest() != null) {
					try {
						request.getRequest().abort();
						requestList.remove(request.getRequest());

						// Log.d("netlib", "netlib ,onDestroy request to  "
						// + request.getRequest().getURI()
						// + "  is removed");
					} catch (UnsupportedOperationException e) {
						// do nothing .
					}
				}
			}
		}
	}

	@Override
	public void onCallbackFromThread(String resultJson) {

	}

	protected void showToast(String message) {
		Toast toast = Toast.makeText(this,
				(!StringUtil.isEmpty(message)) ? message : "",
				Toast.LENGTH_SHORT);
		toast.show();
	}

	public void startHttpRequst(String requestType, String url,
			List<RequestParameter> parameter, boolean isShowLoadingDialog,
			String loadingDialogContent) {
		startHttpRequst(requestType, url, parameter, isShowLoadingDialog,
				loadingDialogContent, true, Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT);
	}

	protected void startHttpRequst(String requestType, String url,
			List<RequestParameter> parameter, boolean isShowLoadingDialog,
			String loadingDialogContent, boolean isHideCloseBtn,
			int connectTimeout, int readTimeout) {
		startHttpRequst(requestType, url, parameter, isShowLoadingDialog,
				loadingDialogContent, isHideCloseBtn, connectTimeout,
				readTimeout, -1);
	}

	public void startHttpRequst(String requestType, String url,
			List<RequestParameter> parameter, boolean isShowLoadingDialog,
			String loadingDialogContent, boolean isHideCloseBtn,
			int connectTimeout, int readTimeout, int resultCode) {

		if (!CheckNetWorkUtil.checkNetWork(this)) {
			return;
		}

		if (null != parameter) {
			parameter.add(new RequestParameter("channel", "android"));
//			 parameter.add(new RequestParameter("imei", Constants.IMEI));
			 parameter.add(new RequestParameter("ver",
			 DeviceUtil.getVersionName(context)));
		}
		for (int i = 0; i < parameter.size(); i++) {
			RequestParameter requestParameter = parameter.get(i);
			LogUtil.d("requestParameter", requestParameter.getName() + "="
					+ requestParameter.getValue());
		}
		BaseRequest httpRequest = null;
		if ("POST".equalsIgnoreCase(requestType)) {
			httpRequest = new AsyncHttpPost(this, url, parameter,
					isShowLoadingDialog, loadingDialogContent, isHideCloseBtn,
					connectTimeout, readTimeout, resultCode);
		} else {
			httpRequest = new AsyncHttpGet(this, url, parameter,
					isShowLoadingDialog, loadingDialogContent, isHideCloseBtn,
					connectTimeout, readTimeout, resultCode, this);
		}
		DefaultThreadPool.getInstance().execute(httpRequest);
		this.requestList.add(httpRequest);
	}

	public static void showActivity(Context context, Class<?> c) {
		String action = getActivityAction(c);

		LogUtil.d("==================", action);
		showActivity(context, action, null);
	}

	public static void showActivity(Context context, Class<?> c, String info) {
		String action = getActivityAction(c);
		Bundle data = new Bundle();
		data.putString("info", info);
		showActivity(context, action, data);
	}

	public static void showActivity(Context context, Class<?> c, Bundle data) {
		String action = getActivityAction(c);
		if (!StringUtil.isEmpty(action)) {
			showActivity(context, action, data);
		}
	}

	public static void showActivity(Context context, String action) {
		showActivity(context, action, null);
	}

	public static void showActivity(Context context, String action, Bundle data) {
		if (data == null)
			data = new Bundle();
		data.putString("action", action);
		data.putString("pAction", getActivityAction(context.getClass()));
		Intent intent = new Intent();
		intent.setAction(action);
		intent.putExtras(data);
		context.startActivity(intent);
		if (context instanceof Activity)
			((Activity) context).overridePendingTransition(
					R.anim.bfgame_activity_right_in,
					R.anim.bfgame_activity_left_out);
	}

	public static void showPreviousActivity(Context context, Class<?> c) {
		String action = getActivityAction(c);
		showPreviousActivity(context, action, null);
	}

	public static void showPreviousActivity(Context context, Class<?> c,
			Bundle data) {
		String action = getActivityAction(c);
		showPreviousActivity(context, action, data);
	}

	public static void showPreviousActivity(Context context, String action) {
		showPreviousActivity(context, action, null);
	}

	public static void showPreviousActivity(Context context, String action,
			Bundle data) {
		if (action == null) {
			if (context instanceof Activity)
				((Activity) context).finish();
		} else {
			if (data == null)
				data = new Bundle();
			Intent intent = new Intent();
			intent.setAction(action);
			intent.putExtras(data);
			context.startActivity(intent);
		}
		if (context instanceof Activity) {
			((Activity) context).finish();
			((Activity) context).overridePendingTransition(
					R.anim.bfgame_activity_left_in,
					R.anim.bfgame_activity_right_out);
		}
	}

	/**
	 * 获取activity的action
	 * 
	 * @param c
	 * @return
	 */
	public static String getActivityAction(Class<?> c) {
		return FIRST_ACTION + c.getSimpleName().replace("Activity", "");
	}

	/**
	 * 初始化图片下载器
	 */
	protected void initImageLoader() {
		mImageThumbSize = context.getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_size);
		mMaxImageThumbSize = context.getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_big_size);
		mImageFetcher = new ImageFetcher(context, mImageThumbSize);
		mImageFetcher.setLoadingImage(R.drawable.bfgame_default_img);

		mMaxImageFetcher = new ImageFetcher(context, mMaxImageThumbSize);
		mMaxImageFetcher.setLoadingImage(R.drawable.bfgame_default_big_img);

		ImageCacheParams cacheParams = new ImageCacheParams((Activity) context,
				IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
													// app memory
		mImageFetcher.addImageCache(
				((BaseActivity) context).getSupportFragmentManager(),
				cacheParams);

		mMaxImageFetcher.addImageCache(
				((BaseActivity) context).getSupportFragmentManager(),
				cacheParams);
	}

	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.title_back_btn) {
			goBack();
		} else if (id == R.id.title_search_btn) {
			showActivity(context, SearchActivity.class);
		} else if (id == R.id.title_gift_btn) {
			showActivity(context, GameGiftActivity.class);
			if (Constants.NEED_TIP_GIFT) {
				Constants.NEED_TIP_GIFT = false;
			}
		} else if (id == R.id.title_download_btn) {
			Intent intent = new Intent();
//			intent.setAction("com.bfgame.app.downloadCenter");
//			intent.setClass(context, DownloadActivity.class);
//			context.startActivity(intent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			goBack();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void goBack() {
		finish();
		overridePendingTransition(R.anim.bfgame_activity_left_in,
				R.anim.bfgame_activity_right_out);
	}

	public abstract void initLayout();

	public abstract void init();

	public abstract void initView();

	public abstract void initListener();

	public abstract void initValue();

	// 下载状态更新
	public abstract void downloadStateUpdate();

	/**
	 * 下载状态更新广播接收器
	 * 
	 * @author apple
	 * 
	 */
	class DownloadStateUpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtil.d("收到下载广播", "==================");
			if (intent != null
					&& DownloadUtil.DOWNLOAD_STATE_UPDATE_ACTION.equals(intent
							.getAction())) {
				int appId = intent
						.getIntExtra(DownloadUtil.TASK_TAG_APP_ID, -1);
				int state = intent.getIntExtra(
						DownloadUtil.TASK_TAG_DOWNLOAD_STATE, -1);
				LogUtil.d("appId", String.valueOf(appId));
				LogUtil.d("state", String.valueOf(state));
				if (appId != -1) {
					DownloadUtil.downloadMap.put(appId, state);
					downloadStateUpdate();
				}
			} else if (DownloadUtil.DOWNLOAD_UPDATE_RED_DOT_ACTION
					.equals(intent.getAction())) {
				isVisible = intent.getBooleanExtra("isVisible", false);
				View v = findViewById(R.id.title_new_iv);
				if (v != null) {
					v.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
				}
			}
		}
	}
}
