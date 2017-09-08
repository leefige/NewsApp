package com.java.group8;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleksii Shliama.
 */
public class BaseRefreshFragment extends Fragment {

    public static final int REFRESH_DELAY = 2000;

    public static final String KEY_ICON = "icon";
    public static final String KEY_COLOR = "color";

    protected NewsCategory category;
    protected List<News> newsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsList = updateList(category);
    }

    public List<News> updateList(NewsCategory c) {
        //TODO: get info from SERVICE NewsDatabase
        List<News> list = new ArrayList<>();
        list.add(new News("science", "123456789", "", "This is a Big News",
                "2017-09-08", "http://cnews.chinadaily.com.cn/2017-09/08/content_31716799.htm",
                "我", "zh_ch", "", "", "Too young, too simple, sometimes naive!"));
        list.add(new News("science", "123456789", "", "This is a Big News",
                "2017-09-08", "http://cnews.chinadaily.com.cn/2017-09/08/content_31716799.htm",
                "我", "zh_ch", "", "", "Too young, too simple, sometimes naive!"));
        list.add(new News("science", "123456789", "", "This is a Big News",
                "2017-09-08", "http://cnews.chinadaily.com.cn/2017-09/08/content_31716799.htm",
                "我", "zh_ch", "", "", "Too young, too simple, sometimes naive!"));
        list.add(new News("science", "123456789", "", "This is a Big News",
                "2017-09-08", "http://cnews.chinadaily.com.cn/2017-09/08/content_31716799.htm",
                "我", "zh_ch", "", "", "Too young, too simple, sometimes naive!"));
        return list;
    }
}
