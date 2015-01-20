package com.bfgame.app.activity.fragment;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bfgame.app.R;
import com.bfgame.app.activity.fragment.adapter.DownloadAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.common.Constants;
import com.bfgame.app.util.AnimationUtil;
import com.bfgame.app.vo.GameInfo;
import com.bfgame.app.widget.view.PullToRefreshListView;
import com.bfgame.app.widget.view.PullToRefreshListView.OnRefreshListener;

public class DownloadingFragment extends BaseFragment {

	private BroadcastReceiver receiver;
	private DownloadAdapter adapter;
	private PullToRefreshListView listView;
	private List<GameInfo> dataList = null;
	private static final String CALL_ACTION = "downloading";

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
			loadView.setVisibility(View.INVISIBLE);
			listView = (PullToRefreshListView) rootView
					.findViewById(R.id.listview_home);
			listView.setItemsCanFocus(true);
			listView.setDividerHeight(0);

			bottomlogo = inflater.inflate(R.layout.bfgame_activity_bottom_logo,
					null);
			adapter = new DownloadAdapter((BaseActivity) getActivity(),
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
	}

	@Override
	public synchronized void onCallbackFromThread(String resultJson,
			int resultCode) {
		try {
			switch (resultCode) {
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

	public void bindReceiver() {
		receiver = new BroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CALL_ACTION);
		context.registerReceiver(receiver, intentFilter);
	}

	public class BroadcastReceiver extends android.content.BroadcastReceiver {

		public BroadcastReceiver() {
			Log.i("xy_Receiver", "construtor");
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			
			Toast.makeText(context, "下载", Toast.LENGTH_LONG).show();
		}

	}

}
