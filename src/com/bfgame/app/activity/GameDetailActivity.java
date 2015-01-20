package com.bfgame.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.BaseJson;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bfgame.app.R;
import com.bfgame.app.activity.adapter.GameDetailRecommendAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.common.Constants;
import com.bfgame.app.download.DownloadUtil;
import com.bfgame.app.net.utils.RequestParameter;
import com.bfgame.app.procotol.GameResponseMessage;
import com.bfgame.app.procotol.GiftResponseMessage;
import com.bfgame.app.util.AnimationUtil;
import com.bfgame.app.util.DeviceUtil;
import com.bfgame.app.util.LogUtil;
import com.bfgame.app.util.StatisticsUtil;
import com.bfgame.app.util.StringUtil;
import com.bfgame.app.util.preference.Preferences;
import com.bfgame.app.vo.GameInfo;
import com.bfgame.app.vo.Gift;
import com.bfgame.app.widget.view.HorizontialListView;
import com.bfgame.app.widget.view.adapter.ImageAdapter;
import com.google.gson.reflect.TypeToken;

public class GameDetailActivity extends BaseActivity {

	private static final long serialVersionUID = 3199197276139704329L;
	private final int GET_GAME_DETAIL_KEY = 1;
	private final int GET_GIFT_CODE_KEY = 2;

	private Button title_back_btn;
	private TextView title_content_tv;
	private Button title_search_btn;
	private Button title_gift_btn;
	private Button title_download_btn;

	private ScrollView body_layout;
	private ImageView detail_icon_iv;
	private TextView detail_name_tv;
	private TextView detail_download_number_tv;
	private TextView detail_size_tv;
	private TextView detail_version_tv;
	private Button detail_download_btn;
	private Button detail_bottom_download;
	private LinearLayout detail_bottom_layout;
	private Button detail_game_strategy_btn;
	private ImageView detail_game_strategy_line_iv;
	private HorizontialListView detail_gallery;
	private TextView detail_desc_tv;
	private TextView detail_desc_show_tv;
	private HorizontialListView detail_recommend_hl;


	private GameInfo gameInfo;
	private List<Gift> giftList;
	/**
	 * 领取礼包列表
	 */
	private Map<String, Gift> receiveGiftList = new HashMap<String, Gift>();
	private LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

	/**
	 * 截图Gallery高度
	 */
	private int customGalleryHeight = 345;

	/**
	 * 截图Adapter
	 */
	private ImageAdapter galleryAdapter;

	/**
	 * 推荐应用Adapter
	 */
	private GameDetailRecommendAdapter recAdapter;

	/**
	 * 是否显示全部介绍
	 */
	private boolean isShowAllDesc = false;

	/**
	 * 上一次滑动Y轴值
	 */
	private int preScrollY = 0;

	/**
	 * 获取详情地址
	 */
	private String url;

