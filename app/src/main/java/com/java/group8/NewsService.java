package com.java.group8;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NewsService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS

    //add value
    public static final String KEY = "getBy";
    public static final String LIST = "List";
    public static final String DETAILS = "Details";

    //add action

    public static final String MAINACTION = "android.intent.action.MAINACTION";


    //add return key
    public static final String NEWSLIST = "newslist";

    private static final String LATEST_URL = "http://166.111.68.66:2042/news/action/query/latest";
    private static final String DETAIL_URL = "http://166.111.68.66:2042/news/action/query/detail?newsId=";

    public NewsService() {
        super("NewsService");
    }


    @Override
    public void onCreate(){
        super.onCreate();

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String key = intent.getStringExtra(KEY);
            switch (key){
                case LIST:
                    getNews();
                    Log.d("start", "startservice");
                    break;
                case DETAILS:
                    final String news_ID = intent.getStringExtra("news_ID");
                    getDetails(news_ID);
                    Log.d("start", "getdetails");
                    break;
                default:
                    break;
            }
        }
    }

    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.i("myIntentService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }*/

    @Override
    public void onDestroy() {
        Log.i("myIntentService", "onDestroy");
        super.onDestroy();
    }

    private void getNews(){
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .get()
                .url(LATEST_URL)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("begin", "233");
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(!response.isSuccessful())
                            throw new IOException("Unexpected code " + response.code());
                        //Log.d("Response", response.body().string());
                        String body = response.body().string();
                        JSONObject json = JSONObject.fromObject(body);
                        JSONArray list = json.getJSONArray("list");
                        ArrayList<News> newslist = new ArrayList<News>();
                        for(int i=0; i<list.size(); i++){
                            JSONObject json_obj = list.getJSONObject(i);
                            //Log.d("res", json_obj.toString());
                            String id = json_obj.getString("news_ID");
                            String tag = json_obj.getString("newsClassTag");
                            String source = json_obj.getString("news_Source");
                            String title = json_obj.getString("news_Title");
                            String time = json_obj.getString("news_Time");
                            String url = json_obj.getString("news_URL");
                            String lang_type = json_obj.getString("lang_Type");
                            String author = json_obj.getString("news_Author");
                            String pic = json_obj.getString("news_Pictures");
                            String video = json_obj.getString("news_Video");
                            String intro = json_obj.getString("news_Intro");
                            News news = new News(tag, id, source, title, time, url, author, lang_type, pic, video, intro);
                            newslist.add(news);
                            //Log.d("tag", tag);
                        }
                        Log.d("wait", "a minute");
                        Intent intent = new Intent();
                        intent.putExtra(NEWSLIST, newslist);
                        intent.setAction(MAINACTION);
                        sendBroadcast(intent);

                    }
                });
            }
        }).start();
    }

    private void getDetails(String news_ID){
        Log.d("begin", "2");
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .get()
                .url(DETAIL_URL+news_ID)
                .build();
        Log.d("begin", "1");
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(!response.isSuccessful())
                            throw new IOException("Unexpected code " + response.code());
                        //Log.d("Response", response.body().string());
                        String body = response.body().string();
                        JSONObject json_obj = JSONObject.fromObject(body);

                        //Log.d("res", json_obj.toString());
                        String id = json_obj.getString("news_ID");
                        String tag = json_obj.getString("newsClassTag");
                        String source = json_obj.getString("news_Source");
                        String title = json_obj.getString("news_Title");
                        String time = json_obj.getString("news_Time");
                        String url = json_obj.getString("news_URL");
                        String lang_type = json_obj.getString("lang_Type");
                        String author = json_obj.getString("news_Author");
                        String pic = json_obj.getString("news_Pictures");
                        String video = json_obj.getString("news_Video");
                        String intro = json_obj.getString("news_Intro");
                        News news = new News(tag, id, source, title, time, url, author, lang_type, pic, video, intro);

                        news.news_content.news_Category = json_obj.getString("news_Category");
                        news.news_content.inborn_KeyWords = json_obj.getString("inborn_KeyWords");
                        news.news_content.news_Content = json_obj.getString("news_Content");
                        news.news_content.crawl_Source = json_obj.getString("crawl_Source");
                        news.news_content.crawl_Time = json_obj.getString("crawl_Time");
                        news.news_content.news_Journal = json_obj.getString("news_Journal");
                        news.news_content.repeat_ID = json_obj.getString("repeat_ID");
                        news.news_content.seggedTitle = json_obj.getString("seggedTitle");
                        news.news_content.wordCountOfTitle = json_obj.getInt("wordCountOfTitle");
                        news.news_content.wordCountOfContent = json_obj.getInt("wordCountOfContent");

                        news.news_content.seggedPListOfContent = new ArrayList<String>();
                        String[] listcontent = (String[]) json_obj.get("seggedPListOfContent");
                        for(int i=0; i<listcontent.length; i++){
                            news.news_content.seggedPListOfContent.add(listcontent[i]);
                        }








                        Log.d("wait", "a minute");
                        Intent intent = new Intent();
                        intent.putExtra("news", news);
                        intent.setAction("android.intent.action.DETAILS_BROADCAST");
                        sendBroadcast(intent);

                    }
                });
            }
        }).start();

    }
}
