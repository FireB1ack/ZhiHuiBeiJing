package com.fireblack.zhihuibeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ChengHao on 2016/7/20.
 * 11个子页签水平滑动的Viewpager
 */
public class HorizontalViewPager extends ViewPager {
    public HorizontalViewPager(Context context) {
        super(context);
    }

    public HorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 事件分发，请求父控件及祖宗控件不要拦截事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getCurrentItem() != 0) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }else {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.dispatchTouchEvent(ev);
    }
}
