package com.bfgame.app.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.BaseJson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bfgame.app.R;
import com.bfgame.app.activity.fragment.adapter.OnlineGiftAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.common.Constants;
import com.bfgame.app.net.utils.RequestParameter;
import com.bfgame.app.procotol.GiftResponseMessage;
import com.bfgame.app.util.AnimationUtil;
import com.bfgame.app.util.DeviceUtil;
import com.bfgame.app.util.StatisticsUtil;
import com.bfgame.app.util.StringUtil;
import com.bfgame.app.util.preference.Preferences;
import com.bfgame.app.vo.Gift;
import com.bfgame.app.widget.view.PullToRefreshListView;
import com.bfgame.app.widget.view.PullToRefreshListView.OnRefreshListener;
import com.google.gson.reflect.TypeToken;

/**
 * 网游礼包页面
 * @author admin
 *
 */
public class OnlineGiftFragment extends BaseFragment {
	private static final long serialVersionUID = -3098129941997778768L;
	private final int GET_GIFT_WANGYOU_KEY = 1;
	private final int GET_GIFT_CODE_KEY = 2;

	private String more;

	private OnlineGiftAdapter adapter;
	private PullToRefreshListView listView;
	private List<Gift> danJiList = null;

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
			adapter = new OnlineGiftAdapter((BaseActivity) getActivity(),
					listView, this);
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
							getOnlineGiftList();
							listView.onRefreshComplete();
						}
					}.execute(null, null, null);
				}
			});
			listView.setAdapter(adapter);
		}

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void init() {
		if (isInit
				|| (danJiList != null && (adapter != null && adapter.getSize() == 0))) {
			isInit = false;
			handler.postDelayed(new Runnable() {
				public void run() {
					getOnlineGiftList();
				}
			}, postDelayed);
		}
	}

	/**
	 * request gift list datas
	 */
	public void getOnlineGiftList() {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("service", "gifts"));
		parameter.add(new RequestParameter("type", "online"));
		startHttpRequst(Constants.HTTP_GET, Constants.URL_MAIN_GIFT_LIST,
				parameter, false, "", false,
				Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT, GET_GIFT_WANGYOU_KEY);
	}

	/**
	 * request gift list datas
	 */
	public void getGiftList(String url) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("service", "gifts"));
		parameter.add(new RequestParameter("type", "online"));
		startHttpRequst(Constants.HTTP_GET, url, parameter, false, "", false,
				Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT, GET_GIFT_WANGYOU_KEY);
	}

	/**
	 * request gift code datas
	 */
	public void getGiftCode(String giftid) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("service", "getgift"));
		parameter.add(new RequestParameter("giftid", giftid));
		parameter.add(new RequestParameter("imei", DeviceUtil
				.getImei(getActivity())));

		startHttpRequst(Constants.HTTP_POST, Constants.URL_MAIN_GET_GIFT,
				parameter, false, "", false,
				Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT, GET_GIFT_CODE_KEY);
	}

	private Gift bean;

	/**
	 * request gift code datas
	 */
	public void getGiftCode(Gift bean) {
		this.bean = bean;
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("service", "getgift"));
		parameter.add(new RequestParameter("giftid", bean.getGiftId()));
		parameter.add(new RequestParameter("imei", DeviceUtil
				.getImei(getActivity())));
		startHttpRequst(Constants.HTTP_POST, Constants.URL_MAIN_GET_GIFT,
				parameter, false, "", false,
				Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT, GET_GIFT_CODE_KEY);
	}

	
	private OnGiftResponseListener onGiftResponseListener;

	/**
	 * 请求礼包监听器
	 */
	public interface OnGiftResponseListener {
		void onGet(Gift bean);
		void onGetted();
	}

	public void setOnGiftResponseListener(OnGiftResponseListener l) {
		onGiftResponseListener = l;
	}

	@Override
	public synchronized void onCallbackFromThread(String resultJson,
			int resultCode) {
		try {
			switch (resultCode) {
			case GET_GIFT_WANGYOU_KEY:
				if (danJiList == null||Constants.IS_REFRESH) {
					StatisticsUtil.addShowPage(preferencesUtils, -6,-6);
				}
				GiftResponseMessage giftResponseMessage = new GiftResponseMessage();
				giftResponseMessage.parseResponse(resultJson);

				more = giftResponseMessage.getMore();
				danJiList = giftResponseMessage.getResultList();
				
				if (Constants.IS_REFRESH) {
					adapter.resetList(danJiList);
					listView.onRefreshComplete();
				} else {
					adapter.addLast(danJiList);
				}
				if (StringUtil.isEmpty(more)) {
					listView.removeFooterView(footView);
					if (!Constants.IS_REFRESH) {
						listView.addFooterView(bottomlogo);
					}
				}
				Constants.IS_REFRESH = false;
				hideLoadView();
				break;
			case GET_GIFT_CODE_KEY:
				giftResponseMessage = new GiftResponseMessage();
				giftResponseMessage.parseResponse(resultJson);
				if (giftResponseMessage.getResultCode() == SUCCESS) {
					bean.setActivationCode(giftResponseMessage.getResult()
							.getActivationCode());
					if (onGiftResponseListener != null) {
						onGiftResponseListener.onGet(bean);
					}
					adapter.setGift(giftResponseMessage.getResult());
					Constants.NEED_REFRESH_GIFT = true;
				} else {
					if (onGiftResponseListener != null) {
						onGiftResponseListener.onGetted();
					}
					showToast(giftResponseMessage.getMessage());
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void initSaveGiftList() {
		String json = preferencesUtils.getString(Preferences.RECEIVE_GIFT, "");
		saveGiftList = (List<Gift>) BaseJson.parser(
				new TypeToken<List<Gift>>() {
				}, json);
		if (saveGiftList == null)
			saveGiftList = new ArrayList<Gift>();
	}

	public void addGift(Gift gift) {
		initSaveGiftList();
		saveGiftList.add(gift);
		preferencesUtils.putString(Preferences.RECEIVE_GIFT,
				BaseJson.toJson(saveGiftList));
	}

	public void nextPage() {
		if (StringUtil.isNotEmpty(more)) {
			getGiftList(more);
		}
	}

	@Override
	public void onDestroy() {
		if (adapter != null)
			adapter.onDestroy();
		super.onDestroy();
	}

	public void hideLoadView() {
		if (loadView.getVisibility() != View.GONE) {
			loadView.setVisibility(View.GONE);
		}
	}

	@Override
	public void downloadStateUpdate() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

}
