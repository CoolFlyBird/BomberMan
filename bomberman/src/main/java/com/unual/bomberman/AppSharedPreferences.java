package com.unual.bomberman;

import android.content.Context;
import android.content.SharedPreferences;

import com.unual.bomberman.view.MapView;

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

    public void setGameConfig(MapView.GameConfig gameConfig) {
//        editor.putString("gameconfig", gameConfig);
        editor.commit();
    }

    public String getAccount() {
        return spf.getString("account", "");
    }

    public String getPassword() {
        return spf.getString("password", "");
    }

    public String getToken() {
        return spf.getString("token", "");
    }

    public String getRealname() {
        return spf.getString("name", "");
    }

    public String getLogourl() {
        return spf.getString("icon", "");
    }

}
