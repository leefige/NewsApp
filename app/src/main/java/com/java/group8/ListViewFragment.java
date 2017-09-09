package com.java.group8;

import android.content.Context;
import android.graphics.Bitmap;
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

import java.net.URL;
import java.util.List;
import java.util.Random;

/**
 * Created by Oleksii Shliama.
 */
public class ListViewFragment extends BaseRefreshFragment {

    private PullToRefreshView mPullToRefreshView;

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
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

        return rootView;
    }

    class SampleAdapter extends ArrayAdapter<News> {
//        public static final String KEY_ICON = "icon";
//        public static final String KEY_COLOR = "color";
        private final LayoutInflater mInflater;
        private final ListView listView;
        private final List<News> listData;
        private AsyncImageLoader asyncImageLoader;

        int[] colors = {
                R.color.saffron,
                R.color.eggplant,
                R.color.sienna};
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
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
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

//            viewHolder.imageView.setImageResource(listData.get(position).get(KEY_ICON));
//            convertView.setBackgroundResource(listData.get(position).get(KEY_COLOR));
            Random rand = new Random();
            URL imgUrl = null;
            Bitmap newsImg = null;

            String imageUrl = data.news_Pictures;
            ImageView tmpImageView = viewHolder.imageView;
            tmpImageView.setTag(imageUrl);

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
            }else{
                viewHolder.imageView.setImageDrawable(cachedImage);
            }

//            try {
//                new URL(data.news_Pictures);
//            } catch (Exception e) {
//                //TODO: WE CAN FIND A PIC FOR IT
//            }
//
//            if (imgUrl != null) {
//                DownloadImg dlImg = new DownloadImg(imgUrl);
//                Thread imgThread = new Thread(dlImg);
//                imgThread.start();
//                try {
//                    imgThread.join();
//                }catch (Exception e) {}
//                newsImg = dlImg.getImg();
//            }
//            if (newsImg != null) {
//                viewHolder.imageView.setImageBitmap(newsImg);
//            }
//            else {
//                viewHolder.imageView.setImageResource(icons[rand.nextInt(3)]);
//            }

            viewHolder.frameView.setBackgroundResource(colors[rand.nextInt(3)]);
//            viewHolder.imageView.setImageResource(icons[rand.nextInt(3)]);
            viewHolder.titleView.setText(data.news_Title);
            viewHolder.contentView.setText(data.news_Intro);

            return convertView;
        }

        class ViewHolder {
            View frameView;
            ImageView imageView;
            TextView titleView;
            TextView contentView;

            public ViewHolder(View convertView) {
                frameView = convertView.findViewById(R.id.item_frame);
                imageView = convertView.findViewById(R.id.image_view_icon);
                titleView = convertView.findViewById(R.id.news_title);
                contentView = convertView.findViewById(R.id.news_content);
            }
        }

//        class DownloadImg implements Runnable {
//            private URL url;
//            private Bitmap img;
//
//            DownloadImg(URL _url) {
//                url = _url;
//            }
//
//            @Override
//            public void run() {
//                try {
//                    img = getBitmap();
//                } catch (IOException e) {
//                    //TODO: I DONT'T KNOW WHAT TO DO :)
//                }
//            }
//
//            public Bitmap getImg() {
//                return img;
//            }
//
//            public void getBitmap() throws IOException {
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setConnectTimeout(5000);
//                conn.setRequestMethod("GET");
//                if (conn.getResponseCode() == 200) {
//                    InputStream inputStream = conn.getInputStream();
////                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    viewHolder.imageView.setImageBitmap(bitmap);
//
////                    return bitmap;
//                }
////                return null;
//            }
//        }
    }
}
