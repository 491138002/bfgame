/*
 * HorizontalListView.java
 *
 * 
 * The MIT License
 * Copyright (c) 2011 Paul Soucy (paul@dev-smart.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.bfgame.app.widget.view;

import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

import com.bfgame.app.util.preference.PreferencesUtils;
/**
 * 横向listview
 * @author admin
 *
 */
public class HorizontialListView extends AdapterView<ListAdapter> {

	public boolean mAlwaysOverrideTouch = true;
	protected ListAdapter mAdapter;
	private int mLeftViewIndex = -1;
	private int mRightViewIndex = 0;
	protected int mCurrentX;
	protected int mNextX;
	private int mMaxX = Integer.MAX_VALUE;
	private int mDisplayOffset = 0;
	protected Scroller mScroller;
//	private GestureDetector mGesture;
	private Queue<View> mRemovedViewQueue = new LinkedList<View>();
	private boolean mDataChanged = false;
	
	protected PreferencesUtils pu;
	
    public int getFirstVisiblePosition() {
        return mLeftViewIndex+1;
    }

    public int getLastVisiblePosition() {
        return mRightViewIndex-1;
    }
    
    public int getNextX(){
    	return mCurrentX;
    }
    
    public void setNextX(int mNextX){
    	this.mNextX = mNextX;
    	requestLayout();
    }
	
	public HorizontialListView(Context context) {
		super(context);
		initView();
	}
	
	public HorizontialListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	private void initView() {
		setClickable(true);
        setFocusableInTouchMode(true);
        setWillNotDraw(false);
        setAlwaysDrawnWithCacheEnabled(false);
		
		mLeftViewIndex = -1;
		mRightViewIndex = 0;
		mDisplayOffset = 0;
		mCurrentX = 0;
		mNextX = 0;
		mMaxX = Integer.MAX_VALUE;
		mScroller = new Scroller(getContext());
//		mGesture = new GestureDetector(getContext(), mOnGesture);
		
		pu = new PreferencesUtils(getContext());
		
	}
	
	private DataSetObserver mDataObserver = new DataSetObserver() {

		@Override
		public void onChanged() {
			synchronized(HorizontialListView.this){
				mDataChanged = true;
			}
			invalidate();
			requestLayout();
		}

		@Override
		public void onInvalidated() {
			reset();
			invalidate();
			requestLayout();
		}
		
	};
	
	@Override
    protected float getLeftFadingEdgeStrength() {
        final int count = getChildCount();
        final float fadeEdge = super.getLeftFadingEdgeStrength();
        if (count == 0) {
            return fadeEdge;
        } else {
            if (getFirstVisiblePosition() > 0) {
                return 1.0f;
            }

            final int left = getChildAt(0).getLeft();
            final float fadeLength = (float) getHorizontalFadingEdgeLength();
            return left < getPaddingLeft() ? (float) -(left - getPaddingLeft()) / fadeLength : fadeEdge;
        }
    }

    @Override
    protected float getRightFadingEdgeStrength() {
        final int count = getChildCount();
        final float fadeEdge = super.getRightFadingEdgeStrength();
        if (count == 0) {
            return fadeEdge;
        } else {
            if (getFirstVisiblePosition() + count - 1 < getAdapter().getCount() - 1) {
                return 1.0f;
            }

            final int right = getChildAt(count - 1).getRight();
            final int width = getWidth();
            final float fadeLength = (float) getHorizontalFadingEdgeLength();
            return right > width - getPaddingRight() ?
                    (float) (right - width + getPaddingRight()) / fadeLength : fadeEdge;
        }
    }


	@Override
	public ListAdapter getAdapter() {
		return mAdapter;
	}

	@Override
	public View getSelectedView() {
		//TODO: implement
		return null;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if(mAdapter != null) {
			mAdapter.unregisterDataSetObserver(mDataObserver);
		}
		mAdapter = adapter;
		mAdapter.registerDataSetObserver(mDataObserver);
		
        reset();
	}
	
	private synchronized void reset(){
		initView();
		removeAllViewsInLayout();
        requestLayout();
	}

	@Override
	public void setSelection(int position) {
		//TODO: implement
	}
	
