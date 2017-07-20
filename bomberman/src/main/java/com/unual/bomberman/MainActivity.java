package com.unual.bomberman;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.unual.bomberman.interfaces.ChapterCallback;
import com.unual.bomberman.view.GameView;
import com.unual.bomberman.view.MapView;

public class MainActivity extends Activity implements ChapterCallback {
    private MapView mapview;
    private GameView gameview;
    private static final int FLAG = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (AppCache.getInstance().getGameConfig() == null) finish();
        setContentView(R.layout.activity_main);
        mapview = (MapView) findViewById(R.id.mapview);
        gameview = (GameView) findViewById(R.id.gameview);
        gameview.addChapterCallback(this);
        gameview.setZOrderOnTop(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(FLAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void reStartGame() {
        AppCache.getInstance().getGameConfig().reStart();
        mapview.renderMap();
        gameview.startRender();
    }

    @Override
    public void nextChapter() {
        AppCache.getInstance().getGameConfig().nextLevel();
        mapview.renderMap();
        gameview.startRender();
    }

}
