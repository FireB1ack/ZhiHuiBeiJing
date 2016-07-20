package com.fireblack.zhihuibeijing.domain;

import java.util.ArrayList;

/**
 * 页签详情页数据
 * Created by ChengHao on 2016/7/20.
 */
public class TabData {
    public int retcode;

    public TabDetail data;

    public class TabDetail{
        public String title;
        public String more;
        public ArrayList<TabNewsData> news;
        public ArrayList<TopNewsData> topnews;

        @Override
        public String toString() {
            return "TabDetail{" +
                    "title='" + title + '\'' +
                    ", news=" + news +
                    ", topnews=" + topnews +
                    '}';
        }
    }

    /**
     *  新闻列表对象
     */
    public class TabNewsData{
        public String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "TabNewsData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    /**
     * 头条新闻
     */
    public class TopNewsData{
        public String id;
        public String topimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "TopNewsData{" +
                    "title='" + title + '\'' +
                    ", topimage='" + topimage + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TabData{" +
                "data=" + data +
                '}';
    }
}
