package com.fireblack.zhihuibeijing.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.fireblack.zhihuibeijing.R;
import com.fireblack.zhihuibeijing.base.BasePager;
import com.fireblack.zhihuibeijing.base.impl.GovAffairsPager;
import com.fireblack.zhihuibeijing.base.impl.HomePager;
import com.fireblack.zhihuibeijing.base.impl.NewsCenterPager;
import com.fireblack.zhihuibeijing.base.impl.SettingPager;
import com.fireblack.zhihuibeijing.base.impl.SmartServicePager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by ChengHao on 2016/7/18.
 */
public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.rg_group)
    private RadioGroup rggroup;

    @ViewInject(R.id.vp_content)
    private ViewPager mViewPager;

    private ArrayList<BasePager> mPagerList;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        ViewUtils.inject(this, view); //注入view和事件
        return view;    }

    @Override
    public void initData() {
        rggroup.check(R.id.rb_home);

        // 初始化5个子页面
        mPagerList = new ArrayList<BasePager>();

        mPagerList.add(new HomePager(mActivity));
        mPagerList.add(new NewsCenterPager(mActivity));
        mPagerList.add(new SmartServicePager(mActivity));
        mPagerList.add(new GovAffairsPager(mActivity));
        mPagerList.add(new SettingPager(mActivity));

        mViewPager.setAdapter(new ContentAdapter());

        rggroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        mViewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4, false);
                        break;
                    default:
                        break;
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPagerList.get(position).initData();// 获取当前被选中的页面, 初始化该页面数据
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagerList.get(0).initData();
    }

    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagerList.size();
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
            BasePager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
//            pager.initData();
            return pager.mRootView;
        }
    }

    /**
     * 获取新闻中心页面
     */
    public NewsCenterPager getNewsCenterPager(){
        return (NewsCenterPager) mPagerList.get(1);
    }
}
