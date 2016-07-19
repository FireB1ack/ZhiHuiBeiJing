package com.fireblack.zhihuibeijing.base;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fireblack.zhihuibeijing.domain.NewsData;

/**
 * Created by ChengHao on 2016/7/19.
 */
public class TabDetailPager extends BaseMenuDetailPager {

    NewsData.NewsTabData mTabData;
    private TextView tvText;

    public TabDetailPager(Activity activity,NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
    }

    @Override
    public View initViews() {
        tvText = new TextView(mActivity);
        tvText.setText("页签详情页");
        tvText.setTextColor(Color.RED);
        tvText.setTextSize(25);
        tvText.setGravity(Gravity.CENTER);
        return tvText;
    }

    @Override
    public void initData() {
        tvText.setText(mTabData.title);
    }
}
