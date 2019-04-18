package com.cnpeak.expressreader.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;


/**
 * The type Zoom view paper.
 * 解决ViewPager+PhotoView多点触控导致crash的问题 -->
 * java.lang.IllegalArgumentException: pointerIndex out of range
 * 思路：将onTouchEvent()消费方法和onInterceptTouchEvent()拦截方法中对此异常进行捕获
 */
public class ZoomViewPaper extends ViewPager {
    /**
     * Instantiates a new Zoom view paper.
     *
     * @param context the context
     */
    public ZoomViewPaper(@NonNull Context context) {
        super(context);
    }

    /**
     * Instantiates a new Zoom view paper.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public ZoomViewPaper(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
