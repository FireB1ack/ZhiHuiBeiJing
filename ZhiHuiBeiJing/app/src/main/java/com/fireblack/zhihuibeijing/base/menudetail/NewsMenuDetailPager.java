package com.fireblack.zhihuibeijing.base.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;


import com.fireblack.zhihuibeijing.MainActivity;
import com.fireblack.zhihuibeijing.R;
import com.fireblack.zhihuibeijing.base.BaseMenuDetailPager;
import com.fireblack.zhihuibeijing.base.TabDetailPager;
import com.fireblack.zhihuibeijing.domain.NewsData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 * Created by ChengHao on 2016/7/19.
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener{

    private ViewPager mViewPager;
    private ArrayList<TabDetailPager> mPageList;
    private ArrayList<NewsData.NewsTabData> mNewsTabData;
    private TabPageIndicator mIndicator;

    public NewsMenuDetailPager(Activity activity,ArrayList<NewsData.NewsTabData> children) {
        super(activity);
        mNewsTabData = children;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.news_menu_detail,null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);

        ViewUtils.inject(this, view);

        mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);

        // mViewPager.setOnPageChangeListener(this);//注意:当viewpager和Indicator绑定时,
        // 滑动监听需要设置给Indicator而不是viewpager
        mIndicator.setOnPageChangeListener(this);

        return view;
    }

    @Override
    public void initData() {
        mPageList = new ArrayList<TabDetailPager>();

        //初始化页签数据
        for(int i = 0; i < mNewsTabData.size(); i++){
            TabDetailPager pager = new TabDetailPager(mActivity,mNewsTabData.get(i));
            mPageList.add(pager);
        }
        mViewPager.setAdapter(new MenuDetailAdapter());
        mIndicator.setViewPager(mViewPager);// 将viewpager和mIndicator关联起来,必须在viewpager设置完adapter后才能调用
    }

    //跳转下一个页面
    @OnClick(R.id.btn_next)
    public void nextPage(View view){
        int currentItem = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(++currentItem);
    }

    class MenuDetailAdapter extends PagerAdapter{

        /**
         * 重写此方法,返回页面标题,用于viewpagerIndicator的页签显示
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mNewsTabData.get(position).title;
        }

        @Override
        public int getCount() {
            return mPageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPageList.get(position);
            container.addView(pager.mRootView);
            pager.initData();
            return pager.mRootView;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {
        MainActivity mainUi = (MainActivity)mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();

        if(position == 0){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
}
