package com.java.group8;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;

import static com.java.group8.NewsPageActivity.mTts;

/**
 * Created by xzwkl on 17/9/9.
 */

public class NewsPageActivity extends AppCompatActivity {

    private ShareActionProvider mShareActionProvider;
    public static SpeechSynthesizer mTts;

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

        ActionBar ab = getSupportActionBar();
        //使能app bar的导航功能
        ab.setDisplayHomeAsUpEnabled(true);


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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_newspage_opinions, menu);

        MenuItem read_newspage = menu.findItem(R.id.read_newspage);
        MyReadActionProvider mActionProvider = (MyReadActionProvider) MenuItemCompat.getActionProvider(read_newspage);

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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_newspage:
                break;
            case R.id.favor_newspage:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
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

    public MyReadActionProvider(Context context) {
        super(context);
    }

    public View onCreateActionView() {
        current_read_state = false;
        not_init = true;
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
                            final String strTextToSpeech = "科大讯飞，让世界聆听我们的声音";
                            mTts.startSpeaking( strTextToSpeech, new SynthesizerListener() {
                                @Override
                                public void onSpeakBegin() {//开始播放
                                }
                                @Override
                                public void onSpeakPaused() {//暂停播放
                                }
                                @Override
                                public void onSpeakResumed() {//继续播放
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
