package com.unual.bomberman;

import android.content.Context;

import com.unual.bomberman.view.MapView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by unual on 2017/7/14.
 */

public class AppCache {
    private static AppCache mInstance;
    private Timer timer;
    private MapView.GameConfig gameConfig;
    private Context context;

    private AppCache() {
        timer = new Timer();
    }

    public static AppCache getInstance() {
        if (mInstance == null) {
            mInstance = new AppCache();
        }
        return mInstance;
    }

    public void setDelayTask(TimerTask task, int milliseconds) {
        timer.schedule(task, milliseconds);
    }

    public MapView.GameConfig getGameConfig() {
        return gameConfig;
    }

    public void setGameConfig(MapView.GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void clean() {
        if (gameConfig != null)
            gameConfig.clean();
        gameConfig = null;
        context = null;
        mInstance = null;
    }
}
