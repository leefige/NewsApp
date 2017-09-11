package com.java.group8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
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
import java.util.Random;

/**
 *  Originally Created by Oleksii Shliama.
 *  @author Li Yifei
 */
public class ListViewFragment extends BaseRefreshFragment {

    private final int ADDITIONAL_SIZE_PER_LOAD = 20;
    private final int INIT_SIZE = 20;
    private Activity parent = null;
    private String tabTitle = null;
    private int listSize = INIT_SIZE;
    private SampleAdapter adapter = null;

    private ViewGroup rootView = null;
    private PullUpRefreshList listView = null;


    private PullToRefreshView pullDownView = null;
//    private PullToRefreshListView pullUpView;


    public void setMetadata(Activity p, NewsCategory c, String tab) {
        parent = p;
        category = c;
        tabTitle = tab;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list_view, container, false);
//        pullUpView = rootView.findViewById(R.id.pull_up_view);
        pullDownView = rootView.findViewById(R.id.pull_to_refresh);

        listView = rootView.findViewById(R.id.list_view);
        adapter = new SampleAdapter(getActivity(), R.layout.list_item, newsList, listView);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(new ListLoadMoreListener());


        pullDownView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullDownView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //TODO: DO REFRESH HERE
                        pullDownView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

//        pullUpView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                // Do work to refresh the list here.
//                new LoadMoreTask().execute();
//            }
//        });

        return rootView;
    }

    private class ListLoadMoreListener implements PullUpRefreshList.OnRefreshListener {
        @Override
        public void onLoadingMore() {
            //TODO: DO SOMETHING
            CircularProgressView progressView = listView.findViewById(R.id.progress_view);
            progressView.startAnimation();
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    SystemClock.sleep(2000);
                    newsList.add(new News(stringOfCategory(NewsCategory.CULTURE), "123456789", "", "This is a Big News",
                            "2017-09-08", "http://cnews.chinadaily.com.cn/2017-09/08/content_31716799.htm;http://img003.21cnimg.com/photos/album/20160808/m600/A3B78A702DF9BF0EE02ADFD5D4F53D54.jpeg",
                            "我", "zh_ch", "http://upload.qianlong.com/2016/0809/1470711910844.jpg", "", "Too young, too simple, sometimes naive!"));
                    newsList.add(new News(stringOfCategory(NewsCategory.SCIENCE), "123456789", "", "This is a Big News",
                            "2017-09-08", "http://cnews.chinadaily.com.cn/2017-09/08/content_31716799.htm;http://img003.21cnimg.com/photos/album/20160808/m600/A3B78A702DF9BF0EE02ADFD5D4F53D54.jpeg",
                            "我", "zh_ch", "http://upload.qianlong.com/2016/0809/1470711910844.jpg", "", "Too young, too simple, sometimes naive!"));
                    newsList.add(new News(stringOfCategory(NewsCategory.SCIENCE), "123456789", "", "This is a Big News",
                            "2017-09-08", "http://cnews.chinadaily.com.cn/2017-09/08/content_31716799.htm;http://img003.21cnimg.com/photos/album/20160808/m600/A3B78A702DF9BF0EE02ADFD5D4F53D54.jpeg",
                            "我", "zh_ch", "http://upload.qianlong.com/2016/0809/1470711910844.jpg", "", "Too young, too simple, sometimes naive!"));
                    newsList.add(new News(stringOfCategory(NewsCategory.SCIENCE), "123456789", "", "This is a Big News",
                            "2017-09-08", "http://cnews.chinadaily.com.cn/2017-09/08/content_31716799.htm;http://img003.21cnimg.com/photos/album/20160808/m600/A3B78A702DF9BF0EE02ADFD5D4F53D54.jpeg",
                            "我", "zh_ch", "http://upload.qianlong.com/2016/0809/1470711910844.jpg", "", "Too young, too simple, sometimes naive!"));
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    adapter.notifyDataSetChanged();

                    // 控制脚布局隐藏
                    listView.hideFooterView();
                }
            }.execute();
        }
    }

//    private class LoadMoreTask extends AsyncTask<Void, Void, List<News>> {
//
//
//        @Override
//        protected List<News> doInBackground (Void... size) {
//            //TODO: GET NEWS LIST FROM SERVICE
//            List<News> list = new ArrayList<>();
//            listSize += ADDITIONAL_SIZE_PER_LOAD;
//            return list;
//        }
//
//        @Override
//        protected void onPostExecute(List<News> result) {
//            // Call onRefreshComplete when the list has been refreshed.
//            pullUpView.onRefreshComplete();
//            //super.onPostExecute(result);
//            //TODO
//        }
//    }

    class SampleAdapter extends ArrayAdapter<News> {
        private final LayoutInflater mInflater;
        private final ListView listView;
        private final List<News> listData;
        private AsyncImageLoader asyncImageLoader;

        int[] icons = {
                R.drawable.icon_1,
                R.drawable.icon_2,
                R.drawable.icon_3};

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
            if (data.read) {
                viewHolder.titleView.setTextColor(getResources().getColor(R.color.newsRead));
            }
            else {
                viewHolder.titleView.setTextColor(getResources().getColor(R.color.newsUnread));
            }

            // download image from web
            Random rand = new Random();
            String imageUrl = data.news_Pictures.split(";")[0];
            ImageView tmpImageView = viewHolder.imageView;
            tmpImageView.setTag(imageUrl);

            if (!imageUrl.equals("")) {
                Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl, new AsyncImageLoader.ImageCallback() {
                    public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                        ImageView imageViewByTag = listView.findViewWithTag(imageUrl);
                        if (imageViewByTag != null) {
                            imageViewByTag.setImageDrawable(imageDrawable);
                        }
                    }
                });
                if (cachedImage == null) {
                    viewHolder.imageView.setImageResource(icons[rand.nextInt(3)]);
                } else {
                    viewHolder.imageView.setImageDrawable(cachedImage);
                }
            }
            else {
                viewHolder.imageView.setImageResource(icons[rand.nextInt(3)]);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(parent.getContext(), NewsPageActivity.class);
                    intent.putExtra("news_ID", data.news_ID);
                    startActivity(intent);
                }
            });
            return convertView;
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
}
