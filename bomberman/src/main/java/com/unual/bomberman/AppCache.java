package com.unual.bomberman;

import android.content.Context;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by unual on 2017/7/14.
 */

public class AppCache {
    private static AppCache mInstance;
    public Random random = new Random();
    private Timer timer;
    private GameConfig gameConfig;
    private Context context;

    private AppCache() {
    }

    public static AppCache getInstance() {
        if (mInstance == null) {
            mInstance = new AppCache();
        }
        return mInstance;
    }

    public Random getRandom() {
        return random;
    }

    public void setDelayTask(TimerTask task, int milliseconds) {
        timer.schedule(task, milliseconds);
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public void setGameConfig(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
        timer = new Timer();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void clean() {
        gameConfig = null;
        context = null;
        mInstance = null;
    }
}
