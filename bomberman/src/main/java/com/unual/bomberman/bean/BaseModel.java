package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.unual.bomberman.view.MapView;

/**
 * Created by unual on 2017/7/7.
 */

public abstract class BaseModel {
    protected byte[][] mapInfo = MapView.GameConfig.mapInfo.getInfo();
    protected int perHeight = MapView.GameConfig.perHeight;
    protected int perWidth = MapView.GameConfig.perWidth;
    protected Location location;
    protected Bitmap icon;

    public abstract void draw(Canvas canvas);

}
