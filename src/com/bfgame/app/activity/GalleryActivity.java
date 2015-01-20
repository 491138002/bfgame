package com.bfgame.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.BaseJson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

import com.bfgame.app.R;
import com.bfgame.app.activity.adapter.GalleryAdapter;
import com.bfgame.app.base.BaseActivity;
import com.bfgame.app.vo.GameInfo;
import com.bfgame.app.widget.view.PointWidget;
import com.google.gson.reflect.TypeToken;
/**
 * 游戏截图轮播
 * @author admin
 *
 */
public class GalleryActivity extends BaseActivity {

	private static final long serialVersionUID = -8181347480824874766L;
	private ViewPager viewPager;
	private GalleryAdapter adapter;
	private List<GameInfo> dataList;
	private PointWidget pw;
	private int select;

	@Override
	public void initLayout() {
		setContentView(R.layout.bfgame_activity_gallery);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		select = getIntent().getExtras().getInt("select");
		dataList = (List<GameInfo>) BaseJson.parser(
				new TypeToken<List<GameInfo>>() {
				}, getIntent().getExtras().getString("info"));
		if (dataList == null)
			dataList = new ArrayList<GameInfo>();
	}

	@Override
	public void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		pw = (PointWidget) findViewById(R.id.viewPoint);
	}

	int startX = 0;

	@Override
	public void initListener() {
	}

	@Override
	public void initValue() {
		adapter = new GalleryAdapter(context, dataList, viewPager, pw);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(select);

	}

	public void close() {
		finish();
		overridePendingTransition(R.anim.bfgame_fade_in, R.anim.bfgame_fade_out);
	}

	@Override
	public void downloadStateUpdate() {

	}
	private float x, y;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x = event.getX();
            y = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (x == event.getX() && y == event.getY()) {
                finish();
                overridePendingTransition(R.anim.bfgame_fade_in, R.anim.bfgame_fade_out);
            }
        }
        return super.dispatchTouchEvent(event);
    }
	public static void show(Context context, List<GameInfo> dataList, int select) {
		Bundle data = new Bundle();
		data.putString("info", BaseJson.toJson(dataList));
		data.putInt("select", select);
		Intent intent = new Intent();
		intent.setAction(getActivityAction(GalleryActivity.class));
		intent.putExtras(data);
		context.startActivity(intent);
		if (context instanceof Activity)
			((Activity) context).overridePendingTransition(
					R.anim.bfgame_fade_in, R.anim.bfgame_fade_out);
	}

	
}
