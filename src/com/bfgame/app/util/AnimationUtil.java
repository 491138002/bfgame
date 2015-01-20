package com.bfgame.app.util;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.bfgame.app.R;
/**
 * 动画工具类
 * @ClassName AnimationUtil
 * @author admin
 *
 */
public class AnimationUtil {

	/**
	 * 开始加载动画
	 * @param imgView  
	 */
    public synchronized static void startLoadingAnim(ImageView imgView) {
        imgView.setBackgroundResource(R.anim.bfgame_loading);
        AnimationDrawable animaition = (AnimationDrawable) imgView.getBackground();
        animaition.setOneShot(false);
        animaition.start();
    }
    /**
	 * 结束加载动画
	 * @param imgView
	 */
    public synchronized static void stopLoadingAnim(ImageView imgView) {
        imgView.setBackgroundResource(R.anim.bfgame_loading);
        AnimationDrawable animaition = (AnimationDrawable) imgView.getBackground();
        animaition.setOneShot(false);
        animaition.stop();
    }

    public static int sDownLoadX;
    public static int sDownLoadY;
    public static int sTitleH;
    public static ViewGroup sFloatView;
    public static List<View> sCurList = new ArrayList<View>();

    
    /**
     * 下载时图标飞行动画
     * @param context  上下文
     * @param res   动画开始点的view
     * @param des   动画结束点的view
     */
    @SuppressLint("NewApi")
    public synchronized static void startDownLoadAnim(Context context, final View res, View des) {
        res.setDrawingCacheEnabled(true);
        Bitmap cache = res.getDrawingCache();
        Log.i("test", "cache = " + cache);
        if (cache == null || sFloatView == null || sCurList.contains(res)) {
            return;
        }
        int[] resL = new int[2];
        res.getLocationOnScreen(resL);
        int dx, dy;
        if (des != null) {
            int[] desL = new int[2];
            des.getLocationOnScreen(desL);
            dx = desL[0] - resL[0];
            dy = desL[1] - resL[1];
        } else {
            dx = sDownLoadX - resL[0];
            dy = sDownLoadY - resL[1];
        }
        Log.i("test", "sDownLoadX:" + sDownLoadX + " sDownLoadY:" + sDownLoadY);
        Log.i("test", "dx:" + dx + " dy:" + dy);

        final PowerImageView iv = new PowerImageView(context);
        FrameLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //lp.leftMargin = resL[0];
        //lp.topMargin = resL[1] - sTitleH;
        iv.setLayoutParams(lp);
        //iv.setX(resL[0]);
        //iv.setY(resL[1] - sTitleH);
        iv.setImageBitmap(cache);
        ((ViewGroup) sFloatView).addView(iv);
        //View view = addFloatView(context, res);

        LinearInterpolator interpolator1 = new LinearInterpolator();
        AccelerateInterpolator interpolator2 = new AccelerateInterpolator();
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(iv, "x", resL[0], sDownLoadX);
        anim1.setInterpolator(interpolator1);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(iv, "y", resL[1] - sTitleH, sDownLoadY - sTitleH);
        anim2.setInterpolator(interpolator2);
        ObjectAnimator anim3 = ObjectAnimator.ofInt(iv, "width", cache.getWidth(), 10);
        ObjectAnimator anim4 = ObjectAnimator.ofInt(iv, "height", cache.getHeight(), 10);

        /* TranslateAnimation translateAnimationX = new TranslateAnimation(0,
         * dx, 0, 0);
         * translateAnimationX.setInterpolator(interpolator1);
         * TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
         * 0, dy);
         * translateAnimationY.setInterpolator(interpolator2);
         * set.addAnimation(translateAnimationY);
         * set.addAnimation(translateAnimationX); 

        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.2f, 1, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(scaleAnimation);
        set.setDuration(500);*/

        AnimatorSet as = new AnimatorSet();
        as.play(anim1).with(anim2).with(anim3).with(anim4);
        as.setDuration(1000);
        as.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                sCurList.add(res);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                iv.setVisibility(View.INVISIBLE);
                if (sCurList.size() == 0) {
                    int count = sFloatView.getChildCount();
                    sFloatView.removeViews(1, count - 1);
                    sFloatView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sCurList.remove(res);
                        }
                    }, 100);
                } else {
                    sCurList.remove(res);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                
            }
        }

        );
        //iv.startAnimation(set);
        /*AnimationSet set = new AnimationSet(false);
        set.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                sMoving = true;
                sCurList.add(res);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                
            }
        });*/

        as.start();
    }

    public static View addFloatView(Context context, View res) {
        /* <Button
         * android:id="@+id/item_download_btn"
         * android:layout_width="80dp"
         * android:layout_height="wrap_content"
         * android:layout_alignParentRight="true"
         * android:layout_centerVertical="true"
         * android:background="@drawable/bfgame_btn_blue_selector"
         * android:gravity="center"
         * android:paddingBottom="1dp"
         * android:paddingTop="1dp"
         * android:text="@string/bfgame_text_download"
         * android:textColor="@android:color/white"
         * android:textSize="@dimen/item_download_btn_sp" /> */
        int[] resL = new int[2];
        res.getLocationOnScreen(resL);
        int width = res.getWidth();
        int height = res.getHeight();

        Button btn = new Button(context);
        btn.setText(context.getResources().getString(R.string.bfgame_text_download));
        btn.setTextColor(context.getResources().getColor(android.R.color.white));
        btn.setTextSize(context.getResources().getDimension(R.dimen.item_download_btn_sp));
        btn.setBackgroundResource(R.drawable.bfgame_btn_blue_selector);
        btn.setFocusable(false);
        btn.setClickable(false);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_PHONE;
        lp.format = PixelFormat.RGBA_8888;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.gravity = Gravity.LEFT | Gravity.TOP;
        lp.width = width;
        lp.height = height;
        lp.x = resL[0];
        lp.y = resL[1];
        windowManager.addView(btn, lp);
        return btn;
    }

    public static class PowerImageView extends ImageView {

        public PowerImageView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            try {
                super.onDraw(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setWidth(int width) {
            FrameLayout.LayoutParams lp = (LayoutParams) this.getLayoutParams();
            lp.width = width;
            setLayoutParams(lp);
        }

        public void setHeight(int height) {
            FrameLayout.LayoutParams lp = (LayoutParams) this.getLayoutParams();
            lp.height = height;
            setLayoutParams(lp);
        }

    }

}
