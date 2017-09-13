package com.java.group8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.java.group8.NewsPageActivity.mTts;

/**
 * Created by xzwkl on 17/9/9.
 */

public class NewsPageActivity extends AppCompatActivity {

    private MyReceiver_newspage receiver = null;
    private ShareActionProvider mShareActionProvider;
    public static SpeechSynthesizer mTts;

    private News current_news;

    public News getCurrentNews() {return current_news;}

    public NewsPageActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_newspage);
        getDelegate().setLocalNightMode(((MyApplication)getApplicationContext()).getNightMode());

        Toolbar toolbar_newspage = (Toolbar) findViewById(R.id.toolbar_newspage);
        setSupportActionBar(toolbar_newspage);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        //使能app bar的导航功能
        if(ab != null){
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        //读入新闻检索信息，从数据库载入
        Intent input = getIntent();
        String news_ID = input.getStringExtra("news_ID");

        receiver = new MyReceiver_newspage();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NewsService.DETAIACTION);
        NewsPageActivity.this.registerReceiver(receiver, filter);
        Intent intent =  new Intent(this, NewsService.class);
        String key = NewsService.KEY;
        String value = NewsService.DETAILS;
        intent.putExtra(key, value);
        String para1 = "news_ID";
        intent.putExtra(para1, news_ID);
        String para2 = NewsService.SERVICEKIND;
        String detail = NewsService.DETAILS;
        intent.putExtra(para2, detail);
        startService(intent);

        TextView content = (TextView) findViewById(R.id.content_newspage);
//        TextView title = (TextView) findViewById(R.id.title_newspage);
//        TextView content = (TextView) findViewById(R.id.content_newspage);
//        BaseAdapter_newspage banp = new BaseAdapter_newspage(this);
//        title.setAdapter


        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=59b4fc49");
//        SpeechUtility s = SpeechUtility.getUtility();
//        if(s == null) System.out.print("hahah");
        mTts = SpeechSynthesizer.createSynthesizer(this.getApplicationContext(), new InitListener() {
            @Override
            public void onInit(int i) {

            }
        });
        String engineType = "xiaoyan";

        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mTts.setParameter(SpeechConstant.ENGINE_MODE, SpeechConstant.MODE_AUTO );

//        if( SpeechConstant.TYPE_LOCAL.equals(engineType)
//                &&SpeechConstant.MODE_MSC.equals(engineMode) ){
//            // 需下载使用对应的离线合成SDK
//            mTts.setParameter( ResourceUtil.TTS_RES_PATH, ttsResPath );
//        }

        mTts.setParameter( SpeechConstant.VOICE_NAME, "xiaoyan");
    }

    @Override
    public void onResume() {
        super.onResume();
        getDelegate().setLocalNightMode(((MyApplication)getApplicationContext()).getNightMode());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_newspage_opinions, menu);

        MenuItem read_newspage = menu.findItem(R.id.read_newspage);
        MyReadActionProvider mActionProvider = (MyReadActionProvider) MenuItemCompat.getActionProvider(read_newspage);
        mActionProvider.setContext(this);

        MenuItem share_newspage = menu.findItem(R.id.share_newspage);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(share_newspage);
