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

    protected NewsCategory category = null; //You are passing this null
    protected List<News> newsList = null; //You are passing this null

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        newsList = updateList(category);
    }

    public List<News> updateList(NewsCategory c) {
        //TODO: get info from SERVICE NewsDatabase
        List<News> list = new ArrayList<>();
        if(c == null)
            c = NewsCategory.CAR;
        list.add(new News(stringOfCategory(c), "123456789", "", "This is a Big News",
                "2017-09-08", "http://cnews.chinadaily.com.cn/2017-09/08/content_31716799.htm;http://img003.21cnimg.com/photos/album/20160808/m600/A3B78A702DF9BF0EE02ADFD5D4F53D54.jpeg",
                "我", "zh_ch", "http://upload.qianlong.com/2016/0809/1470711910844.jpg", "", "Too young, too simple, sometimes naive!"));
        list.add(new News(stringOfCategory(c), "123456789", "", "This is a Big News",
                "2017-09-08", "http://cnews.chinadaily.com.cn/2017-09/08/content_31716799.htm",
                "我", "zh_ch", "http://cms-bucket.nosdn.127.net/catchpic/6/69/69544c89ef3587fc92857afce37f05e7.jpg", "", "Too young, too simple, sometimes naive!"));
        list.add(new News(stringOfCategory(c), "123456789", "", "This is a Big News",
                "2017-09-08", "http://cnews.chinadaily.com.cn/2017-09/08/content_31716799.htm",
                "我", "zh_ch", "http://img003.21cnimg.com/photos/album/20160808/m600/A3B78A702DF9BF0EE02ADFD5D4F53D54.jpeg", "", "Too young, too simple, sometimes naive!"));
        list.add(new News(stringOfCategory(c), "123456789", "", "This is a Big News",
                "2017-09-08", "http://cnews.chinadaily.com.cn/2017-09/08/content_31716799.htm",
                "我", "zh_ch", "http://upload.qianlong.com/2016/0912/1473642904882.jpg", "", "Too young, too simple, sometimes naive!"));
        list.get(1).addDetail();
        return list;
    }

    public String stringOfCategory(NewsCategory c) {
        switch (c) {
            case SCIENCE:
                return getString(R.string.label_science);
            case EDUCATION:
                return getString(R.string.label_education);
            case MILITARY:
                return getString(R.string.label_military);
            case DOMESTIC:
                return getString(R.string.label_domestic);
            case SOCIETY:
                return getString(R.string.label_society);
            case CULTURE:
                return getString(R.string.label_culture);
            case CAR:
                return getString(R.string.label_car);
            case INTERNATIONAL:
                return getString(R.string.label_international);
            case SPORT:
                return getString(R.string.label_sport);
            case ECONOMY:
                return getString(R.string.label_economy);
            case HEALTH:
                return getString(R.string.label_health);
            case ENTERTAINMENT:
                return getString(R.string.label_entertainment);
        }
        return null;
    }
}
