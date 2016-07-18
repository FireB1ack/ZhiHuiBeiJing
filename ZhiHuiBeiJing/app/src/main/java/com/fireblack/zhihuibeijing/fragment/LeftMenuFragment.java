package com.fireblack.zhihuibeijing.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fireblack.zhihuibeijing.R;
import com.fireblack.zhihuibeijing.domain.NewsData;
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
    private MenuAdapter mAdapter;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData() {

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
            return view;
        }
    }
}
