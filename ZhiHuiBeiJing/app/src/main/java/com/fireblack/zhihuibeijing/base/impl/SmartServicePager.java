package com.fireblack.zhihuibeijing.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fireblack.zhihuibeijing.base.BasePager;

/**
 * Created by ChengHao on 2016/7/18.
 * 智慧服务
 */
public class SmartServicePager extends BasePager {
    public SmartServicePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("生活");
        setSlidingMenuEnable(true);//关闭侧边栏

        TextView textView = new TextView(mActivity);
        textView.setText("智慧服务");
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);

        // 向FrameLayout中动态添加布局
        flContent.addView(textView);
    }
}