	private void addAndMeasureChild(final View child, int viewPos) {
		LayoutParams params = child.getLayoutParams();
		if(params == null) {
			params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		}

		addViewInLayout(child, viewPos, params, true);
		child.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.AT_MOST),
				MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.AT_MOST));
	}

	@Override
	protected synchronized void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		if(mAdapter == null){
			return;
		}
		
		if(mDataChanged){
			int oldCurrentX = mCurrentX;
			initView();
			removeAllViewsInLayout();
			mNextX = oldCurrentX;
			mDataChanged = false;
		}

		if(mScroller.computeScrollOffset()){
			int scrollx = mScroller.getCurrX();
			mNextX = scrollx;
		}
		
		if(mNextX < 0){
			mNextX = 0;
			mScroller.forceFinished(true);
		}
		if(mNextX > mMaxX) {
			mNextX = mMaxX;
			mScroller.forceFinished(true);
		}
		
		int dx = mCurrentX - mNextX;
		
		removeNonVisibleItems(dx);
		fillList(dx);
		positionItems(dx);
		
		mCurrentX = mNextX;
		
		if(!mScroller.isFinished()){
			post(new Runnable(){
				@Override
				public void run() {
					requestLayout();
				}
			});
			
		}
	}
	
	private void fillList(final int dx) {
		int edge = 0;
		View child = getChildAt(getChildCount()-1);
		if(child != null) {
			edge = child.getRight();
		}
		fillListRight(edge, dx);
		
		edge = 0;
		child = getChildAt(0);
		if(child != null) {
			edge = child.getLeft();
		}
		fillListLeft(edge, dx);
	}
	
	/**
	 * 
	 * @param rightEdge 当前最后一个view的有边沿
	 * @param dx		两次scroll的距离
	 */
	private void fillListRight(int rightEdge, final int dx) {
		while(rightEdge + dx < getWidth() && mRightViewIndex < mAdapter.getCount()) {
			
			View child = mAdapter.getView(mRightViewIndex, mRemovedViewQueue.poll(), this);
			addAndMeasureChild(child, -1);
			rightEdge += child.getMeasuredWidth();
			
			if(mRightViewIndex == mAdapter.getCount()-1){
				mMaxX = mCurrentX + rightEdge - getWidth();
			}
			
			if(mMaxX < 0)
				mMaxX = 0;
			
			mRightViewIndex++;
			
		}
		
	}
	
	private void fillListLeft(int leftEdge, final int dx) {
		while(leftEdge + dx > 0 && mLeftViewIndex >= 0) {
			View child = mAdapter.getView(mLeftViewIndex, mRemovedViewQueue.poll(), this);
			addAndMeasureChild(child, 0);
			leftEdge -= child.getMeasuredWidth();
			mLeftViewIndex--;
			mDisplayOffset -= child.getMeasuredWidth();
			
		}
	}
	
	private void removeNonVisibleItems(final int dx) {
		View child = getChildAt(0);
		while(child != null && child.getRight() + dx <= 0) {
			mDisplayOffset += child.getMeasuredWidth();
			mRemovedViewQueue.offer(child);
			removeViewInLayout(child);
			mLeftViewIndex++;
			child = getChildAt(0);
			
		}
		
		child = getChildAt(getChildCount()-1);
		while(child != null && child.getLeft() + dx >= getWidth()) {
			mRemovedViewQueue.offer(child);
			removeViewInLayout(child);
			mRightViewIndex--;
			child = getChildAt(getChildCount()-1);
		}
	}
	
	private void positionItems(final int dx) {
		if(getChildCount() > 0){
			mDisplayOffset += dx;
			int left = mDisplayOffset;
			for(int i=0;i<getChildCount();i++){
				View child = getChildAt(i);
				int childWidth = child.getMeasuredWidth();
				child.layout(left, 0, left + childWidth, child.getMeasuredHeight());
				left += childWidth;
			}
		}
	}
	
	public synchronized void scrollTo(int x) {
		mScroller.startScroll(mNextX, 0, x - mNextX, 0);
		requestLayout();
	}
	
	/**
     * Indicates that we are not in the middle of a touch gesture
     */
    static final int TOUCH_MODE_REST = -1;

    /**
     * Indicates we just received the touch event and we are waiting to see if the it is a tap or a
     * scroll gesture.
     */
    static final int TOUCH_MODE_DOWN = 0;

    /**
     * Indicates the touch has been recognized as a tap and we are now waiting to see if the touch
     * is a longpress
     */
    static final int TOUCH_MODE_TAP = 1;

    /**
     * Indicates we have waited for everything we can wait for, but the user's finger is still down
     */
    static final int TOUCH_MODE_DONE_WAITING = 2;

    /**
     * Indicates the touch gesture is a scroll
     */
    static final int TOUCH_MODE_SCROLL = 3;
    
    /**
     * Indicates the view is in the process of being flung
     */
    static final int TOUCH_MODE_FLING = 4;
    
	/**
     * One of TOUCH_MODE_REST, TOUCH_MODE_DOWN, TOUCH_MODE_TAP, TOUCH_MODE_SCROLL, or
     * TOUCH_MODE_DONE_WAITING
     */
    int mTouchMode = TOUCH_MODE_REST;
	
    /**
     * X value from on the previous motion event (if any)
     */
    int mMotionX = 0;
    
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = ev.getAction();

        switch (action) {
	        case MotionEvent.ACTION_DOWN: {
	            int touchMode = mTouchMode;
	            
	            final int x = (int) ev.getX();
	            
	            mMotionX = x;//保存按下位置
	            
	            
	            
	            if (touchMode != TOUCH_MODE_FLING ) {
	                mTouchMode = TOUCH_MODE_DOWN;
	            }
	            if (touchMode == TOUCH_MODE_FLING) {
	                return true;
	            }
	            break;
	        }
	        case MotionEvent.ACTION_MOVE: {
	            switch (mTouchMode) {
	            case TOUCH_MODE_SCROLL:
	            case TOUCH_MODE_DOWN:
	                final int x = (int) ev.getX();
	                if (startScrollIfNeeded(x - mMotionX)) {
	                    return true;
	                }
	                break;
	            }
	            break;
	        }
	
	        case MotionEvent.ACTION_UP: {
	            mTouchMode = TOUCH_MODE_REST;
	//            reportScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
	            break;
	        }
        
        }

        return false;
	}
	
	private boolean startScrollIfNeeded(int deltaX) {
		if(Math.abs(deltaX)>8){
			mTouchMode = TOUCH_MODE_SCROLL;
			requestDisallowInterceptTouchEvent(true);
	        setPressed(false);
	        return true;
		}
		
		return false;
    }
	
	
	/**
     * Determines speed during touch scrolling
     */
    private VelocityTracker mVelocityTracker;
	
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();

        int deltaX;

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        switch (action) {
        case MotionEvent.ACTION_DOWN: {
            final int x = (int) ev.getX();
            
            mMotionX = x;//按下时x坐标
            
            if(mTouchMode == TOUCH_MODE_FLING){
            	mScroller.forceFinished(true);
            	if(mFlingRunnable!=null)
    				mFlingRunnable.endFling();
            	return true;
            }
            break;
        }

        case MotionEvent.ACTION_MOVE: {
            final int x = (int) ev.getX();
            deltaX = x - mMotionX;//划动距离
            mMotionX = x;
            
            mTouchMode = TOUCH_MODE_SCROLL;
            
			requestDisallowInterceptTouchEvent(true);
			mNextX += (int)-deltaX;
			requestLayout();

            break;
        }

        case MotionEvent.ACTION_UP: {
            switch (mTouchMode) {
            case TOUCH_MODE_DOWN:
            case TOUCH_MODE_TAP:
            case TOUCH_MODE_DONE_WAITING:
                mTouchMode = TOUCH_MODE_REST;
                break;
            case TOUCH_MODE_SCROLL:
                final int childCount = getChildCount();
                if (childCount > 0) {
                	//若滚动到最左边
                    if (getFirstVisiblePosition() == 0 && //getChildAt(0).getLeft() >= getPaddingLeft() &&
                    		getFirstVisiblePosition() + childCount < getAdapter().getCount() //&&
                            //getChildAt(childCount - 1).getRight() <= getWidth() - getPaddingRight()
                    		) {
                        mTouchMode = TOUCH_MODE_REST;
//                        reportScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
                    } else {
                        final VelocityTracker velocityTracker = mVelocityTracker;
                        velocityTracker.computeCurrentVelocity(1000, 500000);
                        final int initialVelocity = (int) velocityTracker.getXVelocity();
    
                        if (Math.abs(initialVelocity) > 0) {
                            if (mFlingRunnable == null) {
                                mFlingRunnable = new FlingRunnable();
                            }
//                            reportScrollStateChange(OnScrollListener.SCROLL_STATE_FLING);
                            
                            mFlingRunnable.start(-initialVelocity);
                        } else {
                            mTouchMode = TOUCH_MODE_REST;
//                            reportScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
                        }
                    }
                } else {
                    mTouchMode = TOUCH_MODE_REST;
//                    reportScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
                }
                break;
            }

            setPressed(false);

            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
            
            break;
        }

        case MotionEvent.ACTION_CANCEL: {
        	final int childCount = getChildCount();
            if (childCount > 0) {
            	//若滚动到最左边
                if (getFirstVisiblePosition() == 0 && //getChildAt(0).getLeft() >= getPaddingLeft() &&
                		getFirstVisiblePosition() + childCount < getAdapter().getCount() //&&
                        //getChildAt(childCount - 1).getRight() <= getWidth() - getPaddingRight()
                		) {
                    mTouchMode = TOUCH_MODE_REST;
//                    reportScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
                } else {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, 5000);
                    final int initialVelocity = (int) velocityTracker.getXVelocity();

                    if (Math.abs(initialVelocity) > 0) {
                        if (mFlingRunnable == null) {
                            mFlingRunnable = new FlingRunnable();
                        }
//                        reportScrollStateChange(OnScrollListener.SCROLL_STATE_FLING);
                        
                        mFlingRunnable.start(-initialVelocity);
                    } else {
                        mTouchMode = TOUCH_MODE_REST;
//                        reportScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
                    }
                }
            } else {
                mTouchMode = TOUCH_MODE_REST;
//                reportScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
            }
        	
//            setPressed(false);
//            View motionView = this.getChildAt(mMotionPosition - mFirstPosition);
//            if (motionView != null) {
//                motionView.setPressed(false);
//            }
//            clearScrollingCache();
//

            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
            
            break;
        }
        
        }

        return true;
	}
	
	FlingRunnable mFlingRunnable;
	 /**
     * Responsible for fling behavior. Use {@link #start(int)} to
     * initiate a fling. Each frame of the fling is handled in {@link #run()}.
     * A FlingRunnable will keep re-posting itself until the fling is done.
     *
     */
    private class FlingRunnable implements Runnable {

        void start(int initialVelocity) {
            mScroller.fling(mNextX, 0, initialVelocity, 0, -2000, Integer.MAX_VALUE, 0, 0);
            mTouchMode = TOUCH_MODE_FLING;
            post(this);

        }

//        void startScroll(int distance, int duration) {
//            int initialY = distance < 0 ? Integer.MAX_VALUE : 0;
//            mScroller.startScroll(0, initialY, 0, distance, duration);
//            mTouchMode = TOUCH_MODE_FLING;
//            post(this);
//        }

        private void endFling() {
            mTouchMode = TOUCH_MODE_REST;

//            reportScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
//            clearScrollingCache();

            removeCallbacks(this);

        }

        public void run() {
            switch (mTouchMode) {
            default:
                return;
                
            case TOUCH_MODE_FLING: {
                if (getAdapter().getCount() == 0 || getChildCount() == 0) {
                    endFling();
                    return;
                }

                boolean more = mScroller.computeScrollOffset();
                
                if (more) {
                	requestLayout();
                    post(this);
                } else {
                    endFling();
                }
                break;
            }
            }

        }
    }
    
    public void setMoveScrollX(int x) {
		this.mCurrentX += x;
		mNextX = mCurrentX;
		requestLayout();
	}
	
	public int getListScrollX(){
		return mCurrentX;
	}
    