//        MenuItem.OnMenuItemClickListener omicl = mActionProvider;
//        read_newspage.setOnMenuItemClickListener(omicl);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        setShareIntent(sendIntent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_newspage:
                break;
            case R.id.favor_newspage:
                Intent intent = new Intent(this, NewsService.class);
                String key = NewsService.KEY;
                String value = NewsService.FAV;
                String para1 = NewsService.NEWSID;
                String news_ID = current_news.news_ID;
                String para2 = NewsService.SERVICEKIND;
                String servicekind = NewsService.FAV;
                intent.putExtra(key, value);
                intent.putExtra(para1, news_ID);
                intent.putExtra(para2, servicekind);
                startService(intent);
                break;
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }


    public class MyReceiver_newspage extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String servicekind = bundle.getString(NewsService.SERVICEKIND);
            if(servicekind.equals(NewsService.DETAILS)) {
                current_news = (News) bundle.getSerializable(NewsService.NEWSDETAILS);
                TextView title = (TextView) findViewById(R.id.title_newspage);
                title.setText(current_news.news_Title);
                String src = !current_news.news_content.news_Journal.equals("") ? current_news.news_content.news_Journal :
                        !current_news.news_Author.equals("") ? current_news.news_Author :
                                !current_news.news_Source.equals("") ? current_news.news_Source : "未知来源";
                ((TextView) findViewById(R.id.journal_newspage)).setText(src);
                if(current_news.news_Time.length() > 8){
                    String time_string = current_news.news_Time.substring(0, 8);
                    String show_time = time_string.substring(0, 4)+"年"+time_string.substring(4, 6)+"月"+time_string.substring(6, 8)+"日";
                    ((TextView) findViewById(R.id.time_newspage)).setText(show_time);
                }

                //String time_string = current_news.news_Time.substring(0, 8);
                //String show_time = time_string.substring(0, 4)+"年"+time_string.substring(4, 6)+"月"+time_string.substring(6, 8)+"日";
                //((TextView) findViewById(R.id.time_newspage)).setText(show_time);

                String my_news_content = current_news.news_content.news_Content;
                Pattern p =  Pattern.compile("\\s\\s+");
                Matcher matcher = p.matcher(my_news_content);
                my_news_content = matcher.replaceAll("\n\n");
                p = Pattern.compile("^\\s+");
                matcher = p.matcher(my_news_content);
                my_news_content = matcher.replaceFirst("");

                TextView content = (TextView) findViewById(R.id.content_newspage);
                SpannableString ss = new SpannableString(my_news_content);
                setSpanPart(ss);
                content.setText(ss);
                content.setMovementMethod(LinkMovementMethod.getInstance());
            } else if(servicekind.equals(NewsService.FAV)) {
                Toast.makeText((NewsPageActivity)context, "233", Toast.LENGTH_SHORT).show();
            }
        }
        private void setSpanPart(SpannableString ss) {
            Pattern p;
            Matcher m;
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(R.attr.textColorSpan, typedValue, true);
            for(News.NewsDetail.Person i : current_news.news_content.persons) {
                p = Pattern.compile(i.word);
                m = p.matcher(ss);
                int start = 0;
                while(m.find(start)) {
                    ss.setSpan(new URLSpan("https://baike.baidu.com/item/" + i.word), m.start(),
                            m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(getResources().getColor(typedValue.resourceId)), m.start(),
                            m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = m.end();
                }
            }
            for(News.NewsDetail.Location i : current_news.news_content.locations) {
                p = Pattern.compile(i.word);
                m = p.matcher(ss);
                int start = 0;
                while(m.find(start)) {
                    ss.setSpan(new URLSpan("https://baike.baidu.com/item/" + i.word), m.start(),
                            m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(getResources().getColor(typedValue.resourceId)), m.start(),
                            m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = m.end();
                }
            }
            for(News.NewsDetail.Location i : current_news.news_content.organizations) {
                p = Pattern.compile(i.word);
                m = p.matcher(ss);
                int start = 0;
                while(m.find(start)) {
                    ss.setSpan(new URLSpan("https://baike.baidu.com/item/" + i.word), m.start(),
                            m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(getResources().getColor(typedValue.resourceId)), m.start(),
                            m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = m.end();
                }
            }
        }

    }
}
class MyReadActionProvider extends ActionProvider {

    private Context context;
    private boolean current_read_state;
    private boolean not_init;
    private boolean current_read_character;

//    public boolean onMenuItemClick(MenuItem item) {
//        return true;
//    }
    public void setContext(Context context) {
        this.context = context;
    }

    public MyReadActionProvider(Context context) {
        super(context);
    }

    public View onCreateActionView() {
        current_read_state = false;
        not_init = true;
        current_read_character = false;
        return null;
    }
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();

        subMenu.add((current_read_state) ? "暂停播报" : "开始播报")
                .setIcon(R.mipmap.ic_launcher)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        if(not_init) {
                            not_init = false;
                            current_read_state = true;
                            final String strTextToSpeech = ((NewsPageActivity)context).getCurrentNews().news_content.news_Content;
                            mTts.startSpeaking( strTextToSpeech, new SynthesizerListener() {
                                @Override
                                public void onSpeakBegin() {//开始播放
                                    Toast.makeText(context, "开始播报", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onSpeakPaused() {//暂停播放
                                    Toast.makeText(context, "暂停播报", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onSpeakResumed() {//继续播放
                                    Toast.makeText(context, "继续播报", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onBufferProgress(int i, int i1, int i2, String s) {//合成进度
                                }
                                @Override
                                public void onSpeakProgress(int i, int i1, int i2) {//播放进度
                                }
                                @Override
                                public void onCompleted(SpeechError speechError) {

                                }
                                @Override
                                public void onEvent(int i, int i1, int i2, Bundle bundle) {
                                    // 以下代码用于获取与云端的会话 id，当业务出错时将会话 id提供给技术支持人员，可用于查询会话日志，定位出错原因
                                    // 若使用本地能力，会话 id为null
                                    //if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                                    //     String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                                    //     Log.d(TAG, "session id =" + sid);
                                    //}
                                }
                            });
                        }
                        else if(current_read_state){
                            mTts.pauseSpeaking();
                            current_read_state = !current_read_state;
                        }
                        else {
                            mTts.resumeSpeaking();
                            current_read_state = !current_read_state;
                        }
                        return true;
                    }
                });
        subMenu.add("结束播报")
                .setIcon(R.mipmap.ic_launcher)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        if(!not_init) mTts.stopSpeaking();
                        current_read_state = false;
                        not_init = true;
                        return true;
                    }
                });
        subMenu.add(current_read_character ? "切换女声" : "切换男声")
                .setIcon(R.mipmap.ic_launcher)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        if(current_read_character) {
                            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
                            current_read_character = !current_read_character;
                        }
                        else {
                            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyu");
                            current_read_character = !current_read_character;
                        }
                        return true;
                    }
                });
    }
    public boolean hasSubMenu() {
        return true;
    }
}