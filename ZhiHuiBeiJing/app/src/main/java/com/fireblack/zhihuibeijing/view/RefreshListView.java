package com.fireblack.zhihuibeijing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.fireblack.zhihuibeijing.R;

/**
 * 下拉刷新的ListView
 * Created by ChengHao on 2016/7/21.
 */
public class RefreshListView extends ListView {


    private View mHeaderView;

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
    }


    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        this.addHeaderView(mHeaderView);

        mHeaderView.measure(0,0);
        int height = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0,-height,0,0);//隐藏头布局
    }

}
