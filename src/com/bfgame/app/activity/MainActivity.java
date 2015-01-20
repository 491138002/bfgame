package com.bfgame.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.bfgame.app.R;
import com.bfgame.app.activity.fragment.MainCategoryFragment;
import com.bfgame.app.activity.fragment.MainHomeFragment;
import com.bfgame.app.activity.fragment.MainOnlineFragment;
import com.bfgame.app.activity.fragment.MainSingleFragment;
import com.bfgame.app.activity.fragment.MainTopFragment;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.common.Constants;
import com.bfgame.app.net.utils.RequestParameter;
import com.bfgame.app.procotol.GameResponseMessage;
import com.bfgame.app.util.AnimationUtil;
import com.bfgame.app.util.ApkInfo;
import com.bfgame.app.util.DateUtils;
import com.bfgame.app.util.DeviceUtil;
import com.bfgame.app.util.DialogUtil;
import com.bfgame.app.util.StatisticsUtil;
import com.bfgame.app.util.preference.Preferences;
import com.bfgame.app.vo.AppsInfo;
import com.bfgame.app.vo.AppsInfoBase;
import com.bfgame.app.widget.view.CustomTitleBarLayout;
import com.bfgame.app.widget.view.CustomTitleBarLayout.OnTitleChangeListener;

public class MainActivity extends BaseActivity {
	private static final long serialVersionUID = 3929972195646483665L;
	private final int RESPONSE_STATISTICS_KEY = 1;
	private final int GET_HOME_AD_KEY = 2;
	private final int RESPONSE_INSTALL_APPINFO_KEY = 3;

	public final String ARGUMENTS_NAME = "arg";
	private CustomTitleBarLayout page_title;
	private ViewPager viewPager;
	public String[] tabTitle = { "首页", "分类", "排行", "网游", "单机" }; // 标题
	private TabFragmentPagerAdapter mAdapter;

	private MainHomeFragment homeFragment;
	private MainCategoryFragment categoryFragment;
	private MainTopFragment topFragment;
	private MainOnlineFragment onlineFragment;
	private MainSingleFragment offlineFragment;

	private Button title_back_btn;
	private Button title_search_btn;
	private Button title_download_btn;
	private Button title_gift_btn;

	private int uploadSize = 0;
	private FrameLayout fl_float;
	public CustomTitleBarLayout.OnTitleChangeListener onTitleChangeListener = new OnTitleChangeListener() {
		public void change(int i) {
			viewPager.setCurrentItem(i, true);
		}
	};


