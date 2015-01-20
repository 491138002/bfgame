package com.bfgame.app.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;

import com.bfgame.app.R;
import com.bfgame.app.activity.fragment.DownloadFragment;
import com.bfgame.app.activity.fragment.DownloadingFragment;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.base.BaseFragment;
import com.bfgame.app.common.Constants;
import com.bfgame.app.widget.view.DownloadTitleBarLayout;
import com.bfgame.app.widget.view.DownloadTitleBarLayout.OnTitleChangeListener;

public class DownloadActivity extends BaseActivity {
	private static final long serialVersionUID = 4919492968242858440L;

	public final String ARGUMENTS_NAME = "arg";
	private DownloadTitleBarLayout gift_title;
	private ViewPager viewPager;
	
	public String[] tabTitle = { "下载中", "已下载"}; // 标题
	public int[] ids={R.drawable.gift,R.drawable.gift,R.drawable.gift_save};
	private TabFragmentPagerAdapter mAdapter;

	private DownloadingFragment downloadingFragment;
	private DownloadFragment downloadFragment;

	private Button title_back_btn;
	private Button title_search_btn;
	private Button title_download_btn;
	

	public DownloadTitleBarLayout.OnTitleChangeListener onTitleChangeListener = new OnTitleChangeListener() {
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
				if (downloadingFragment == null) {
					downloadingFragment = new DownloadingFragment();
					downloadingFragment.setContext(context);
					downloadingFragment.init();
				}
				ft = downloadingFragment;
				break;
			case 1:
				if (downloadFragment == null) {
					downloadFragment = new DownloadFragment();
					downloadFragment.setContext(context);
				}
				ft = downloadFragment;
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
		setContentView(R.layout.bfgame_activity_download_main);
	}

	@Override
	public void init() {
	}

	@Override
	public void initView() {
		gift_title = (DownloadTitleBarLayout) findViewById(R.id.gift_title);
		viewPager = (ViewPager) findViewById(R.id.viewPager_gift);

	}

	@Override
	public void initListener() {

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
		viewPager.setOffscreenPageLimit(0);
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
		if (downloadingFragment != null)
			downloadingFragment.downloadStateUpdate();
		if (downloadFragment != null)
			downloadFragment.downloadStateUpdate();
	}

}
