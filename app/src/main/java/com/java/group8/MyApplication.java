package com.java.group8;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by Liyf on 2017/9/11.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 默认设置为日间模式
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
