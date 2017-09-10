package com.java.group8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_search);

        SearchActivityOnClickListener saocl = new SearchActivityOnClickListener(this);

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

            View view = mInflater.inflate(R.layout.horizonal_display_search,
                    mGallery_commend, false);
            ImageView img = (ImageView) view
                    .findViewById(R.id.image_search);
            img.setImageResource(mImgIds[i]);
            TextView txt = (TextView) view
                    .findViewById(R.id.text_search);
            txt.setText("some info ");
            mGallery_commend.addView(view);
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
        }
    }
}

