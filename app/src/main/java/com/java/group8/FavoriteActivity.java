package com.java.group8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.swipe.util.Attributes;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by xzwkl on 17/9/10.
 */

public class FavoriteActivity extends AppCompatActivity {

    private ListView listview;
    private SwipeLayout swipeLayout;
    private MyReceiver_favorite receiver;
    private ArrayList<News> favList;
    private ListViewAdapter lva;

    public News getFavListX(int x) { return favList.get(x);}
    public void delFavListX(int x) {
        favList.remove(x);
    }

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_favorite);
        getDelegate().setLocalNightMode(((MyApplication)getApplicationContext()).getNightMode());

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        favList = new ArrayList<News>();

        receiver = new MyReceiver_favorite();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NewsService.FAVACTION);
        FavoriteActivity.this.registerReceiver(receiver, filter);

        //TODO : favorite
        Intent serviceIntent = new Intent(this, NewsService.class);
        String key = NewsService.KEY;
        String value = NewsService.NEWSFAVORITE;
        String para1 = NewsService.SERVICEKIND;
        String newsfavorite = NewsService.NEWSFAVORITE;
        serviceIntent.putExtra(key, value);
        serviceIntent.putExtra(para1, newsfavorite);
        startService(serviceIntent);

        listview = (ListView) findViewById(R.id.listview_favorite);
//        SwipeLayout.SwipeListener sl = new SwipeLayout.SwipeListener() {
//            @Override
//            public void onStartOpen(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onOpen(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onStartClose(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onClose(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
//
//            }
//
//            @Override
//            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
//
//            }
//        };
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout)(listview.getChildAt(position - listview.getFirstVisiblePosition()))).open(true);
            }
        });
        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ListView", "OnTouch");
                return false;
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Object mContext;
//                Toast.makeText(mContext, "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        listview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });

//        SwipeLayout s0 = (SwipeLayout) findViewById(R.id.swipeLayout_favorite);
//        TextView s0_delete = (TextView) s0.findViewById(R.id.delete_favorite);
//        s0_delete.setText("0");
//        listview.addView(s0);

//        swipeLayout =  (SwipeLayout)findViewById(R.id.swipeLayout_favorite);
//
////set show mode.
//        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
//
////add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
//        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_view_favorite));
//

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favourite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
            case R.id.action_clear_favourite:
                //TODO: CLEAR FAVOURITE
                Intent intent = new Intent(this, NewsService.class);
                String key = NewsService.KEY;
                String value = NewsService.CLEARFAV;
                String para1 = NewsService.SERVICEKIND;
                String servicekind = NewsService.DELETE_ALL;
                intent.putExtra(key, value);
                intent.putExtra(para1, servicekind);
                startService(intent);
                favList.clear();
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public class MyReceiver_favorite extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String servicekind = bundle.getString(NewsService.SERVICEKIND);
            if(servicekind.equals(NewsService.NEWSFAVORITE)) {
                ArrayList<News> tmp = (ArrayList<News>) bundle.getSerializable(NewsService.FAVLIST);
                if(tmp == null) {
                    favList.clear();
                } else {
                    favList.addAll(tmp);
                }
            } else if(servicekind.equals(NewsService.DELEATE_ONE)) {
                Toast.makeText(context, "该条已删除", Toast.LENGTH_SHORT).show();
            } else if(servicekind.equals(NewsService.DELETE_ALL)) {
                Toast.makeText(context, "收藏已清空", Toast.LENGTH_SHORT).show();
            }
            int size = (favList == null) ? 0 : favList.size();
            lva = new ListViewAdapter(context, size);
            listview.setAdapter(lva);
            lva.setMode(Attributes.Mode.Single);
        }
    }
}

class ListViewAdapter extends BaseSwipeAdapter {
    private Context mContext;
    private int size;

    public ListViewAdapter(Context mContext, int s) {
        this.mContext = mContext;
        this.size = s;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeLayout_favorite;
    }

    //ATTENTION: Never bind listener or fill values in generateView.
    //           You have to do that in fillValues method.
    @Override
    public View generateView(int position, ViewGroup parent) {
        final int position_tmp = position;
        final View v = LayoutInflater.from(mContext).inflate(R.layout.swipe_layout_item, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {

            }
        });
        TextView delete = v.findViewById(R.id.delete_favorite);
        TextView url = v.findViewById(R.id.url_favorite);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "record delete", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, NewsService.class);
                String key = NewsService.KEY;
                String value = NewsService.FAV;
                String para1 = NewsService.NEWSID;
                News tmp = ((FavoriteActivity)mContext).getFavListX(position_tmp);
                String news_ID = tmp.news_ID;
                String para2 = NewsService.SERVICEKIND;
                String servicekind = NewsService.DELEATE_ONE;
                intent.putExtra(key, value);
                intent.putExtra(para1, news_ID);
                intent.putExtra(para2, servicekind);
                mContext.startService(intent);
                ((FavoriteActivity)mContext).delFavListX(position_tmp);
                //notifyDataSetChanged();
            }
        });
        final News tar = ((FavoriteActivity) mContext).getFavListX(position);
        url.setText(tar.news_Title);
        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent input = new Intent(mContext, NewsPageActivity.class);
                input.putExtra("news_ID", tar.news_ID);
                Toast.makeText(mContext, "go to" + tar.news_URL, Toast.LENGTH_SHORT).show();
                mContext.startActivity(input);
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView bottom_t = (TextView) convertView.findViewById(R.id.delete_favorite);
        bottom_t.setText("删除");
        TextView surface_t = (TextView) convertView.findViewById(R.id.url_favorite);
        surface_t.setText(((FavoriteActivity) mContext).getFavListX(position).news_Title);
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}