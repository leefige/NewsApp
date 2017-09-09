package com.java.group8;

/**
 * @auther Li Yifei
 *
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    final static int RESPONSE_FROM_CATEGORY = 0;

    private RecyclerView recyclerView;
    private List<NewsCategory> myTags;
    private HomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initData();

        recyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//设置适配器
        recyclerView.setAdapter(adapter = new HomeAdapter());
        adapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("category", position);
                setResult(RESPONSE_FROM_CATEGORY, intent);
                finish();
            }

            @Override
            public void onItemLongClick(View view, int position) {
//                TODO: ADD REMOVE TAG
                onItemClick(view, position);
            }
        });
//设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("category", -1);
                setResult(RESPONSE_FROM_CATEGORY, intent);
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initData()
    {
        myTags = new ArrayList<>();
        for (int i = 1; i <= 12; i++)
        {
            myTags.add(NewsCategory.valueOf(i));
        }
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

    public int iconOfCategory(NewsCategory c) {
        switch (c) {
            case SCIENCE:
                return R.drawable.ic_atom;
            case EDUCATION:
                return R.drawable.ic_book;
            case MILITARY:
                return R.drawable.ic_sword;
            case DOMESTIC:
                return R.drawable.ic_greatwall;
            case SOCIETY:
                return R.drawable.ic_social;
            case CULTURE:
                return R.drawable.ic_culture;
            case CAR:
                return R.drawable.ic_car;
            case INTERNATIONAL:
                return R.drawable.ic_earth;
            case SPORT:
                return R.drawable.ic_olympic;
            case ECONOMY:
                return R.drawable.ic_economics;
            case HEALTH:
                return R.drawable.ic_health;
            case ENTERTAINMENT:
                return R.drawable.ic_game;
        }
        return R.drawable.ic_atom;
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(CategoryActivity.this).inflate(R.layout.recyclerview_item, parent,
                    false));
            return holder;
        }

        @Override
        public int getItemCount()
        {
            return myTags.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {
            View itemFrame;
            ImageView iv;
            TextView tv;

            public MyViewHolder(View view)
            {
                super(view);
                tv = view.findViewById(R.id.id_num);
                itemFrame = view.findViewById(R.id.category_main_frame);
                iv = view.findViewById(R.id.category_icon);
//                iv.setMinimumHeight(iv.getHeight());
//                tv.setHeight(((Double)(0.25 * itemFrame.getWidth())).intValue());
            }
        }

        private OnItemClickLitener onItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
        {
            this.onItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position)
        {
            holder.tv.setText(stringOfCategory(myTags.get(position)));
            holder.iv.setImageResource(iconOfCategory(myTags.get(position)));
//            holder.iv.setMaxHeight(((Double)(0.5 * holder.itemFrame.getWidth())).intValue());
//            holder.tv.setHeight(holder.itemFrame.getWidth() - holder.iv.getHeight());

            // 如果设置了回调，则设置点击事件
            if (onItemClickLitener != null)
            {
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int pos = holder.getLayoutPosition();
                        onItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        int pos = holder.getLayoutPosition();
                        onItemClickLitener.onItemLongClick(holder.itemView, pos);
                        return false;
                    }
                });
            }
        }
    }
}
