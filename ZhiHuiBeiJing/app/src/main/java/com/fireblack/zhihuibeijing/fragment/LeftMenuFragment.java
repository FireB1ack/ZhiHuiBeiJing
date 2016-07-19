package com.fireblack.zhihuibeijing.fragment;

import android.app.Activity;
import android.app.Notification;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fireblack.zhihuibeijing.MainActivity;
import com.fireblack.zhihuibeijing.R;
import com.fireblack.zhihuibeijing.base.impl.NewsCenterPager;
import com.fireblack.zhihuibeijing.domain.NewsData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by ChengHao on 2016/7/18.
 */
public class LeftMenuFragment extends BaseFragment {

    @ViewInject(R.id.lv_list)
    private ListView lvList;
    private ArrayList<NewsData.NewsMenuData> mMenuList;
    private int mCurrentPos;//当前被点击的菜单项
    private MenuAdapter mAdapter;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                mAdapter.notifyDataSetChanged();

                setCurrentMenuDetailPager(position);
                tooggleSLidingMenu();
            }
        });
    }

    /**
     * @param position
     * 设置当前菜单详情页
     */
    private void setCurrentMenuDetailPager(int position) {
        MainActivity mainUi = (MainActivity)mActivity;
        ContentFragment fragment = mainUi.getContentFragment();
        NewsCenterPager pager = fragment.getNewsCenterPager();
        pager.setCurrentMenuDetailPager(position);
    }

    /**
     * 切换SlidingMenu的状态
     */
    protected void tooggleSLidingMenu(){
        MainActivity mainUi = (MainActivity)mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle();// 切换状态, 显示时隐藏, 隐藏时显示
    }

    public void setMenuData(NewsData data){
        System.out.print("拿到数据" + data);
        mMenuList = data.data;
        mAdapter = new MenuAdapter();
        lvList.setAdapter(mAdapter);

    }

    class MenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mMenuList.size();
        }

        @Override
        public NewsData.NewsMenuData getItem(int position) {
            return mMenuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_menu_item, null);
            TextView tvTitle = (TextView)view.findViewById(R.id.tv_title);
            NewsData.NewsMenuData newsMenuData= getItem(position);
            tvTitle.setText(newsMenuData.title);

            if(mCurrentPos == position){
                tvTitle.setEnabled(true);// 显示红色
            }else {
                tvTitle.setEnabled(false);// 显示白色
            }
            return view;
        }
    }
}
