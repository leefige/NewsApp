package com.java.group8;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * @author Li Yifei
 */

public class MyApplication extends Application {

    private int nightMode;
    private String lastIndex;

    public int getNightMode() {
        return nightMode;
    }
    public String getLastIndex() {return lastIndex;}

    public void setNightMode(int nightMode) {
        this.nightMode = nightMode;
    }
    public void setLastIndex(String index) {this.lastIndex = index;}

    @Override
    public void onCreate() {
        super.onCreate();
        // 默认设置为日间模式
        nightMode = AppCompatDelegate.MODE_NIGHT_NO;
        lastIndex = "t";
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }
}
