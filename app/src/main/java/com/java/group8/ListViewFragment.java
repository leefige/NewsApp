package com.java.group8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 *  Originally Created by Oleksii Shliama.
 *  @author Li Yifei
 */
public class ListViewFragment extends Fragment {

    private final int INIT_SIZE = 20;
    public static final int REFRESH_DELAY = 2000;
    public static final int LOAD_DELAY = 2000;

    private ViewGroup rootView = null;
    private PullUpRefreshList listView = null;

    private SampleAdapter adapter = null;
    private PullToRefreshView pullDownView;

    private Activity parent = null;
    private String tabTitle = null;

    protected NewsCategory category = null; //You are passing this null
    protected List<News> newsList = null;   //You are passing this null
    private List<News> receiveList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        newsList = new ArrayList<>();
        updateList(category);
    }

    public SampleAdapter getAdapter() {
        return adapter;
    }

    public void goToTop() {
        GoTopTask task=new GoTopTask();
        task.execute(0);
    }

    public void notifyRead() {
        adapter.notifyDataSetChanged();
    }

    public void updateList(NewsCategory c) {

        Intent intent = new Intent(parent, NewsService.class);
        String key = NewsService.KEY;
        String value = NewsService.LIST;
        intent.putExtra(key, value);
        intent.putExtra(NewsService.NEWSCATEGORY, c);
        intent.putExtra(NewsService.MOVETYPE, NewsService.LOAD);
        parent.startService(intent);
    }

    public void refreshList(NewsCategory c) {

        Intent intent = new Intent(parent, NewsService.class);
        String key = NewsService.KEY;
        String value = NewsService.LIST;
        intent.putExtra(key, value);
        intent.putExtra(NewsService.NEWSCATEGORY, c);
        intent.putExtra(NewsService.MOVETYPE, NewsService.REFRESH);
        parent.startService(intent);
    }

    public void receiveListFromService(List<News> li, String moveType) {
        receiveList = li;
        switch (moveType) {
            case NewsService.LOAD:
                newsList.addAll(receiveList);
                break;
            case NewsService.REFRESH:
                newsList.clear();
                newsList.addAll(receiveList);
                break;
        }

        if (newsList == null) {
            Log.d("newsList", "is null");
        }
        else {
            Log.d("ITEM_size ", String.valueOf(newsList.size()));
        }
        adapter.notifyDataSetChanged();
    }

    public String stringOfCategory(NewsCategory c) {
        switch (c) {
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

    public void setMetadata(Activity p, NewsCategory c, String tab) {
        parent = p;
        category = c;
        tabTitle = tab;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list_view, container, false);
        pullDownView = rootView.findViewById(R.id.pull_to_refresh);

        listView = rootView.findViewById(R.id.list_view);
        adapter = new SampleAdapter(getActivity(), R.layout.list_item, newsList, listView);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(new ListLoadMoreListener());

        /**
         * Pull down to refresh
         */
        pullDownView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullDownView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshList(category);
                        pullDownView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

        return rootView;
    }


    /**
     * Pull up to load more
     */
    private class ListLoadMoreListener implements PullUpRefreshList.OnRefreshListener {
        @Override
        public void onLoadingMore() {
            //TODO: DO SOMETHING
            CircularProgressView progressView = listView.findViewById(R.id.progress_view);
            progressView.startAnimation();
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    SystemClock.sleep(LOAD_DELAY);
                    updateList(category);
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    // 控制脚布局隐藏
                    listView.hideFooterView();
                }
            }.execute();
        }
    }

    /**
     * About how to generate view
     */
    class SampleAdapter extends ArrayAdapter<News> {
        private final LayoutInflater mInflater;
        private final ListView listView;
        private final List<News> listData;
        private AsyncImageLoader asyncImageLoader;
        private final String REQUEST_HEAD = "https://image.baidu.com/search/index?tn=baiduimage&ipn=r&ct=201326592&cl=2&lm=-1&st=-1&sf=1&fmq=&pv=&ic=0&nc=1&z=&se=1&showtab=0&fb=0&width=&height=&face=0&istype=2&ie=utf-8&fm=index&pos=history&word=";

        public SampleAdapter(Context context, int layoutResourceId, List<News> data, ListView _listView) {
            super(context, layoutResourceId, data);
            listView = _listView;
            listData = data;
            mInflater = LayoutInflater.from(context);
            asyncImageLoader = new AsyncImageLoader();
        }

        @Override
        public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
            final ViewHolder viewHolder;
            final News data = listData.get(position);
            // setup view holder
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.titleView.setText(data.news_Title);
            viewHolder.contentView.setText(data.news_Intro);
            viewHolder.categoryView.setText(data.newsClassTag);
            TypedValue typedValue = new TypedValue();
            if (data.read) {
                getContext().getTheme().resolveAttribute(R.attr.textColorRead, typedValue, true);
            }
            else {
                getContext().getTheme().resolveAttribute(R.attr.textColorUnread, typedValue, true);
            }
            viewHolder.titleView.setTextColor(getResources().getColor(typedValue.resourceId));

            // download image from web
            String imageUrl = data.news_Pictures.split(";")[0];
            ImageView tmpImageView = viewHolder.imageView;
            tmpImageView.setTag(imageUrl);

            if (!imageUrl.equals("") && ((MyApplication)(parent.getContext().getApplicationContext())).isImageOn()) {
//                Log.d("PIC AT "+position, imageUrl);
                Drawable cachedImage = null;
                try {
                    cachedImage = asyncImageLoader.loadDrawable(imageUrl, new AsyncImageLoader.ImageCallback() {
                        public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                            ImageView imageViewByTag = listView.findViewWithTag(imageUrl);
                            if (imageViewByTag != null) {
                                imageViewByTag.setImageDrawable(imageDrawable);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.d("Pic Exception", e.getMessage());
                }
                finally {
                    if (cachedImage == null) {
                        viewHolder.imageView.setImageResource(R.drawable.icon_3);
                    } else {
                        viewHolder.imageView.setImageDrawable(cachedImage);
                    }
                }
            }
            else {
//                Log.d("PIC AT "+position, "empty");
                viewHolder.imageView.setImageResource(R.drawable.icon_3);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(parent.getContext(), NewsPageActivity.class);
                    intent.putExtra("news_ID", data.news_ID);
                    Log.d("click news item", "news_ID: "+data.news_ID);
                    listData.get(position).addDetail();
                    startActivity(intent);
                }
            });
            return convertView;
        }

        public ListView getListView() {
            return listView;
        }

        class ViewHolder {
            View frameView;
            ImageView imageView;
            TextView titleView;
            TextView contentView;
            TextView categoryView;

            public ViewHolder(View convertView) {
                frameView = convertView.findViewById(R.id.item_frame);
                imageView = convertView.findViewById(R.id.image_view_icon);
                titleView = convertView.findViewById(R.id.news_title);
                contentView = convertView.findViewById(R.id.news_content);
                categoryView = convertView.findViewById(R.id.news_category);
            }
        }
    }

    private class GoTopTask extends AsyncTask<Integer, Integer, String> {
        private int time;
        final private int MAX_SCROLL = 25;
        @Override
        protected void onPreExecute() {
            //回到顶部时间置0  此处的时间不是侠义上的时间
            time=0;
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Integer... params) {
            // TODO Auto-generated method stub

            for(int i=params[0];i>=0;i--){
                publishProgress(i);
                //返回顶部时间耗费15个item还没回去，则直接去顶部
                //目的：要产生滚动的假象，但也不能耗时过多
                time++;
                if(time > MAX_SCROLL){
                    publishProgress(0);
                    return null;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            adapter.getListView().setSelection(values[0]);
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }

    }
}
