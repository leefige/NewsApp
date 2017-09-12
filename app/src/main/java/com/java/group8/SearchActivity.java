package com.java.group8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by xzwkl on 17/9/7.
 */

public class SearchActivity extends AppCompatActivity {

    private LinearLayout mGallery_commend;
    private LinearLayout mGallery_history;
    private int[] mImgIds;
    private LayoutInflater mInflater;
    private SearchActivityOnClickListener saocl;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_search);
        getDelegate().setLocalNightMode(((MyApplication)getApplicationContext()).getNightMode());

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        saocl = new SearchActivityOnClickListener(this);

        TextView cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(saocl);

        TextView submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(saocl);

        EditText searchInput = (EditText) findViewById(R.id.searchInput);
        searchInput.selectAll();

        mInflater = LayoutInflater.from(this);
        initData();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDelegate().setLocalNightMode(((MyApplication)getApplicationContext()).getNightMode());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData()
    {
        mImgIds = new int[] { R.drawable.ic_main_category, R.drawable.ic_main_category,
                R.drawable.ic_main_category,
                R.drawable.ic_main_category, R.drawable.ic_main_category,
                R.drawable.ic_main_category, R.drawable.ic_main_category,
                R.drawable.ic_main_category, R.drawable.ic_main_category };
    }

    private void initView()
    {
        mGallery_commend = (LinearLayout) findViewById(R.id.commendLayout);
        mGallery_history = (LinearLayout) findViewById(R.id.historyLayout);

        for (int i = 0; i < mImgIds.length; i++)
        {
//            // download image from web
//            String imageUrl = data.news_Pictures.split(";")[0];
//            ImageView tmpImageView = viewHolder.imageView;
//            tmpImageView.setTag(imageUrl);
//
//            if (!imageUrl.equals("")) {
////                Log.d("PIC AT "+position, imageUrl);
//                Drawable cachedImage = null;
//                try {
//                    cachedImage = asyncImageLoader.loadDrawable(imageUrl, new AsyncImageLoader.ImageCallback() {
//                        public void imageLoaded(Drawable imageDrawable, String imageUrl) {
//                            ImageView imageViewByTag = listView.findViewWithTag(imageUrl);
//                            if (imageViewByTag != null) {
//                                imageViewByTag.setImageDrawable(imageDrawable);
//                            }
//                        }
//                    });
//                } catch (Exception e) {
//                    Log.d("Pic Exception", e.getMessage());
//                }
//                finally {
//                    if (cachedImage == null) {
//                        viewHolder.imageView.setImageResource(R.drawable.icon_3);
//                    } else {
//                        viewHolder.imageView.setImageDrawable(cachedImage);
//                    }
//                }
//            }
//            else {
////                Log.d("PIC AT "+position, "empty");
//                viewHolder.imageView.setImageResource(R.drawable.icon_3);
//            }
        }
        for (int i = 0; i < mImgIds.length; i++)
        {

            View view = mInflater.inflate(R.layout.horizonal_display_search,
                    mGallery_history, false);
            ImageView img = (ImageView) view
                    .findViewById(R.id.image_search);
            img.setImageResource(mImgIds[i]);
            TextView txt = (TextView) view
                    .findViewById(R.id.text_search);
            txt.setText("some info ");
            view.setOnClickListener(saocl);
            mGallery_history.addView(view);
        }
    }

    public class SearchActivityOnClickListener implements View.OnClickListener {
        Activity current_activity;
        public SearchActivityOnClickListener(Activity a) {
            current_activity = a;
        }
        public void onClick(View view) {
            if(view.getId() == R.id.cancel) {
                Intent i = new Intent(current_activity, MainActivity.class);
                startActivity(i);
            }
            else if(view.getId() == R.id.submit) {
                Intent i = new Intent(current_activity, ResultActivity.class);
                EditText e = (EditText) findViewById(R.id.searchInput);
                i.putExtra("input", e.getText().toString());
                startActivity(i);
            }
            else if(view.getId() == R.id.layout_search) {
                Intent i = new Intent(current_activity, NewsPageActivity.class);
                startActivity(i);
            }
        }
    }
}

