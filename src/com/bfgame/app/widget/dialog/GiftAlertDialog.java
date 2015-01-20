package com.bfgame.app.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bfgame.app.R;
import com.bfgame.app.util.DeviceUtil;
/**
 * 领取礼包弹出框
 * @author admin
 *
 */
public class GiftAlertDialog extends Dialog implements
		android.view.View.OnClickListener {

	TextView tv_code;
	Button bt_copy;
	Button bt_close;
	String str = "";

	public GiftAlertDialog(Context context, String str) {
		super(context);
		this.str = str;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置view样式
		setContentView(R.layout.bfgame_custom_gift_alert_dialog);
		tv_code = (TextView) findViewById(R.id.tv_code);
		bt_close = (Button) findViewById(R.id.bt_close);
		bt_copy = (Button) findViewById(R.id.bt_copy);

		tv_code.setText(str);
		bt_close.setOnClickListener(this);
		bt_copy.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_close:
			dismiss();
			break;

		case R.id.bt_copy:
			DeviceUtil.copyToClipboard(getContext(), str);
			Toast.makeText(getContext(), getContext().getString(R.string.bfgame_toast_copy), Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
