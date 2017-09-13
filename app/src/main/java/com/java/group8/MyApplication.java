package com.java.group8;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import android.text.format.Time;

/**
 * @author Li Yifei
 */

public class MyApplication extends Application {


    public void setImageOn(boolean imageOn) {
        this.imageOn = imageOn;
    }

    public boolean isImageOn() {
        return imageOn;
    }

    private boolean imageOn = true;

    private int nightMode;

    private String lastIndex;

    public int getNightMode() {
        return nightMode;
    }

    public String getLastIndex() {
        return lastIndex;
    }

    public void setNightMode(int nightMode) {
        this.nightMode = nightMode;
    }

    public void setLastIndex(String index) {
        this.lastIndex = index;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 默认设置为日间模式
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int hour = t.hour; // 0-23
        nightMode = hour < 7 || hour >= 23 ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        lastIndex = "科技";
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }
}
