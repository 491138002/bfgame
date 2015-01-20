package com.bfgame.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bfgame.app.R;
import com.bfgame.app.activity.adapter.CategoryResultAdapter;
import com.bfgame.app.base.BFBaseAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.common.Constants;
import com.bfgame.app.net.utils.RequestParameter;
import com.bfgame.app.procotol.GameResponseMessage;
import com.bfgame.app.util.AnimationUtil;
import com.bfgame.app.util.StringUtil;
import com.bfgame.app.vo.GameInfo;
import com.bfgame.app.widget.view.PullToRefreshListView;
import com.bfgame.app.widget.view.PullToRefreshListView.OnRefreshListener;

/**
 * 分类详情
 * @author admin
 *
 */
public class CategoryActivity extends BaseActivity {

	private static final long serialVersionUID = 7656902139384572185L;

	private final int GET_CATEGORY_RESULT_KEY = 1;

	private Button title_back_btn;
	private TextView title_content_tv;
	private Button title_search_btn;
	private Button title_gift_btn;
	private Button title_download_btn;
	
	private PullToRefreshListView listview;

	private CategoryResultAdapter resultAdapter;
	
	private List<GameInfo> dataList;
	private String more;
	private String title;

	@Override
	public void initLayout() {
		setContentView(R.layout.bfgame_activity_category_result);
	}

	@Override
	public void init() {
		title = getIntent().getExtras().getString("title");
		more = getIntent().getExtras().getString("more");
	}

	@Override
	public void initView() {
		title_back_btn = (Button) findViewById(R.id.title_back_btn);
		title_content_tv = (TextView) findViewById(R.id.title_content_tv);
		title_search_btn = (Button) findViewById(R.id.title_search_btn);
		title_gift_btn = (Button) findViewById(R.id.title_gift_btn);
		title_download_btn = (Button) findViewById(R.id.title_download_btn);
		
		listview = (PullToRefreshListView) findViewById(R.id.listview);
		loadView = findViewById(R.id.loading_layout);
		bottomlogo = getLayoutInflater().inflate(R.layout.bfgame_activity_bottom_logo, null);
	}

	@Override
	public void initListener() {
		title_back_btn.setOnClickListener(this);
		title_gift_btn.setOnClickListener(this);
		title_search_btn.setOnClickListener(this);
		title_download_btn.setOnClickListener(this);
		listview.setonRefreshListener(new OnRefreshListener() {

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
						Constants.IS_REFRESH=true;
						more = getIntent().getExtras().getString("more");
						getCategoryResultByURL(more);
						listview.onRefreshComplete();
					}
				}.execute(null, null, null);
			}
		});
	}

	@Override
	public void initValue() {
		//set loading icon anim
		ImageView loading_icon_iv = (ImageView) loadView.findViewById(R.id.loading_icon_iv);
		AnimationUtil.startLoadingAnim(loading_icon_iv);
		
		title_content_tv.setText(title);
		
		resultAdapter = new CategoryResultAdapter(context, listview);
		listview.setDividerHeight(0);
		listview.setAdapter(resultAdapter);
		addFootView();
		
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if(resultAdapter != null && (resultAdapter.updateType() == BFBaseAdapter.TYPE_IS_NULL || resultAdapter.updateType() == BFBaseAdapter.TYPE_LOADING)){
			getCategoryResultByURL(more);
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
			getCategoryResultByURL(more);
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
	public void getCategoryResultByURL(String url){
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		startHttpRequst(Constants.HTTP_GET, url,
				parameter, false, "", false, Constants.CONNECTION_SHORT_TIMEOUT,
				Constants.READ_SHORT_TIMEOUT,
				GET_CATEGORY_RESULT_KEY);
	}

	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		super.onCallbackFromThread(resultJson, resultCode);

		try {
			switch (resultCode) {
			case GET_CATEGORY_RESULT_KEY:
				GameResponseMessage gameResponseMessage = new GameResponseMessage();
				gameResponseMessage.parseResponse(resultJson);
				dataList = gameResponseMessage.getResultList();
				
				if (Constants.IS_REFRESH) {
					resultAdapter.resetList(dataList);
				} else {
					resultAdapter.addLast(dataList);
				}
				more = gameResponseMessage.getMore();
				if(StringUtil.isEmpty(more)){
					listview.removeFooterView(footView);
					if (!Constants.IS_REFRESH) {
						listview .addFooterView(bottomlogo);
					}
				}
				
				Constants.IS_REFRESH=false;
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
		
		super.onDestroy();
	}

	@Override
	public void downloadStateUpdate() {
		if(resultAdapter != null)
			resultAdapter.notifyDataSetChanged();
	}
}
