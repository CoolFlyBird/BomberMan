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
        GameConfig gameConfig = AppSharedPreferences.getInstance().createGameConfig();
        AppCache.getInstance().setGameConfig(gameConfig);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (AppCache.getInstance().getGameConfig() == null) {
            GameConfig gameConfig = AppSharedPreferences.getInstance().createGameConfig();
            AppCache.getInstance().setGameConfig(gameConfig);
        }
    }
}
