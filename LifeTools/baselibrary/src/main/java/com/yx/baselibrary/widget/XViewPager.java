package com.yx.baselibrary.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @ClassName XViewPager
 * @Description 设置是否禁用viewpager滑动
 * Created by yx on 2017-02-27.
 */

public class XViewPager extends ViewPager {
    private boolean isEnableScroll = true;

    public void setEnableScroll(boolean isEnableScroll) {
        this.isEnableScroll = isEnableScroll;
    }

    public XViewPager(Context context) {
        super(context);
    }

    public XViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !this.isEnableScroll ? false : super.onInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return !this.isEnableScroll ? false : super.onTouchEvent(ev);
    }
}