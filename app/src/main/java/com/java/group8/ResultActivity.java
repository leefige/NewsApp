package com.java.group8;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.java.group8.ResultActivity.raocl;

/**
 * Created by xzwkl on 17/9/8.
 */

public class ResultActivity extends AppCompatActivity {

    public static ResultActivityOnClickListener raocl;
    private BaseAdapter_ResultActivity bara;
    private MyReceiver_result receiver = null;
    private ArrayList<News> resultList;
    public boolean loaded = false;

    public News getResultListX(int x) {
        return resultList.get(x);
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_result);
        getDelegate().setLocalNightMode(((MyApplication)getApplicationContext()).getNightMode());

        raocl = new ResultActivityOnClickListener(this);

        resultList = new ArrayList<News>();

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

        receiver = new MyReceiver_result();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NewsService.SEARCHACTION);
        ResultActivity.this.registerReceiver(receiver, filter);

        Intent serviceIntent = new Intent(this, NewsService.class);
        String key = NewsService.KEY;
        String value = NewsService.RESULT;
        String para1 = NewsService.NEWSKEYWORD;
        String keyword = input;
        serviceIntent.putExtra(key, value);
        serviceIntent.putExtra(para1, keyword);
        startService(serviceIntent);

        //resultList.addView(findViewById(R.id.cancel));
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

    public class MyReceiver_result extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            resultList.clear();
            resultList.addAll((ArrayList<News>) bundle.getSerializable(NewsService.NEWSLIST));
            loaded = true;
            bara = new BaseAdapter_ResultActivity(context, resultList.size());
            ListView resultList = (ListView) findViewById(R.id.resultList);
            resultList.setAdapter(bara);
        }
    }

    public class ResultActivityOnClickListener implements View.OnClickListener {
        private Activity currentActivity;
        public ResultActivityOnClickListener(Activity a) {
            currentActivity = a;
        }
        public void onClick(View view) {
            if(view.getId() == R.id.submit) {
                //startActivity(new Intent(currentActivity, NewsPageActivity.class));
            }
            else if(view.getId() == R.id.cancel) {
                Intent i = new Intent(currentActivity, SearchActivity.class);
                startActivity(i);
            }
            else if(view.getId() == R.id.searchInput) {
                startActivity(new Intent(currentActivity, SearchActivity.class));
            }
            else if(view.getId() == R.id.layout_result) {
                TextView text = view.findViewById(R.id.text_result);
                String title = text.getText().toString();
                News tar = null;
                for(News tmp : resultList) {
                    if(title.equals(tmp.news_Title)) {
                        tar = tmp;
                        break;
                    }
                }
                String news_ID = tar.news_ID;
                Intent inputIntent = new Intent(currentActivity, NewsPageActivity.class);
                inputIntent.putExtra("news_ID", news_ID);
                startActivity(inputIntent);
            }
        }
    }
}

class BaseAdapter_ResultActivity extends BaseAdapter {
    private Context context;
    private int size;
    public BaseAdapter_ResultActivity(Context c, int s) {
        context = c;
        size = s;
    }
    @Override
    public int getCount() {
        return (size <= 20) ? size : 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_display_result, null);
        view.setOnClickListener(raocl);
        TextView text = view.findViewById(R.id.text_result);
        text.setTextSize(18);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.textColorNorm, typedValue, true);
        text.setTextColor(context.getResources().getColor(typedValue.resourceId));
        text.setMinHeight(130);
        text.setPadding(0, 11, 0, 10);
        text.setText(((ResultActivity) context).getResultListX(position).news_Title);
        return view;
    }
}
