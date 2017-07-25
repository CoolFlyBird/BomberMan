package com.unual.bomberman;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by unual on 2017/7/14.
 */

public class InitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCache.getInstance().setContext(this);
        AppCache.getInstance().setGameConfig(new GameConfig());
        AppSharedPreferences.getInstance().loadConfig(AppCache.getInstance().getGameConfig());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (AppCache.getInstance().getGameConfig() == null) {
            AppCache.getInstance().setGameConfig(new GameConfig());
            AppSharedPreferences.getInstance().loadConfig(AppCache.getInstance().getGameConfig());
        }
    }
}
