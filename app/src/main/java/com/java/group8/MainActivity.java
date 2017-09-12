package com.java.group8;

/**
 * @auther Li Yifei
 *
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.content.IntentFilter;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.EditText;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    final static int PAGE_COUNT = 12;
    final static int CALL_FROM_MAIN = 0;

    private boolean nightChecked = false;

    //receiver接受service数据
    private MyReceiver receiver = null;

    private int requestIndex = 0;


    ////////////////////////////////////////
    HashMap<NewsCategory, ListViewFragment> fragMap = null;
    ////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        //开启receiver,选择filter
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NewsService.MAINACTION);
        //绑定filter
        MainActivity.this.registerReceiver(receiver,filter);

        //像service发送数据
        Intent intent = new Intent(this, NewsService.class);
        String key = NewsService.KEY;
        String value = NewsService.LIST;
        intent.putExtra(key, value);
        //intent.putExtra(NewsService.NEWSKEYWORD, "北京");
        NewsCategory c = null;   //NewsCategory.CAR;
        intent.putExtra(NewsService.NEWSCATEGORY, c);
        //intent.putExtra(NewsService.NEWSID, "20160913041301d5fc6a41214a149cd8a0581d3a014f");
        startService(intent);
        /**
         *  Following lines are for Tab & Slide page
         */
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        /**
         *  Following lines are for Navigation drawer
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**
         *  Following lines are for Floating button
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fragMap = new HashMap<>();
    }
    @Override
    public void onResume() {
        super.onResume();
        getDelegate().setLocalNightMode(((MyApplication)getApplicationContext()).getNightMode());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CategoryActivity.RESPONSE_FROM_CATEGORY) {
            int idx = data.getIntExtra("category", tabLayout.getSelectedTabPosition());
            if (idx < 0) {
                return;
            }
            tabLayout.getTabAt(idx).select();
        }
    }

    /**
     * Methods related to OPTIONS MENU
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_category) {
            startActivityForResult(new Intent(this, CategoryActivity.class), CALL_FROM_MAIN);
            return true;
        }
        else if (id == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Methods related to NAVIGATION PAGE
     */
    // Make sure navigation page is closed after back
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favourite) {
//TODO:     add favourite page
            startActivity(new Intent(this, FavoriteActivity.class));
        }
        else if (id == R.id.nav_manage) {
//TODO:     clear cache
        }
        else if (id == R.id.nav_night) {
//            boolean isChecked = item.isChecked();
//            item.setChecked(!isChecked);
            nightChecked = !nightChecked;
            item.setChecked(nightChecked);
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            int neoNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_NO ?
                    AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
            getDelegate().setLocalNightMode(neoNightMode);
            ((MyApplication)getApplicationContext()).setNightMode(neoNightMode);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
//            recreate();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("night", nightChecked);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nightChecked = savedInstanceState.getBoolean("night");
    }
    /**
     * Classes defined for LEFT-RIGHT SLIDE
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //TODO: PASS CATEGORY TO FRAGMENT
            ListViewFragment fragment = new ListViewFragment();
            fragment.setMetadata(MainActivity.this, NewsCategory.valueOf(position + 1), String.valueOf(getPageTitle(position)));
            fragMap.put(NewsCategory.valueOf(position + 1), fragment);

            return fragment;
        }


        @Override
        public int getCount() {
            // Show total pages.
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (NewsCategory.valueOf(position + 1)) {
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
    public void setRequestIndex(int requestIndex) {
        this.requestIndex = requestIndex;
    }
    //receiver需要类
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            ArrayList<News> newslist = (ArrayList<News>) bundle.get(NewsService.NEWSLIST);
            ListViewFragment frag = fragMap.get(NewsCategory.valueOf(requestIndex));
            frag.receiveListFromService(newslist);
            Log.d("yew", "perfect");
            String name = newslist.get(0).newsClassTag;
            Log.d("news", name);
        }
    }
}
