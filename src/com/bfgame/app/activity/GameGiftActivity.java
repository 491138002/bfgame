package com.bfgame.app.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;

import com.bfgame.app.R;
import com.bfgame.app.activity.fragment.OnlineGiftFragment;
import com.bfgame.app.activity.fragment.SaveGiftFragment;
import com.bfgame.app.activity.fragment.SingleGiftFragment;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.common.Constants;
import com.bfgame.app.widget.view.GiftTitleBarLayout;
import com.bfgame.app.widget.view.GiftTitleBarLayout.OnTitleChangeListener;

public class GameGiftActivity extends BaseActivity {

	private static final long serialVersionUID = -3330317059508167733L;

	public final String ARGUMENTS_NAME = "arg";
	private GiftTitleBarLayout gift_title;
	private ViewPager viewPager;
	
	public String[] tabTitle = { "网游", "单机", "存号箱" }; // 标题
	public int[] ids={R.drawable.gift,R.drawable.gift,R.drawable.gift_save};
	private TabFragmentPagerAdapter mAdapter;

	private OnlineGiftFragment onlineGiftFragment;
	private SingleGiftFragment offlineGiftFragment;
	private SaveGiftFragment saveGiftFragment;

	private Button title_back_btn;
	private Button title_search_btn;
	private Button title_download_btn;
	

	public GiftTitleBarLayout.OnTitleChangeListener onTitleChangeListener = new OnTitleChangeListener() {
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
				if (onlineGiftFragment == null) {
					onlineGiftFragment = new OnlineGiftFragment();
					onlineGiftFragment.setContext(context);
					onlineGiftFragment.init();
				}
				ft = onlineGiftFragment;
				break;
			case 1:
				if (offlineGiftFragment == null) {
					offlineGiftFragment = new SingleGiftFragment();
					offlineGiftFragment.setContext(context);
				}
				ft = offlineGiftFragment;
				break;
			case 2:
				if (saveGiftFragment == null) {
					saveGiftFragment = new SaveGiftFragment();
					saveGiftFragment.setContext(context);
				}
				ft = saveGiftFragment;
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
		setContentView(R.layout.bfgame_activity_gift_main);
	}

	@Override
	public void init() {
	}

	@Override
	public void initView() {
		gift_title = (GiftTitleBarLayout) findViewById(R.id.gift_title);
		viewPager = (ViewPager) findViewById(R.id.viewPager_gift);

		title_back_btn = (Button) findViewById(R.id.gift_title_back_btn);
		title_search_btn = (Button) findViewById(R.id.gift_title_search_btn);
		title_download_btn = (Button) findViewById(R.id.gift_title_download_btn);
	}

	@Override
	public void initListener() {
		title_back_btn.setOnClickListener(this);
		title_search_btn.setOnClickListener(this);
		title_download_btn.setOnClickListener(this);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				gift_title.setSelectPosition(position);
				BaseFragment bf = (BaseFragment) mAdapter.getItem(position);
				bf.init();
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				gift_title.setLinePoint(arg0, arg2 / tabTitle.length);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void initValue() {
		viewPager.setOffscreenPageLimit(4);
		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);

		gift_title.setTab(ids,tabTitle, 0);
		gift_title.setOnTitleChangeListener(onTitleChangeListener);
	}

	@Override
	protected void onStart() {
		super.onStart();
		downloadStateUpdate();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gift_title_back_btn:
			goBack();
			break;
		case R.id.gift_title_search_btn:
			showActivity(context, SearchActivity.class);
			break;
		case R.id.gift_title_download_btn:
			Intent intent = new Intent();
			intent.setAction("com.bfgame.app.downloadCenter");
			context.startActivity(intent);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		Constants.NEED_REFRESH_GIFT = true;
		context = null;
		super.onDestroy();
	}



	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
	}


	@Override
	public void downloadStateUpdate() {
		if (offlineGiftFragment != null)
			offlineGiftFragment.downloadStateUpdate();
		if (onlineGiftFragment != null)
			onlineGiftFragment.downloadStateUpdate();
		if (saveGiftFragment != null)
			saveGiftFragment.downloadStateUpdate();
	}
}
