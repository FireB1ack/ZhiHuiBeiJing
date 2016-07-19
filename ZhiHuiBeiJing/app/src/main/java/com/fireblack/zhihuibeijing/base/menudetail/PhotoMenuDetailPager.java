package com.fireblack.zhihuibeijing.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fireblack.zhihuibeijing.base.BaseMenuDetailPager;

/**
 * 菜单详情页-组图
 * Created by ChengHao on 2016/7/19.
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager{

    public PhotoMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initViews() {
        TextView textView = new TextView(mActivity);
        textView.setText("菜单详情页-组图");
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
    }
}
