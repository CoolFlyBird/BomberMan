package com.unual.bomberman.widget;

/**
 * Created by unual on 2017/7/6.
 */

public class GameConfig {
    private static GameConfig mInstance;
    public int width, height;
    public int fps;

    private GameConfig() {
//        height = 9;
//        width = 17;
        height = 13;
        width = 21;
        fps = 30;
    }

    public static GameConfig getInstance() {
        if (mInstance == null) {
            mInstance = new GameConfig();
        }
        return mInstance;
    }
}
