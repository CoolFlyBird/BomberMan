package com.unual.bomberman;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

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
}
