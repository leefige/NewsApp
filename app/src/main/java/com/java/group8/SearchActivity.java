package com.java.group8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by xzwkl on 17/9/7.
 */

public class SearchActivity extends AppCompatActivity {
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

