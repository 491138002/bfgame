package com.bfgame.app.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bfgame.app.common.Constants;
import com.bfgame.app.net.AsyncHttpGet;
import com.bfgame.app.net.AsyncHttpPost;
import com.bfgame.app.net.BaseRequest;
import com.bfgame.app.net.DefaultThreadPool;
import com.bfgame.app.net.ThreadCallBack;
import com.bfgame.app.net.utils.CheckNetWorkUtil;
import com.bfgame.app.net.utils.RequestParameter;
import com.bfgame.app.util.DeviceUtil;
import com.bfgame.app.util.LogUtil;
import com.bfgame.app.util.preference.Preferences;
import com.bfgame.app.util.preference.PreferencesUtils;
import com.bfgame.app.vo.Gift;
import com.bfgame.app.widget.dialog.CustomLoadingDialog;

public abstract class BaseFragment extends Fragment implements ThreadCallBack {

	private static final long serialVersionUID = 7504844264561549011L;

	/**
	 * 当前activity所持有的所有请求
	 */
	List<BaseRequest> requestList = new ArrayList<BaseRequest>();

	
	/**
	 * rootView
	 */
	protected View rootView;
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
	 * 主线程Handler
	 */
	protected Handler handler = new Handler();

	/**
	 * 加载提示框
	 */
	 public static CustomLoadingDialog loadingDialog;

	/**
	 * Activity 前缀
	 */
	public static final String FIRST_ACTION = "com.bfgame.app.";

	/**
	 * 配置文件信息
	 */
	public static PreferencesUtils preferencesUtils;

	/**
	 * 是否初始化
	 */
	protected boolean isInit = true;

	/**
	 * 访问网络成功Tag
	 */
	protected final int SUCCESS = 1;

	/**
	 * 上下文
	 */
	protected Context context;

	/**
	 * 延迟请求时间
	 */
	protected long postDelayed = 0;

	/**
	 * 已领取礼包列表
	 */
	public List<Gift> saveGiftList;

	/**
	 * 线程回调时调用的方法
	 */
	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {

	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferencesUtils = new PreferencesUtils(getActivity(),
				Preferences.CONFIG_FILE);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	
	@Override
	public void onDestroy() {
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

		if (!CheckNetWorkUtil.checkNetWork(getActivity())) {
			return;
		}

		if (null != parameter) {
			parameter.add(new RequestParameter("channel", "android"));
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
					connectTimeout, readTimeout, resultCode, context);
		}
		DefaultThreadPool.getInstance().execute(httpRequest);
		this.requestList.add(httpRequest);
	}

	public void showToast(String content) {
		Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 初始化方法
	 */
	public abstract void init();

	public void onCallbackFromThread(String resultJson) {

	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	// 下载状态更新
	public abstract void downloadStateUpdate();
}
