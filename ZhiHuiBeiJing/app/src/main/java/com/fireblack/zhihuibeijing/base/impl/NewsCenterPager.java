package com.fireblack.zhihuibeijing.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fireblack.zhihuibeijing.MainActivity;
import com.fireblack.zhihuibeijing.base.BaseMenuDetailPager;
import com.fireblack.zhihuibeijing.base.BasePager;
import com.fireblack.zhihuibeijing.base.menudetail.InteractMenuDetailPager;
import com.fireblack.zhihuibeijing.base.menudetail.NewsMenuDetailPager;
import com.fireblack.zhihuibeijing.base.menudetail.PhotoMenuDetailPager;
import com.fireblack.zhihuibeijing.base.menudetail.TopicMenuDetailPager;
import com.fireblack.zhihuibeijing.domain.NewsData;
import com.fireblack.zhihuibeijing.fragment.LeftMenuFragment;
import com.fireblack.zhihuibeijing.global.GlobalContants;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * Created by ChengHao on 2016/7/18.
 * 新闻中心
 */
public class NewsCenterPager extends BasePager {

    private NewsData mNewsData;
    private ArrayList<BaseMenuDetailPager> mPagers;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        setSlidingMenuEnable(true);//打开侧边栏

        getDataFromServer();
    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalContants.CATEGORIES_URI, new RequestCallBack<String>() {
            //访问成功
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                parseData(result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    /**
     * 解析网络数据
     * @param result
     */
    protected void parseData(String result) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(result, NewsData.class);
        System.out.print("解析结果:" + mNewsData);

        //刷新侧边栏数据
        MainActivity mainUi = (MainActivity)mActivity;
        LeftMenuFragment leftMenuFragment = mainUi.getLeftMenuFragment();
        leftMenuFragment.setMenuData(mNewsData);

        //装备4个菜单详情页
        mPagers = new ArrayList<BaseMenuDetailPager>();
        mPagers.add(new NewsMenuDetailPager(mActivity,mNewsData.data.get(0).children));
        mPagers.add(new TopicMenuDetailPager(mActivity));
        mPagers.add(new PhotoMenuDetailPager(mActivity));
        mPagers.add(new InteractMenuDetailPager(mActivity));

        setCurrentMenuDetailPager(0);
    }

    /**
     * 设置当前菜单详情页
     */
    public void setCurrentMenuDetailPager(int position) {
        BaseMenuDetailPager pager = mPagers.get(position);
        flContent.removeAllViews();// 清除之前的布局
        flContent.addView(pager.mRootView);// 将菜单详情页的布局设置给帧布局

        //设置当前的标题
        NewsData.NewsMenuData menuData = mNewsData.data.get(position);
        tvTitle.setText(menuData.title);

        pager.initData();// 初始化当前页面的数据
    }


}
