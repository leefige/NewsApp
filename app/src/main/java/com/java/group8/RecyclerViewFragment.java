package com.java.group8;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalantis.phoenix.PullToRefreshView;

import java.util.Random;

/**
 * Created by Oleksii Shliama.
 */
public class RecyclerViewFragment extends BaseRefreshFragment {

    private PullToRefreshView mPullToRefreshView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recycler_view, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(new SampleAdapter());

        mPullToRefreshView = rootView.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//TODO: REFRESH HERE
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

        return rootView;
    }

    /**
     * Adapter for recyclerview
     * accept the list of entries
     */
    private class SampleAdapter extends RecyclerView.Adapter<SampleHolder> {

        @Override
        public SampleHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            return new SampleHolder(view);
        }

        @Override
        public void onBindViewHolder(SampleHolder holder, int pos) {
            News data = newsList.get(pos);
            holder.bindData(data);
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }
    }

    /**
     * View holder for recyclerview, the container of real content
     * one entry, one holder
     */
    private class SampleHolder extends RecyclerView.ViewHolder {

        private View mRootView;
        private ImageView mImageViewIcon;
        private TextView titleView;
        private TextView contentView;

        private News mData; // an item in the list
        int[] colors = {
                R.color.saffron,
                R.color.eggplant,
                R.color.sienna};
        int[] icons = {
                R.drawable.icon_1,
                R.drawable.icon_2,
                R.drawable.icon_3};

        public SampleHolder(View itemView) {
            super(itemView);

            mRootView = itemView;
            mImageViewIcon = itemView.findViewById(R.id.image_view_icon);
            titleView = itemView.findViewById(R.id.news_title);
            contentView = itemView.findViewById(R.id.news_content);
        }

        public void bindData(News data) {
            mData = data;
            Random rand = new Random();
            mRootView.setBackgroundResource(colors[rand.nextInt(3)]);
            mImageViewIcon.setImageResource(icons[rand.nextInt(3)]);
            titleView.setText(mData.news_Title);
            contentView.setText(mData.news_Intro);
        }
    }

}
