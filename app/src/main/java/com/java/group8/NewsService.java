package com.java.group8;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

    //add key
    public static final String KEY = "getBy";
    public static final String NEWSID = "news_ID";
    public static final String NEWSCATEGORY = "_category";
    public static final String NEWSKEYWORD = "_keyword";

    //add value
    public static final String LIST = "List";
    public static final String DETAILS = "Details";
    public static final String SEARCH = "Search";


    //add action

    public static final String MAINACTION = "android.intent.action.NEWSLIST";
    public static final String DETAIACTION = "android.intent.action.NEWSDETAILS";
    public static final String SEARCHACTION = "android.intent.action.NEWSSEARCH";

    //add return key
    public static final String NEWSLIST = "newslist";
    public static final String NEWSDETAILS = "newsdetails";

    //add URL
    private static final String LATEST_URL = "http://166.111.68.66:2042/news/action/query/latest";
    private static final String LATEST_CATEGORY_URL = "http://166.111.68.66:2042/news/action/query/latest?category=";
    private static final String DETAIL_URL = "http://166.111.68.66:2042/news/action/query/detail?newsId=";
    private static final String SEARCH_URL = "http://166.111.68.66:2042/news/action/query/search?keyword=";

    //database
    private static final String SELECT = "select * from ";
    private static final String WHEREID = " where ID =?";

    public NewsService() {
        super("NewsService");
    }

    NewsDatabase dbmanager = null;

    @Override
    public void onCreate(){
        super.onCreate();
        dbmanager = new NewsDatabase(this);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String key = intent.getStringExtra(KEY);
            switch (key){
                case LIST:
                    Bundle b = intent.getExtras();
                    getNews((NewsCategory)b.get(NEWSCATEGORY));
                    Log.d("start", "startservice");
                    break;
                case DETAILS:
                    final String news_ID = intent.getStringExtra(NEWSID);
                    getDetails(news_ID);
                    Log.d("start", "getdetails");
                    break;
                case SEARCH:
                    final String keyword = intent.getStringExtra(NEWSKEYWORD);
                    getResult(keyword);
                    Log.d("start", "getresult");
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

    private void getNews(NewsCategory category){
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .get()
                .url(category == null? LATEST_URL : LATEST_CATEGORY_URL + category.getIndex())
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
                        String body = response.body().string();
                        Log.d("Response", body);
                        JSONObject json_obj = JSONObject.fromObject(body);
                        ContentValues values = new ContentValues();

                        //Log.d("res", json_obj.toString());
                        String id = json_obj.getString("news_ID");
                        values.put("ID", id);
                        String tag = json_obj.getString("newsClassTag");
                        values.put("ClassTag", tag);
                        String source = json_obj.getString("news_Source");
                        values.put("Source", source);
                        String title = json_obj.getString("news_Title");
                        values.put("Title", title);
                        String time = json_obj.getString("news_Time");
                        values.put("Time", time);
                        String url = json_obj.getString("news_URL");
                        values.put("URL", url);
                        String lang_type = json_obj.getString("lang_Type");
                        values.put("Type", lang_type);
                        String author = json_obj.getString("news_Author");
                        values.put("Author", author);
                        String pic = json_obj.getString("news_Pictures");
                        values.put("Pictures", pic);
                        String video = json_obj.getString("news_Video");
                        values.put("Video", video);
                        values.put("Read", 1);
                        News news = new News(tag, id, source, title, time, url, author, lang_type, pic, video, null);
                        news.read = true;

                        news.news_content.news_Category = json_obj.getString("news_Category");
                        //values.put("Category", news.news_content.news_Category);
                        news.news_content.inborn_KeyWords = json_obj.getString("inborn_KeyWords");
                        //values.put("Inborn", news.news_content.inborn_KeyWords);
                        news.news_content.news_Content = json_obj.getString("news_Content");
                        //values.put("Content", news.news_content.news_Content);
                        news.news_content.crawl_Source = json_obj.getString("crawl_Source");
                        //values.put("Crawl_Source", news.news_content.crawl_Source);
                        news.news_content.crawl_Time = json_obj.getString("crawl_Time");
                        //values.put("Crawl_Time", news.news_content.crawl_Time);
                        news.news_content.news_Journal = json_obj.getString("news_Journal");
                        //values.put("News_Journal", news.news_content.news_Journal);
                        news.news_content.repeat_ID = json_obj.getString("repeat_ID");
                        //values.put("Repeat_ID", news.news_content.repeat_ID);
                        news.news_content.seggedTitle = json_obj.getString("seggedTitle");
                        //values.put("Seggedtitle", news.news_content.seggedTitle);
                        news.news_content.wordCountOfTitle = json_obj.getInt("wordCountOfTitle");
                        //values.put("Counttitle", news.news_content.wordCountOfTitle);
                        news.news_content.wordCountOfContent = json_obj.getInt("wordCountOfContent");
                        //values.put("Countcontent", news.news_content.wordCountOfContent);


                        news.news_content.seggedPListOfContent = new ArrayList<String>();
                        JSONArray listcontent = json_obj.getJSONArray("seggedPListOfContent");
                        for(int i=0; i<listcontent.size(); i++){
                            news.news_content.seggedPListOfContent.add(listcontent.getString(i));
                            //Log.d("set", listcontent.getString(i));
                        }
                        //values.put("Seggedcontent", sb.toString());

                        news.news_content.persons = new ArrayList<News.NewsDetail.Person>();
                        JSONArray person_json = json_obj.getJSONArray("persons");
                        for(int i=0; i<person_json.size(); i++){
                            JSONObject person_json_obj = person_json.getJSONObject(i);
                            String word = person_json_obj.getString("word");
                            int count = person_json_obj.getInt("count");
                            News.NewsDetail.Person person = news.news_content.new Person(word, count);
                            news.news_content.persons.add(person);
                        }

                        news.news_content.locations = new ArrayList<News.NewsDetail.Location>();
                        JSONArray location_json = json_obj.getJSONArray("locations");
                        for(int i=0; i<location_json.size(); i++){
                            JSONObject location_json_obj = location_json.getJSONObject(i);
                            String word = location_json_obj.getString("word");
                            int count = location_json_obj.getInt("count");
                            News.NewsDetail.Location location = news.news_content.new Location(word, count);
                            news.news_content.locations.add(location);
                        }
                        news.news_content.organizations = new ArrayList<News.NewsDetail.Location>();
                        JSONArray organ_json = json_obj.getJSONArray("organizations");
                        for(int i=0; i<organ_json.size(); i++){
                            JSONObject organ_json_obj = organ_json.getJSONObject(i);
                            String word =organ_json_obj.getString("word");
                            int count = organ_json_obj.getInt("count");
                            News.NewsDetail.Location location = news.news_content.new Location(word, count);
                            news.news_content.organizations.add(location);
                        }
                        news.news_content.Keywords = new ArrayList<News.NewsDetail.Keyword>();
                        JSONArray keyword_json = json_obj.getJSONArray("Keywords");
                        for(int i=0; i<keyword_json.size(); i++){
                            JSONObject keyword_json_obj = keyword_json.getJSONObject(i);
                            String word = keyword_json_obj.getString("word");
                            double count = keyword_json_obj.getDouble("score");
                            News.NewsDetail.Keyword keyword = news.news_content.new Keyword(word, count);
                            news.news_content.Keywords.add(keyword);
                        }
                        news.news_content.bagOfWords = new ArrayList<News.NewsDetail.Word>();
                        JSONArray bag_json = json_obj.getJSONArray("bagOfWords");
                        for(int i=0; i<bag_json.size(); i++){
                            JSONObject bag_json_obj = bag_json.getJSONObject(i);
                            String word = bag_json_obj.getString("word");
                            double count = bag_json_obj.getDouble("score");
                            News.NewsDetail.Word bagword = news.news_content.new Word(word, count);
                            news.news_content.bagOfWords.add(bagword);
                        }
                        ByteArrayOutputStream OutputStream = new ByteArrayOutputStream();
                        try{
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(OutputStream);
                            objectOutputStream.writeObject(news.news_content);
                            objectOutputStream.flush();
                            byte data[] = OutputStream.toByteArray();
                            objectOutputStream.close();
                            OutputStream.close();
                            values.put("Details", data);
                            String _str = SELECT + NewsDatabase.ALL_TABLE_NAME + WHEREID;
                            String[] _s = {news.news_ID};
                            Cursor c = dbmanager.query(_str, _s);
                            if(c.moveToFirst() == false)
                                dbmanager.insert(values, NewsDatabase.ALL_TABLE_NAME);
                            Log.d("insert", "database");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        Log.d("wait", "a new");
                        Intent intent = new Intent();
                        intent.putExtra(NEWSDETAILS, news);
                        intent.setAction(DETAIACTION);
                        sendBroadcast(intent);
                    }
                });
            }
        }).start();

    }

    private void getResult(String keyWord){
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .get()
                .url(SEARCH_URL + keyWord)
                .build();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("begin", "SEARCH");
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
                        intent.setAction(SEARCHACTION);
                        sendBroadcast(intent);

                    }
                });
            }
        }).start();
    }

}
