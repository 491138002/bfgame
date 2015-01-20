package com.bfgame.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.bfgame.app.R;
import com.bfgame.app.activity.adapter.SearchAdapter;
import com.bfgame.app.activity.adapter.SearchKeyAdapter;
import com.bfgame.app.base.BFBaseAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.common.Constants;
import com.bfgame.app.net.utils.RequestParameter;
import com.bfgame.app.procotol.GameResponseMessage;
import com.bfgame.app.procotol.SearchKeyResponseMessage;
import com.bfgame.app.util.AnimationUtil;
import com.bfgame.app.util.DeviceUtil;
import com.bfgame.app.util.StatisticsUtil;
import com.bfgame.app.util.StringUtil;
import com.bfgame.app.vo.GameInfo;

public class SearchActivity extends BaseActivity {

	private static final long serialVersionUID = -5417376378891474216L;
	private final int GET_SEARCH_KEY = 1;
	private final int GET_SEARCH_RESULT_KEY = 2;

	private Button title_back_btn;
	private EditText title_search_et;
	private Button title_search_btn;
	private ListView listview;
	private SearchAdapter resultAdapter;
	private SearchKeyAdapter keyAdapter;
	
	private List<GameInfo> dataList;
	private String more;

	@Override
	public void initLayout() {
		setContentView(R.layout.bfgame_activity_search);
	}

	@Override
	public void init() {
		StatisticsUtil.addShowPage(preferencesUtils, -7,-7);
	}

	@Override
	public void initView() {
		title_back_btn = (Button) findViewById(R.id.title_back_btn);
		title_search_et = (EditText) findViewById(R.id.title_search_et);
		title_search_btn = (Button) findViewById(R.id.title_search_btn);
		listview = (ListView) findViewById(R.id.listview);
		loadView = findViewById(R.id.loading_layout);
	}

	@Override
	public void initListener() {
		title_back_btn.setOnClickListener(this);
		title_search_btn.setOnClickListener(this);
	}

	@Override
	public void initValue() {
		//set loading icon anim
		ImageView loading_icon_iv = (ImageView) loadView.findViewById(R.id.loading_icon_iv);
		AnimationUtil.startLoadingAnim(loading_icon_iv);
		bottomlogo = getLayoutInflater().inflate(R.layout.bfgame_activity_bottom_logo, null);
		keyAdapter = new SearchKeyAdapter(context, listview);
		resultAdapter = new SearchAdapter(context, listview);
		
		listview.setDividerHeight(0);
		listview.setAdapter(keyAdapter);
		
		handler.postDelayed(new Runnable() {
			public void run() {
				DeviceUtil.showIMM(context, title_search_et);
			}
		}, 500);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		if(keyAdapter != null && (keyAdapter.updateType() == BFBaseAdapter.TYPE_IS_NULL
				|| keyAdapter.updateType() == BFBaseAdapter.TYPE_LOADING)){
			getSearchKey();
		}
	}
	
	public void addFootView(){
		// add loading footbar
		footView = getLayoutInflater().inflate(R.layout.bfgame_list_footbar, null);
		ImageView loading_icon_iv = (ImageView) footView.findViewById(R.id.loading_icon_iv);
		AnimationUtil.startLoadingAnim(loading_icon_iv);
		listview.addFooterView(footView, null, false);
	}

	public void nextPage() {
		if(StringUtil.isNotEmpty(more)){
			getSearchResultByURL(more);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		int id = v.getId();
		if (id == R.id.title_back_btn) {
			DeviceUtil.hideIMM(context, title_search_et);
			goBack();
		} else if (id == R.id.title_search_btn) {
			if(StringUtil.isNotEmpty(title_search_et.getText().toString())){
				getSearchResult(title_search_et.getText().toString());
			}else{
				showToast(getString(R.string.bfgame_search_key_is_null));
			}
		}
	}
	
	public void showLoadView(){
		if(loadView.getVisibility() != View.VISIBLE)
			loadView.setVisibility(View.VISIBLE);
	}
	
	public void hideLoadView(){
		if(loadView.getVisibility() != View.GONE)
			loadView.setVisibility(View.GONE);
	}
	
	/**
	 * request search key datas
	 */
	public void getSearchKey(){
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("service", "findkey"));
		startHttpRequst(Constants.HTTP_GET, Constants.URL_SEARCH,
				parameter, false, "", false, Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT,
				GET_SEARCH_KEY);
	}
	
	/**
	 * request search key datas
	 */
	public void getSearchResult(String key){
		showLoadView();
		resultAdapter.clear();
		addFootView();
		listview.setAdapter(resultAdapter);
		title_search_et.setText(key);
		DeviceUtil.hideIMM(context, title_search_et);
		
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("service", "info"));
		parameter.add(new RequestParameter("key", key));
		startHttpRequst(Constants.HTTP_GET, Constants.URL_SEARCH,
				parameter, false, "", false, Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT,
				GET_SEARCH_RESULT_KEY);
	}
	
	/**
	 * request search key datas
	 */
	public void getSearchResultByURL(String url){
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		startHttpRequst(Constants.HTTP_GET, url,
				parameter, false, "", false, Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT,
				GET_SEARCH_RESULT_KEY);
	}

	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		super.onCallbackFromThread(resultJson, resultCode);

		try {
			switch (resultCode) {
			case GET_SEARCH_KEY:
				SearchKeyResponseMessage searchKeyResponseMessage = new SearchKeyResponseMessage();
				searchKeyResponseMessage.parseResponse(resultJson);
				keyAdapter.addItem(searchKeyResponseMessage.getResult());
				
				hideLoadView();
				if(searchKeyResponseMessage.getResultCode() != 1){
					showToast(searchKeyResponseMessage.getMessage());
				}
				break;
			case GET_SEARCH_RESULT_KEY:
				GameResponseMessage gameResponseMessage = new GameResponseMessage();
				gameResponseMessage.parseResponse(resultJson);
				dataList = gameResponseMessage.getResultList();
				more = gameResponseMessage.getMore();
				resultAdapter.addLast(dataList);
				if(StringUtil.isEmpty(more)){
					listview.removeFooterView(footView);
					listview.addFooterView(bottomlogo);
				}
				hideLoadView();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		if(resultAdapter != null)
			resultAdapter.onDestroy();
		if(keyAdapter != null)
			keyAdapter.onDestroy();
		if(title_search_et != null)
			DeviceUtil.hideIMM(context, title_search_et);
		
		super.onDestroy();
	}

	@Override
	public void downloadStateUpdate() {
		if(resultAdapter != null)
			resultAdapter.notifyDataSetChanged();
	}
}
