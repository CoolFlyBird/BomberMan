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

    public GameConfig createGameConfig() {
        int bombcount = spf.getInt("bombcount", 1);
        int bomblength = spf.getInt("bomblength", 1);
        int bomberspeed = spf.getInt("bomberspeed", 2);
        int maplevel = spf.getInt("maplevel", 1);
        return new GameConfig(maplevel, bombcount, bomblength, bomberspeed);
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
        editor.putInt("bomblength", gameConfig.getBombLength());
        editor.putInt("bomberspeed", gameConfig.getSpeedLevel());
        editor.putInt("maplevel", gameConfig.getMapLevel());
        editor.commit();
    }

}
