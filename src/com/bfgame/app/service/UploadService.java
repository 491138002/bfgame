package com.bfgame.app.service;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.BaseJson;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.bfgame.app.common.Constants;
import com.bfgame.app.net.AsyncHttpGet;
import com.bfgame.app.net.AsyncHttpPost;
import com.bfgame.app.net.BaseRequest;
import com.bfgame.app.net.DefaultThreadPool;
import com.bfgame.app.net.ThreadCallBack;
import com.bfgame.app.net.utils.CheckNetWorkUtil;
import com.bfgame.app.net.utils.RequestParameter;
import com.bfgame.app.procotol.GameResponseMessage;
import com.bfgame.app.util.DeviceUtil;
import com.bfgame.app.util.LogUtil;
import com.bfgame.app.util.StatisticsUtil;
import com.bfgame.app.util.preference.Preferences;
import com.bfgame.app.util.preference.PreferencesUtils;
import com.bfgame.app.vo.StatisticsVO;

public class UploadService extends Service implements ThreadCallBack{
	private static final long serialVersionUID = -8336110056822052742L;
	private int uploadSize = 0;
	private final int RESPONSE_STATISTICS_KEY = 1;
	private PreferencesUtils preferencesUtils;
	private Handler handler = new Handler();
	private UploadRunnable uploadRunnable = new UploadRunnable();
	
	/**
	 * 当前activity所持有的所有请求
	 */
	List<BaseRequest> requestList = new ArrayList<BaseRequest>();
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		handler.removeCallbacks(uploadRunnable);
		handler.post(uploadRunnable);
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
					} catch (UnsupportedOperationException e) {
						// do nothing .
					}
				}
			}
		}
	}
	
	public void uploadStatistics(){
		if(preferencesUtils == null){
			preferencesUtils = new PreferencesUtils(this, Preferences.CONFIG_FILE);
		}
		
		List<StatisticsVO> sList = StatisticsUtil.getStatisticsList(preferencesUtils);
		if(sList != null && sList.size() > 0){
			uploadSize = sList.size();
			uploadStatistics(BaseJson.toJson(sList));
		}
	}
	
	/**
	 * response upload statistics
	 */
	public void uploadStatistics(String json){
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("mac", DeviceUtil.getMacAddress(this)));
		parameter.add(new RequestParameter("imei", DeviceUtil.getImei(this)));
		parameter.add(new RequestParameter("mtype", DeviceUtil.getDeviceModel()));
		parameter.add(new RequestParameter("mos", DeviceUtil.getSDKVersion()));
		parameter.add(new RequestParameter("data", json));
		startHttpRequst(Constants.HTTP_POST, Constants.URL_UPLOAD_STATISTICS,
				parameter, false, "", false, Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT,
				RESPONSE_STATISTICS_KEY);
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

	@Override
	public void onCallbackFromThread(String resultJson) {
		
	}

	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		try{
			switch(resultCode){
			case RESPONSE_STATISTICS_KEY:
				GameResponseMessage responseMessage = new GameResponseMessage();
				responseMessage.parseResponse(resultJson);
				if(responseMessage.getResultCode() == 1){
					StatisticsUtil.clearStatistics(preferencesUtils, uploadSize);
				}
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	class UploadRunnable implements Runnable{
		@Override
		public void run() {
			cancelRequest();
			uploadStatistics();
			handler.removeCallbacks(uploadRunnable);
			handler.postDelayed(uploadRunnable, 60*1000);
		}
	}
}
