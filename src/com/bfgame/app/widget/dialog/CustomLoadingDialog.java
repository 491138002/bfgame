package com.bfgame.app.widget.dialog;



import java.util.concurrent.TimeUnit;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bfgame.app.R;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.common.Constants;
import com.bfgame.app.net.DefaultThreadPool;
import com.bfgame.app.util.LogUtil;
import com.bfgame.app.util.StringUtil;

/**
 * 
* @ClassName: CustomLoadingDialog
* @Description: 自定义loading对话框
* @author jzy
* @date 2012-7-20 下午03:41:39
*
 */
public class CustomLoadingDialog extends Dialog implements android.view.View.OnClickListener{
	TextView loading_text;
	TextView loading_del_textview;
	static int theme = R.style.bfgame_custom_dialog;
	String content = "";
	boolean isHideCloseBtn = false;
	private Context context;
	private final String DEFAULT_CONTENT = "正在加载，请稍后...";
	
	public CustomLoadingDialog(Context context,String content,boolean isHideCloseBtn) {
		super(context,theme);
	    this.content = content;
	    this.context = context;
	    this.isHideCloseBtn = isHideCloseBtn;
		LogUtil.d("TAG","****************************");
	}
	
	public CustomLoadingDialog(Context context,int content,boolean isHideCloseBtn) {
		super(context,theme);
	    this.content = context.getString(content);
	    this.isHideCloseBtn = isHideCloseBtn;
		LogUtil.d("TAG","****************************");
	}

	protected void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 //去掉标题
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 //设置view样式
		 setContentView(R.layout.bfgame_custom_loading_dialog);	
		 loading_text= (TextView) findViewById(R.id.loading_text);
		 loading_del_textview = (TextView) findViewById(R.id.loading_del_textview);
		 loading_del_textview.setOnClickListener(this);
		 if(isHideCloseBtn){
			 loading_del_textview.setVisibility(View.GONE);
		 }
		 if(StringUtil.isNotEmpty(content)){
			 	loading_text.setText(content);
			}else{
				loading_text.setText(DEFAULT_CONTENT);
			}
	 }
	 //called when this dialog is dismissed
	 protected void onStop() {
		 LogUtil.d("onStop()","this dialog is dismissed");
		 super.onStop();
	 }
	 @Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		 LogUtil.d("show()","this dialog is shown");
		 Constants.IS_STOP_REQUEST = false;
		 
	}
	 @Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		 LogUtil.d("dismiss()","this dialog is dismissed");
		 try{
			 DefaultThreadPool.pool.awaitTermination(1, TimeUnit.MICROSECONDS);
		 }catch (Exception e) {
			 LogUtil.d("awaitTermination","awaitTermination");
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		 LogUtil.d("onStart()","this dialog is shown");
	}
	 
	
	 @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_BACK ) { 
			 if(isHideCloseBtn){
				 Constants.IS_STOP_REQUEST = true;
				 dismiss();
				 if(context instanceof BaseActivity){
					 ((BaseActivity)context).finish();
				 }
				 return super.onKeyDown(keyCode, event);
			 }else{
				 Constants.IS_STOP_REQUEST = true;
				 LogUtil.d("onKeyDown","this dialog is force dismissed");
				 return super.onKeyDown(keyCode, event);
			 }
			 	
		 }
		 
		return super.onKeyDown(keyCode, event);
		
	}
	 

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.loading_del_textview) {
			Constants.IS_STOP_REQUEST = true;
			LogUtil.d("onClick","this dialog is force dismissed");
			dismiss();
		} else {
		}
	}
	 
}
