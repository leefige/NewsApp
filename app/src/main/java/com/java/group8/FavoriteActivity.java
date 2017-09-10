package com.java.group8;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.daimajia.swipe.SwipeLayout;

/**
 * Created by xzwkl on 17/9/10.
 */

public class FavoriteActivity extends AppCompatActivity {

    private SwipeLayout swipeLayout;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_favorite);

        Toolbar toolbar_favorite = (Toolbar) findViewById(R.id.toolbar_favorite);
        setSupportActionBar(toolbar_favorite);

        ActionBar ab = getSupportActionBar();
        //使能app bar的导航功能
        ab.setDisplayHomeAsUpEnabled(true);

        swipeLayout =  (SwipeLayout)findViewById(R.id.swipeLayout_favorite);

//set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_view_favorite));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
    }
}
