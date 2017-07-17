package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.unual.bomberman.view.MapView;

/**
 * Created by unual on 2017/7/7.
 */

public abstract class BaseModel {
    protected static byte[][] mapInfo = MapView.GameConfig.mapInfo;
    protected static int perHeight = MapView.GameConfig.perHeight;
    protected static int perWidth = MapView.GameConfig.perWidth;
    protected Location location;
    protected Bitmap icon;

    public abstract boolean canUp(int x, int y);

    public abstract boolean canDown(int x, int y);

    public abstract boolean canLeft(int x, int y);

    public abstract boolean canRight(int x, int y);

    public abstract void draw(Canvas canvas);

}
