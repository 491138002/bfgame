package com.bfgame.app.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.bfgame.app.activity.fragment.adapter.MainOnlineAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.common.Constants;
import com.bfgame.app.net.utils.RequestParameter;
import com.bfgame.app.procotol.GameResponseMessage;
import com.bfgame.app.util.AnimationUtil;
import com.bfgame.app.util.StatisticsUtil;
import com.bfgame.app.util.StringUtil;
import com.bfgame.app.vo.GameInfo;
import com.bfgame.app.widget.view.ChildViewPager;
import com.bfgame.app.widget.view.PointWidget;
import com.bfgame.app.widget.view.PullToRefreshListView;
import com.bfgame.app.widget.view.PullToRefreshListView.OnRefreshListener;

public class MainOnlineFragment extends BaseFragment {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5761975331897789386L;
	private final int GET_WANGYOU_LIST_KEY = 1;	//request new list key
	private final int GET_ONLINE_BANNER_KEY = 2;	//request banner key
	private String more;
	
	
	private MainOnlineAdapter adapter;
	private PullToRefreshListView listView;
	private List<GameInfo> dataList = null;
	private List<GameInfo> bannerDataList = null;
	private View bannerView;
	private View bannerLoadView;
	private ChildViewPager bannerVP;
	private PointWidget pw;
	private MainBannerAdapter bannerAdapter;
	/**
	 * Banner 默认宽高
	 */
	private int bannerDefaultWidth = 480;
	private int bannerDefaultHeight = 180;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		WindowManager window = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		window.getDefaultDisplay().getMetrics(dm);
		
		if(rootView != null && rootView.getParent() != null)
			((ViewGroup)rootView.getParent()).removeView(rootView);
		if(rootView == null){
			//create rootview
			rootView = inflater.inflate(R.layout.bfgame_fragment_category_layout, container, false);
			//loading layout
			loadView = rootView.findViewById(R.id.loading_layout);
			//set loading icon anim
			ImageView loading_icon_iv = (ImageView) rootView.findViewById(R.id.loading_icon_iv);
			AnimationUtil.startLoadingAnim(loading_icon_iv);
			// init listview
			listView = (PullToRefreshListView) rootView.findViewById(R.id.listview);
			listView.setDividerHeight(0);
			
			
			
			//add Banner Header
			bannerView = inflater.inflate(R.layout.bfgame_main_home_banner_item, null);
			bannerLoadView = bannerView.findViewById(R.id.loading_layout);
			bannerVP = (ChildViewPager) bannerView.findViewById(R.id.bannerVP);
			pw = (PointWidget) bannerView.findViewById(R.id.banner_ponit);
			float bannerHeight = (float)dm.widthPixels / bannerDefaultWidth * bannerDefaultHeight;
			@SuppressWarnings("deprecation")
			AbsListView.LayoutParams bannerParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			bannerParams.height = (int)bannerHeight;//DeviceUtil.dip2px(bannerHeight, dm.scaledDensity);
			bannerView.setLayoutParams(bannerParams);
			listView.addHeaderView(bannerView, null, false);
			bottomlogo = inflater.inflate(R.layout.bfgame_activity_bottom_logo, null);
			// add loading footbar
			footView = inflater.inflate(R.layout.bfgame_list_footbar, null);
			loading_icon_iv = (ImageView) footView.findViewById(R.id.loading_icon_iv);
			AnimationUtil.startLoadingAnim(loading_icon_iv);
			
			listView.addFooterView(footView, null, false);
			//setting adapter
			adapter = new MainOnlineAdapter((BaseActivity)getActivity(), listView, this);
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
							getOnlineList();
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
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void init() {
		if(isInit || (dataList != null && (adapter != null && adapter.getSize() == 0))){
			isInit = false;
			handler.postDelayed(new Runnable() {
				public void run() {
					getBanner();
					//request new list datas
					getOnlineList();
				}
			}, postDelayed);
		}
	}

	
	/**
	 * request banner datas
	 */
	public void getBanner(){
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("service", "online_slide"));
		startHttpRequst(Constants.HTTP_GET, Constants.URL_ONLINE_BANNER,
				parameter, false, "", false, Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT,
				GET_ONLINE_BANNER_KEY);
	}
	/**
	 * request new list datas
	 */
	public void getOnlineList(){
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("service", "onlines"));
		parameter.add(new RequestParameter("page", "1"));
		startHttpRequst(Constants.HTTP_GET, Constants.URL_MAIN_ONLINE_LIST,
				parameter, false, "", false, Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT,
				GET_WANGYOU_LIST_KEY);
	}
		
	/**
	 * request hot list datas
	 */
	public void getOnlineList(String url){
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		startHttpRequst(Constants.HTTP_GET, url,
				parameter, false, "", false, Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT,
				GET_WANGYOU_LIST_KEY);
	}
	
	@Override
	public synchronized void onCallbackFromThread(String resultJson, int resultCode) {
		try{
			switch(resultCode){
				case GET_WANGYOU_LIST_KEY:
					if(Constants.IS_REFRESH ||dataList == null){
						StatisticsUtil.addShowPage(preferencesUtils, -4,-4);
					}
					GameResponseMessage gameResponseMessage = new GameResponseMessage();
					gameResponseMessage.parseResponse(resultJson);
					dataList = gameResponseMessage.getResultList();
					if (Constants.IS_REFRESH) {
						adapter.resetList(dataList);
					} else {
						adapter.addLast(dataList);
					}
					more = gameResponseMessage.getMore();
					if (StringUtil.isEmpty(more)) {
						listView.removeFooterView(footView);
						if (!Constants.IS_REFRESH) {
							listView.addFooterView(bottomlogo);
						}
					}
					Constants.IS_REFRESH = false;
					hideLoadView();
					break;
				case GET_ONLINE_BANNER_KEY:
					gameResponseMessage = new GameResponseMessage();
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
					
					if(bannerLoadView != null && bannerDataList != null && bannerDataList.size() > 0){
						bannerLoadView.setVisibility(View.GONE);
						if (!Constants.IS_REFRESH) {
							handler.postDelayed(new Runnable() {
								public void run() {
									bannerVP.setCurrentItem(1000-(Integer.MAX_VALUE/2%bannerDataList.size())-1, false);
								}
							}, 1000);
						}
					}
					StatisticsUtil.addShowBannerList(preferencesUtils, bannerDataList);
					hideLoadView();
					adapter.notifyDataSetChanged();
					break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<GameInfo> getBannerList(){
		return bannerDataList;
	}
	public void nextPage(){
		if(StringUtil.isNotEmpty(more)){
			getOnlineList(more);
		}
	}
	
	public void hideLoadView(){
		if(loadView.getVisibility() != View.GONE){
			loadView.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onDestroy() {
		if (bannerAdapter!=null) {
			bannerAdapter.onDestory();
		}
		if(adapter != null)
			adapter.onDestroy();
		super.onDestroy();
	}

	@Override
	public void downloadStateUpdate() {
		if(adapter != null)
			adapter.notifyDataSetChanged();
	}

}
