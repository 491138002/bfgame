package com.bfgame.app.widget.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bfgame.app.R;
import com.bfgame.app.util.DeviceUtil;
/**
 * 礼包中心导航栏
 * @author admin
 *
 */
public class DownloadTitleBarLayout extends LinearLayout{
	
	private Context context;
	private LayoutInflater mInflater;
	
	/**
	 * 屏幕宽高
	 */
	private int screenWidth = 0;
	private int screenHeight = 0;
	
	/**
	 * 线宽高
	 */
	private int lineWidth = 0;
	private int lineHeight = 0;
	
	/**
	 * 文字大小
	 */
	private int textSize = 0;
	private int textHeight = 0;
	
	/**
	 * 选中线
	 */
	private ImageView line;
	
	/**
	 * tab父布局
	 */
	private LinearLayout tabLayout;
	
	/**
	 * 主题颜色
	 */
	private String mainColor = "#0a5cad";
	
	/**
	 * 背景颜色
	 */
	private String bgColor = "#EFEFEF";
	
	/**
	 * 当前选中节点
	 */
	private int curSelectPosition = 0;
	
	private OnTitleChangeListener onTitleChangeListener;
	private TextView textView;
	
	public OnTitleChangeListener getOnTitleChangeListener() {
		return onTitleChangeListener;
	}

	public void setOnTitleChangeListener(OnTitleChangeListener onTitleChangeListener) {
		this.onTitleChangeListener = onTitleChangeListener;
	}

	public DownloadTitleBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		init(context);
	}
	
	/**
	 * 初始化
	 * @param context
	 */
	public void init(Context context){
		WindowManager window = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		window.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		lineWidth = screenWidth/2;
		lineHeight = DeviceUtil.dip2px(2, dm.scaledDensity);
		textHeight = DeviceUtil.dip2px(34, dm.scaledDensity);
		textSize = context.getResources().getDimensionPixelSize(R.dimen.custom_title_text_size);
		setOrientation(VERTICAL);
		setBackgroundColor(Color.parseColor(bgColor));
	}
	
	/**
	 * 设置tab内容
	 * @param tabs
	 * @param position
	 */
	public void setTab(int[] ids, String[] tabs, int position){
		if(tabs != null && tabs.length > 0){
			removeAllViews();
			tabLayout = new LinearLayout(context);
			for (int i = 0; i < tabs.length; i++) {
				ImageView image=(ImageView) this.mInflater.inflate(R.layout.bfgame_tab_titlebar_gift_image, null);
				image.setBackgroundResource(ids[i]);
				textView = (TextView) this.mInflater.inflate(R.layout.bfgame_tab_titlebar_gift_text, null);
				TextView textView1 = (TextView) this.mInflater.inflate(R.layout.bfgame_tab_titlebar_gift_text, null);
				textView.setText(tabs[i]);
				textView1.setWidth(10);
				textView.setTextColor(Color.BLACK);
				textView.setTextSize(16);
				LinearLayout layout=new LinearLayout(context);
				layout.setOrientation(HORIZONTAL);
				layout.addView(image, 0);
				
				layout.addView(textView1, 1);
				layout.addView(textView, 2);
				
				layout.setGravity(Gravity.CENTER);
				layout.setLayoutParams(new LayoutParams(lineWidth, textHeight));
				
				final int m = i;
				textView.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						if(onTitleChangeListener != null){
							onTitleChangeListener.change(m);
						}
					}
				});
				tabLayout.addView(layout);
			}
			this.addView(tabLayout);
			
			RelativeLayout bottomLayout = new RelativeLayout(context);
			bottomLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, lineHeight));
			this.addView(bottomLayout);
			
			ImageView bottomLine = new ImageView(context);
			bottomLine.setBackgroundColor(Color.parseColor("#b5b5b5"));
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, lineHeight/2);
			params2.setMargins(0, lineHeight/2, 0, 0);
			bottomLine.setLayoutParams(params2);
			bottomLayout.addView(bottomLine);
			
			line = new ImageView(context);
			line.setLayoutParams(new RelativeLayout.LayoutParams(lineWidth, lineHeight));
			line.setBackgroundColor(Color.parseColor(mainColor));
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)line.getLayoutParams();
			params.setMargins(0, 0, 0, 0);
			line.setLayoutParams(params);
			bottomLayout.addView(line);
			
			
			curSelectPosition = position;
			setSelectPosition(position);
		}
	}
	
	/**
	 * 设置线位置
	 * @param left
	 */
	public void setLinePoint(int position, int left){
		setLinePoint(position*lineWidth + left);
	}
	
	/**
	 * 设置线位置
	 * @param left
	 */
	public void setLinePoint(int left){
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)line.getLayoutParams();
		params.setMargins(left, 0, 0, 0);
		line.setLayoutParams(params);
	}
	
	/**
	 * 设置选中项
	 * @param position
	 */
	public void setSelectPosition(int position){
		LinearLayout preView = (LinearLayout)tabLayout.findViewWithTag("selected");
		LinearLayout curView = (LinearLayout)tabLayout.getChildAt(position);
		if(preView != null){
			((TextView) preView.getChildAt(2)).setTextColor(Color.BLACK);
			preView.setTag("");
		}
		if(curView != null){
			((TextView) curView.getChildAt(2)).setTextColor(Color.parseColor(mainColor));
			curView.setTag("selected");
		}
	}
	
	public interface OnTitleChangeListener{
		public void change(int i);
	}
}
