package com.bfgame.app.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebSettings.RenderPriority;
import android.widget.ProgressBar;

import com.bfgame.app.R;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.util.StringUtil;

public class GameStrategyActivity extends BaseActivity{

	private static final long serialVersionUID = 3711341538451528712L;
	private WebView webview;
	private ProgressBar webPb;
	private String url;
	
	@Override
	public void initLayout() {
		setContentView(R.layout.bfgame_activity_game_strategy);
	}

	@Override
	public void init() {
		url = getIntent().getExtras().getString("url");
		if(StringUtil.isEmpty(url)){
			showToast(getString(R.string.bfgame_not_strategy));
			finish();
		}
	}

	@Override
	public void initView() {
		webview = (WebView) findViewById(R.id.webview);
		webPb = (ProgressBar) findViewById(R.id.web_pb);
	}

	@Override
	public void initListener() {
		findViewById(R.id.gift_title_back_btn).setOnClickListener(this);
	}

	@SuppressLint("SetJavaScriptEnabled") @Override
	public void initValue() {
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setSavePassword(false);
		webview.getSettings().setBlockNetworkImage(true);
		webview.getSettings().setRenderPriority(RenderPriority.HIGH);
		webview.getSettings().setBuiltInZoomControls(false);
		
		webview.setWebChromeClient(new WebChromeClient(){
        	public void onProgressChanged(WebView view,int progress){//载入进度改变而触发 
                super.onProgressChanged(view, progress);  
                webPb.setProgress(progress);
                if (progress == 100) {
					webview.getSettings().setBlockNetworkImage(false);
					webPb.setVisibility(View.GONE);
				}else{
					webPb.setVisibility(View.VISIBLE);
				}
            }   
        });
		webview.loadUrl(url);
	}

	@Override
	public void downloadStateUpdate() {
		
	}

}
