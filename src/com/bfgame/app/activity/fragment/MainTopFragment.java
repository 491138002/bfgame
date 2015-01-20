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
import com.bfgame.app.activity.fragment.adapter.MainTopAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.common.Constants;
import com.bfgame.app.net.utils.RequestParameter;
import com.bfgame.app.procotol.GameResponseMessage;
import com.bfgame.app.util.AnimationUtil;
import com.bfgame.app.util.StatisticsUtil;
import com.bfgame.app.util.StringUtil;
import com.bfgame.app.vo.GameInfo;
import com.bfgame.app.widget.view.PullToRefreshListView;
import com.bfgame.app.widget.view.PullToRefreshListView.OnRefreshListener;

public class MainTopFragment extends BaseFragment {

	private static final long serialVersionUID = -3380467372556854406L;

	private final int GET_TOP_LIST_KEY = 1; // request new list key

	private String more;
	private MainTopAdapter adapter;
	private PullToRefreshListView listView;
	private List<GameInfo> dataList = null;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView != null && rootView.getParent() != null)
			((ViewGroup) rootView.getParent()).removeView(rootView);
		if (rootView == null) {
			// create rootview
			rootView = inflater.inflate(
					R.layout.bfgame_fragment_category_layout, container, false);
			// loading layout
			loadView = rootView.findViewById(R.id.loading_layout);
			// set loading icon anim
			ImageView loading_icon_iv = (ImageView) rootView
					.findViewById(R.id.loading_icon_iv);
			AnimationUtil.startLoadingAnim(loading_icon_iv);
			// init listview
			listView = (PullToRefreshListView) rootView
					.findViewById(R.id.listview);
			listView.setDividerHeight(0);

			// add loading footbar
			footView = inflater.inflate(R.layout.bfgame_list_footbar, null);
			bottomlogo = inflater.inflate(R.layout.bfgame_activity_bottom_logo,
					null);
			loading_icon_iv = (ImageView) footView
					.findViewById(R.id.loading_icon_iv);
			AnimationUtil.startLoadingAnim(loading_icon_iv);
			listView.addFooterView(footView, null, false);

			// setting adapter
			adapter = new MainTopAdapter((BaseActivity) getActivity(),
					listView, this);
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
							getTopList();
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
		if (isInit
				|| (dataList != null && (adapter != null && adapter.getSize() == 0))) {
			isInit = false;
			handler.postDelayed(new Runnable() {
				public void run() {
					// request new list datas
					getTopList();
				}
			}, postDelayed);
		}
	}

	/**
	 * request new list datas
	 */
	public void getTopList() {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("service", "ranklist"));
		parameter.add(new RequestParameter("page", "1"));
		startHttpRequst(Constants.HTTP_GET, Constants.URL_MAIN_TOP_LIST,
				parameter, false, "", false,
				Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT, GET_TOP_LIST_KEY);
	}

	/**
	 * request top list datas
	 */
	public void getTopList(String url) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		startHttpRequst(Constants.HTTP_GET, url, parameter, false, "", false,
				Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT, GET_TOP_LIST_KEY);
	}

	@Override
	public synchronized void onCallbackFromThread(String resultJson,
			int resultCode) {
		try {
			switch (resultCode) {
			case GET_TOP_LIST_KEY:
				if (Constants.IS_REFRESH || dataList == null) {
					StatisticsUtil.addShowPage(preferencesUtils, -3,-3);
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void nextPage() {
		if (StringUtil.isNotEmpty(more)) {
			getTopList(more);
		}
	}

	public void hideLoadView() {
		if (loadView.getVisibility() != View.GONE) {
			loadView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onDestroy() {
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
