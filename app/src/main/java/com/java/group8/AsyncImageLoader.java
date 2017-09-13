package com.java.group8;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * @author Li Yifei
 */

public class AsyncImageLoader {
    private HashMap<String, SoftReference<Drawable>> imageCache;
    private final String IMAGE_URL = "https://api.cognitive.microsoft.com/bing/v5.0/images/search?subscription-key=2a11735afda3496aa0c4fc2c9a6c5641&q=";

    public AsyncImageLoader() {
        imageCache = new HashMap<>();
    }

    public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
        if (imageCache.containsKey(imageUrl)) {
            SoftReference<Drawable> softReference = imageCache.get(imageUrl);
            Drawable drawable = softReference.get();
            if (drawable != null) {
                return drawable;
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
            }
        };
        new Thread() {
            @Override
            public void run() {
                Drawable drawable = loadImageFromUrl(imageUrl);
                imageCache.put(imageUrl, new SoftReference<>(drawable));
                Message message = handler.obtainMessage(0, drawable);
                handler.sendMessage(message);
            }
        }.start();
        return null;
    }

    public Drawable loadDrawableFromTitle(final String title, final ImageCallback imageCallback) {
        if (imageCache.containsKey(title)) {
            SoftReference<Drawable> softReference = imageCache.get(title);
            Drawable drawable = softReference.get();
            if (drawable != null) {
                return drawable;
            }
        }

        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Drawable) message.obj, title);
            }
        };

        new Thread() {
            @Override
            public void run() {
                String mUrl = getImage(title);
                Log.d("Find IMG", mUrl);
                Drawable drawable = loadImageFromUrl(mUrl);
                imageCache.put(title, new SoftReference<>(drawable));
                Message message = handler.obtainMessage(0, drawable);
                handler.sendMessage(message);
            }
        }.start();
        return null;
    }

    public static Drawable loadImageFromUrl(String url) {
        URL m;
        InputStream i = null;
        try {
            m = new URL(url);
            i = (InputStream) m.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable d = Drawable.createFromStream(i, "src");
        return d;
    }

    private String getImage(String title) {
        String str = title.substring(0, 6 > title.length() ? title.length() : 6);
        Log.d("image", str);
        HttpURLConnection coon = null;
        InputStream inputStream = null;
        try {
            URL mURL = new URL(IMAGE_URL + str);
            coon = (HttpURLConnection) mURL.openConnection();
            coon.setReadTimeout(1000);
            coon.setConnectTimeout(1000);

            coon.setRequestMethod("GET");
            int statusCode = coon.getResponseCode();
            if (statusCode == 200) {
                InputStream is = coon.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                // 模板代码 必须熟练
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                is.close();
                String state = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
                os.close();
                JSONObject json = JSONObject.fromObject(state);
                //Log.d("state", state);
                JSONArray json_array = json.getJSONArray("value");
                if (json_array.size() == 0)
                    return "";
                String pic = json_array.getJSONObject(0).getString("contentUrl");
                Log.d("image", pic);
                return pic;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("image", "flase");
        return "";
    }

    public interface ImageCallback {
        void imageLoaded(Drawable imageDrawable, String imageUrl);
    }
}