	public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment ft = null;
			switch (arg0) {
			case 0:
				if (homeFragment == null) {
					homeFragment = new MainHomeFragment();
					homeFragment.setContext(context);
					homeFragment.init();
				}
				ft = homeFragment;
				break;
			case 1:
				if (categoryFragment == null) {
					categoryFragment = new MainCategoryFragment();
					categoryFragment.setContext(context);
				}
				ft = categoryFragment;
				break;
			case 2:
				if (topFragment == null) {
					topFragment = new MainTopFragment();
					topFragment.setContext(context);
				}
				ft = topFragment;
				break;
			case 3:
				if (onlineFragment == null) {
					onlineFragment = new MainOnlineFragment();
					onlineFragment.setContext(context);
				}
				ft = onlineFragment;
				break;
			case 4:
				if (offlineFragment == null) {
					offlineFragment = new MainSingleFragment();
					offlineFragment.setContext(context);
				}
				ft = offlineFragment;
				break;

			}
			return ft;
		}

		@Override
		public int getCount() {
			return tabTitle.length;
		}

	}

	@Override
	public void initLayout() {
		setContentView(R.layout.bfgame_activity_main);
	}

	@Override
	public void init() {
		PropertyConfigurator.configure("log4j.properties");
//		initLog4j();
//		PropertyConfigurator.configure("bin/log4j.properties"); 
//		PropertyConfigurator.configure(ClassLoader.getSystemResource("log4j.properties"));
		// new LoadInstallApkThread().start();
		// List<StatisticsVO> sList =
		// StatisticsUtil.getStatisticsList(preferencesUtils);
		// uploadSize = sList.size();
		// uploadStatistics(BaseJson.toJson(sList));
		// Intent uploadService = new Intent(context, UploadService.class);
		// startService(uploadService);
	}

	
	private static void initLog4j() {
		Properties prop = new Properties();

		prop.setProperty("log4j.rootLogger", "DEBUG, CONSOLE");
		prop.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", "%d{HH:mm:ss,SSS} [%t] %-5p %C{1} : %m%n");

		PropertyConfigurator.configure(prop);
		}
	@Override
	public void initView() {

		page_title = (CustomTitleBarLayout) findViewById(R.id.page_title);
		viewPager = (ViewPager) findViewById(R.id.viewPager);

		title_back_btn = (Button) findViewById(R.id.title_back_btn);
		title_search_btn = (Button) findViewById(R.id.title_search_btn);
		title_gift_btn = (Button) findViewById(R.id.title_gift_btn);
		title_download_btn = (Button) findViewById(R.id.title_download_btn);
		fl_float = (FrameLayout) findViewById(R.id.fl_float);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && AnimationUtil.sFloatView == null) {
			int[] desL = new int[2];
			title_download_btn.getLocationOnScreen(desL);
			AnimationUtil.sDownLoadX = desL[0] + DeviceUtil.dip2px(context, 15);
			AnimationUtil.sDownLoadY = desL[1] + DeviceUtil.dip2px(context, 15);
			fl_float.getLocationOnScreen(desL);
			AnimationUtil.sTitleH = desL[1];
			AnimationUtil.sFloatView = fl_float;
		}
	}

	@Override
	public void initListener() {
		title_back_btn.setOnClickListener(this);
		title_search_btn.setOnClickListener(this);
		title_download_btn.setOnClickListener(this);
		title_gift_btn.setOnClickListener(this);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				page_title.setSelectPosition(position);
				BaseFragment bf = (BaseFragment) mAdapter.getItem(position);
				bf.init();
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				page_title.setLinePoint(arg0, arg2 / tabTitle.length);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void initValue() {
		viewPager.setOffscreenPageLimit(0);
		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);

		page_title.setTab(tabTitle, 0);
		page_title.setOnTitleChangeListener(onTitleChangeListener);

		String preTime = preferencesUtils.getString(Preferences.AD_SHOW_TIME,
				"");
		String curTime = DateUtils.getDate();
		if (!curTime.equals(preTime)) {
			handler.postDelayed(new Runnable() {
				public void run() {
					getAD();
				}
			}, 1000);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		downloadStateUpdate();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

	@Override
	protected void onDestroy() {
		context = null;
		AnimationUtil.sFloatView = null;
		super.onDestroy();
	}

	/**
	 * response upload statistics
	 */
	public void uploadStatistics(String json) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("mac", DeviceUtil
				.getMacAddress(context)));
		parameter
				.add(new RequestParameter("imei", DeviceUtil.getImei(context)));
		parameter
				.add(new RequestParameter("mtype", DeviceUtil.getDeviceModel()));
		parameter.add(new RequestParameter("mos", DeviceUtil.getSDKVersion()));
		parameter.add(new RequestParameter("data", json));
		startHttpRequst(Constants.HTTP_POST, Constants.URL_UPLOAD_STATISTICS,
				parameter, false, "", false,
				Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT, RESPONSE_STATISTICS_KEY);
	}

	/**
	 * response upload install app info
	 */
	public void uploadInstallAppInfo(String json) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("service", "findappbypackage"));
		parameter.add(new RequestParameter("data", json));
		startHttpRequst(Constants.HTTP_POST, Constants.URL_UPLOAD_APP,
				parameter, false, "", false,
				Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT, RESPONSE_INSTALL_APPINFO_KEY);
	}

	/**
	 * get home AD datas
	 */
	public void getAD() {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("service", "getad"));
		startHttpRequst(Constants.HTTP_GET, Constants.URL_AD, parameter, false,
				"", false, Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT, GET_HOME_AD_KEY);
	}

	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		// TODO Auto-generated method stub
		super.onCallbackFromThread(resultJson, resultCode);
		try {
			switch (resultCode) {
			case RESPONSE_STATISTICS_KEY:
				GameResponseMessage responseMessage = new GameResponseMessage();
				responseMessage.parseResponse(resultJson);
				if (responseMessage.getResultCode() == SUCCESS) {
					StatisticsUtil
							.clearStatistics(preferencesUtils, uploadSize);
				}
				break;
			case GET_HOME_AD_KEY:
				GameResponseMessage gameResponseMessage = new GameResponseMessage();
				gameResponseMessage.parseResponse(resultJson);
				if (gameResponseMessage.getResultCode() == SUCCESS) {
					DialogUtil.showAdDialog(context,
							gameResponseMessage.getResult(), mMaxImageFetcher,
							null, preferencesUtils).show();
				}
				break;
			case RESPONSE_INSTALL_APPINFO_KEY:
				gameResponseMessage = new GameResponseMessage();
				gameResponseMessage.parseResponse(resultJson);
				if (gameResponseMessage.getResultCode() == SUCCESS) {

				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加载已经安装应用线程
	 * 
	 * @author apple
	 * 
	 */
	class LoadInstallApkThread extends Thread {
		public void run() {
			final List<AppsInfoBase> appsList = new ArrayList<AppsInfoBase>();
			HashMap<String, AppsInfo> appMap = ApkInfo
					.getInstalledAppInfo(context);
			if (context != null) {
				Iterator<Entry<String, AppsInfo>> iterator = appMap.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					appsList.add(iterator.next().getValue());
				}
				handler.post(new Runnable() {
					public void run() {
						// uploadInstallAppInfo(BaseJson.toJson(appsList));
					}
				});
			}
		}
	}

	@Override
	public void downloadStateUpdate() {
		if (homeFragment != null)
			homeFragment.downloadStateUpdate();
		if (offlineFragment != null)
			offlineFragment.downloadStateUpdate();
		if (onlineFragment != null)
			onlineFragment.downloadStateUpdate();
		if (categoryFragment != null)
			categoryFragment.downloadStateUpdate();
		if (topFragment != null)
			topFragment.downloadStateUpdate();

		// 广告
		DialogUtil.setAdDownloadState(context);
	}
}
