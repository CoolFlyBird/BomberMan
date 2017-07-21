package com.unual.bomberman;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.unual.bomberman.bean.Bomb;

/**
 * Created by unual on 2017/7/5.
 */

public class AppSharedPreferences {
    private static AppSharedPreferences mInstance;
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;

    AppSharedPreferences() {
        spf = AppCache.getInstance().getContext().getSharedPreferences("app_shareprefs", Context.MODE_PRIVATE);
        editor = spf.edit();
    }

    public static AppSharedPreferences getInstance() {
        if (mInstance == null) {
            mInstance = new AppSharedPreferences();
        }
        return mInstance;
    }

    public void loadConfig(GameConfig gameConfig) {
        gameConfig.setBombCount(spf.getInt("bombcount", 1));
        gameConfig.setBombLength(spf.getInt("bomblength", 1));
        gameConfig.setBomberSpeed(spf.getInt("bomberspeed", 2));
        gameConfig.setLevel(spf.getInt("maplevel", 1));
    }

    public void cleanGameConfig() {
        editor.putInt("bombcount", 1);
        editor.putInt("bomblength", 1);
        editor.putInt("bomberspeed", 2);
        editor.putInt("maplevel", 1);
        editor.commit();
    }

    public void setGameConfig(GameConfig gameConfig) {
        editor.putInt("bombcount", gameConfig.getBombCount());
        editor.putInt("bomblength", Bomb.getBombLength());
        editor.putInt("bomberspeed", gameConfig.getBomber().getSpeedLevel());
        editor.putInt("maplevel", gameConfig.getMapLevel());
        editor.commit();
    }

}
