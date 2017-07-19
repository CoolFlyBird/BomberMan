package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.R;

/**
 * Created by unual on 2017/7/19.
 */

public class PropDoor extends PropModel {

    public PropDoor() {
        icon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_prop_door);
        icon = Bitmap.createScaledBitmap(icon, perWidth, perHeight, false);
    }

}