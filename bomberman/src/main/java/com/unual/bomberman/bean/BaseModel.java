package com.unual.bomberman.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.interfaces.IControl;
import com.unual.bomberman.interfaces.IDirection;

/**
 * Created by unual on 2017/7/7.
 */

public abstract class BaseModel {
    protected Bitmap icon;
    protected Location location;
    protected int perWidth, perHeight;

    public BaseModel(int resId, int perWidth, int perHeight) {
        this.perWidth = perWidth;
        this.perHeight = perHeight;
        icon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), resId);
        icon = Bitmap.createScaledBitmap(icon, perWidth, perHeight, false);
        location = new Location();
        initLocation(location);
    }

    public abstract void initLocation(Location location);

    private boolean onXLine() {
        if ((Math.abs(location.yOffset) <= 10e-6)) {
            return true;
        }
        return false;
    }

    private boolean onYLine() {
        if ((Math.abs(location.xOffset) <= 10e-6)) {
            return true;
        }
        return false;
    }

    private boolean onXYLine() {
        if ((Math.abs(location.xOffset) <= 10e-6) && (Math.abs(location.yOffset) <= 10e-6)) {
            return true;
        }
        return false;
    }

    public abstract void draw(Canvas canvas);

}
