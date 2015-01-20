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
import com.bfgame.app.activity.fragment.adapter.SaveGiftAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.common.Constants;
import com.bfgame.app.util.AnimationUtil;
import com.bfgame.app.util.preference.Preferences;
import com.bfgame.app.vo.Gift;
import com.bfgame.app.widget.view.PullToRefreshListView;
import com.bfgame.app.widget.view.PullToRefreshListView.OnRefreshListener;
import com.google.gson.reflect.TypeToken;

public class SaveGiftFragment extends BaseFragment {

	private static final long serialVersionUID = 5142475454326939700L;
	private SaveGiftAdapter adapter;
	private PullToRefreshListView listView;
	private List<Gift> giftList = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// create layout
		View rootView = inflater.inflate(
				R.layout.bfgame_fragment_category_layout, container, false);
		// loading layout
		loadView = rootView.findViewById(R.id.loading_layout);
		loadView.setVisibility(View.GONE);
		// set loading icon anim
		ImageView loading_icon_iv = (ImageView) rootView
				.findViewById(R.id.loading_icon_iv);
		AnimationUtil.startLoadingAnim(loading_icon_iv);
		// init listview
		listView = (PullToRefreshListView) rootView.findViewById(R.id.listview);
		listView.setDividerHeight(0);

		// add loading footbar
		footView = inflater.inflate(R.layout.bfgame_list_footbar, null);
		loading_icon_iv = (ImageView) footView
				.findViewById(R.id.loading_icon_iv);
		AnimationUtil.startLoadingAnim(loading_icon_iv);
		// listView.addFooterView(footView, null, false);

		// setting adapter
		adapter = new SaveGiftAdapter((BaseActivity) getActivity(), listView,
				this);

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
						adapter.notifyDataSetChanged();
						listView.onRefreshComplete();
					}
				}.execute(null, null, null);
			}
		});

		listView.setAdapter(adapter);

		return rootView;
	}

	@SuppressWarnings("unchecked")
	public void initSaveGiftList() {

		String json = preferencesUtils.getString(Preferences.RECEIVE_GIFT, "");

		if (giftList == null){
			giftList = new ArrayList<Gift>();
		}else{
			giftList.clear();
		}
		giftList = (List<Gift>) BaseJson.parser(new TypeToken<List<Gift>>() {
		}, json);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void init() {
		if (Constants.NEED_REFRESH_GIFT) {
			Constants.NEED_REFRESH_GIFT = false;
			initSaveGiftList();
			adapter.clear();
			adapter.addLast(giftList);
		}
	}

	public void hideLoadView() {
		if (loadView.getVisibility() != View.GONE) {
			loadView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		if (adapter != null)
			adapter.onDestroy();
		super.onDestroy();
	}

	@Override
	public void downloadStateUpdate() {
	}

}
