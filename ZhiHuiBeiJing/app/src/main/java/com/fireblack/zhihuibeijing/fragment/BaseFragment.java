package com.fireblack.zhihuibeijing.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ChengHao on 2016/7/18.
 */
public abstract class BaseFragment extends android.support.v4.app.Fragment {
    public Activity mActivity;
    @Nullable
    // fragment创建
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initViews();
    }

    // 处理fragment的布局
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    // 依附的activity创建完成
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    // 子类必须实现初始化布局的方法
    public abstract View initViews();

    // 初始化数据, 可以不实现
    public void initData() {

    }
}
