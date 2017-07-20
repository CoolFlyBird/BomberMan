package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.unual.bomberman.GameConfig;

/**
 * Created by unual on 2017/7/7.
 */

public abstract class BaseModel {
    protected byte[][] mapInfo;
    protected int perHeight;
    protected int perWidth;
    protected Location location;
    protected Bitmap icon;

    public BaseModel(GameConfig gameConfig) {
        mapInfo = gameConfig.getMapInfo().getInfo();
        perHeight = gameConfig.PER_HEIGHT;
        perWidth = gameConfig.PER_WIDTH;
    }

    public abstract void draw(Canvas canvas);

}
