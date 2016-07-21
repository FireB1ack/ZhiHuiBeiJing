package com.fireblack.zhihuibeijing.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fireblack.zhihuibeijing.MainActivity;
import com.fireblack.zhihuibeijing.NewsDetailActivity;
import com.fireblack.zhihuibeijing.R;
import com.fireblack.zhihuibeijing.domain.NewsData;
import com.fireblack.zhihuibeijing.domain.TabData;
import com.fireblack.zhihuibeijing.global.GlobalContants;
import com.fireblack.zhihuibeijing.utils.PrefUtils;
import com.fireblack.zhihuibeijing.view.RefreshListView;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by ChengHao on 2016/7/19.
 */
public class TabDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener{

    NewsData.NewsTabData mTabData;
    private String mUri;

    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;

    private TabData mTabDetailData;
    private ArrayList<TabData.TopNewsData> mTopNewsList;//头条新闻数据集合

    @ViewInject(R.id.vp_news)
    private ViewPager mViewPager;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.lv_tab_detail)
    private RefreshListView lvList;
    private ArrayList<TabData.TabNewsData> mNewsList;// 新闻数据集合
    private NewsAdapter mNewsAdapter;
    private String mMoreUrl;


    public TabDetailPager(Activity activity,NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUri = GlobalContants.SERVER_URI + mTabData.url;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager,null);
        View headerView = View.inflate(mActivity,R.layout.list_header_topnews,null);
        ViewUtils.inject(this, view);
        ViewUtils.inject(this, headerView);

        // 将头条新闻以头布局的形式加给listview
        lvList.addHeaderView(headerView);

        //设置下拉刷新监听
        lvList.setOnRefshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void OnRefresh() {
                getDataFromServer();
            }

            @Override
            public void OnLoadMore() {
                if(mMoreUrl != null){
                    getMoreDataFromServer();
                }else {
                    Toast.makeText(mActivity,"到最后一页了",Toast.LENGTH_SHORT).show();
                    lvList.OnRefreshComplete(false);// 收起加载更多的布局
                }
            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("tag", String.valueOf(position));
                //在本地记录已读状态
                String ids = PrefUtils.getString(mActivity, "read_ids", "");
                String readId = mNewsList.get(position).id;
                if(!ids.contains(readId)){
                    ids = ids + readId + ",";
                    PrefUtils.putString(mActivity,"read_ids",ids);
                }
                changeReadState(view);//实现局部界面刷新, 这个view就是被点击的item布局对象

                //跳转新闻页面
                Intent intent = new Intent();
                intent.setClass(mActivity, NewsDetailActivity.class);
                intent.putExtra("url",mNewsList.get(position).url);
                mActivity.startActivity(intent);
            }
        });
        return view;
    }

    /**
     * @param view
     * 改变已读数据的颜色
     */
    private void changeReadState(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setTextColor(Color.GRAY);
    }

    /**
     * 加载下一页数据
     */
    private void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                parseData(result,true);

                lvList.OnRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                e.printStackTrace();

                lvList.OnRefreshComplete(false);
            }
        });
    }

    @Override
    public void initData() {
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mUri, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                parseData(result,false);

                lvList.OnRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                e.printStackTrace();

                lvList.OnRefreshComplete(false);
            }
        });
    }

    private void parseData(String result,boolean isMore) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);
        System.out.print("页签解析详情" + mTabDetailData);

        //处理下一页链接
        String more = mTabDetailData.data.more;
        if(!TextUtils.isEmpty(more)) {
            mMoreUrl = GlobalContants.SERVER_URI + more;
        }else {
            mMoreUrl = null;
        }

        if(!isMore) {
            mTopNewsList = mTabDetailData.data.topnews;
            mNewsList = mTabDetailData.data.news;

            if (mTopNewsList != null) {
                mViewPager.setAdapter(new TopNewsAdapter());
                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true);// 支持快照显示
                mIndicator.setOnPageChangeListener(this);
                mIndicator.onPageSelected(0);//让指示器重新定位到第一个点
                tv_title.setText(mTopNewsList.get(0).title);
            }

            if (mNewsList != null) {
                mNewsAdapter = new NewsAdapter();
                lvList.setAdapter(mNewsAdapter);
            }
        }else {//加载下一页,需要将数据追加给原来的集合
            ArrayList<TabData.TabNewsData> news = mTabDetailData.data.news;
            mNewsList.addAll(news);
            mNewsAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 头条新闻适配器
     */
    class TopNewsAdapter extends PagerAdapter{

        private BitmapUtils utils;

        public TopNewsAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.drawable.topnews_item_default);// 设置默认图片
        }

        @Override
        public int getCount() {
            return mTabDetailData.data.topnews.size();
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
            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);// 基于控件大小填充图片
            TabData.TopNewsData topNewsData = mTopNewsList.get(position);
            String topimage = topNewsData.topimage.replace("http://10.0.2.2:8080/zhbj", GlobalContants.SERVER_URI);
            System.out.print("imageuri"+topimage);
            utils.display(imageView,topimage);// 传递imagView对象和图片地址
            container.addView(imageView);
            return imageView;
        }
    }

    /**
     *  新闻列表适配器
     */
    class NewsAdapter extends BaseAdapter{

        private BitmapUtils utils;

        public NewsAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(mActivity,R.layout.list_news_item,null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            TabData.TabNewsData item = (TabData.TabNewsData) getItem(position);
            holder.tvTitle.setText(item.title);
            holder.tvDate.setText(item.pubdate);
            String imageUri = item.listimage.replace("http://10.0.2.2:8080/zhbj",GlobalContants.SERVER_URI);
            System.out.print("pubdate"+item.pubdate+"imageUri"+imageUri);
            Log.v("12345","pubdate"+item.pubdate+"imageUri"+imageUri);
            Log.v("12345","加载成功");
            utils.display(holder.ivPic,imageUri);
            return convertView;
        }
    }

    static class ViewHolder{
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView ivPic;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        TabData.TopNewsData topNewsData = mTabDetailData.data.topnews.get(position);
        tv_title.setText(topNewsData.title);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