//	protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//				float velocityY) {
//		synchronized(HorizontialListView.this){
//			mScroller.fling(mNextX, 0, (int)(-velocityX*0.8f), 0, 0, mMaxX, 0, 0);
//		}
//		requestLayout();
//		
//		return true;
//	}
//	
//	protected boolean onDown(MotionEvent e) {
//		if(!mScroller.isFinished()){
//			mScroller.forceFinished(true);
//			return true;
//		}
//		
//		return false;
//	}
	
//	private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {
//
//		@Override
//		public boolean onDown(MotionEvent e) {
//			boolean state = HorizontialListView.this.onDown(e);
//			return state;
//		}
//		
//		@Override
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//				float velocityY) {
//			if(Math.abs(velocityX)>Math.abs(velocityY)){
//				getParent().requestDisallowInterceptTouchEvent(true);
//				return  HorizontialListView.this.onFling(e1, e2, velocityX, velocityY);
//			}
//			
//			return false;
//		}
//
//		@Override
//		public boolean onScroll(MotionEvent e1, MotionEvent e2,
//				float distanceX, float distanceY) {
////			synchronized(HorizontialListView.this){
//				getParent().requestDisallowInterceptTouchEvent(true);
//				mNextX += (int)distanceX;
////			}
//			requestLayout();
//			
//			return true;
//		}
//		
//		@Override
//		public boolean onSingleTapConfirmed(MotionEvent e) {
//			Rect viewRect = new Rect();
//			for(int i=0;i<getChildCount();i++){
//				View child = getChildAt(i);
//				int left = child.getLeft();
//				int right = child.getRight();
//				int top = child.getTop();
//				int bottom = child.getBottom();
//				viewRect.set(left, top, right, bottom);
//				if(viewRect.contains((int)e.getX(), (int)e.getY())){
//					child.requestFocus();
//					if(mOnItemClicked != null){
//						mOnItemClicked.onItemClick(HorizontialListView.this, child, mLeftViewIndex + 1 + i, 0);
//					}
//					if(mOnItemSelected != null){
//						mOnItemSelected.onItemSelected(HorizontialListView.this, child, mLeftViewIndex + 1 + i, 0);
//					}
//					break;
//				}
//				
//			}
//			return true;
//		}
//	};
}
