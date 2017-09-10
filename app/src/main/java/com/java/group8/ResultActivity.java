package com.java.group8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by xzwkl on 17/9/8.
 */

public class ResultActivity extends AppCompatActivity {
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_result);

        ResultActivityOnClickListener raocl = new ResultActivityOnClickListener(this);

        EditText searchInput = (EditText) findViewById(R.id.searchInput);
        searchInput.setOnClickListener(raocl);
        searchInput.clearFocus();
        searchInput.selectAll();
        Intent intent = getIntent();
        String input = intent.getStringExtra("input");
        searchInput.setText(input);

        TextView cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(raocl);
        TextView submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(raocl);

        ListView resultList = (ListView) findViewById(R.id.resultList);
        //resultList.addView(findViewById(R.id.cancel));
    }
    public class ResultActivityOnClickListener implements View.OnClickListener {
        private Activity currentActivity;
        public ResultActivityOnClickListener(Activity a) {
            currentActivity = a;
        }
        public void onClick(View view) {
            if(view.getId() == R.id.submit) {
                startActivity(new Intent(currentActivity, NewsPageActivity.class));
            }
            else if(view.getId() == R.id.cancel) {
                startActivity(new Intent(currentActivity, MainActivity.class));
            }
            else if(view.getId() == R.id.searchInput) {
                startActivity(new Intent(currentActivity, SearchActivity.class));
            }
        }
    }
}
