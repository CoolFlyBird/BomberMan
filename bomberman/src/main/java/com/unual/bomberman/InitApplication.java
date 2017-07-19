package com.unual.bomberman;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.unual.bomberman.view.MapView;

/**
 * Created by unual on 2017/7/14.
 */

public class InitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCache.getInstance().setContext(this);
        AppCache.getInstance().setGameConfig(new MapView.GameConfig());
    }

    public InitApplication() {
        super();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (AppCache.getInstance().getGameConfig() == null) {
            AppCache.getInstance().setGameConfig(new MapView.GameConfig());
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
