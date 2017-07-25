package com.unual.bomberman;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import com.unual.bomberman.bean.Bomb;
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
        Toast.makeText(getBaseContext(), "level " + AppCache.getInstance().getGameConfig().getMapLevel(), Toast.LENGTH_SHORT).show();
        Log.e("123", "level:" + AppCache.getInstance().getGameConfig().getMapLevel() + ",length:" + Bomb.getBombLength() + ",count:" + AppCache.getInstance().getGameConfig().getBombCount());
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
    public void renderMap() {
        mapview.renderMap();
    }

    @Override
    public void reStartGame() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "level " + AppCache.getInstance().getGameConfig().getMapLevel(), Toast.LENGTH_SHORT).show();
            }
        });
        AppCache.getInstance().getGameConfig().reStart();
        mapview.renderMap();
        gameview.startRender();
        AppSharedPreferences.getInstance().cleanGameConfig();
        Log.e("123", "level:" + AppCache.getInstance().getGameConfig().getMapLevel() + ",length:" + Bomb.getBombLength() + ",count:" + AppCache.getInstance().getGameConfig().getBombCount());
    }

    @Override
    public void nextChapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "level " + AppCache.getInstance().getGameConfig().getMapLevel(), Toast.LENGTH_SHORT).show();
            }
        });
        AppCache.getInstance().getGameConfig().nextLevel();
        mapview.renderMap();
        gameview.startRender();
        AppSharedPreferences.getInstance().setGameConfig(AppCache.getInstance().getGameConfig());
        Log.e("123", "level:" + AppCache.getInstance().getGameConfig().getMapLevel() + ",length:" + Bomb.getBombLength() + ",count:" + AppCache.getInstance().getGameConfig().getBombCount());
    }

}
