package com.fireblack.zhihuibeijing.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fireblack.zhihuibeijing.base.BasePager;

/**
 * Created by ChengHao on 2016/7/18.
 * 设置页面
 */
public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("设置");
        btnMenu.setVisibility(View.GONE);
        setSlidingMenuEnable(false);//关闭侧边栏

        TextView textView = new TextView(mActivity);
        textView.setText("设置");
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);

        // 向FrameLayout中动态添加布局
        flContent.addView(textView);
    }
}
