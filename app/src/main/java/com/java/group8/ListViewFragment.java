package com.java.group8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;
import java.util.Random;

/**
 *  Originally Created by Oleksii Shliama.
 *  @author Li Yifei
 */
public class ListViewFragment extends BaseRefreshFragment {

    private PullToRefreshView mPullToRefreshView;
    private Activity parent;
    private String tabTitle;

    public void setMetadata(Activity p, NewsCategory c, String tab) {
        parent = p;
        category = c;
        tabTitle = tab;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list_view, container, false);

        ListView listView = rootView.findViewById(R.id.list_view);
        listView.setAdapter(new SampleAdapter(getActivity(), R.layout.list_item, newsList, listView));

        mPullToRefreshView = rootView.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //TODO: DO REFRESH HERE
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

        return rootView;
    }

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
