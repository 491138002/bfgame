package com.bfgame.app.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bfgame.app.R;
import com.bfgame.app.activity.fragment.adapter.MainCategoryAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.common.Constants;
import com.bfgame.app.net.utils.RequestParameter;
import com.bfgame.app.procotol.CategoryResponseMessage;
import com.bfgame.app.util.AnimationUtil;
import com.bfgame.app.util.StatisticsUtil;
import com.bfgame.app.vo.Category;
import com.bfgame.app.widget.view.PullToRefreshListView;
import com.bfgame.app.widget.view.PullToRefreshListView.OnRefreshListener;


public class MainCategoryFragment extends BaseFragment {
	
	private static final long serialVersionUID = 1215253729218871269L;

	private final int GET_CATEGORY_LIST_KEY = 1;	//request category list key
	private MainCategoryAdapter adapter;
	private PullToRefreshListView listView;
	private List<Category> dataList = new ArrayList<Category>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
			
			// add loading footbar
			footView = inflater.inflate(R.layout.bfgame_list_footbar, null);
			loading_icon_iv = (ImageView) footView.findViewById(R.id.loading_icon_iv);
			AnimationUtil.startLoadingAnim(loading_icon_iv);
			listView.addFooterView(footView, null, false);
			
			bottomlogo = inflater.inflate(R.layout.bfgame_activity_bottom_logo,
					null);
			//setting adapter
			adapter = new MainCategoryAdapter((BaseActivity)getActivity(), listView, this);
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
		                    getCategoryList();
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
		if(isInit || (dataList != null && (adapter != null && adapter.getSize() == 0))){
			isInit = false;
			handler.postDelayed(new Runnable() {
				public void run() {
					//request category list datas
					getCategoryList();
				}
			}, postDelayed);
		}
	}

	/**
	 * request category list datas
	 */
	public void getCategoryList(){
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("service", "findcategories"));
		startHttpRequst(Constants.HTTP_GET, Constants.URL_MAIN_CATEGORY_LIST,
				parameter, false, "", false, Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT,
				GET_CATEGORY_LIST_KEY);
	}
		
	@Override
	public synchronized void onCallbackFromThread(String resultJson, int resultCode) {
		try{
			switch(resultCode){
				case GET_CATEGORY_LIST_KEY:
					if(dataList == null){
						StatisticsUtil.addShowPage(preferencesUtils, -2,-2);
					}
					CategoryResponseMessage categoryResponseMessage = new CategoryResponseMessage();
					categoryResponseMessage.parseResponse(resultJson);
					dataList = categoryResponseMessage.getResultList();
					
					 if (Constants.IS_REFRESH) {
		                    adapter.resetList(dataList);
		                } else {
		                    adapter.addLast(dataList);
		                }
					if(categoryResponseMessage.getResultCode() == 1){
						
						listView.removeFooterView(footView);
						if (!Constants.IS_REFRESH) {
							listView.addFooterView(bottomlogo);
						}
						Constants.IS_REFRESH = false;
						hideLoadView();
					}
					break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void hideLoadView(){
		if(loadView.getVisibility() != View.GONE){
			loadView.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onDestroy() {
		if(adapter != null)
			adapter.onDestroy();
		super.onDestroy();
	}

	@Override
	public void downloadStateUpdate() {
		
	}

}
