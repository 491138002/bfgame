package com.bfgame.app.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;

import com.bfgame.app.R;
import com.bfgame.app.activity.fragment.adapter.MainBannerAdapter;
import com.bfgame.app.activity.fragment.adapter.MainHomeListAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.common.Constants;
import com.bfgame.app.net.utils.RequestParameter;
import com.bfgame.app.procotol.GameResponseMessage;
import com.bfgame.app.util.AnimationUtil;
import com.bfgame.app.util.StatisticsUtil;
import com.bfgame.app.vo.GameInfo;
import com.bfgame.app.widget.view.ChildViewPager;
import com.bfgame.app.widget.view.PointWidget;
import com.bfgame.app.widget.view.PullToRefreshListView;
import com.bfgame.app.widget.view.PullToRefreshListView.OnRefreshListener;

public class MainHomeFragment extends BaseFragment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 222080818286153742L;
	private final int GET_BANNER_KEY = 1; // request banner key
	private final int GET_HOME_LIST_KEY = 2; // request hot list key

	/**
	 * Banner 默认宽高
	 */
	private int bannerDefaultWidth = 480;
	private int bannerDefaultHeight = 180;
	private MainHomeListAdapter adapter;
	private PullToRefreshListView listView;
	private List<GameInfo> dataList = null;
	private List<GameInfo> bannerDataList = null;

	private View bannerView;
	private View bannerLoadView;
	private ChildViewPager bannerVP;
	private PointWidget pw;
	private MainBannerAdapter bannerAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		WindowManager window = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		window.getDefaultDisplay().getMetrics(dm);

		if (rootView != null && rootView.getParent() != null)
			((ViewGroup) rootView.getParent()).removeView(rootView);
		if (rootView == null) {
			// create rootview
			rootView = inflater.inflate(R.layout.bfgame_fragment_main_layout,
					container, false);
			// loading layout
			loadView = rootView.findViewById(R.id.loading_layout);
			// set loading icon anim
			ImageView loading_icon_iv = (ImageView) rootView
					.findViewById(R.id.loading_icon_iv);
			AnimationUtil.startLoadingAnim(loading_icon_iv);
			// init listview
			listView = (PullToRefreshListView) rootView
					.findViewById(R.id.listview_home);
			listView.setItemsCanFocus(true);
			listView.setDividerHeight(0);

			// add Banner Header
			bannerView = inflater.inflate(
					R.layout.bfgame_main_home_banner_item, null);
			bannerLoadView = bannerView.findViewById(R.id.loading_layout);
			bannerVP = (ChildViewPager) bannerView.findViewById(R.id.bannerVP);
			pw = (PointWidget) bannerView.findViewById(R.id.banner_ponit);
			float bannerHeight = (float) dm.widthPixels / bannerDefaultWidth
					* bannerDefaultHeight;
			@SuppressWarnings("deprecation")
			AbsListView.LayoutParams bannerParams = new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			bannerParams.height = (int) bannerHeight;
			// DeviceUtil.dip2px(bannerHeight, dm.scaledDensity);
			bannerView.setLayoutParams(bannerParams);

			listView.addHeaderView(bannerView, null, false);

			bottomlogo = inflater.inflate(R.layout.bfgame_activity_bottom_logo,
					null);
			adapter = new MainHomeListAdapter((BaseActivity) getActivity(),
					listView);
			listView.setAdapter(adapter);

			listView.setonRefreshListener(new OnRefreshListener() {

				@Override
				public void onRefresh() {
					new AsyncTask<Void, Void, Void>() {

						protected Void doInBackground(Void... params) {
							try {
								Thread.sleep(500);
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {

							Constants.IS_REFRESH = true;

							getBanner();

							getHomeList();
							listView.onRefreshComplete();
						}
					}.execute(null, null, null);
				}
			});
		}

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void init() {
		if (isInit) {
			isInit = false;
			// handler.postDelayed(new Runnable() {
			// public void run() {
			// // request banner datasz
			getBanner();
			getHomeList();
			// request hot list datas
			// }
			// }, postDelayed);
		}
	}

	/**
	 * request banner datas
	 */
	public void getBanner() {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("service", "home_slide"));
		startHttpRequst(Constants.HTTP_GET, Constants.URL_MAIN_BANNER,
				parameter, false, "", false,
				Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT, GET_BANNER_KEY);

	}

	/**
	 * request hot list datas
	 */
	public void getHomeList() {

		System.out.println("start" + SystemClock.currentThreadTimeMillis());
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		startHttpRequst(Constants.HTTP_GET, Constants.URL_MAIN_HOME_LIST,
				parameter, false, "", false,
				Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT, GET_HOME_LIST_KEY);
	}

	@Override
	public synchronized void onCallbackFromThread(String resultJson,
			int resultCode) {
		try {
			switch (resultCode) {
			case GET_BANNER_KEY:

				GameResponseMessage gameResponseMessage = new GameResponseMessage();
				gameResponseMessage.parseResponse(resultJson);
				bannerDataList = gameResponseMessage.getResultList();

				if (!Constants.IS_REFRESH) {
					bannerAdapter = new MainBannerAdapter(getActivity(),
							bannerDataList, bannerVP, pw);
				}
				if (Constants.IS_REFRESH) {
					bannerAdapter.resetList(bannerDataList);
				} else {
					bannerVP.setAdapter(bannerAdapter);
				}
				if (bannerLoadView != null && bannerDataList != null
						&& bannerDataList.size() > 0) {
					bannerLoadView.setVisibility(View.GONE);
					if (!Constants.IS_REFRESH) {
						handler.postDelayed(new Runnable() {
							public void run() {
								bannerVP.setCurrentItem(
										1000 - (Integer.MAX_VALUE / 2 % bannerDataList
												.size()) - 1, false);
							}
						}, 1000);
					}
				}
				StatisticsUtil.addShowBannerList(preferencesUtils,
						bannerDataList);
//				hideLoadView();
				adapter.notifyDataSetChanged();
				break;
			case GET_HOME_LIST_KEY:
				System.out.println("mid"
						+ SystemClock.currentThreadTimeMillis());
				if (Constants.IS_REFRESH || dataList == null) {
					StatisticsUtil.addShowPage(preferencesUtils, -1, -1);
				}
				gameResponseMessage = new GameResponseMessage();
				gameResponseMessage.parseResponse(resultJson);

				dataList = gameResponseMessage.getResult().getRecommend();

				if (Constants.IS_REFRESH) {
					adapter.resetList(dataList);
				} else {
					adapter.updateMap(0, dataList);
				}
				dataList = gameResponseMessage.getResult().getSingle();
				if (Constants.IS_REFRESH) {
					adapter.resetList(dataList);
				} else {
					adapter.updateMap(1, dataList);
				}
				dataList = gameResponseMessage.getResult().getOnline();
				if (Constants.IS_REFRESH) {
					adapter.resetList(dataList);
				} else {
					adapter.updateMap(2, dataList);
				}
				// adapter.updateMap(0, gameResponseMessage.getResult()
				// .getRecommend());
				// adapter.updateMap(1, gameResponseMessage.getResult()
				// .getSingle());
				// adapter.updateMap(2, gameResponseMessage.getResult()
				// .getOnline());
				if (!Constants.IS_REFRESH) {
					listView.addFooterView(bottomlogo);
				}
				Constants.IS_REFRESH = false;
				hideLoadView();
				System.out.println("end"
						+ SystemClock.currentThreadTimeMillis());
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void hideLoadView() {
		if (loadView.getVisibility() != View.GONE) {
			loadView.setVisibility(View.GONE);
		}
	}

	public List<GameInfo> getBannerList() {
		return bannerDataList;
	}

	@Override
	public void onDestroy() {
		if (bannerAdapter != null) {
			bannerAdapter.onDestory();
		}
		if (adapter != null)
			adapter.onDestroy();
		super.onDestroy();
	}

	@Override
	public void downloadStateUpdate() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

}
