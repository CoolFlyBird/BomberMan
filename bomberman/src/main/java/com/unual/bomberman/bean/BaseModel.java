package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;

/**
 * Created by unual on 2017/7/7.
 */

public abstract class BaseModel {
    protected byte[][] mapInfo;
    protected int perHeight = GameConfig.perHeight;
    protected int perWidth = GameConfig.perWidth;
    protected Location location;
    protected Bitmap icon;

    public BaseModel() {
        mapInfo = AppCache.getInstance().getGameConfig().mapInfo.getInfo();
    }

    public abstract void draw(Canvas canvas);

}
