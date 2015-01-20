package com.bfgame.app.widget.view;

import java.lang.reflect.Method;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class CustomGallery extends Gallery {

	public CustomGallery(Context context, AttributeSet attrSet) {
		super(context, attrSet);
		// TODO Auto-generated constructor stub
	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		// return super.onFling(e1, e2, 0, velocityY);//方法一：只去除翻页惯性
		// return false;//方法二：只去除翻页惯性 注：没有被注释掉的代码实现了开始说的2种效果。
		int kEvent;
		if (isScrollingLeft(e1, e2)) {
			// Check if scrolling left
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else {
			// Otherwise scrolling right
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(kEvent, null);
		return true;
	}

	
	public static Method getMethod(Class clazz, String methodName,
			final Class[] classes) throws Exception {
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(methodName, classes);
		} catch (NoSuchMethodException e) {
			try {
				method = clazz.getMethod(methodName, classes);
			} catch (NoSuchMethodException ex) {
				if (clazz.getSuperclass() == null) {
					return method;
				} else {
					method = getMethod(clazz.getSuperclass(), methodName,
							classes);
				}
			}
		}
		return method;
	}

	public void next() {
		Method method;
		try {
			method = getMethod(this.getClass(), "moveNext", new Class[] {});
			method.setAccessible(true);
			method.invoke(this, new Object[] {});
//			if (getSelectedItemId() < getChildCount()) {
//				View view = getChildAt((int) getSelectedItemId() + 1);
//				mPrev = getChildAt((int) getSelectedItemId());
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void previous() {
		Method method;
		try {
			method = getMethod(this.getClass(), "movePrevious", new Class[] {});
			method.setAccessible(true);
			method.invoke(this, new Object[] {});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