	@Override
	public void initLayout() {
		setContentView(R.layout.bfgame_activity_game_detail);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void init() {
		Display display = getWindowManager().getDefaultDisplay();
		customGalleryHeight *= display.getWidth() / 480.0f;

		url = getIntent().getExtras().getString("url");
	}

	@Override
	public void initView() {
		title_back_btn = (Button) findViewById(R.id.title_back_btn);
		title_content_tv = (TextView) findViewById(R.id.title_content_tv);
		title_search_btn = (Button) findViewById(R.id.title_search_btn);
		title_gift_btn = (Button) findViewById(R.id.title_gift_btn);
		title_download_btn = (Button) findViewById(R.id.title_download_btn);

		body_layout = (ScrollView) findViewById(R.id.body_layout);
		detail_icon_iv = (ImageView) findViewById(R.id.detail_icon_iv);
		detail_name_tv = (TextView) findViewById(R.id.detail_name_tv);
		detail_download_number_tv = (TextView) findViewById(R.id.detail_download_number_tv);
		detail_size_tv = (TextView) findViewById(R.id.detail_size_tv);
		detail_version_tv = (TextView) findViewById(R.id.detail_version_tv);
		detail_download_btn = (Button) findViewById(R.id.detail_download_btn);
		detail_bottom_download = (Button) findViewById(R.id.bottom_download);
		detail_bottom_layout = (LinearLayout) findViewById(R.id.detail_bottom_layout);
		detail_game_strategy_btn = (Button) findViewById(R.id.detail_game_strategy_btn);
		detail_game_strategy_line_iv = (ImageView) findViewById(R.id.detail_game_strategy_line_iv);
		detail_gallery = (HorizontialListView) findViewById(R.id.detail_gallery);
		detail_desc_tv = (TextView) findViewById(R.id.detail_desc_tv);
		detail_desc_show_tv = (TextView) findViewById(R.id.detail_desc_show_tv);
		detail_recommend_hl = (HorizontialListView) findViewById(R.id.detail_recommend_hl);

		loadView = findViewById(R.id.loading_layout);
	}

	@Override
	public void initListener() {
		title_back_btn.setOnClickListener(this);
		title_search_btn.setOnClickListener(this);
		title_gift_btn.setOnClickListener(this);
		title_download_btn.setOnClickListener(this);
		detail_download_btn.setOnClickListener(this);
		detail_game_strategy_btn.setOnClickListener(this);
		detail_desc_show_tv.setOnClickListener(this);
		detail_bottom_download.setOnClickListener(this);
	}

	@Override
	public void initValue() {
		initReceiveGiftList();

		ImageView loading_icon_iv = (ImageView) loadView
				.findViewById(R.id.loading_icon_iv);
		AnimationUtil.startLoadingAnim(loading_icon_iv);

		title_content_tv.setText(R.string.bfgame_game_detail_title);

		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) detail_gallery
				.getLayoutParams();
		params.height = customGalleryHeight;// DeviceUtil.dip2px(context, );
		detail_gallery.setLayoutParams(params);

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (gameInfo == null) {
			getGameDetail(url);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		int id = v.getId();
		if (id == R.id.detail_download_btn) {
			if (gameInfo != null)
				if (title_download_btn.getText().equals("下载")) {

					AnimationUtil.startDownLoadAnim(context,
							title_download_btn, null);
					new Handler().postDelayed(new Runnable() {

						public void run() {

							DownloadUtil.downloadClick(context,
									preferencesUtils, gameInfo.getId(),1,gameInfo.getCategoryId(),
									gameInfo.getName(),
									gameInfo.getDownloadUri(),
									gameInfo.getPackageName(),
									gameInfo.getIcon(),
									gameInfo.getVersionCode(),
									gameInfo.getVersionName(),
									gameInfo.getDownloadNum(),
									gameInfo.getSize(), detail_download_btn);
						}

					}, 500);
				}

			DownloadUtil.downloadClick(context, preferencesUtils,
					gameInfo.getId(),1,gameInfo.getCategoryId(), gameInfo.getName(),
					gameInfo.getDownloadUri(), gameInfo.getPackageName(),
					gameInfo.getIcon(), gameInfo.getVersionCode(),
					gameInfo.getVersionName(), gameInfo.getDownloadNum(),
					gameInfo.getSize(), detail_download_btn);

		} else if (id == R.id.bottom_download) {

			DownloadUtil.downloadClick(context, preferencesUtils,
					gameInfo.getId(),1,gameInfo.getCategoryId(), gameInfo.getName(),
					gameInfo.getDownloadUri(), gameInfo.getPackageName(),
					gameInfo.getIcon(), gameInfo.getVersionCode(),
					gameInfo.getVersionName(), gameInfo.getDownloadNum(),
					gameInfo.getSize(), detail_bottom_download);
		} else if (id == R.id.detail_game_strategy_btn) {
			Bundle data = new Bundle();
			data.putString("url", gameInfo.getRaiders());
			showActivity(context, GameStrategyActivity.class, data);
		} else if (id == R.id.detail_desc_show_tv) {
			isShowAllDesc = !isShowAllDesc;
//			detail_desc_tv.setText(Html.fromHtml(getGameDesc()));
			detail_desc_tv.setText(getGameDesc());
			if (isShowAllDesc) {
				detail_desc_show_tv.setText(R.string.bfgame_description_hide);
				setRightDrawable(0);
			} else {
				detail_desc_show_tv.setText(R.string.bfgame_description_show);
				setRightDrawable(1);
			}
		}
	}

	/**
	 * 初始化领取礼包列表
	 */
	@SuppressWarnings("unchecked")
	public void initReceiveGiftList() {
		
		giftList = (List<Gift>) BaseJson.parser(new TypeToken<List<Gift>>() {
		}, preferencesUtils.getString(Preferences.RECEIVE_GIFT, ""));
		if (giftList == null)
			giftList = new ArrayList<Gift>();
		this.receiveGiftList.clear();
		for (int i = 0; giftList != null && i < giftList.size(); i++) {
			for (int j = 0; j < giftList.get(i).getGiftList().size(); j++) {
				this.receiveGiftList.put(giftList.get(i).getGiftList().get(j)
						.getGiftId(), giftList.get(i).getGiftList().get(j));
			}
		}
	}

	/**
	 * 设置领取的礼包
	 * @param gift
	 */
	public void setRecieveGift(Gift gift) {
		if (gameInfo.getGift().getGiftId().equals(gift.getGiftId())) {
			Gift bean = new Gift();
			bean.setGameInfo(gameInfo);
			bean.getGiftList().get(0)
					.setActivationCode(gift.getActivationCode());
			bean.getGiftList().get(0).setGiftCount(gift.getGiftCount());
			bean.getGiftList().get(0).setGiftState(gift.getGiftState());
			bean.getGiftList().get(0).setGiftExplain(gameInfo.getGift().getGiftExplain());
			bean.getGiftList().get(0).setGiftStartTime(gift.getGiftStartTime());
			bean.getGiftList().get(0).setGiftEndTime(gift.getGiftEndTime());
			bean.getGiftList().get(0).setGiftId(gift.getGiftId());
			bean.setActivationCode(gift.getActivationCode());
			bean.setGiftExplain(gameInfo.getGift().getGiftExplain());
			gameInfo.setGift(gift);
			initReceiveGiftList();
			giftList.add(bean);
			receiveGiftList.put(gift.getGiftId(), bean);
			preferencesUtils.putString(Preferences.RECEIVE_GIFT,
					BaseJson.toJson(giftList));
			setGiftList();
		}
	}

	/**
	 * 设置界面信息
	 */
	public void setInfo() {
		if (gameInfo != null) {
			StatisticsUtil.addShowPage(preferencesUtils, gameInfo.getId(),1);
			mImageFetcher.loadImage(gameInfo.getIcon(), detail_icon_iv);

			galleryAdapter = new ImageAdapter(context, detail_gallery,
					customGalleryHeight);
			detail_gallery.setAdapter(galleryAdapter);
			galleryAdapter.addLast(gameInfo.getPreviewList(), false);

			recAdapter = new GameDetailRecommendAdapter(context,
					detail_recommend_hl);
			detail_recommend_hl.setAdapter(recAdapter);
			recAdapter.addLast(gameInfo.getRecommendList(), false);

			mImageFetcher.loadImage(gameInfo.getIcon(), detail_icon_iv);
			detail_name_tv.setText(gameInfo.getName());
			detail_download_number_tv.setText(gameInfo.getDownloadCount());
			detail_size_tv.setText(gameInfo.getSize());
			detail_version_tv.setText(gameInfo.getVersionName());
//			detail_desc_tv.setText(Html.fromHtml(getGameDesc()));
			detail_desc_tv.setText(getGameDesc());
			detail_download_btn.setText(DownloadUtil.getDownloadText(context,
					gameInfo.getId(), gameInfo.getPackageName(),
					gameInfo.getVersionCode(), gameInfo.getName()));
			detail_download_btn.setBackgroundColor(Color
					.parseColor(DownloadUtil.getTextBg(context,
							gameInfo.getId(), gameInfo.getPackageName(),
							gameInfo.getVersionCode(), gameInfo.getName())));
			detail_bottom_download.setText(DownloadUtil.getDownloadText(
					context, gameInfo.getId(), gameInfo.getPackageName(),
					gameInfo.getVersionCode(), gameInfo.getName()));
			detail_bottom_download.setBackgroundColor(Color
					.parseColor(DownloadUtil.getTextBg(context,
							gameInfo.getId(), gameInfo.getPackageName(),
							gameInfo.getVersionCode(), gameInfo.getName())));

			if (StringUtil.isEmpty(gameInfo.getRaiders())) {
				detail_game_strategy_btn.setVisibility(View.GONE);
				detail_game_strategy_line_iv.setVisibility(View.GONE);
			} else {
				detail_game_strategy_btn.setVisibility(View.VISIBLE);
				detail_game_strategy_line_iv.setVisibility(View.VISIBLE);
			}

			setGiftList();

			body_layout.postDelayed(new Runnable() {

				@Override
				public void run() {
					body_layout.scrollTo(0, 0);
				}
			}, 10);
		}
	}

	/**
	 * 设置礼包列表
	 */
	public void setGiftList() {
		detail_bottom_layout.removeAllViews();
		if (gameInfo.getGift() == null
				|| StringUtil.isEmpty(gameInfo.getGift().getGiftId())) {
			detail_bottom_layout.setVisibility(View.GONE);
		} else {
			detail_bottom_layout.setVisibility(View.VISIBLE);
			View childView = getLayoutInflater().inflate(
					R.layout.bfgame_detail_gift_item, null);
			detail_bottom_layout.addView(childView);

			RelativeLayout item_body_item_layout = (RelativeLayout) childView
					.findViewById(R.id.item_body_item_layout);
			TextView item_time_tv = (TextView) childView
					.findViewById(R.id.item_time_tv);
			TextView item_surplus_tv = (TextView) childView
					.findViewById(R.id.item_surplus_tv);
			TextView item_gift_content_tv = (TextView) childView
					.findViewById(R.id.item_gift_content_tv);
			final Button item_get_btn = (Button) childView
					.findViewById(R.id.item_get_btn);

			RelativeLayout item_code_layout = (RelativeLayout) childView
					.findViewById(R.id.item_code_layout);
			TextView item_expire_tv = (TextView) childView
					.findViewById(R.id.item_expire_tv);
			final EditText item_code_et = (EditText) childView
					.findViewById(R.id.item_code_et);
			final Button item_copy_btn = (Button) childView
					.findViewById(R.id.item_copy_btn);
			if (receiveGiftList.get(gameInfo.getGift().getGiftId()) != null
					&& StringUtil
							.isNotEmpty(receiveGiftList.get(
									gameInfo.getGift().getGiftId())
									.getActivationCode())) {
				
				final Gift childBean = receiveGiftList.get(gameInfo.getGift()
						.getGiftId());
				item_body_item_layout.setVisibility(View.GONE);
				item_code_layout.setVisibility(View.VISIBLE);

				item_expire_tv
						.setText(context.getString(R.string.bfgame_gift_expire,
								childBean.getGiftEndTime()));
				
				item_code_et.setText(childBean.getActivationCode());
				item_copy_btn.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						DeviceUtil.copyToClipboard(context, item_code_et
								.getText().toString());
						showToast(context.getString(R.string.bfgame_toast_copy));
					}
				});
			} else {
				final Gift childBean = gameInfo.getGift();
				item_body_item_layout.setVisibility(View.VISIBLE);
				item_code_layout.setVisibility(View.GONE);

				item_time_tv.setText(childBean.getGiftStartTime());
				item_surplus_tv.setText("剩余"+childBean.getGiftRemain()+"份");
				item_gift_content_tv.setText(childBean.getGiftInfo());
				item_get_btn.setText(R.string.bfgame_gift_receive);
				item_get_btn.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						item_get_btn
								.setText(R.string.bfgame_gift_receive_loading);
						getGiftCode(childBean.getGiftId());
					}
				});
				
			}
		}
	}

	public ImageView getLine() {
		ImageView imgView = new ImageView(context);
		imgView.setBackgroundResource(R.drawable.bfgame_gray_line);
		imgView.setLayoutParams(lineParams);
		return imgView;
	}

	public void hideLoadView() {
		if (loadView.getVisibility() != View.GONE)
			loadView.setVisibility(View.GONE);
	}

	/**
	 * request game detail datas
	 */
	public void getGameDetail(String url) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		
		startHttpRequst(Constants.HTTP_GET, url, parameter, false, "", false,
				Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT, GET_GAME_DETAIL_KEY);
	}

	/**
	 * request gift code datas
	 */
	public void getGiftCode(String giftid) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("giftid", giftid));
		parameter.add(new RequestParameter("imei", DeviceUtil.getImei(this)));
		parameter.add(new RequestParameter("service", "getgift"));
		startHttpRequst(Constants.HTTP_POST, Constants.URL_MAIN_GET_GIFT,
				parameter, false, "", false,
				Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT, GET_GIFT_CODE_KEY);
	}

	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		super.onCallbackFromThread(resultJson, resultCode);

		try {
			switch (resultCode) {
			case GET_GAME_DETAIL_KEY:
				GameResponseMessage gameResponseMessage = new GameResponseMessage();
				gameResponseMessage.parseResponse(resultJson);
				gameInfo = gameResponseMessage.getResult();
				setInfo();
				hideLoadView();
				break;
			case GET_GIFT_CODE_KEY:
				GiftResponseMessage giftResponseMessage = new GiftResponseMessage();
				giftResponseMessage.parseResponse(resultJson);
				if (giftResponseMessage.getResultCode() == 1) {
					setRecieveGift(giftResponseMessage.getResult());
				} else {
					showToast(giftResponseMessage.getMessage());
					setGiftList();
				}

				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getGameDesc() {
		String desc = "";
		if (gameInfo != null) {
			if (isShowAllDesc) {
				preScrollY = body_layout.getScrollY();
				desc = gameInfo.getDescription();
			} else {
				if (preScrollY != 0)
					body_layout.scrollBy(0, preScrollY);
				if (gameInfo.getDescription().length() > 50) {
					desc = gameInfo.getDescription().substring(0, 50) + "...";
				} else {
					desc = gameInfo.getDescription();
				}
			}
		}
		return desc;
	}

	public static void show(Context context, String url) {
		LogUtil.d("=======Game Detail URL", url);
		Bundle data = new Bundle();
		data.putString("url", url);
		showActivity(context, GameDetailActivity.class, data);
	}

	@Override
	protected void onDestroy() {
		if (galleryAdapter != null)
			galleryAdapter.onDestroy();
		if (recAdapter != null)
			recAdapter.onDestroy();
		if (giftList != null) {
			giftList.clear();
			giftList = null;
		}
		if (receiveGiftList != null) {
			receiveGiftList.clear();
			receiveGiftList = null;
		}
		gameInfo = null;
		super.onDestroy();
	}

	@Override
	public void downloadStateUpdate() {
		if (gameInfo != null) {
			detail_download_btn.setText(DownloadUtil.getDownloadText(context,
					gameInfo.getId(), gameInfo.getPackageName(),
					gameInfo.getVersionCode(), gameInfo.getName()));
			detail_bottom_download.setText(DownloadUtil.getDownloadText(context,
					gameInfo.getId(), gameInfo.getPackageName(),
					gameInfo.getVersionCode(), gameInfo.getName()));
		}
	}

	private void setRightDrawable(int state) {
		if (state == 1) {
			Resources res = context.getResources();
			Drawable img_off = res.getDrawable(R.drawable.bfgame_desc_show);
			img_off.setBounds(0, 0, img_off.getMinimumWidth(),
					img_off.getMinimumHeight());
			detail_desc_show_tv.setCompoundDrawables(null, null, img_off, null); // 设置左图标
		} else {
			Resources res = context.getResources();
			Drawable img_off = res.getDrawable(R.drawable.bfgame_desc_hidden);
			img_off.setBounds(0, 0, img_off.getMinimumWidth(),
					img_off.getMinimumHeight());
			detail_desc_show_tv.setCompoundDrawables(null, null, img_off, null); // 设置左图标
		}
	}
}
