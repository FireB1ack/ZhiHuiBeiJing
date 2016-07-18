package com.fireblack.zhihuibeijing.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fireblack.zhihuibeijing.base.BasePager;

/**
 * Created by ChengHao on 2016/7/18.
 * 政务
 */
public class GOvAffairsPager extends BasePager {
    public GOvAffairsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("人口管理");
        btnMenu.setVisibility(View.GONE);
        setSlidingMenuEnable(true);//关闭侧边栏

        TextView textView = new TextView(mActivity);
        textView.setText("政务");
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);

        // 向FrameLayout中动态添加布局
        flContent.addView(textView);
    }
}
